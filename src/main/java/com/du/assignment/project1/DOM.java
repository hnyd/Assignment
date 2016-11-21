package com.du.assignment.project1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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
                        ProjectDOM projectABC = new ProjectDOM(builder.newDocument(), "ABC_COMP");
                        ProjectDOM projectIBM = new ProjectDOM(builder.newDocument(), "IBM_COMP");

                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Element element = (Element) nodeList.item(i);
                            Optional.ofNullable(element.getAttribute("comp_name"))
                                    .ifPresent(compName -> {
                                        switch (compName) {
                                            case "ABC":
                                                projectABC.append(projectABC.getCompElement(), element);
                                                break;
                                            case "IBM":
                                                projectIBM.append(projectIBM.getCompElement(), element);
                                                break;
                                        }
                                    });
                        }
                        try {
                            Transformer transformer = TransformerFactory.newInstance().newTransformer();
                            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                            transformer.transform(new DOMSource(projectABC.getDoc()), new StreamResult(new File(path + "DOM/ABC_COMP.xml")));
                            transformer.transform(new DOMSource(projectIBM.getDoc()), new StreamResult(new File(path + "DOM/IBM_COMP.xml")));
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        }
                    });

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }
}

/**
 * ProjectDOM类是对输出xml文件的dom模型的封装
 */
class ProjectDOM {
    private Document doc;
    private Element compElement;

    ProjectDOM(Document doc, String compName) {
        this.doc = doc;
        this.init(compName);
    }

    private void init(String compName) {
        this.compElement = doc.createElement(compName);
        Element root = doc.createElement("purchaseOrders");
        root.appendChild(compElement);
        doc.appendChild(root);
    }

    void append(Element rootElement, Node childElement) {
        Element e = doc.createElement(childElement.getNodeName());
        rootElement.appendChild(e);

        // 检查节点是否有属性节点
        Optional.ofNullable(childElement.getAttributes())
                .ifPresent(namedNodeMap -> {
                    for (int i = 0; i < namedNodeMap.getLength(); i++) {
                        Node node = namedNodeMap.item(i);
                        String nodeName = node.getNodeName();
                        if (nodeName.equals("comp_name")) continue;
                        Element temp = doc.createElement(node.getNodeName());
                        temp.appendChild(doc.createTextNode(node.getNodeValue()));
                        e.appendChild(temp);
                    }
                });

        Optional.ofNullable(childElement.getChildNodes())
                .ifPresent(childList -> {
                    for (int i = 0; i < childList.getLength(); i++) {
                        Node node = childList.item(i);
                        switch (node.getNodeType()) {
                            case 1:
                                append(e, node);
                                break;
                            case 3:
                                e.appendChild(doc.createTextNode(node.getNodeValue()));
                                break;
                        }
                    }
                });
    }

    Document getDoc() {
        return doc;
    }

    Element getCompElement() {
        return compElement;
    }

}
