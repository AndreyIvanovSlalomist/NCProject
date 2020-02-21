package ru.nc.musiclib.utils;

import ru.nc.musiclib.logger.MusicLibLogger;

import java.io.*;

public class StreamUtils {

    private StreamUtils() {
    }

    private final static MusicLibLogger logger = new MusicLibLogger(StreamUtils.class);

    public static void streamToFile(ObjectInputStream inputStream, String fileName) {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File(fileName)))) {
            Object line;
            logger.info("Start while (true)");
            while ((line = inputStream.readObject()) != null) {
                out.write((String) line);
                out.newLine();
            }
            out.flush();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    public static void fileToStream(ObjectOutputStream out, String fileName) {
        try (BufferedReader xmlfile = new BufferedReader(new FileReader(fileName))) {
            String xmldata;
            while ((xmldata = xmlfile.readLine()) != null) {
                out.writeObject(xmldata);
                out.flush();
            }
            out.writeObject(null);
            out.flush();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    public static void saveToSerializable(Object object, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(object);
        } catch (IOException e) {
            logger.error("Ошибка при сохранении!");
        }
    }

    public static Object loadObjectFromFileInputStream(String fileName) {
        Object object = null;
        try (ObjectInputStream in = new ObjectInputStream(getFIle(fileName))) {
            object = in.readObject();
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
