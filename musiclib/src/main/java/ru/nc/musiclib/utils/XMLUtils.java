package ru.nc.musiclib.utils;

import ru.nc.musiclib.logger.MusicLibLogger;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XMLUtils {
    private XMLUtils() {
    }

    private final static MusicLibLogger logger = new MusicLibLogger(XMLUtils.class);
    public static void saveToXML(Object object, String fileName, Class... classes) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(classes);
        } catch (JAXBException e) {
            logger.error("newInstance Exception");
            return;
        }
        Marshaller jaxbMarshaller = null;
        try {
            jaxbMarshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            logger.error("createMarshaller Exception");
            return;
        }

        try {
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (PropertyException e) {
            logger.error("setProperty Exception");
            return;
        }

        try {
            jaxbMarshaller.marshal(object, new File(fileName));
        } catch (JAXBException e) {
            logger.error("marshal Exception");
        }

    }

    public static Object loadFromXml(String fileName, Class... classes) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(classes);
        } catch (JAXBException e) {
            logger.error("newInstance Exception");
            return null;
        }
        Unmarshaller jaxbUnmarshaller = null;
        try {
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            logger.error("createUnmarshaller Exception");
            return null;
        }

        try {
            return jaxbUnmarshaller.unmarshal(new FileInputStream(fileName));
        } catch (JAXBException | FileNotFoundException e) {
            logger.error("unmarshal Exception");
            return null;
        }
    }

}
