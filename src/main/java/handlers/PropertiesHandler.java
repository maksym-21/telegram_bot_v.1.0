package handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesHandler.class);

    public static String getStringFromProperty(String key){
        try {
            Properties properties = new Properties();

            FileInputStream fileStream = new FileInputStream("src\\main\\resources\\config.properties");

            properties.load(fileStream);

            return String.valueOf(properties.get(key));
        } catch (IOException e) {
            LOG.error("Exception -> {}",e.getMessage());

            return e.getMessage();
        }
    }
}
