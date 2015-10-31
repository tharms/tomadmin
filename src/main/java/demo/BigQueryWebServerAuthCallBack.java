package demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.security.Credential;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeCallbackServlet;

import com.google.appengine.api.users.UserServiceFactory;

/**
 * Created by tharms on 31/10/15.
 */
/*
 * BigQueryWebServerAuthCallBack.java extends the AbstractAppEngineAuthorizationCodeCallbackServlet class
 * available in the Google OAuth Java library (https://github.com/google/google-oauth-java-client). If the
 * logged in end user grants authorization, they will be redirected to this servlet, and the onSuccess()
 * method will be called. In this example, the user will be redirected to the app's root URL.
 */
public class BigQueryWebServerAuthCallBack extends AbstractAppEngineAuthorizationCodeCallbackServlet {

    protected void onSuccess(final HttpServletRequest req, final HttpServletResponse resp, final Credential credential)
        throws ServletException, IOException {
        resp.sendRedirect("/");
    }

    protected void onError(final HttpServletRequest req, final HttpServletResponse resp,
            final AuthorizationCodeResponseUrl errorResponse) throws ServletException, IOException {
        String nickname = UserServiceFactory.getUserService().getCurrentUser().getNickname();
        resp.getWriter().print("<p>" + nickname + ", you've declined to authorize this application.</p>");
        resp.getWriter().print("<p><a href=\"/\">Visit this page</a> to try again.</p>");
        resp.setStatus(200);
        resp.addHeader("Content-Type", "text/html");
    }

    @Override
    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
        return CredentialUtils.newFlow();
    }

    @Override
    protected String getRedirectUri(final HttpServletRequest request) throws ServletException, IOException {
        return CredentialUtils.getRedirectUri(request);
    }

}
