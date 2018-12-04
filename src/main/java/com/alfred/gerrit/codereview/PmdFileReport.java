package com.alfred.gerrit.codereview;

/**
 * @author GaoChuanjun
 * @date 2018/12/4 7:08 PM
 */
public class PmdFileReport {
  private String fileName;

  private int beginLine;

  private int endLine;

  private int beginColumn;

  private int endColumn;

  private String rule;

  private String ruleSet;

  private String packageName;

  private String className;

  private String method;

  private int priority;

  private String desc;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public int getBeginLine() {
    return beginLine;
  }

  public void setBeginLine(int beginLine) {
    this.beginLine = beginLine;
  }

  public int getEndLine() {
    return endLine;
  }

  public void setEndLine(int endLine) {
    this.endLine = endLine;
  }

  public int getBeginColumn() {
    return beginColumn;
  }

  public void setBeginColumn(int beginColumn) {
    this.beginColumn = beginColumn;
  }

  public int getEndColumn() {
    return endColumn;
  }

  public void setEndColumn(int endColumn) {
    this.endColumn = endColumn;
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public String getRuleSet() {
    return ruleSet;
  }

  public void setRuleSet(String ruleSet) {
    this.ruleSet = ruleSet;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  @Override
  public String toString() {
    return "PmdFileReport{"
        + "fileName='"
        + fileName
        + '\''
        + ", beginLine="
        + beginLine
        + ", endLine="
        + endLine
        + ", beginColumn="
        + beginColumn
        + ", endColumn="
        + endColumn
        + ", rule='"
        + rule
        + '\''
        + ", ruleSet='"
        + ruleSet
        + '\''
        + ", packageName='"
        + packageName
        + '\''
        + ", className='"
        + className
        + '\''
        + ", method='"
        + method
        + '\''
        + ", priority="
        + priority
        + ", desc='"
        + desc
        + '\''
        + '}';
  }
}
