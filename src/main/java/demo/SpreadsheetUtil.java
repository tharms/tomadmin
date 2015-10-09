package demo;

import java.io.IOException;

import java.net.URL;

import java.util.Calendar;
import java.util.List;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

/**
 * Created by tharms on 03/10/15.
 */
public class SpreadsheetUtil {

    private static final int REFRESH_MINUTES = 2;
    private Credential creds;
    private SpreadsheetService myService;

    /**
     * Keep the expiration time of the access token to renew it before expiry.
     */
    private Calendar expirationTime = Calendar.getInstance();

    public SpreadsheetUtil() {
        initCredentials();
        myService = new SpreadsheetService("ToMGoalRepo");
        myService.setOAuth2Credentials(creds);
        myService.setContentType(ContentType.JSON);
    }

    protected void initCredentials() {

        /*
         * List<String> scopes = Arrays.asList("https://spreadsheets.google.com/feeds");
         * AppIdentityService appIdentity = AppIdentityServiceFactory.getAppIdentityService();
         * AppIdentityService.GetAccessTokenResult accessToken = appIdentity.getAccessToken(scopes);
         *
         * expirationTime.setTime(accessToken.getExpirationTime());
         * expirationTime.add(Calendar.MINUTE, -REFRESH_MINUTES); // refresh some time before expiry
         *
         */
        creds = new Credential(BearerToken.authorizationHeaderAccessMethod());

        final String token = "ya29.BwK-J-nnpaTr0i_7tm1W6MwmprGLjsaG3QeyrretTmfZX0qh6ODhQL1L3G7Szd96cpCR";
        creds.setAccessToken(token);
    }

    public List<SpreadsheetEntry> readSpreadsheetEntries() throws IOException, ServiceException {

        // Define the URL to request.  This should never change.
        URL SPREADSHEET_FEED_URL = new URL(
                "https://spreadsheets.google.com/feeds/worksheets/1bRO_aI_FBudyAMZWFuDWr-V7l_rofAjMVd2_dJRzPb4/private/full");

        // Make a request to the API and get all spreadsheets.
        SpreadsheetFeed feed = myService.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        return spreadsheets;
    }

}
