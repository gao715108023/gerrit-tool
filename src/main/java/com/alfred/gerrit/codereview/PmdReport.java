package com.alfred.gerrit.codereview;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GaoChuanjun
 * @date 2018/12/4 7:09 PM
 */
public class PmdReport {

  private String version;

  private String timestamp;

  private List<PmdFileReport> pmdFileReportList;

  public PmdReport() {
    this.pmdFileReportList = new ArrayList<>();
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public List<PmdFileReport> getPmdFileReportList() {
    return pmdFileReportList;
  }

  public void setPmdFileReportList(
      List<PmdFileReport> pmdFileReportList) {
    this.pmdFileReportList = pmdFileReportList;
  }

  @Override
  public String toString() {
    return "PmdReport{" +
        "version='" + version + '\'' +
        ", timestamp='" + timestamp + '\'' +
        ", pmdFileReportList=" + pmdFileReportList +
        '}';
  }

}
