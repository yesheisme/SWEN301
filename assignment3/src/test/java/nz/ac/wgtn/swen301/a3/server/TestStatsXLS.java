package nz.ac.wgtn.swen301.a3.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayInputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.google.gson.JsonObject;

public class TestStatsXLS {
    MockHttpServletRequest req;
    MockHttpServletResponse res;
    StatsExcelServlet serv;
    Persistency DB = new Persistency();

    @BeforeEach
    public void init() {
        DB.clear();
        serv = new StatsExcelServlet();
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
    public void testGetXLS() throws Exception {
        serv.doGet(req, res);
        assertEquals(200, res.getStatus());
    }

    @Test
    public void testContent() throws Exception {
        serv.doGet(req, res);
        assertEquals(200, res.getStatus());

        XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(res.getContentAsByteArray()));
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(1);

        assertEquals("test", row.getCell(0).getStringCellValue());
        assertEquals(1, row.getCell(4).getNumericCellValue());
        assertEquals(0, row.getCell(5).getNumericCellValue());
        assertEquals(1, row.getCell(6).getNumericCellValue());
    }

    @Test
    public void testHeader() throws Exception {
        serv.doGet(req, res);
        assertEquals(200, res.getStatus());
        XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(res.getContentAsByteArray()));
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(0);

        assertEquals("Logger", row.getCell(0).getStringCellValue());
        assertEquals("ALL", row.getCell(1).getStringCellValue());
        assertEquals("TRACE", row.getCell(2).getStringCellValue());
        assertEquals("DEBUG", row.getCell(3).getStringCellValue());
        assertEquals("INFO", row.getCell(4).getStringCellValue());
        assertEquals("WARN", row.getCell(5).getStringCellValue());
        assertEquals("ERROR", row.getCell(6).getStringCellValue());
        assertEquals("FATAL", row.getCell(7).getStringCellValue());
        assertEquals("OFF", row.getCell(8).getStringCellValue());
    }
}