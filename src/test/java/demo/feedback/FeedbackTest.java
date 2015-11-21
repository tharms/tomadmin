package demo.feedback;

import com.google.api.client.util.Lists;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by tharms on 21/11/15.
 */
public class FeedbackTest {

    @Test
    public void parseSimpleData() {
        final FeedbackReceiverParser feedbackReceiverParser = new FeedbackReceiverParser("Feedback Receiver", "Lead Name");
        List<String> rawData = readFile("src/test/java/demo/feedback/single.csv");

        Feedback feedback = feedbackReceiverParser.parse(rawData);

        assertNotNull(feedback);
        assertNotNull(feedback.getLead());
        assertNotNull(feedback.getMulti());
        assertNotNull(feedback.getSelf());

    }

    @Test
    public void parseComplexData() {
        final FeedbackParser feedbackParser = new FeedbackParser();
        List<String> rawData = readComplexFile("src/test/java/demo/feedback/Export.csv");

        Map<String, List<String>> parsed = feedbackParser.parse(rawData);

        assertNotNull(parsed);
    }


    public List<String> readFile(String file) {
        List<String> rawData = Lists.newArrayList();
        File f = new File(file);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line;
            while((line = br.readLine()) != null) {
                if (line.startsWith("\"Feedback Receiver\"")) {
                    rawData.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData;
    }

    public List<String> readComplexFile(String file) {
        List<String> rawData = Lists.newArrayList();
        File f = new File(file);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line;
            boolean firstLine = true;
            while((line = br.readLine()) != null) {
                if(firstLine) {
                    firstLine = false;
                } else {
                    rawData.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData;
    }

}