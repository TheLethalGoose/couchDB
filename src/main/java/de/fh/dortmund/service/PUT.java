package de.fh.dortmund.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

import static de.fh.dortmund.Main.COUCHDB_URL;
import static de.fh.dortmund.service.GET.getDocumentById;

public class PUT {

    public static HttpResponse addPostToFavorites(String userId, String postId) throws IOException {


        HttpPut request = new HttpPut(COUCHDB_URL + "/" + userId);

        JsonObject userDocument = getDocumentById(userId);

        if (userDocument.has("Favoriten")) {
            JsonArray favoritenArray = userDocument.getAsJsonArray("Favoriten");
            favoritenArray.add(postId);
        } else {
            JsonArray favoritenArray = new JsonArray();
            favoritenArray.add(postId);
            userDocument.add("Favoriten", favoritenArray);
        }

        StringEntity requestEntity = new StringEntity(userDocument.toString(), ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        return executeRequest(request);
    }

    public static HttpResponse updatePost(String postId, String content) throws IOException {

        HttpPut request = new HttpPut(COUCHDB_URL + "/" + postId);
        JsonObject postDocument = getDocumentById(postId);

        postDocument.addProperty("Content", content);
        postDocument.addProperty("ModifiedAt", LocalDateTime.now().toString());

        StringEntity requestEntity = new StringEntity(postDocument.toString(), ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        return executeRequest(request);
    }

    public static HttpResponse markAnswerAsAccepted(String answerId) throws IOException {

        HttpPut request = new HttpPut(COUCHDB_URL + "/" + answerId);
        JsonObject postDocument = getDocumentById(answerId);

        postDocument.addProperty("Accepted", true);
        StringEntity requestEntity = new StringEntity(postDocument.toString(), ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        return executeRequest(request);
    }

    private static HttpResponse executeRequest(HttpPut request) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient.execute(request);
    }

}
