package de.fh.dortmund.couchDB;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;

public class couchInitializer {

    public static void init(String host, int port, String databaseName, String user, String password) {

        // Configuration of the target host
        HttpHost target = new HttpHost(host, port, "http");

        // On default couchDB only allows admin users to create databases

        // Creating the AuthCache and Basic authentication
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(target, basicAuth);

        // Creating the CredentialsProvider and setting the credentials
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(target),
                new UsernamePasswordCredentials(user, password));

        // Creating the HttpClientContext and assigning the AuthCache
        HttpClientContext context = HttpClientContext.create();
        context.setAuthCache(authCache);

        URI uri = URI.create("http://" + host + ":" + port + "/" + databaseName);

        HttpPut request = new HttpPut(uri);

        try {

            CloseableHttpClient httpClient = HttpClientBuilder.create().
                    setDefaultCredentialsProvider(credentialsProvider).build();

            HttpResponse response = httpClient.execute(request, context);

            int responseCode = response.getStatusLine().getStatusCode();

            if (responseCode == 201) {
                System.out.println("Database " + databaseName + " created successfully.");
                return;
            }
            if (responseCode == 412) {
                System.out.println("Database " + databaseName + " already exists.");
                return;
            }

            System.out.println("Error creating database " + databaseName);
            System.out.println("Response: " + responseCode + " " + response.getStatusLine().getReasonPhrase());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
