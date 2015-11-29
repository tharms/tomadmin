package demo.feedback;


import com.google.api.client.util.Lists;
import java.util.List;

/**
 * Created by tharms on 21/11/15.
 */
public class FeedbackReceiverParser {
    public static final int LINE_ELEMENTS = 43;
    private final String leadName;
    private final String user;

    public FeedbackReceiverParser(String user, String leadName) {
        this.user = user;
        this.leadName = leadName;
    }

    public List<Row> parse(List<String> rawData, long idOffset) {
        List<Row> result = Lists.newArrayList();

        long id = idOffset;
        for (String line : rawData) {
            String[] split = splitAndClean(line);
            if (split.length != LINE_ELEMENTS) {
                throw new IllegalArgumentException(line);
            }

            if (split[3].equalsIgnoreCase("Completed")) {
                List<Integer> ratings = Lists.newArrayList();
                List<String> comments = Lists.newArrayList();
                String receiver = split[0];
                String giver = split[1];
                int feedbackType = 0;
                if (receiver.equals(giver)) {
                    feedbackType = Row.SELF_FEEDBACK;
                } else if (giver.equals(leadName)) {
                    feedbackType = Row.LEAD_FEEDBACK;
                } else {
                    feedbackType = Row.MULTI_FEEDBACK;
                }

                for (int i=7; i<LINE_ELEMENTS; i+=2) {
                    Integer rating = Integer.parseInt(split[i]);
                    String comment = split[i+1];
                    ratings.add(rating);
                    comments.add(comment);
                }
                result.add(new Row(id++, user, receiver, giver, leadName, ratings, comments, feedbackType));
            }
        }
        return result;
    }

    private String[] splitAndClean(String line) {
        String[] splits = line.split("\\\",");
        for (int i=0; i<splits.length; i++) {
            splits[i] = splits[i].replace("\\\"","").replace("\\","");
            if (splits[i].startsWith("\"")) {
                splits[i] = splits[i].substring(1);
            }
        }
        return splits;
    }

}
