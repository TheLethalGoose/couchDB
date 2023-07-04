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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static de.fh.dortmund.Main.COUCHDB_URL;

public class DELETE {

    public static StatusLine delete(List<?> objects) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(COUCHDB_URL + "/_bulk_docs");
        Gson gson = new Gson();
        JsonObject docs = new JsonObject();

        JsonArray objectArray = new JsonArray();

        for (Object object : objects) {
            String id = (String) object.getClass().getMethod("getId").invoke(object);
            String rev = (String) object.getClass().getMethod("getRevision").invoke(object);
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("_id", id);
            jsonObject.addProperty("_rev", rev);
            jsonObject.addProperty("_deleted", true);

            objectArray.add(jsonObject);
        }

        docs.add("docs", objectArray);

        request.setEntity(new StringEntity(docs.toString(), ContentType.APPLICATION_JSON));

        HttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();

        return statusLine;
    }

}
