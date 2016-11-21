package com.du.assignment.project1;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by hnyd1 on 2016/11/19.
 */
public class JDOM {

    public static void main(String[] args) {
        String path = "src/main/resources/";
        SAXBuilder saxBuilder = new SAXBuilder();

        try {
            Document docABC = new Document(new Element("purchaseOrders"));
            Document docIBM = new Document(new Element("purchaseOrders"));

            Optional.ofNullable(saxBuilder.build(new File(path + "ipo.xml")))
                    .map(Document::getRootElement)
                    .map(Element::getChildren)
                    .ifPresent(list -> {
                        Element rootABC = docABC.getRootElement();
                        Element rootIBM = docIBM.getRootElement();

                        list.forEach(element -> {
                            Element e = (Element) element;
                            Optional.ofNullable(e.getAttributeValue("comp_name"))
                                    .ifPresent(compName -> {
                                        switch (compName) {
                                            case "ABC":
                                                rootABC.addContent((Element) e.clone());
                                                break;
                                            case "IBM":
                                                rootIBM.addContent((Element) e.clone());
                                                break;
                                        }
                                    });
                        });
                        try {
                            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
                            out.output(docABC, new FileOutputStream(new File(path + "JDOM/ABC_COMP.xml")));
                            out.output(docIBM, new FileOutputStream(new File(path + "JDOM/IBM_COMP.xml")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

    }
}
