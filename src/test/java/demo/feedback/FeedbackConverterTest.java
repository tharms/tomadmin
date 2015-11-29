package demo.feedback;

import com.google.api.client.util.Lists;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.googlecode.objectify.ObjectifyService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by tharms on 25/11/15.
 */
public class FeedbackConverterTest {
    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalUserServiceTestConfig()).setEnvIsAdmin(true).setEnvIsLoggedIn(true).setEnvEmail("test@zalando.de");

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
    public void testFeedbackConversion() {
        final FeedbackParser feedbackParser = new FeedbackParser();
        final List<String> rawData = FeedbackTools.readFile("src/test/java/demo/feedback/single.csv");
        final Map<String, List<String>> parsed = feedbackParser.parse(rawData);
        final FeedbackReceiverParser feedbackReceiverParser = new FeedbackReceiverParser("abc", "xx");
        final List<Row> rows = Lists.newArrayList();
        for (List<String> feedback : parsed.values()) {
            rows.addAll(feedbackReceiverParser.parse(feedback, 1L));
        }

        final FeedbackResponse feedbackResponse = FeedbackConverter.toFeedbackResponse(rows);
        assert(feedbackResponse.getMultiComments() != null);
    }

}