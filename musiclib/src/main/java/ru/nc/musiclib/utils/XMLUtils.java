package ru.nc.musiclib.utils;

import javax.xml.bind.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLUtils {
    private final static MusicLibLogger logger = new MusicLibLogger(XMLUtils.class);

    private XMLUtils() {
    }

    public static void saveToXML(Object object, String fileName, Class... classes) {
        JAXBContext jaxbContext;
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            jaxbContext = JAXBContext.newInstance(classes);
            Marshaller jaxbMarshaller;
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(object, fileOutputStream);
        } catch (PropertyException e) {
            logger.error(e.getLocalizedMessage());
        } catch (JAXBException e) {
            logger.error(e.getLocalizedMessage());
        } catch (FileNotFoundException e) {
            logger.error(e.getLocalizedMessage());
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    public static Object loadFromXml(String fileName, Class... classes) {
        Unmarshaller jaxbUnmarshaller;
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            JAXBContext jaxbContext;
            jaxbContext = JAXBContext.newInstance(classes);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return jaxbUnmarshaller.unmarshal(fileInputStream);
        } catch (FileNotFoundException e) {
            logger.error(e.getLocalizedMessage());
        } catch (JAXBException e) {
            logger.error(e.getLocalizedMessage());
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

}
