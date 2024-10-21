package nz.ac.wgtn.swen301.a3.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet("/stats/excel") 
public class StatsExcelServlet {
    Persistency DB = new Persistency();

    public StatsExcelServlet() {}

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Content-Disposition", "attachment; filename=statistics.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        Map<String, Map<String, Integer>> logMap = DB.getLogMap();
        int rowNum = 0;
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Statistics");
        Row headerRow = sheet.createRow(rowNum++);

        String[] hdrs = { "Logger", "ALL", "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF" };

        for (int i = 0; i < hdrs.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(hdrs[i]);
        }

        for (String logger : logMap.keySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(logger);
            Map<String, Integer> logStats = logMap.get(logger);
            for (int i = 1; i < hdrs.length; i++) {
                String level = hdrs[i];
                row.createCell(i).setCellValue(logStats.get(level));
            }
        }

        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
            workbook.close();
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}