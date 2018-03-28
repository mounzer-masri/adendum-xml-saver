package utils;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by Imona Andoid on 21.11.2017.
 */

public class PropertyReader {
    private static PropertyReader ourInstance = new PropertyReader();
    private Properties mainProperties;

    private static PropertyReader getInstance() {
        return ourInstance;
    }

    private PropertyReader() {
        try {
            //to load application's properties, we use this class
            mainProperties = new Properties();
            String path = "./main.properties";
            FileInputStream file;
            //load the file handle for main.properties
            file = new FileInputStream(path);

            //load all the properties from this file
            mainProperties.load(file);
            file.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getAppProperty(String key) {
        return PropertyReader.getInstance().mainProperties.getProperty(key);
    }
}
