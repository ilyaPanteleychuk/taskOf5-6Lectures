package ilya.profitsoft.task1.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class PropertyReader {
    
    public static Properties readProperties(String path){
        Properties properties = new Properties();
        try(FileReader reader = new FileReader(path)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
