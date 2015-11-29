package demo.feedback;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import demo.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

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
    public void testUpdate() throws Exception {
        final FeedbackParser feedbackParser = new FeedbackParser();
        final List<String> rawData = FeedbackTools.readFile("src/test/java/demo/feedback/single.csv");
        final Map<String, List<String>> parsed = feedbackParser.parse(rawData);

        final Objectify ofy = ObjectifyService.factory().begin();
        final FeedbackReceiverParser feedbackReceiverParser = new FeedbackReceiverParser("abc", "xx");

        int size = 0;
        for (List<String> feedback : parsed.values()) {
            final List<Row> rows = feedbackReceiverParser.parse(feedback, 1L);
            size += rows.size();
            feedbackRepository.add(rows);
        }

        assert(feedbackRepository.getPersistedFeedbacksOfUser("abc").size() == size && size > 0);
    }

    @Test
    public void testFindBySampleId() {
        final Objectify ofy = ObjectifyService.factory().begin();
        final FeedbackParser feedbackParser = new FeedbackParser();
        final List<String> rawData = FeedbackTools.readFile("src/test/java/demo/feedback/single.csv");
        final Map<String, List<String>> parsed = feedbackParser.parse(rawData);
        final FeedbackReceiverParser feedbackReceiverParser = new FeedbackReceiverParser("abc", "xx");
        for (List<String> feedback : parsed.values()) {
            final List<Row> rows = feedbackReceiverParser.parse(feedback, 1L);
            feedbackRepository.add(rows);
        }
        final Long xxId = feedbackRepository.getPersistedFeedbacksOfUser("abc").get(0).getId();
        final List<Row> xx = feedbackRepository.getFeedbackForSampleRow("abc", xxId);
        assert(xx.size() > 0);
    }

}
