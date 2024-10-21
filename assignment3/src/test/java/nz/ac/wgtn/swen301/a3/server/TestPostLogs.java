package nz.ac.wgtn.swen301.a3.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.google.gson.JsonObject;

public class TestPostLogs {
    LogsServlet service;
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    Persistency DB = new Persistency();

    @BeforeEach
    public void init() {
        DB.clear();
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        service = new LogsServlet();
    }

    @Test
    public void testValidPost() throws ServletException, IOException {
        JsonObject logEvent = new JsonObject();
        logEvent.addProperty("id", "1");
        logEvent.addProperty("message", "test message");
        logEvent.addProperty("timestamp", "15-05-2003 13:30:45");
        logEvent.addProperty("thread", "main");
        logEvent.addProperty("logger", "test");
        logEvent.addProperty("level", "WARN");
        logEvent.addProperty("errorDetails", "test error");

        String json = logEvent.toString();
        request.setContent(json.getBytes());
        service.doPost(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testNoMessage() throws ServletException, IOException {
        JsonObject logEvent = new JsonObject();
        logEvent.addProperty("timestamp", "15-05-2003 13:30:45");
        logEvent.addProperty("logger", "test");
        logEvent.addProperty("thread", "main");
        logEvent.addProperty("id", "1");
        logEvent.addProperty("errorDetails", "test error");
        logEvent.addProperty("level", "WARN");

        String json = logEvent.toString();
        request.setContent(json.getBytes());
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testNoTimestamp() throws ServletException, IOException {
        JsonObject logEvent = new JsonObject();
        logEvent.addProperty("message", "test message");
        logEvent.addProperty("logger", "test");
        logEvent.addProperty("thread", "main");
        logEvent.addProperty("id", "1");
        logEvent.addProperty("errorDetails", "test error");
        logEvent.addProperty("level", "WARN");

        String json = logEvent.toString();
        request.setContent(json.getBytes());
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testNoThread() throws ServletException, IOException {
        JsonObject logEvent = new JsonObject();
        logEvent.addProperty("message", "test message");
        logEvent.addProperty("logger", "test");
        logEvent.addProperty("timestamp", "15-05-2003 13:30:45");
        logEvent.addProperty("id", "1");
        logEvent.addProperty("errorDetails", "test error");
        logEvent.addProperty("level", "WARN");

        String json = logEvent.toString();
        request.setContent(json.getBytes());
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testNoId() throws ServletException, IOException {
        JsonObject logEvent = new JsonObject();
        logEvent.addProperty("message", "test message");
        logEvent.addProperty("timestamp", "15-05-2003 13:30:45");
        logEvent.addProperty("thread", "main");
        logEvent.addProperty("logger", "test");
        logEvent.addProperty("level", "WARN");
        logEvent.addProperty("errorDetails", "test error");

        String json = logEvent.toString();
        request.setContent(json.getBytes());
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testNoLogger() throws ServletException, IOException {
        JsonObject logEvent = new JsonObject();
        logEvent.addProperty("id", "1");
        logEvent.addProperty("message", "test message");
        logEvent.addProperty("timestamp", "15-05-2003 13:30:45");
        logEvent.addProperty("thread", "main");
        logEvent.addProperty("level", "WARN");
        logEvent.addProperty("errorDetails", "test error");

        String json = logEvent.toString();
        request.setContent(json.getBytes());
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testNoLevel() throws ServletException, IOException {
        JsonObject logEvent = new JsonObject();
        logEvent.addProperty("id", "1");
        logEvent.addProperty("message", "test message");
        logEvent.addProperty("timestamp", "15-05-2003 13:30:45");
        logEvent.addProperty("thread", "main");
        logEvent.addProperty("logger", "test");
        logEvent.addProperty("errorDetails", "test error");

        String json = logEvent.toString();
        request.setContent(json.getBytes());
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testBadLevel() throws ServletException, IOException {
        JsonObject logEvent = new JsonObject();
        logEvent.addProperty("id", "1");
        logEvent.addProperty("message", "test message");
        logEvent.addProperty("timestamp", "15-05-2003 13:30:45");
        logEvent.addProperty("thread", "main");
        logEvent.addProperty("logger", "test");
        logEvent.addProperty("level", "BAD_LEVEL");
        logEvent.addProperty("errorDetails", "test error");

        String json = logEvent.toString();
        request.setContent(json.getBytes());
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }
}