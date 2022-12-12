package ilya.profitsoft.task1.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JsonReader {
    
    private static final Map<String, Double> commonMap = new HashMap<>();
    private static final ReentrantLock lock = new ReentrantLock();
    
    public void calculateCurrentFile(File file){
        //regex to find json from till closing }
        String regex = "(.*}).*";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        ObjectMapper objectMapper = new ObjectMapper();
        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String currentLine = "";
            String fileContent = "";
            while((currentLine = bufferedReader.readLine()) != null){
                fileContent = fileContent.concat(currentLine);
                Matcher jsonMatcher = pattern.matcher(fileContent);
                while (jsonMatcher.find()){
                    //to let objectMapper.readTree works correctly
                    fileContent = fileContent.replace("[", "")
                            .replace("]", "");
                    JsonNode jsonNode = objectMapper.readTree(fileContent);
                    double fineAmount = jsonNode.get("fine_amount").asDouble();
                    lock.lock();
                    commonMap.compute(jsonNode.get("type").asText(), (k, v) ->
                            (v == null) ? fineAmount : v + fineAmount);
                    fileContent = "";
                    lock.unlock();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Map<String, Double> getCommonMap(){
        return commonMap;
    }
}
