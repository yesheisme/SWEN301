package nz.ac.wgtn.swen301.a3.server;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/stats/html")
public class StatsHTMLServlet {
    Persistency DB = new Persistency();

    // default constructor
    public StatsHTMLServlet() {}

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        Map<String, Map<String, Integer>> logMap = DB.getLogMap();
        PrintWriter writer = response.getWriter();
        writer.write("<html>");
        writer.write("<head>");
        writer.write("<title>Statistics</title>");
        writer.write("</head>");
        writer.write("<body>");
        writer.write("<table>");
        writer.write("<tr>");
        writer.write("<th>Logger</th><th>ALL</th><th>TRACE</th><th>DEBUG</th><th>INFO</th><th>WARN</th><th>ERROR</th><th>FATAL</th><th>OFF</th>");
        writer.write("</tr>");

        for (String logger : logMap.keySet()) {
            writer.write("<tr>");
            writer.write("<td>" + logger + "</td>");
            Map<String, Integer> logStats = logMap.get(logger);
            writer.write("<td>" + logStats.get("ALL") + "</td>");
            writer.write("<td>" + logStats.get("TRACE") + "</td>");
            writer.write("<td>" + logStats.get("DEBUG") + "</td>");
            writer.write("<td>" + logStats.get("INFO") + "</td>");
            writer.write("<td>" + logStats.get("WARN") + "</td>");
            writer.write("<td>" + logStats.get("ERROR") + "</td>");
            writer.write("<td>" + logStats.get("FATAL") + "</td>");
            writer.write("<td>" + logStats.get("OFF") + "</td>");
            writer.write("</tr>");  
        }
        writer.write("</table>");
        writer.write("</body>");
        writer.write("</html>");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
