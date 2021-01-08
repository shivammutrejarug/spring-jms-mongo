package com.eaiproject;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class ServerResponse {
    public String requestId;
    public String action;
    public String data;
    public String message;
    public Integer statusCode;

    public String marshal() {
        JAXBContext jc = null;
        StringWriter stringResponse = new StringWriter();
        try {
            jc = JAXBContext.newInstance(ServerResponse.class);
            Marshaller marshaller = jc.createMarshaller();

            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
            // marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(this, stringResponse);
        } catch (JAXBException e) {
           System.out.println("[ServerResponse.Marshal] Error while marshalling response " + this.toString() + " : " + e.toString());
        }

        return stringResponse.toString();
    }
}
