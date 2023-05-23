package de.fh.dortmund.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class POST {

    Gson gson = new Gson();
    HttpClient httpClient = HttpClientBuilder.create().build();
    String databaseName;

    public POST(String databaseName) {
        this.databaseName = databaseName;
    }

    public HttpResponse post(Object object) throws IOException {

        HttpPost request = new HttpPost("http://localhost:5984/" + databaseName);

        request.setEntity(new StringEntity(gson.toJson(object), ContentType.APPLICATION_JSON));

        return executeRequest(request);

    }
    public HttpResponse post(List<?> objects) throws IOException {


        HttpPost request = new HttpPost("http://localhost:5984/" + databaseName + "/_bulk_docs");

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new Gson().toJsonTree(objects).getAsJsonArray();
        jsonObject.add("docs", jsonArray);

        request.setEntity(new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON));

        return executeRequest(request);

    }

    private HttpResponse executeRequest(HttpPost request) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();

        return httpClient.execute(request);

    }

}
