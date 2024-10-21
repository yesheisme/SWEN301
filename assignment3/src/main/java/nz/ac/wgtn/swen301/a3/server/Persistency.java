package nz.ac.wgtn.swen301.a3.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Persistency {
    public static List<JsonObject> DB = new ArrayList<>();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Map<String, Map<String, Integer>> logMap = new HashMap<>();

    public List<JsonObject> getLogsByLevel(String requestLevel, int limit) {
        List<JsonObject> res = new ArrayList<>();
        for (int i = 0; i < DB.size(); i++) {
            JsonObject logEvent = DB.get(i);
            if (logEvent.get("level").getAsString().toUpperCase().equals(requestLevel.toUpperCase())) res.add(logEvent);
            if (res.size() == limit) break;
        }
        return res;
    }

    public void addLogEvent(JsonObject log) { 
        DB.add(log);
        incrementLvl(log.get("logger").getAsString(), log.get("level").getAsString().toUpperCase());
    }

    public Gson getGson() { return gson; }
    public void clear() { DB.clear(); clearLogs(); }
    public List<JsonObject> getDB() { return Collections.unmodifiableList(DB); }

    public Map<String, Map<String, Integer>> getLogMap() {
        return Collections.unmodifiableMap(logMap);
    }

    public void incrementLvl(String log, String level) {
        Map<String, Integer> currentLog = logMap.get(log);
        if (currentLog == null) {
            currentLog = logMap.get(log);
            addLog(log);
        } else {currentLog.put(level.toUpperCase(), currentLog.get(level.toUpperCase()) + 1);}
    }

    public void addLog(String log) {
        logMap.putIfAbsent(log, new HashMap<>(){{
            put("ALL", 0);
            put("TRACE", 0);
            put("DEBUG", 0);
            put("INFO", 0);
            put("WARN", 0);
            put("ERROR", 0);
            put("FATAL", 0);
            put("OFF", 0);
        }});
    }

    public void clearLogs() {
        logMap = new HashMap<String, Map<String, Integer>>();
    }
    
}