package de.fh.dortmund.service;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class GET {

    Gson gson = new Gson();
    HttpClient httpClient = HttpClientBuilder.create().build();
    String databaseName;

    public GET(String databaseName) {
        this.databaseName = databaseName + "/";
    }

    public int GET(String documentName) {
        HttpGet request = new HttpGet("http://localhost:5984/" + databaseName + documentName);
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getStatusLine().getStatusCode();

    }




}
