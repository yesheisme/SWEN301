package nz.ac.wgtn.swen301.a3.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.gson.JsonObject;

public class TestDeleteLogs {

    @Test
    public void TestDelete() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        Persistency DB = new Persistency();

        JsonObject json = new JsonObject();
        json.addProperty("timestamp", "15-05-2003 10:30:00");
        json.addProperty("thread", "main");
        json.addProperty("logger", "test");
        json.addProperty("id", "1");
        json.addProperty("level", "ERROR");
        json.addProperty("message", "test message");
        json.addProperty("errorDetails", "test error");

        DB.addLogEvent(json);

        JsonObject json2 = json.deepCopy();
        json2.addProperty("id", "2");
        json2.addProperty("level", "WARN");
        json2.addProperty("timestamp", "21-12-2000 13:30:45");

        DB.addLogEvent(json2);

        JsonObject json3 = json.deepCopy();
        json3.addProperty("id", "your-new-id-3");
        json3.addProperty("timestamp", "01-20-2023 10:15:30");
        json3.addProperty("level", "ERROR");

        DB.addLogEvent(json3);

        service.doDelete(request, response);
        assertEquals(200, response.getStatus());
        assertEquals(DB.getDB().size(), 0);
    }
    
}