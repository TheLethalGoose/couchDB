package de.fh.dortmund.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static de.fh.dortmund.Main.COUCHDB_URL;

public class GET {


    public static JsonArray getDocsFromView(String designDocId, String viewName, String key) {

        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
        String urlString = COUCHDB_URL + "/" + designDocId + "/_view/" + viewName + "?key=%22" + encodedKey + "%22";

        return Objects.requireNonNull(parseResponse(urlString)).getAsJsonArray("rows");
    }

    public static JsonArray getDocsFromView(String designDocId, String viewName, String key, boolean group) {

        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
        String urlString = COUCHDB_URL + "/" + designDocId + "/_view/" + viewName + "?key=%22" + encodedKey + "%22" + "&group=" + group;

        return Objects.requireNonNull(parseResponse(urlString)).getAsJsonArray("rows");
    }


    public static JsonArray getDocsFromView(String designDocId, String viewName, boolean reduce, boolean group) {

        String urlString = COUCHDB_URL + "/" + designDocId + "/_view/" + viewName + "?reduce=" + reduce + "&group=" + group;

        return Objects.requireNonNull(parseResponse(urlString)).getAsJsonArray("rows");

    }

    public static JsonArray getDocsFromView(String designDocId, String viewName) {

        String urlString = COUCHDB_URL + "/" + designDocId + "/_view/" + viewName;

        return Objects.requireNonNull(parseResponse(urlString)).getAsJsonArray("rows");

    }

    public static JsonObject getDocumentById(String id) {
        String urlString = COUCHDB_URL + "/" + id;

        return parseResponse(urlString);
    }

    private static HttpResponse executeRequest(HttpGet request) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        return httpClient.execute(request);
    }

    private static JsonObject parseResponse(String urlString) {

        HttpGet httpGet = new HttpGet(urlString);

        try {
            HttpResponse response = executeRequest(httpGet);
            HttpEntity entity = response.getEntity();
            String responseJson = EntityUtils.toString(entity);

            return JsonParser.parseString(responseJson).getAsJsonObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}