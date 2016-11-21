package com.du.assignment.project1;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Optional;

/**
 * Created by hnyd1 on 2016/11/21.
 */
public class DOM4J {

    public static void main(String[] args) {
        String path = "src/main/resources/";
        SAXReader reader = new SAXReader();

        try {
            Document docABC = DocumentHelper.createDocument();
            Document docIBM = DocumentHelper.createDocument();
            docABC.addElement("purchaseOrders");
            docIBM.addElement("purchaseOrders");

            Optional.ofNullable(reader.read(new File(path + "ipo.xml")))
                    .map(Document::getRootElement)
                    .map(Element::elements)
                    .ifPresent(list -> {
                        Element rootABC = docABC.getRootElement();
                        Element rootIBM = docIBM.getRootElement();

                        list.forEach(element -> {
                            Element e = (Element) element;
                            Optional.ofNullable(e.attributeValue("comp_name"))
                                    .ifPresent(compName -> {
                                        switch (compName) {
                                            case "ABC":
                                                rootABC.add(e.createCopy());
                                                break;
                                            case "IBM":
                                                rootIBM.add(e.createCopy());
                                                break;
                                        }
                                    });
                        });
                        try {
                            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
                            writer.setOutputStream(new FileOutputStream(new File(path + "DOM4J/ABC_COMP.xml")));
                            writer.write(docABC);
                            writer.setOutputStream(new FileOutputStream(new File(path + "DOM4J/IBM_COMP.xml")));
                            writer.write(docIBM);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
}
