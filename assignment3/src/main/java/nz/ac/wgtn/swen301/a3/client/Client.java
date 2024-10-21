package nz.ac.wgtn.swen301.a3.client;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class Client {
    private static HttpClient httpClient;
    private static String fileName;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("File type/name missing");
        }

        // Setup the file type and name
        String url = "http://localhost:8080/logstore/stats/";
        String type = args[0];
        fileName = args[1];
        httpClient = HttpClients.createDefault();

        if (!(type.equalsIgnoreCase("excel") || type.equalsIgnoreCase("csv"))) { 
            throw new Exception("Invalid file type. Expected 'csv' or 'excel'");
        }

        // Append the correct file extension
        if (type.equalsIgnoreCase("csv")) {
            fileName += ".csv";
        } else {
            fileName += ".xlsx";
        }

        // Create the appropriate URL
        String requestUrl = url + (type.equalsIgnoreCase("csv") ? "csv" : "excel");
        HttpGet httpGet = new HttpGet(requestUrl);

        try {
            // Execute the request and get the response
            HttpResponse res = httpClient.execute(httpGet);

            // Check if the request was successful (HTTP status code 200)
            if (res.getStatusLine().getStatusCode() == 200) {
                // Try-with-resources to automatically close streams
                try (OutputStream out = new FileOutputStream(fileName);
                     InputStream in = res.getEntity().getContent()) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Data saved to " + fileName);
                }
            } else {
                System.out.println("Failed to fetch data. Status code: " + res.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            System.out.println("Error occurred while fetching data: " + e.getMessage());
        }
    }
}