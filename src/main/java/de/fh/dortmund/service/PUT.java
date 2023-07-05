package de.fh.dortmund.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.fh.dortmund.models.Answer;
import de.fh.dortmund.models.Post;
import de.fh.dortmund.models.Question;
import de.fh.dortmund.models.User;
import de.fh.dortmund.models.enums.ModerationTag;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDateTime;

import static de.fh.dortmund.Main.COUCHDB_URL;
import static de.fh.dortmund.service.GET.getDocumentById;

public class PUT {


    public static StatusLine addPostToFavorites(User user, Post post) throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(COUCHDB_URL + "/" + user.getId());
        Gson gson = new Gson();

        JsonObject userDocument = getDocumentById(user.getId());

        if (userDocument.has("Favoriten")) {
            JsonArray favoritenArray = userDocument.getAsJsonArray("Favoriten");
            favoritenArray.add(post.getId());
        } else {
            JsonArray favoritenArray = new JsonArray();
            favoritenArray.add(post.getId());
            userDocument.add("Favoriten", favoritenArray);
        }

        StringEntity requestEntity = new StringEntity(userDocument.toString(), ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == 201) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject documentResponse = gson.fromJson(responseBody, JsonObject.class);

            String documentRev = documentResponse.get("rev").getAsString();

            user.setRevision(documentRev);
        }

        return statusLine;

    }

    public static StatusLine updatePost(Post post, String content) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(COUCHDB_URL + "/" + post.getId());
        Gson gson = new Gson();

        JsonObject postDocument = getDocumentById(post.getId());

        postDocument.addProperty("Content", content);
        postDocument.addProperty("ModifiedAt", LocalDateTime.now().toString());

        StringEntity requestEntity = new StringEntity(postDocument.toString(), ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == 201) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject documentResponse = gson.fromJson(responseBody, JsonObject.class);

            String documentRev = documentResponse.get("rev").getAsString();

            post.setRevision(documentRev);
        }

        return statusLine;
    }

    public static StatusLine markAnswerAsAccepted(Answer answer) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(COUCHDB_URL + "/" + answer.getId());
        Gson gson = new Gson();

        JsonObject postDocument = getDocumentById(answer.getId());

        postDocument.addProperty("Accepted", true);
        StringEntity requestEntity = new StringEntity(postDocument.toString(), ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == 201) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject documentResponse = gson.fromJson(responseBody, JsonObject.class);

            String documentRev = documentResponse.get("rev").getAsString();
            answer.setRevision(documentRev);
        }

        return statusLine;
    }
    public static StatusLine moderateQuestion(Question question, ModerationTag[] moderationTags, String reason) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut(COUCHDB_URL + "/" + question.getId());
        Gson gson = new Gson();

        JsonObject postDocument = getDocumentById(question.getId());

        JsonObject moderationObject = new JsonObject();

        moderationObject.addProperty("reason", reason);
        moderationObject.addProperty("moderated", true);
        moderationObject.add("moderationTags", gson.toJsonTree(moderationTags).getAsJsonArray());
        moderationObject.addProperty("reportedAt", LocalDateTime.now().toString());

        postDocument.add("moderated", moderationObject);

        StringEntity requestEntity = new StringEntity(postDocument.toString(), ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == 201) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject documentResponse = gson.fromJson(responseBody, JsonObject.class);

            String documentRev = documentResponse.get("rev").getAsString();
            question.setRevision(documentRev);
        }

        return statusLine;
    }


}
