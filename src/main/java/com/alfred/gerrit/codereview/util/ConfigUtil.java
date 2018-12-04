package com.alfred.gerrit.codereview.util;

import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author GaoChuanjun
 * @date 2018/12/4 7:07 PM
 */
public class ConfigUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

  private static Properties p = new Properties();

  /** 加载配置文件 */
  public static synchronized void load(String propPath) {

    try {
      if (p.size() == 0) {
        p.load(ConfigUtil.class.getResourceAsStream(propPath));
      }
    } catch (IOException e) {
      LOGGER.error("", e);
      System.exit(-1);
    }
  }

  /**
   * getProperty方法的简写
   *
   * @param name 属性名
   * @return value
   */
  public static String get(String name) {
    return getProperty(name);
  }

  private static String getProperty(String name) {
    return p.getProperty(name);
  }

  /**
   * 获取一个配制项，如果项没有被配制，则返回设置的默认值
   *
   * @param name 配制项名
   * @param defaultValue 默认值
   * @return 配制值
   */
  public static String getString(String name, String defaultValue) {
    String ret = getProperty(name);
    return ret == null ? defaultValue : ret;
  }

  /**
   * 从配制文件中获取一个整形的配制值，如果没有配制，则返回默认值
   *
   * @param item 属性名
   * @param defaultValue 默认值
   * @return int value
   */
  public static int getInt(String item, int defaultValue) {
    String value = getProperty(item);
    if (value == null) {
      return defaultValue;
    }
    int ret = defaultValue;
    try {
      ret = Integer.parseInt(value);
    } catch (Exception e) {
      LOGGER.error("", e);
    }
    return ret;
  }

  public static boolean getBoolean(String item, boolean defaultValue) {
    String value = getProperty(item);
    if (value == null) {
      return defaultValue;
    }
    boolean ret = defaultValue;
    try {
      ret = Boolean.parseBoolean(value);
    } catch (Exception e) {
      LOGGER.error("", e);
    }
    return ret;
  }
}
