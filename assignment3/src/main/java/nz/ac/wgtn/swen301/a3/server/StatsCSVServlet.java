package nz.ac.wgtn.swen301.a3.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/stats/csv")
public class StatsCSVServlet {
    Persistency DB = new Persistency();

    public StatsCSVServlet() {}

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,Map<String, Integer>> statsMap = DB.getLogMap();
        PrintWriter outWriter = response.getWriter();
        response.setContentType("text/csv");
        outWriter.write("logger\t"+"ALL\tTrace\tDebug\tInfo\tWarn\tError\tFatal\tOff".toUpperCase() +"\n");
        for (String logger : statsMap.keySet()) {
            outWriter.write(logger + "\t");
            Map<String, Integer> logStats = statsMap.get(logger);
            outWriter.write(logStats.get("ALL")+"\t");
            outWriter.write(logStats.get("TRACE")+"\t");
            outWriter.write(logStats.get("DEBUG")+"\t");
            outWriter.write(logStats.get("INFO")+"\t");
            outWriter.write(logStats.get("WARN")+"\t");
            outWriter.write(logStats.get("ERROR")+"\t");
            outWriter.write(logStats.get("FATAL")+"\t");
            outWriter.write(logStats.get("OFF")+"\n");
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
