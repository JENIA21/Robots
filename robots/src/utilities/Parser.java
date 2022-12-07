package utilities;

import com.google.gson.GsonBuilder;
import gui.GameVisualizer;
import com.google.gson.Gson;
import utilities.parsedclass.GameVisualizerData;

public class Parser {
    private static final Gson GSON =
            new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).create();
    public static synchronized String convertObjectToJson(Object obj){
        return GSON.toJson(obj);
    }
    public static synchronized GameVisualizerData parseFromJsonToObject(String json){
        return GSON.fromJson(json, GameVisualizerData.class);
    }
}
