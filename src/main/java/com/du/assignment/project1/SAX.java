package com.du.assignment.project1;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by hnyd1 on 2016/11/19.
 */
public class SAX {

    public static void main(String[] args) {
        String path = "src/main/resources/";
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(path + "ipo.xml"), new MyHandler());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

}

class MyHandler extends DefaultHandler {
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Optional.ofNullable(attributes)
                .ifPresent(att -> {
                    for (int i = 0; i < att.getLength(); i++) {
                        System.out.println(att.getValue(i));
                    }
                });
    }
}
