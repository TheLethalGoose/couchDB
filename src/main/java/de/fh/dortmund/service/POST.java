package de.fh.dortmund.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import static de.fh.dortmund.Main.COUCHDB_URL;

public class POST {

    public static StatusLine post(Object object) throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(COUCHDB_URL);
        Gson gson = new Gson();

        JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();
        jsonObject.addProperty("type", object.getClass().getSimpleName().toLowerCase());

        request.setEntity(new StringEntity(jsonObject.getAsString(), ContentType.APPLICATION_JSON));

        HttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == 201) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject documentResponse = gson.fromJson(responseBody, JsonObject.class);

            String documentId = documentResponse.get("id").getAsString();
            String documentRev = documentResponse.get("rev").getAsString();

            updateObject(object, documentId, documentRev);

        }

        return statusLine;
    }

    private static void updateObject(Object object, String documentId, String documentRev) {
        try {
            String id = (String) object.getClass().getMethod("getId").invoke(object);
            if (documentId.equals(id)) {
                object.getClass().getMethod("setRevision", String.class).invoke(object, documentRev);
            }else{
                System.out.println("Document ID and Object ID do not match");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StatusLine post(List<?> objects) throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(COUCHDB_URL + "/_bulk_docs");
        Gson gson = new Gson();
        JsonObject docs = new JsonObject();

        JsonArray objectArray = objects.stream()
                .map(object -> {
                    JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();
                    String type = object.getClass().getSimpleName().toLowerCase();
                    jsonObject.addProperty("type", type);
                    return jsonObject;
                })
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);

        docs.add("docs", objectArray);

        request.setEntity(new StringEntity(docs.toString(), ContentType.APPLICATION_JSON));

        HttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() == 201) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonArray documentResponses = gson.fromJson(responseBody, JsonArray.class);

            for(int i = 0; i < documentResponses.size(); i++) {
                JsonObject documentResponse = documentResponses.get(i).getAsJsonObject();
                String documentId = documentResponse.get("id").getAsString();
                String documentRev = documentResponse.get("rev").getAsString();

                Object obj = objects.get(i);
                updateObject(obj, documentId, documentRev);
            }
        }

        return statusLine;
    }

}
