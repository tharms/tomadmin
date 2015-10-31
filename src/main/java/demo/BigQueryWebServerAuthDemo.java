package demo;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.model.ProjectList;

/*
 * BigQueryWebServerAuthDemo.java extends the AbstractAppEngineAuthorizationCodeServlet class available
 * in the Google OAuth Java library (https://github.com/google/google-oauth-java-client). The first time
 * an end user arrives at the page handled by this servlet, they will be redirected in the browser to a
 * Google BigQuery API authorization page.
 */
public class BigQueryWebServerAuthDemo extends AbstractAppEngineAuthorizationCodeServlet {

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter writer = response.getWriter();

        Bigquery bigquery = CredentialUtils.loadbigquery();

        Bigquery.Projects.List projectListRequest = bigquery.projects().list();
        ProjectList projectList = projectListRequest.execute();

        if (projectList.getProjects() != null) {

            List<ProjectList.Projects> projects = projectList.getProjects();
            writer.println("<h3>BigQuery project list:</h3>");

            for (ProjectList.Projects project : projects) {
                writer.printf("%s<br />", project.getProjectReference().getProjectId());
            }

        }
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
