package demo.feedback;

import com.google.api.client.util.Lists;

import java.util.List;

/**
 * Created by tharms on 25/11/15.
 */
public class FeedbackConverter {
    public static FeedbackResponse toFeedbackResponse(List<Row> feedbackRows) {
        final FeedbackResponse.Builder frb = new FeedbackResponse.Builder(feedbackRows.get(0).getReceiver());

        for (Row row: feedbackRows) {
            if ( row.getFeedbackType() == Row.SELF_FEEDBACK) {

                frb.selfRatings(feedbackRows.get(0).getRatings())
                        .selfComments(feedbackRows.get(0).getComments());

            } else if (row.getFeedbackType() == Row.LEAD_FEEDBACK) {

                frb.leadRatings(feedbackRows.get(1).getRatings())
                        .leadComments(feedbackRows.get(1).getComments());
            }
        }

        final List<Integer> multiRatings = Lists.newArrayList();
        final List<String> multiComments = Lists.newArrayList();
        for (int i=0; i<Row.FEEDBACK_COLUMNS; i++) {
            int ratingSum = 0;
            int numRatings = 0;
            StringBuffer comments = new StringBuffer();
            for (Row row: feedbackRows) {
                if (row.getFeedbackType() != Row.MULTI_FEEDBACK) {
                    continue;
                }
                ratingSum += row.getRatings().get(i);
                numRatings++;
                if (multiComments.size() == i) {
                    multiComments.add(row.getComments().get(i));
                } else {
                    if (! row.getComments().get(i).isEmpty()) {
                        if (multiComments.get(i).isEmpty()) {
                            multiComments.set(i, row.getComments().get(i));
                        } else {
                            multiComments.set(i, " | " + row.getComments().get(i));
                        }
                    }
                }
            }
            if (numRatings > 0) {
                multiRatings.add(ratingSum / numRatings);
            } else {
                multiRatings.add(-1);
            }

        }

        frb.multiRatings(multiRatings).multiComments(multiComments);

        return frb.build();
    }

}
