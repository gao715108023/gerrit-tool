package com.github.gerrit.codereview.pmd;

import com.github.gerrit.codereview.ParsePmdXml;
import com.github.gerrit.codereview.PmdReport;
import com.github.gerrit.codereview.constant.PMDConstant;
import com.github.gerrit.codereview.ruleset.RuleSets;
import com.github.gerrit.codereview.util.ConfigUtil;
import com.google.common.base.Strings;
import net.sourceforge.pmd.cli.PMDCommandLineInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author GaoChuanjun
 * @date 2018/12/4 7:06 PM
 */
public class PMDCommand {
  private static final Logger LOGGER = LoggerFactory.getLogger(PMDCommand.class);

  private static final ParsePmdXml PARSE_PMD_XML = new ParsePmdXml();

  private static String rulesetsPath;

  static {
    ConfigUtil.load("/service.properties");
    rulesetsPath = stripTrailingSlash(ConfigUtil.get(PMDConstant.RULESETS_PATH));
  }

  private static String stripTrailingSlash(String path) {
    if (!Strings.isNullOrEmpty(path) && path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
    }
    return path;
  }


  public static PmdReport run(String sourceDir, String reportFile) {
    try {
      String[] arg = new String[12];
      arg[0] = "-d";
      arg[1] = sourceDir;
      arg[2] = "-f";
      arg[3] = "xml";
      arg[4] = "-R";
      arg[5] = RuleSets.listAllRuleSets(rulesetsPath);
      arg[6] = "-version";
      arg[7] = " 1.8";
      arg[8] = "-language";
      arg[9] = "java";
      arg[10] = "-r";
      arg[11] = reportFile;
      PMDCommandLineInterface.run(arg);
      PmdReport pmdReport = PARSE_PMD_XML.parseXml(reportFile);

      LOGGER.info(pmdReport.toString());
      return pmdReport;
    } catch (Exception e) {
      LOGGER.error("", e);
    }
    return new PmdReport();
  }
}
