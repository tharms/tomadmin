package demo.persistence;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;
import demo.feedback.*;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP is run. This is required to let JSP's
 * access Ofy.
 */
public class OfyHelper implements ServletContextListener {
    public void contextInitialized(final ServletContextEvent event) {

        // This will be invoked as part of a warmup request, or the first user request if no warmup
        // request.
        ObjectifyService.register(ClusterGroup.class);
        ObjectifyService.register(Cluster.class);
        ObjectifyService.register(GoalGroup.class);
        ObjectifyService.register(Goal.class);
        ObjectifyService.register(FeedbackGroup.class);
        ObjectifyService.register(Row.class);
    }

    public void contextDestroyed(final ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}
