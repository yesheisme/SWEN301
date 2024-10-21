package nz.ac.wgtn.swen301.a3.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.google.gson.JsonObject;

public class TestStatsCSV {
    MockHttpServletRequest req;
    MockHttpServletResponse res;
    StatsCSVServlet serv;
    Persistency DB = new Persistency();

    @BeforeEach
    public void init() {
        DB.clear();
        serv = new StatsCSVServlet();
        req = new MockHttpServletRequest();
        res = new MockHttpServletResponse();

        JsonObject json = new JsonObject();
        json.addProperty("id", "1");
        json.addProperty("message", "This is a test message");
        json.addProperty("timestamp", "04-05-2021 13:30:45");
        json.addProperty("thread", "main");
        json.addProperty("logger", "test");
        json.addProperty("level", "WARN");
        json.addProperty("errorDetails", "This is a test error");
        
        JsonObject json2 = json.deepCopy();
        json2.addProperty("id", "2");
        json2.addProperty("timestamp", "04-05-2000 13:30:45");
        json2.addProperty("level", "INFO");
        
        JsonObject json3 = json.deepCopy();
        json3.addProperty("id", "3");
        json3.addProperty("timestamp", "01-20-2023 10:15:30");
        json3.addProperty("level", "ERROR");
        
        DB.addLogEvent(json);
        DB.addLogEvent(json2);
        DB.addLogEvent(json3);
    }

    @Test
    public void testGetCSV() throws ServletException, IOException {
        req.setMethod("GET");
        serv.doGet(req, res);
        assertEquals(200, res.getStatus());
    }

    @Test
    public void testContentType() throws ServletException, IOException {
        serv.doGet(req, res);
        assertEquals("text/csv", res.getContentType());
    }

    @Test
    public void testHeader() throws ServletException, IOException {
        req.setParameter("level", "DEBUG");
        req.setParameter("limit", "1");
        serv.doGet(req, res);
        String csv = res.getContentAsString();
        String[] lines = csv.split("\n");
        assertEquals("logger\tALL\tTRACE\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tOFF", lines[0]);
    }
}