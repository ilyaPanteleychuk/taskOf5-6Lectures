package ilya.profitsoft.task1.service;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;


public class JsonFineStatisticFormatter {
    
    private final JsonReader jsonReader;
    
    public JsonFineStatisticFormatter(JsonReader jsonReader) {
        this.jsonReader = jsonReader;
    }
    
    public void calculateSharedStatistic(File folder, ExecutorService executorService) {
        File[] files = folder.listFiles();
        for(File file : files) {
            CompletableFuture.runAsync(() -> jsonReader.calculateCurrentFile(file),
                    executorService);
        }
        executorService.shutdown();
    }
}
