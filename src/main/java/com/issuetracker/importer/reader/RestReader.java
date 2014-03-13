package com.issuetracker.importer.reader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * REST calls handler.
 *
 * @author Jiri Holusa
 */
public class RestReader {

    /**
     * Accesses provided URL and returns its response.
     *
     * @param requestUrl
     * @return JSON response
     */
    public String read(String requestUrl) {
        Client client = ClientBuilder.newClient();

        String myURL = requestUrl;
        URL url = null;
        try {
            url = new URL(myURL);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL address provided.", e);
        }

        URI uri;
        try {
            uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Error occurred during processing the URL.", e);
        }

        WebTarget resourceTarget = client.target(uri.toString());
        Invocation invocation = resourceTarget.request("application/json").buildGet();

        String response = invocation.invoke(String.class);

        return response;
    }
}
