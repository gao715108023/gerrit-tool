package com.github.gerrit.codereview;

import com.github.gerrit.codereview.constant.GerritConstant;
import com.github.gerrit.codereview.ignore.ReviewIgnore;
import com.github.gerrit.codereview.pmd.PMDCommand;
import com.github.gerrit.codereview.util.ConfigUtil;
import com.github.gerrit.codereview.util.FileTools;
import com.google.gerrit.extensions.api.GerritApi;
import com.google.gerrit.extensions.api.changes.ChangeApi;
import com.google.gerrit.extensions.api.changes.Changes;
import com.google.gerrit.extensions.api.changes.DraftApi;
import com.google.gerrit.extensions.api.changes.DraftInput;
import com.google.gerrit.extensions.api.changes.FileApi;
import com.google.gerrit.extensions.api.changes.ReviewInput;
import com.google.gerrit.extensions.api.changes.ReviewInput.DraftHandling;
import com.google.gerrit.extensions.api.changes.RevisionApi;
import com.google.gerrit.extensions.client.Comment.Range;
import com.google.gerrit.extensions.common.ChangeInfo;
import com.google.gerrit.extensions.common.FileInfo;
import com.google.gerrit.extensions.restapi.BinaryResult;
import com.urswolfer.gerrit.client.rest.GerritAuthData;
import com.urswolfer.gerrit.client.rest.GerritRestApiFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author GaoChuanjun
 * @date 2018/12/4 7:07 PM
 */
public class GerritCodeReview {
  private static final Logger LOGGER = LoggerFactory.getLogger(GerritCodeReview.class);
  private static final String HOME_PATH = System.getProperty("user.home");
  private static final String CODE_REVIEW_WORKSPACE = HOME_PATH + "/CodeReview/";
  private static final String CODE_REVIEW_REVIEWED = CODE_REVIEW_WORKSPACE + "reviewed.txt";
  private static String gerritUrl;
  private static String gerritUser;
  private static String gerritPassword;
  private static int sleepMillis;

  static {
    ConfigUtil.load("/service.properties");
    gerritUrl = ConfigUtil.get(GerritConstant.GERRIT_URL);
    gerritUser = ConfigUtil.get(GerritConstant.GERRIT_USER);
    gerritPassword = ConfigUtil.get(GerritConstant.GERRIT_PASSWORD);
    sleepMillis = ConfigUtil.getInt(GerritConstant.REVIEW_SLEEP_SECOND, 60) * 1000;
  }

  public static void review() {
    System.setProperty("net.sourceforge.pmd.cli.noExit", "false");
    GerritRestApiFactory gerritRestApiFactory = new GerritRestApiFactory();
    GerritAuthData.Basic authData = new GerritAuthData.Basic(gerritUrl, gerritUser, gerritPassword);
    GerritApi gerritApi = gerritRestApiFactory.create(authData);
    Changes changes = gerritApi.changes();
    System.out.println("gerrit code review service server started");
    File reviewedFile = new File(CODE_REVIEW_REVIEWED);
    if (!reviewedFile.exists()) {
      try {
        if (reviewedFile.createNewFile()) {
          LOGGER.info("create " + CODE_REVIEW_REVIEWED + " success.");
        } else {
          LOGGER.error("create " + CODE_REVIEW_REVIEWED + " failure.");
        }
      } catch (IOException e) {
        LOGGER.error("create " + CODE_REVIEW_REVIEWED + " failure.", e);
      }
    }
    while (true) {
      try {
        Thread.sleep(sleepMillis);
        List<ChangeInfo> changeInfoList =
            changes.query("is:open+reviewer:self").withLimit(10).get();
        if (changeInfoList == null || changeInfoList.isEmpty()) {
          LOGGER.info("No incoming reviews!");
          continue;
        }

        String reviewedChangeIds = FileUtils.readFileToString(reviewedFile, "utf-8");

        Set<String> changeIds = new HashSet<>();

        Set<String> newChangeIds = new HashSet<>();

        if (StringUtils.isNotBlank(reviewedChangeIds)) {
          Collections.addAll(changeIds, reviewedChangeIds.split(","));
        }

        for (ChangeInfo change : changeInfoList) {
          if (changeIds.contains(change.changeId)) {
            newChangeIds.add(change.changeId);
            continue;
          }
          ChangeApi changeApi = changes.id(change.changeId);
          RevisionApi revisionApi = changeApi.current();
          Map<String, FileInfo> fileInfoMap = revisionApi.files();
          short score = 2;
          for (Map.Entry<String, FileInfo> fileInfoEntry : fileInfoMap.entrySet()) {
            try {
              // 忽略不相关文件
              if (ReviewIgnore.containsIgnore(fileInfoEntry.getKey())) {
                continue;
              }
              FileApi fileApi = revisionApi.file(fileInfoEntry.getKey());
              BinaryResult binaryResult = fileApi.content();
              String path = CODE_REVIEW_WORKSPACE + change.project + "/" + fileInfoEntry.getKey();
              String data = new String(Base64.decodeBase64(binaryResult.asString()));
              FileUtils.writeStringToFile(new File(path), data, "utf-8");
              if (!path.endsWith(".java")) {
                continue;
              }

              String rootReportPath =
                  CODE_REVIEW_WORKSPACE
                      + change.project
                      + "/report/"
                      + DateFormatUtils.format(new Date(), "yyyyMMdd");
              FileTools.mkdirs(rootReportPath);

              String reportPath = rootReportPath + "/report-" + System.currentTimeMillis() + ".xml";
              PmdReport pmdReport = PMDCommand.run(path, reportPath);
              if (!pmdReport.getPmdFileReportList().isEmpty()) {
                for (PmdFileReport pmdFileReport : pmdReport.getPmdFileReportList()) {
                  try {
                    if (!"PojoMustOverrideToStringRule".equalsIgnoreCase(pmdFileReport.getRule())
                        && !"ClassMustHaveAuthorRule".equalsIgnoreCase(pmdFileReport.getRule())) {
                      score--;
                    }
                    DraftInput draftInput = new DraftInput();
                    Range range = new Range();
                    range.startLine = pmdFileReport.getBeginLine();
                    range.endLine = pmdFileReport.getEndLine();
                    range.startCharacter = pmdFileReport.getBeginColumn();
                    range.endCharacter = pmdFileReport.getEndColumn();
                    draftInput.range = range;
                    draftInput.path = fileInfoEntry.getKey();
                    draftInput.line = pmdFileReport.getEndLine();
                    draftInput.message = pmdFileReport.getDesc();

                    if ("ClassNamingShouldBeCamelRule".equalsIgnoreCase(pmdFileReport.getRule())
                        || "ClassMustHaveAuthorRule".equalsIgnoreCase(pmdFileReport.getRule())) {
                      range.startLine = pmdFileReport.getBeginLine();
                      range.endLine = pmdFileReport.getBeginLine();
                      range.startCharacter = pmdFileReport.getBeginColumn();
                      range.endCharacter =
                          pmdFileReport.getBeginColumn() + pmdFileReport.getClassName().length();
                      draftInput.line = pmdFileReport.getBeginLine();
                    }
                    DraftApi draftApi = revisionApi.createDraft(draftInput);
                  } catch (Exception e) {
                    LOGGER.error("", e);
                  }
                }
              }
            } catch (Exception e) {
              LOGGER.error("", e);
            }
          }
          if (score < -2) {
            score = -2;
          }

          if (score >= 0) {
            score = 2;
          }
          ReviewInput reviewInput = new ReviewInput();
          Map<String, Short> labels = new HashMap<>(1);
          labels.put("Code-Review", score);
          reviewInput.labels = labels;
          reviewInput.drafts = DraftHandling.PUBLISH_ALL_REVISIONS;
          reviewInput.strictLabels = true;
          revisionApi.review(reviewInput);
          if (score == 2) {
            try {
              revisionApi.submit();
            } catch (Exception e) {
              LOGGER.error("", e);
            }
          }
          newChangeIds.add(change.changeId);
        }

        if (!newChangeIds.isEmpty()) {
          StringBuilder stringBuilder = new StringBuilder();
          for (String newChangeId : newChangeIds) {
            stringBuilder.append(newChangeId).append(",");
          }
          stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
          FileUtils.writeStringToFile(reviewedFile, stringBuilder.toString(), "utf-8", false);
        }
      } catch (Exception e) {
        LOGGER.error("", e);
      }
    }
  }
}
