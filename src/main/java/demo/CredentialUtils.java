package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;

import com.google.appengine.api.users.UserServiceFactory;

/*
 * CredentialUtils.java provides helper methods for generating a callback URI, handling
 * an API authorization code flow, and providing an authorized BigQuery API client.
 */
public class CredentialUtils {

    static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    static final String RESOURCE_LOCATION = "client_secrets.json";
    private static GoogleClientSecrets clientSecrets = null;

    static String getRedirectUri(final HttpServletRequest req) {
        GenericUrl url = new GenericUrl(req.getRequestURL().toString());
        url.setRawPath("/oauth2callback");
        return url.build();
    }

    static GoogleClientSecrets getClientCredential() throws IOException {
        if (clientSecrets == null) {
            InputStream inputStream = new FileInputStream(new File(RESOURCE_LOCATION));
            Preconditions.checkNotNull(inputStream, "Cannot open: %s" + RESOURCE_LOCATION);
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
        }

        return clientSecrets;
    }

    static GoogleAuthorizationCodeFlow newFlow() throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, getClientCredential(),
                Collections.singleton(BigqueryScopes.BIGQUERY)).setCredentialStore(new AppEngineCredentialStore())
                                                               .setAccessType("offline").build();
    }

    static Bigquery loadbigquery() throws IOException {
        String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
        Credential credential = newFlow().loadCredential(userId);
        return new Bigquery.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();
    }

}
