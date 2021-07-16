package handlers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHandler {
    public static String getStringFromProperty(String key){
        try {
            Properties properties = new Properties();

            FileInputStream fileStream = new FileInputStream("src\\main\\resources\\config.properties");

            properties.load(fileStream);

            return String.valueOf(properties.get(key));
        } catch (IOException e) {
            e.printStackTrace();

            return e.getMessage();
        }
    }
}
