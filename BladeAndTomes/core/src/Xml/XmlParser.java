package Xml;

import com.badlogic.game.creatures.Item;
import com.fasterxml.jackson.xml.XmlMapper;
import com.sun.org.apache.xml.internal.serializer.ToSAXHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


/*
 Note: to implement this in other classes need to call following command lines in show
 SaxParser some variable = SaxParserFactory.newInstance().newSaxParser();
 somevariable.parse(new FileInputStream("some xml file"), new XmlParser());
 */

/*
    Anri had brought to my attention that for some purposes Serializing objects may better suit our needs
    While xml parsing itself may not be thrown out will be implementing serialization in regards to items
    Sources provided as follows
    https://www.youtube.com/watch?v=YzwiuRDgSSY - Thanks Professor for the video on the subject
    https://www.tutorialspoint.com/java/java_serialization.htm

    The following link was useful in serializing and deserialize from/to xml
    https://stackabuse.com/serialize-and-deserialize-xml-in-java-with-jackson/
 */
public class XmlParser {

    //added dependency so can take the needed information from xml files and serialize them back
    public static void Serialize() {
        try {
            XmlMapper xmlMapper = new XmlMapper();

            try {
                String xmlString = xmlMapper.writeValueAsString(new Item());

                File xmlOutput = new File("Items.xml");
                FileWriter fileWriter = new FileWriter(xmlOutput);
                fileWriter.write(xmlString);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Deserialize() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            String readItemsFile = new String(Files.readAllBytes(Paths.get("Items.xml")));
            Item deserializeItem = xmlMapper.readValue(readItemsFile, Item.class);

            System.out.println("Item");
            System.out.println("Name: " + deserializeItem.getName());
            System.out.println("Description: " + deserializeItem.getDescription());
            System.out.println("Category: " + deserializeItem.getItemType());
            System.out.println("Slot: " + deserializeItem.getSlot());
            System.out.println("Value: " + deserializeItem.getValue());
        } catch (IOException e) {
            // for now use auto generated
            e.printStackTrace();
        }
    }

}
    /* Need comeback and go over this. For now leave it commented out since just want what will function
        try {
        File settingsFile = new File("Settings.xml");
        DocumentBuilderFactory settingsFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder settingsBuilder = null;
        try {
            settingsBuilder = settingsFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        Document doc = null;
        try {
            doc = settingsBuilder.parse(settingsFile);
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("bindings");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println("Current Bindings ");
                System.out.println("Movement up : "
                        + eElement.getElementsByTagName("moveup").item(0).getTextContent());
                System.out.println("Movement down : "
                       + eElement.getElementsByTagName("movedown").item(0).getTextContent() );
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        CHANGE THIS SHIT BELOW YOU BRENT!

    public void startDocument() {
        System.out.println("Player Info");
    }

    public void startElement(String uri, String localName, String qName, Attributes atrs) throws SAXException {
        System.out.print(qName + ":");
    }

    public void characters(char ch[], int start, int length) {
        System.out.print(new String(ch, start, length));
    }

    public void endElement(String uri, String localName, String qName) {
        System.out.println(qName + ":");
    }

    public void endDocument() throws SAXException {
        System.out.println("End of Player Info");
    }

     */
