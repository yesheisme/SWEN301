package nz.ac.wgtn.swen301.a3.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestStatsHTML {
    MockHttpServletRequest req;
    MockHttpServletResponse res;
    StatsHTMLServlet serv;
    Persistency DB = new Persistency();

    @BeforeEach
    public void init() {
        DB.clear();
        serv = new StatsHTMLServlet();
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
    public void testGetHTML() throws Exception {
        serv.doGet(req, res);
        assertEquals(200, res.getStatus());
    }

    @Test
    public void testContent() throws Exception {
        serv.doGet(req, res);
        assertEquals(200, res.getStatus());
        String htmlContent = res.getContentAsString();
        Document doc = Jsoup.parse(htmlContent);

        Element table = doc.select("table").first();
        Element row = table.select("tr").get(1);
        assertEquals("test", row.select("td").get(0).text());
        assertEquals("1", row.select("td").get(4).text());
        assertEquals("0", row.select("td").get(5).text());
        assertEquals("1", row.select("td").get(6).text());
    }

    @Test
    public void testHeader() throws Exception {
        serv.doGet(req, res);
        assertEquals(200, res.getStatus());
        String htmlContent = new String(res.getContentAsByteArray(), StandardCharsets.UTF_8);
        Document doc = Jsoup.parse(htmlContent);
        Element table = doc.select("table").first();
        Element headerRow = table.select("tr").first();
        
        assertNotNull(headerRow);
        assertNotNull(table);
        assertEquals("Logger", headerRow.select("th").get(0).text());
        assertEquals("ALL", headerRow.select("th").get(1).text());
        assertEquals("TRACE", headerRow.select("th").get(2).text());
        assertEquals("DEBUG", headerRow.select("th").get(3).text());
        assertEquals("INFO", headerRow.select("th").get(4).text());
        assertEquals("WARN", headerRow.select("th").get(5).text());
        assertEquals("ERROR", headerRow.select("th").get(6).text());
        assertEquals("FATAL", headerRow.select("th").get(7).text());
        assertEquals("OFF", headerRow.select("th").get(8).text());
    }
}