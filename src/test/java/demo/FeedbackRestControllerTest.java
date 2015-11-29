package demo;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import demo.feedback.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by tharms on 28/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class FeedbackRestControllerTest {
    private static LocalServiceTestHelper helper;
    static {
        HashMap<String, Object> envAttr = new HashMap<String, Object>();

        envAttr.put("com.google.appengine.api.users.UserService.user_id_key", "10");

        helper = new LocalServiceTestHelper(
                new LocalUserServiceTestConfig())
                .setEnvIsAdmin(true)
                .setEnvIsLoggedIn(true)
                .setEnvEmail("test@zalando.de")
                .setEnvAuthDomain("test")
                .setEnvAttributes(envAttr);
    }


    @Autowired
    private FeedbackRestController feedbackRestController;

    @Before
    public void setUp() {
        helper.setUp();

        ObjectifyService.register(FeedbackGroup.class);
        ObjectifyService.register(Row.class);
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }


    @Test
    public void testFeedbackRoundTrip() {
        final Objectify ofy = ObjectifyService.factory().begin();

        final String feedback = FeedbackTools.readFeedbackAsString("src/test/java/demo/feedback/single.csv");
        final FeedbackData feedbackData = new FeedbackData();
        feedbackData.setFeedback(feedback);
        feedbackData.setPeopleLead("Lead Name");

        final List<String> errorProtocol = feedbackRestController.receiveFeedbackData(feedbackData);

        assert (errorProtocol.isEmpty());

        final List<FeedbackReceiver> receiver = feedbackRestController.receiver();

        assert (! receiver.isEmpty());

        final Integer receiverId = receiver.get(0).getId();

        final FeedbackResponse receiverFeedback = feedbackRestController.getFeedback(receiverId.toString());

        assertNotNull (receiverFeedback.getLeadComments());
        assertNotNull (receiverFeedback.getLeadRatings());
        assertNotNull (receiverFeedback.getMultiComments());
        assertNotNull (receiverFeedback.getMultiRatings());
        assertNotNull (receiverFeedback.getSelfComments());
        assertNotNull (receiverFeedback.getSelfRatings());
        assertNotNull (receiverFeedback.getReceiver());
    }
}