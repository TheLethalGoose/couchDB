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

import static de.fh.dortmund.Main.COUCHDB_URL;

public class POST {

    public static HttpResponse post(Object object) throws IOException {

        HttpPost request = new HttpPost(COUCHDB_URL);
        Gson gson = new Gson();

        JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();
        jsonObject.addProperty("type", object.getClass().getSimpleName().toLowerCase());

        request.setEntity(new StringEntity(jsonObject.getAsString(), ContentType.APPLICATION_JSON));

        return executeRequest(request);

    }
    public static HttpResponse post(List<?> objects) throws IOException {


        HttpPost request = new HttpPost(COUCHDB_URL + "/_bulk_docs");
        Gson gson = new Gson();
        JsonObject docs = new JsonObject();

        JsonArray jsonArray = objects.stream()
                .map(object -> {
                    JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();
                    String type = object.getClass().getSimpleName().toLowerCase();
                    jsonObject.addProperty("type", type);
                    return jsonObject;
                })
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);

        docs.add("docs", jsonArray);

        request.setEntity(new StringEntity(docs.toString(), ContentType.APPLICATION_JSON));

        return executeRequest(request);

    }

    private static HttpResponse executeRequest(HttpPost request) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient.execute(request);
    }

}
