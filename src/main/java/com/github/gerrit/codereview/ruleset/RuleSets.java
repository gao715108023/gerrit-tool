package com.github.gerrit.codereview.ruleset;

import java.io.File;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * @author GaoChuanjun
 * @date 2018/12/4 7:06 PM
 */
public class RuleSets {
  public static String listAllRuleSets(String rulePath) {
    Collection<File> files = FileUtils
        .listFiles(new File(rulePath), new IOFileFilter() {
          @Override
          public boolean accept(File file) {
            return file.getName().endsWith(".xml");
          }

          @Override
          public boolean accept(File dir, String name) {
            return true;
          }
        }, TrueFileFilter.INSTANCE);
    if (files.isEmpty()) {
      return null;
    }
    StringBuilder ruleSetsPath = new StringBuilder();
    for (File file : files) {
      ruleSetsPath.append(file.getAbsolutePath()).append(",");
    }
    ruleSetsPath.deleteCharAt(ruleSetsPath.length() - 1);
    return ruleSetsPath.toString();
  }
}
