package com.codenotfound;

import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class FetchData {
    public String Id;

    public static FetchData unmarshal(String jsonData) {
        JAXBContext jc = null;
        FetchData data = null;
        try {
            jc = JAXBContext.newInstance(FetchData.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
            unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);

            data = (FetchData) unmarshaller.unmarshal(new StringReader(jsonData));
        } catch (JAXBException e) {
            System.out.println("[FetchData.Unmarshal] Error in unmarshalling fetchData " + jsonData + " : " + e.toString());
        }

        return data;
    }
}
