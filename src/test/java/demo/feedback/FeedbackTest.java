package demo.feedback;

import com.google.api.client.util.Lists;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by tharms on 21/11/15.
 */
public class FeedbackTest {

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
    public void parseSimpleData() {
        final FeedbackReceiverParser feedbackReceiverParser = new FeedbackReceiverParser("abc", "Lead Name");
        List<String> rawData = FeedbackTools.readFile("src/test/java/demo/feedback/single.csv");

        List<Row> feedback = feedbackReceiverParser.parse(rawData, 1L);

        assert(feedback.size() == 9 );
    }

    @Test
    public void parseComplexData() {
        final FeedbackParser feedbackParser = new FeedbackParser();
        List<String> rawData = FeedbackTools.readComplexFile("src/test/java/demo/feedback/Export.csv");

        Map<String, List<String>> parsed = feedbackParser.parse(rawData);

        assertNotNull(parsed);
    }



}