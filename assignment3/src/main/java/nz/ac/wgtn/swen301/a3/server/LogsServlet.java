package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@WebServlet("/logs")
public class LogsServlet extends HttpServlet {
    private Persistency DB = new Persistency();
    List<String> levels = Arrays.asList("ALL", "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF");

    // default constructor
    public LogsServlet() {}

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<JsonObject> result = new ArrayList<>();

        String requestLevel = "";
        int limit = 0;

        if (request.getParameter("level") != null) requestLevel = request.getParameter("level");
        if (request.getParameter("limit") != null) limit = Integer.parseInt(request.getParameter("limit"));

        // checking for bad level and limit
        if (!requestLevel.equals("")) {
            if (levels.contains(requestLevel.toUpperCase())) {
                result = DB.getLogsByLevel(requestLevel, limit);
            } else { 
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return; 
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (limit <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);
        response.setContentType("application/json");
        result.sort((one, two) -> {
            try {
                return dateTimeFormatter.parse(one.get("timestamp").getAsString())
                    .compareTo(dateTimeFormatter.parse(two.get("timestamp").getAsString()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Gson gson = DB.getGson();
        JsonArray jsonArray = new JsonArray();
        for (JsonObject logEvent: result) {
            jsonArray.add(logEvent);
        }
        response.getWriter().write(gson.toJson(jsonArray));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObj = requestToJson(request, response);

        if (jsonObj == null)
            return;
        DB.addLogEvent(jsonObj);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Sucess");
    }

    private JsonObject requestToJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder jsonLog = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonLog.append(line);
        }
        Gson gson = DB.getGson();
        JsonObject logEvent = gson.fromJson(jsonLog.toString(), JsonObject.class);
        if (!checkRequired(logEvent)) {
            System.out.println("Invalid log");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if (!levels.contains(logEvent.get("level").getAsString().toUpperCase())) {
            System.out.println("invalid level");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return logEvent;
    }

    private boolean checkRequired(JsonObject logEvent) {
            try {
                if (logEvent.get("timestamp").getAsString().equals("") || logEvent.get("level").getAsString().equals("") || logEvent.get("logger").getAsString().equals("") || logEvent.get("thread").getAsString().equals("") || logEvent.get("message").getAsString().equals("") || logEvent.get("id").getAsString().equals("")) return false;
            } catch (Exception e) { return false; }
        return true;
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DB.clear();
        response.setContentType("html/text");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("DB cleared");
    }
}