package com.codenotfound;

import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringReader;


@XmlRootElement
public class ServerMessage {
    public String requestId;
    public String action;
    public String data;

    public static ServerMessage unmarshal(String jsonMessage)  {
        JAXBContext jc = null;
        ServerMessage serverMessage = null;
        try {
            jc = JAXBContext.newInstance(ServerMessage.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
            unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);

            serverMessage = (ServerMessage) unmarshaller.unmarshal(new StringReader(jsonMessage));
        } catch (JAXBException e) {
            System.out.println("[ServerMessage.Unmarshal] Error in unmarshalling server message " + jsonMessage + " : " + e.toString());
            e.printStackTrace();
        }
        return serverMessage;
    }
}
