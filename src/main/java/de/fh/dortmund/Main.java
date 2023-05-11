package de.fh.dortmund;

import com.google.gson.Gson;
import de.fh.dortmund.couchDB.couchInitializer;
import de.fh.dortmund.models.User;
import de.fh.dortmund.service.POST;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws IOException {
        URI uri = URI.create("http://admin:admin@localhost:5984/");

        couchInitializer.init(uri, "stackoverflow", true);

        User user = new User("test@test", "test", "test", 0);

        POST post = new POST("stackoverflow");
        System.out.println(post.POST(user));


    }
}