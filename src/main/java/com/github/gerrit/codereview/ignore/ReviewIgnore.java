package com.github.gerrit.codereview.ignore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author GaoChuanjun
 * @date 2018/12/4 7:05 PM
 */
public class ReviewIgnore {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReviewIgnore.class);

  private static final List<String> IGNORE_KEYWORDS = new ArrayList<>();

  static {
    InputStream inputStream = ReviewIgnore.class.getResourceAsStream("/review-ignore");
    try {
      List<String> lines = IOUtils.readLines(inputStream, Charsets.toCharset("utf-8"));
      IGNORE_KEYWORDS.addAll(lines);
    } catch (IOException e) {

    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        LOGGER.error("", e);
      }
    }
  }

  public static boolean containsIgnore(String keyword) {
    for (String ignoreKeyword : IGNORE_KEYWORDS) {
      if (keyword.contains(ignoreKeyword)) {
        return true;
      }
    }
    return false;
  }
}
