
package demo.feedback;

/**
 * Author: tharms
 * Date: 08/06/2014
 * Time: 09:09
 */

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.googlecode.objectify.ObjectifyService;
import demo.Application;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class FeedbackIntegrationTests {
    private static final String TEST_FILE = "src/test/java/demo/feedback/single.csv";
    private static final Logger log = Logger.getLogger(FeedbackIntegrationTests.class);

    @Value("${local.server.port}")
    private int port;

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

    @Ignore
    @Test
    public void testUpload() {
        RestTemplate template = new TestRestTemplate("lead name", "password");
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("name", TEST_FILE);
        parts.add("file", new FileSystemResource(TEST_FILE));
        String response = template.postForObject("http://127.0.0.1:" + port + "/uploadFeedback",
                parts, String.class);
        System.out.println(response);
        assertTrue("Wrong response" + response, response.contains("<html>"));
    }

    @Ignore
    @Test
    public void testReceiver() throws IOException {
        final TestRestTemplate testRestTemplate = new TestRestTemplate("lead name", "password");
        String body = testRestTemplate.getForObject("http://127.0.0.1:" + port + "/receiver", String.class);
        log.info("found info = " + body);
        assertTrue("Wrong body: " + body, body.contains("["));
    }

}
