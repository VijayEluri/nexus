package org.isolution.nexus.xml;

import org.junit.Before;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * User: agwibowo
 * Date: 28/12/10
 * Time: 12:31 AM
 */
public abstract class AbstractXMLUnitTest {
    private XMLInputFactory xmlInputFactory;

    @Before
    public void setup() {
        xmlInputFactory = XMLInputFactory.newInstance();
    }

    protected XMLStreamReader getXMLStreamReader(String xmlString) throws XMLStreamException {
        return xmlInputFactory.createXMLStreamReader(new StringReader(xmlString));
    }

    protected XMLStreamReader getXMLStreamReader(byte[] bytes) throws XMLStreamException {
        return xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(bytes));
    }

    protected XMLStreamReader getXMLStreamReader(String xmlString, String charEncoding)
            throws UnsupportedEncodingException, XMLStreamException {
        byte[] messageBytes = xmlString.getBytes(charEncoding);
        return getXMLStreamReader(messageBytes);
    }
}
