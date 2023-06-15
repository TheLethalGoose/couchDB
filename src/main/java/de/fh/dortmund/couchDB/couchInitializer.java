package de.fh.dortmund.couchDB;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class couchInitializer {

    private static HttpClientContext httpClientContext(HttpHost target){

        // Creating the AuthCache and Basic authentication
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(target, basicAuth);

        // Creating the HttpClientContext and assigning the AuthCache
        HttpClientContext context = HttpClientContext.create();
        context.setAuthCache(authCache);

        return context;
    }

    private static CredentialsProvider getCredentialsProvider(String user, String password, HttpHost target){

        // Creating the CredentialsProvider and setting the credentials
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(target),
                new UsernamePasswordCredentials(user, password));

        return credentialsProvider;
    }

    private static HttpResponse createDatabase(URI databaseUri, HttpClientContext context, CredentialsProvider credentialsProvider) throws IOException {

        HttpPut createRequest = new HttpPut(databaseUri);

        CloseableHttpClient httpClient = HttpClientBuilder.create().
                setDefaultCredentialsProvider(credentialsProvider).build();

        return httpClient.execute(createRequest, context);
    }
    private static HttpResponse deleteDatabase(URI databaseUri, HttpClientContext context, CredentialsProvider credentialsProvider) throws IOException {

        HttpDelete createRequest = new HttpDelete(databaseUri);

        CloseableHttpClient httpClient = HttpClientBuilder.create().
                setDefaultCredentialsProvider(credentialsProvider).build();

        return httpClient.execute(createRequest, context);
    }

    private static HttpResponse defineSecurityRules(URI databaseUri, HttpClientContext context, CredentialsProvider credentialsProvider) throws IOException {


        String accessControlJson = "{\"admins\":{\"names\":[],\"roles\":[]},\"members\":{\"names\":[],\"roles\":[]}}";

        HttpPut securityRequest = new HttpPut(databaseUri + "/_security");
        StringEntity entity = new StringEntity(accessControlJson, ContentType.APPLICATION_JSON);
        securityRequest.setEntity(entity);

        CloseableHttpClient httpClient = HttpClientBuilder.create().
                setDefaultCredentialsProvider(credentialsProvider).build();

        return httpClient.execute(securityRequest, context);
    }


    public static void init(String host, int port, String databaseName, String user, String password, boolean flushData) throws URISyntaxException {

        // Configuration of the target host
        HttpHost target = new HttpHost(host, port, "http");

        // Creating the HttpClientContext
        HttpClientContext context = httpClientContext(target);

        // Creating the database URI
        URI databaseUri = new URIBuilder().setScheme("http").setHost(host).setPort(port).setPath("/" + databaseName).build();

        // Creating the CredentialsProvider
        CredentialsProvider credentialsProvider = getCredentialsProvider(user, password, target);

        if(flushData) {

            try {
                HttpResponse responseDelete = deleteDatabase(databaseUri, context, credentialsProvider);
                int responseCodeDelete = responseDelete.getStatusLine().getStatusCode();

                if (responseCodeDelete == 200) {
                    System.out.println("Flushed database " + databaseName + " successfully.");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        try {

            HttpResponse responseCreate = createDatabase(databaseUri, context, credentialsProvider);
            int responseCodeCreate = responseCreate.getStatusLine().getStatusCode();

            if (responseCodeCreate == 201) {
                System.out.println("Database " + databaseName + " created successfully.");

                HttpResponse responseSecurity = defineSecurityRules(databaseUri, context, credentialsProvider);

                if(responseSecurity.getStatusLine().getStatusCode() == 200) {
                    System.out.println("Security rules created successfully.");
                }

                return;
            }
            if(responseCodeCreate == 412) {
                System.out.println("Database " + databaseName + " already exists.");
                return;
            }

            System.out.println("Error creating database " + databaseName);
            System.out.println("Response Create: " + responseCodeCreate + " " + responseCreate.getStatusLine().getReasonPhrase());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
