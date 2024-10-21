package nz.ac.wgtn.swen301.a3.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestGetLogs {
    MockHttpServletRequest req;
    MockHttpServletResponse res;
    LogsServlet serv;
    Persistency DB = new Persistency();

    @BeforeEach
    public void init() {
        DB.clear();
        serv = new LogsServlet();
        res = new MockHttpServletResponse();
        req = new MockHttpServletRequest();

        JsonObject json = new JsonObject();
        json.addProperty("id", "1");
        json.addProperty("level", "INFO");
        json.addProperty("timestamp", "9-03-2023 08:17:55");
        json.addProperty("thread", "main");
        json.addProperty("logger", "test");
        json.addProperty("message", "test message");
        json.addProperty("errorDetails", "test error");

        JsonObject json2 = json.deepCopy();
        json2.addProperty("id", "2");
        json2.addProperty("level", "ERROR");
        json2.addProperty("timestamp", "11-01-2002 13:30:45");
        
        JsonObject json3 = json.deepCopy();
        json3.addProperty("id", "3");
        json3.addProperty("level", "WARN");
        json3.addProperty("timestamp", "07-10-2022 15:42:30");
        
        DB.addLogEvent(json);
        DB.addLogEvent(json2);
        DB.addLogEvent(json3);
    }

    @Test
    public void testGetLogs() throws ServletException, IOException {
        req.setMethod("GET");
        req.setRequestURI("/logs");
        serv.doGet(req, res);
        assertEquals(400, res.getStatus());
    }

    @Test
    public void testContent() throws ServletException, IOException {
        req.setParameter("level", "WARN");
        req.setParameter("limit", "7");
        serv.doGet(req, res);
        assertEquals("application/json", res.getContentType());
    }

    @Test
    public void testValidGet() throws ServletException, IOException {
        req.setMethod("GET");
        req.setRequestURI("/logs");
        req.setParameter("limit", "7");
        req.setParameter("level", "WARN");
        serv.doGet(req, res);
        assertEquals(200, res.getStatus());
        Gson gson = new Gson();
        String json = gson.toJson(DB.getLogsByLevel("WARN", 4));
        String normalResp = JsonParser.parseString(res.getContentAsString()).toString();
        assertEquals(json, normalResp);
    }

    @Test
    public void testBadLevel_1() throws ServletException, IOException {
        req.setMethod("GET");
        req.setRequestURI("/logs");
        req.setParameter("limit", "7");
        req.setParameter("level", "BAD_LEVEL");
        serv.doGet(req, res);
        assertEquals(400, res.getStatus());
    }

    @Test
    public void testBadLevel_2() throws ServletException, IOException {
        req.setMethod("GET");
        req.setRequestURI("/logs");
        req.setParameter("limit", "7");
        req.setParameter("fakeLevel", "FAKE");
        serv.doGet(req, res);
        assertEquals(400, res.getStatus());
    }

    @Test
    public void testNegativeLimit() throws ServletException, IOException {
        req.setMethod("GET");
        req.setRequestURI("/logs");
        req.setParameter("limit", "-7");
        req.setParameter("level", "INFO");
        serv.doGet(req, res);
        assertEquals(400, res.getStatus());
    }
}