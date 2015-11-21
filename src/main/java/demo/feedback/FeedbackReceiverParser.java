package demo.feedback;


import com.google.api.client.util.Lists;

import java.util.IllegalFormatException;
import java.util.List;

/**
 * Created by tharms on 21/11/15.
 */
public class FeedbackReceiverParser {
    public static final int LINE_ELEMENTS = 43;
    private final String receiver;
    private final String leadName;

    public FeedbackReceiverParser(String receiver, String leadName) {
        this.receiver = receiver;
        this.leadName = leadName;
    }

    public Feedback parse(List<String> rawData) {
        FeedbackSource self = null;
        FeedbackSource lead = null;
        FeedbackSource multi;
        List<Row> multiRows = Lists.newArrayList();

        for (String line : rawData) {
            String[] split = line.split("\",\"");
            if (split.length != LINE_ELEMENTS) {
                throw new IllegalArgumentException(line);
            }

            if (split[3].equalsIgnoreCase("Completed")) {
                List<FeedbackElement> rowData = Lists.newArrayList();

                for (int i=7; i<LINE_ELEMENTS; i+=2) {
                    Integer rating = Integer.parseInt(split[i]);
                    String comment = split[i+1];
                    FeedbackElement feedbackElement = new FeedbackElement(rating, comment);
                    rowData.add(feedbackElement);
                }


                if(split[1].trim().equalsIgnoreCase(receiver)) {
                    List<Row> rows = Lists.newArrayList();
                    rows.add(new Row(rowData));
                    self = new FeedbackSource(rows);
                } else if (split[1].trim().equalsIgnoreCase(leadName)) {
                    List<Row> rows = Lists.newArrayList();
                    rows.add(new Row(rowData));
                    lead = new FeedbackSource(rows);
                } else {
                    multiRows.add(new Row(rowData));
                }
            }
        }

        multi = new FeedbackSource(multiRows);

        return new Feedback(receiver, leadName, self, lead, multi);
    }
}
