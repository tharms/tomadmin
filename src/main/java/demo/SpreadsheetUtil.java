package demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.URL;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

/**
 * Created by tharms on 03/10/15.
 */
@Component
public class SpreadsheetUtil {
    private static Logger logger = Logger.getLogger(SpreadsheetUtil.class.getName());

    private static final int REFRESH_MINUTES = 2;
    private Credential creds;
    private SpreadsheetService myService;

    /**
     * Keep the expiration time of the access token to renew it before expiry.
     */
    private Calendar expirationTime = Calendar.getInstance();

    public SpreadsheetUtil() {
        myService = new SpreadsheetService("ToMGoalRepo");
        myService.setContentType(ContentType.JSON);
    }

    public void refreshCredentials() {

        if (creds == null || creds.getExpiresInSeconds() < 10) {
            final HttpTransport transport = new NetHttpTransport();

            final JsonFactory jsonFactory = new JacksonFactory();
            try {
                final InputStreamReader inputStreamReader = new InputStreamReader(SpreadsheetUtil.class
                            .getResourceAsStream("/ClientSecret.json"), "UTF-8");
                final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, inputStreamReader);

                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setRefreshToken("1/HItFXvjM0QOoLbQCCM5e5-Y1qh-EqEy8VAkYLVKJPDBIgOrJDtdun6zK6XiATCKT");

                creds = createCredentialWithRefreshToken(transport, jsonFactory, clientSecrets, tokenResponse);
                creds.refreshToken();

                myService.setOAuth2Credentials(creds);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            logger.info("Token has been refreshed.");
        }

    }

    public static GoogleCredential createCredentialWithRefreshToken(final HttpTransport transport,
            final JsonFactory jsonFactory, final GoogleClientSecrets clientSecrets, final TokenResponse tokenResponse) {
        return new GoogleCredential.Builder().setTransport(transport).setJsonFactory(jsonFactory)
                                             .setClientSecrets(clientSecrets).build().setFromTokenResponse(
                                                 tokenResponse);
    }

    public List<WorksheetEntry> readWorksheetEntries(final URL tomGoalRepo) throws IOException, ServiceException {

        // Make a request to the API and get all spreadsheets.
        final WorksheetFeed worksheetFeed = myService.getFeed(tomGoalRepo, WorksheetFeed.class);
        return worksheetFeed.getEntries();
    }

    public List<ListEntry> getListEntries(final URL spreadSheetListEntryFeedUrl) throws IOException, ServiceException {
        ListFeed listFeed = myService.getFeed(spreadSheetListEntryFeedUrl, ListFeed.class);
        return listFeed.getEntries();
    }

}
