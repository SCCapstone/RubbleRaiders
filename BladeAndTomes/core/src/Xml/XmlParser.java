package Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 Got majority of info for xml parsing from following link. Determined that Sax parsing would be
 more applicable then dom parsing. Need to update println info for later Specifically Start/End Doc
 May not be needed
 https://www.youtube.com/watch?v=aLxrk1vK0ZU
 */

/*
 Note: to implement this in other classes need to call following command lines in show
 SaxParser some variable = SaxParserFactory.newInstance().newSaxParser();
 somevariable.parse(new FileInputStream("some xml file"), new XmlParser());
 */

public class XmlParser extends DefaultHandler {

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
}
