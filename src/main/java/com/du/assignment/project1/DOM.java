package com.du.assignment.project1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by hnyd1 on 2016/11/19.
 */
public class DOM {

    public static void main(String[] args) {

        String path = "src/main/resources/";
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            Optional.ofNullable(builder.parse(new File(path + "ipo.xml")))
                    .map(document -> document.getElementsByTagName("purchaseOrder"))
                    .ifPresent(nodeList -> {
                        Document docABC = builder.newDocument();
                        Document docIBM = builder.newDocument();
                        Element rootABC = docABC.createElement("purchaseOrders");
                        Element rootIBM = docIBM.createElement("purchaseOrders");
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Element element = (Element) nodeList.item(i);
                            Optional.ofNullable(element.getAttribute("comp_name"))
                                    .ifPresent(compName -> {
                                        switch (compName) {
                                            case "ABC":
                                                rootABC.appendChild(docABC.importNode(element, true));
                                                break;
                                            case "IBM":
                                                rootIBM.appendChild(docIBM.importNode(element, true));
                                                break;
                                        }
                                    });
                        }
                        docABC.appendChild(rootABC);
                        docIBM.appendChild(rootIBM);
                        try {
                            Transformer transformer = TransformerFactory.newInstance().newTransformer();
                            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                            transformer.transform(new DOMSource(docABC), new StreamResult(new File(path + "DOM/ABC_COMP.xml")));
                            transformer.transform(new DOMSource(docIBM), new StreamResult(new File(path + "DOM/IBM_COMP.xml")));
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

}
