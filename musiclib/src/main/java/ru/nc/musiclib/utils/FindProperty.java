package ru.nc.musiclib.utils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

public class FindProperty {
    private FindProperty() {
    }

    private static final String PROPERTIES_PATH = "musiclib.properties";
    private static final String CUSTOM_PROPERTIES_PATH = "musiclib_custom.properties";

    private static String getPropertiesInFile(String nameProperty, String propertiesPath) throws IOException {
        Properties properties = new Properties();
        InputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(propertiesPath);
            properties.load(fileInputStream);
            return properties.getProperty(nameProperty);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден " + propertiesPath);
        }
        return null;
    }

    public static String getProperty(String nameProperty) {
        String filePath = getParentDirectoryFromJar() + File.separator + CUSTOM_PROPERTIES_PATH;
        try {
            String valueProperty = getPropertiesInFile(nameProperty, filePath);
            if (valueProperty == null) {
                filePath = FindProperty.class.getClassLoader().getResource(PROPERTIES_PATH).getPath();
                valueProperty = getPropertiesInFile(nameProperty, filePath);
            }
            System.out.println(" Имя параметра " + nameProperty + " Значение параметра " + valueProperty + " Из файла " + filePath);
            return valueProperty;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getParentDirectoryFromJar() {
        String dirtyPath = FindProperty.class.getResource("").toString();
        String jarPath = dirtyPath.replaceAll("^.*file:/", ""); //removes file:/ and everything before it
        jarPath = jarPath.replaceAll("jar!.*", "jar"); //removes everything after .jar, if .jar exists in dirtyPath
        jarPath = jarPath.replaceAll("%20", " "); //necessary if path has spaces within
        if (!jarPath.endsWith(".jar")) { // this is needed if you plan to run the app using Spring Tools Suit play button.
            jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
        }
        return Paths.get(jarPath).getParent().toString();
    }
}
