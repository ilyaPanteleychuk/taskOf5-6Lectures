package ilya.profitsoft.task1;

import ilya.profitsoft.task1.service.JsonFineStatisticFormatter;
import ilya.profitsoft.task1.service.JsonReader;
import ilya.profitsoft.task1.service.XmlFineFileProcessor;
import ilya.profitsoft.task1.utils.PropertyReader;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
    
    private static final String PROPERTY_PATH =
            "src/main/resources/task1/application.properties";
    
    public static void main(String[] args) {
        Properties properties = PropertyReader.readProperties(PROPERTY_PATH);
        try {
            JsonFineStatisticFormatter jsonFineStatisticFormatter =
                    new JsonFineStatisticFormatter(new JsonReader());
            File file = new File(properties.getProperty("task1.jsonsFolder"));
            ExecutorService executorService = Executors.newFixedThreadPool(8);
            jsonFineStatisticFormatter.calculateSharedStatistic(file, executorService);
            executorService.awaitTermination(1000, TimeUnit.SECONDS);
            XmlFineFileProcessor xmlFineFileProcessor = new XmlFineFileProcessor();
            xmlFineFileProcessor.writeFinesToXml(JsonReader.getCommonMap(),
                    new File(properties.getProperty("task1.output")));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
