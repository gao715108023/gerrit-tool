package com.github.gerrit.codereview.util;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author GaoChuanjun
 * @date 2018/12/4 7:07 PM
 */
public class FileTools {
  public static boolean mkdirs(final String filePath) throws IOException {
    if (StringUtils.isBlank(filePath)) {
      return false;
    }
    File file = new File(filePath);
    return file.mkdirs();
  }
}
