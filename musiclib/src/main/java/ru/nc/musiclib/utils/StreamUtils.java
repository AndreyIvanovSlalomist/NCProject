package ru.nc.musiclib.utils;

import ru.nc.musiclib.logger.MusicLibLogger;

import java.io.*;

public class StreamUtils {

    private final static MusicLibLogger logger = new MusicLibLogger(StreamUtils.class);

    public static void streamToFile(ObjectInputStream inputStream, String fileName) {
        File file = new File(fileName);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

        Object line = null;
        logger.info("Start while (true)");
        while (true) {
            try {
                try {
                    line = inputStream.readObject();
                } catch (ClassNotFoundException e) {
                    logger.error(e.getLocalizedMessage());
                }
                if (line == null) break;
                out.write((String)line);
                out.newLine();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        try {
            out.flush();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        try {
            out.close();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

    }

    public static void fileToStream(ObjectOutputStream out, String fileName) {

        BufferedReader xmlfile = null;
        try {
            xmlfile = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            logger.error(e.getLocalizedMessage());
        }
        String xmldata;
        while (true) {
            try {
                if ((xmldata = xmlfile.readLine()) == null) break;
                out.writeObject(xmldata);
                out.flush();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        try {
            out.writeObject(null);
            out.flush();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

        try {
            xmlfile.close();
            logger.info("xmlfile.close();");
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    public static void saveToSerializable(Object object, String fileName) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(object);
            out.close();
        } catch (IOException e) {
            logger.error("Ошибка при сохранении!");
        }
    }

    public static  Object loadObjectFromFileInputStream(String fileName) {
        Object object = null;
        try {
            ObjectInputStream in = new ObjectInputStream(getFIle(fileName));
            object = in.readObject();
            in.close();
        } catch (IOException e) {
            logger.error("Ошибка ввода/вывода при загрузке!");
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка при загрузке, класс не найден!");
        }
        return object;
    }
    public static FileInputStream getFIle(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            logger.error("Ошибка при чтении из файла");
        }
        return null;
    }

}
