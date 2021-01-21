/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.theoretics;

import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Theoretics
 */
public class XMLreader {
    
    public String getAttributeValue(String xmlfile, String XMLelement, String AttribName) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Reader fileReader = new FileReader(xmlfile);
        XMLEventReader reader = factory.createXMLEventReader(fileReader);
        
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement element = (StartElement) event;
                if (element.getName().toString().compareToIgnoreCase(XMLelement) == 0) {
                    //log.info("Start Element: " + element.getName());
                    Iterator iterator = element.getAttributes();
                    while (iterator.hasNext()) {
                        Attribute attribute = (Attribute) iterator.next();
                        QName name = attribute.getName();
                        if (name.toString().compareToIgnoreCase(AttribName) == 0) {
                            String value = attribute.getValue();
                            fileReader.close();
                            return (value);
                        }
                    }
                }
            }
        }
        fileReader.close();
        return ("");
    }

    public String getElementValue(String xmlfile, String XMLelement) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Reader fileReader = new FileReader(xmlfile);
        XMLEventReader reader = factory.createXMLEventReader(fileReader);
        
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            //log.info(event.toString() + event.getEventType());

            if (event.isStartElement()) {
                StartElement element = (StartElement) event;
                if (element.getName().toString().compareToIgnoreCase(XMLelement) == 0) {
                    //log.info("Element: " + element.getName());
                    event = reader.nextEvent();
                    if (event.isCharacters()) {
                        Characters characters = (Characters) event;
                        fileReader.close();
                        return (characters.getData());
                    }
                }
            }

        }
        fileReader.close();
        return ("");
    }

    public static void main(String args[]) throws Exception {

        XMLreader xx = new XMLreader();

        String outme, slotsmode;
        slotsmode = xx.getElementValue("C://JTerminals/initN.xml", "slotsmode");
        outme = xx.getAttributeValue("C://JTerminals/initN.xml", "entry", "one");
        System.out.print(slotsmode + " Out this := " + outme);
    }
}
