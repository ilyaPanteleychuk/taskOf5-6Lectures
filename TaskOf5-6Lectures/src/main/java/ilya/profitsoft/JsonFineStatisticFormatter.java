package ilya.profitsoft;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class JsonFineStatisticFormatter {
    
    private final JsonReader jsonReader;
    
    public JsonFineStatisticFormatter(JsonReader jsonReader) {
        this.jsonReader = jsonReader;
    }
    
    public void calculateSharedStatistic(File folder, ExecutorService executorService) {
        File[] files = folder.listFiles();
        for(File file : files) {
            CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread().getName() + " Ya ibashu salto");
                jsonReader.calculateCurrentFile(file);
            }, executorService);
        }
        executorService.shutdown();
    }
}
