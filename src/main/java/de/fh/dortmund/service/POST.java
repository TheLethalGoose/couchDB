package de.fh.dortmund.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class POST {

    Gson gson = new Gson();
    HttpClient httpClient = HttpClientBuilder.create().build();
    String databaseName;

    public POST(String databaseName) {
        this.databaseName = databaseName + "/";
    }

    public int POST(Object object) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:5984/" + databaseName);
        request.setEntity(new StringEntity(gson.toJson(object), ContentType.APPLICATION_JSON));
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response.getStatusLine().getStatusCode();

    }
}
