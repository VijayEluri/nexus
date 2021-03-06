package org.isolution.nexus.xml;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.isolution.nexus.domain.ServiceURI;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A representation of a single SOAP message that will be read from a given {@link XMLStreamReader}.
 * <p/>
 * User: Alex Wibowo
 * Date: 27/12/10
 * Time: 8:03 PM
 */
public class SOAPDocument {

    private StAXOMBuilder stAXOMBuilder;

    private final SoapMessage rawSoapMessage;

    public SOAPDocument(SoapMessage rawSoapMessage, XMLStreamReader soapPayloadReader) {
        checkNotNull(rawSoapMessage);
        this.stAXOMBuilder = new StAXOMBuilder(soapPayloadReader);
        this.rawSoapMessage = rawSoapMessage;
    }

    /**
     *
     * @return {@link org.isolution.nexus.domain.ServiceURI} for the XML document containing the namespace for the root element
     * as the {@link org.isolution.nexus.domain.Service#serviceURI}'s{@link org.isolution.nexus.domain.ServiceURI#namespace}
     * and the root element name as the {@link org.isolution.nexus.domain.Service#serviceURI}'s {@link org.isolution.nexus.domain.ServiceURI#localName}
     */
    public ServiceURI getServiceURI() {
        OMElement documentElement = getDocumentElement();
        OMNamespace omNamespace = documentElement.getNamespace();
        String namespaceURI = omNamespace!=null?omNamespace.getNamespaceURI():null;
        String localName = documentElement.getLocalName();
        return new ServiceURI(namespaceURI, localName);
    }

    public SoapMessage getRawSoapMessage() {
        return rawSoapMessage;
    }

    private OMElement getDocumentElement() {
        return stAXOMBuilder.getDocumentElement();
    }

    /**
     * @param os {@link OutputStream} to write the SOAP document payload to
     * @throws XMLStreamException exception that occurred during the XML serialization
     */
    public void writePayloadTo(OutputStream os)
            throws XMLStreamException {
        stAXOMBuilder.getDocument().serialize(os);
    }

    /**
     * @return character encoding of the SOAP document
     */
    public String getCharacterEncoding() {
        return stAXOMBuilder.getCharacterEncoding();
    }
}
