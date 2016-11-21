package com.du.assignment.project1;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by hnyd1 on 2016/11/21.
 */
public class DOM4J {

    public static void main(String[] args) {
        String path = "src/main/resources/";
        SAXReader reader = new SAXReader();

        try {
            Optional.ofNullable(reader.read(new File(path + "ipo.xml")))
                    .map(Document::getRootElement)
                    .map(Element::elements)
                    .ifPresent(list -> {
                        ProjectDOM4J projectABC = new ProjectDOM4J(DocumentHelper.createDocument(), "ABC_COMP");
                        ProjectDOM4J projectIBM = new ProjectDOM4J(DocumentHelper.createDocument(), "IBM_COMP");

                        list.forEach(element -> {
                            Element e = (Element) element;
                            Optional.ofNullable(e.attributeValue("comp_name"))
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
                            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
                            writer.setOutputStream(new FileOutputStream(new File(path + "DOM4J/ABC_COMP.xml")));
                            writer.write(projectABC.getDoc());
                            writer.setOutputStream(new FileOutputStream(new File(path + "DOM4J/IBM_COMP.xml")));
                            writer.write(projectIBM.getDoc());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

/**
 * ProjectDOM4J类是对输出xml文件的dom模型的封装
 */
class ProjectDOM4J {
    private Document doc;
    private Element compElement;

    ProjectDOM4J(Document doc, String compName) {
        this.doc = doc;
        this.init(compName);
    }

    private void init(String compName) {
        doc.addElement("purchaseOrders");
        this.compElement = doc.getRootElement().addElement(compName);
    }

    void append(Element rootElement, Element childElement) {
        Element e = rootElement.addElement(childElement.getName());

        Optional.ofNullable(childElement.attributes())
                .ifPresent(attrList -> attrList.forEach(attribute -> {
                    Attribute attr = (Attribute) attribute;
                    if (!attr.getName().equals("comp_name")) {
                        e.addElement(attr.getName()).setText(attr.getValue());
                    }
                }));

        if (childElement.elements().isEmpty()) {
            e.setText(childElement.getText());
        } else {
            childElement.elements().forEach(element -> append(e, (Element) element));
        }
    }

    public Document getDoc() {
        return doc;
    }

    public Element getCompElement() {
        return compElement;
    }

}