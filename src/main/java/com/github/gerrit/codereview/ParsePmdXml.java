package com.github.gerrit.codereview;

import java.io.File;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author GaoChuanjun
 * @date 2018/12/4 7:08 PM
 */
public class ParsePmdXml {
  public PmdReport parseXml(String xmlPath) throws DocumentException {
    PmdReport pmdReport = new PmdReport();
    SAXReader saxReader = new SAXReader();
    Document document = saxReader.read(new File(xmlPath));
    Element rootElement = document.getRootElement();

    pmdReport.setVersion(rootElement.attributeValue("version"));
    pmdReport.setTimestamp(rootElement.attributeValue("timestamp"));

    Iterator<Element> fileIterator = rootElement.elements("file").iterator();
    while (fileIterator.hasNext()) {
      PmdFileReport pmdFileReport = new PmdFileReport();
      Element fileElement = fileIterator.next();
      pmdFileReport.setFileName(fileElement.attributeValue("name"));
      Element violationElement = fileElement.element("violation");
      pmdFileReport.setBeginLine(Integer.parseInt(violationElement.attributeValue("beginline")));
      pmdFileReport.setEndLine(Integer.parseInt(violationElement.attributeValue("endline")));
      pmdFileReport
          .setBeginColumn(Integer.parseInt(violationElement.attributeValue("begincolumn")));
      pmdFileReport.setEndColumn(Integer.parseInt(violationElement.attributeValue("endcolumn")));
      pmdFileReport.setRule(violationElement.attributeValue("rule"));
      pmdFileReport.setRuleSet(violationElement.attributeValue("ruleset"));
      pmdFileReport.setPackageName(violationElement.attributeValue("package"));
      pmdFileReport.setClassName(violationElement.attributeValue("class"));
      pmdFileReport.setMethod(violationElement.attributeValue("method"));
      pmdFileReport.setPriority(Integer.parseInt(violationElement.attributeValue("priority")));
      pmdFileReport.setDesc(violationElement.getText());
      pmdReport.getPmdFileReportList().add(pmdFileReport);
    }
    return pmdReport;
  }
}
