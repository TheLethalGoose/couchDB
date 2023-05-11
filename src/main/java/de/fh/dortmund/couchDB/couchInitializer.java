package de.fh.dortmund.couchDB;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

public class couchInitializer {

    public static void init(URI uri, String databaseName, boolean flushData) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut request = new HttpPut("http://localhost:5984/" + databaseName);

        try {
            HttpResponse response = httpClient.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();

            if (responseCode == 201) {
                System.out.println("Database " + databaseName + " created successfully.");
            } else {
                System.out.println("Error creating database " + databaseName + ". Response code: " + responseCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
