package com.du.assignment.project1;

import org.jdom.Attribute;
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
            Optional.ofNullable(saxBuilder.build(new File(path + "ipo.xml")))
                    .map(Document::getRootElement)
                    .map(Element::getChildren)
                    .ifPresent(list -> {
                        ProjectJDOM projectABC = new ProjectJDOM(new Document(), "ABC_COMP");
                        ProjectJDOM projectIBM = new ProjectJDOM(new Document(), "IBM_COMP");

                        list.forEach(element -> {
                            Element e = (Element) element;
                            Optional.ofNullable(e.getAttributeValue("comp_name"))
                                    .ifPresent(compName -> {
                                        switch (compName) {
                                            case "ABC":
                                                projectABC.append(projectABC.getCompElement(), e);
                                                break;
                                            case "IBM":
                                                projectIBM.append(projectIBM.getCompElement(), e);
                                                break;
                                        }
                                    });
                        });
                        try {
                            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
                            out.output(projectABC.getDoc(), new FileOutputStream(new File(path + "JDOM/ABC_COMP.xml")));
                            out.output(projectIBM.getDoc(), new FileOutputStream(new File(path + "JDOM/IBM_COMP.xml")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * ProjectJDOM类是对输出xml文件的dom模型的封装
 */
class ProjectJDOM {
    private Document doc;
    private Element compElement;

    ProjectJDOM(Document doc, String compName) {
        this.doc = doc;
        this.init(compName);
    }

    private void init(String compName) {
        Element root = new Element("purchaseOrders");
        this.compElement = new Element(compName);
        root.addContent(this.compElement);
        doc.addContent(root);
    }

    void append(Element rootElement, Element childElement) {
        Element e = new Element(childElement.getName());
        rootElement.addContent(e);

        Optional.ofNullable(childElement.getAttributes())
                .ifPresent(attrList -> attrList.forEach(attribute -> {
                    Attribute attr = (Attribute) attribute;
                    if (!attr.getName().equals("comp_name")) {
                        Element temp = new Element(attr.getName());
                        temp.setText(attr.getValue());
                        e.addContent(temp);
                    }
                }));

        if (childElement.getChildren().isEmpty()) {
            e.setText(childElement.getText());
        } else {
            childElement.getChildren().forEach(element -> append(e, (Element) element));
        }
    }

    Document getDoc() {
        return doc;
    }

    Element getCompElement() {
        return compElement;
    }
}
