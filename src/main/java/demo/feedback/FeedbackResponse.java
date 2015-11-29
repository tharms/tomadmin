package demo.feedback;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tharms on 25/11/15.
 */
public class FeedbackResponse {
    private String receiver;
    private List<Integer> selfRatings;
    private List<String>selfComments;
    private List<Integer> leadRatings;
    private List<String>leadComments;
    private List<Integer> multiRatings;
    private List<String>multiComments;

    private FeedbackResponse(Builder builder) {
        this.receiver = builder.receiver;
        this.selfComments = builder.selfComments;
        this.selfRatings = builder.selfRatings;
        this.leadComments = builder.leadComments;
        this.leadRatings = builder.leadRatings;
        this.multiComments = builder.multiComments;
        this.multiRatings = builder.multiRatings;
    }

    public List<Integer> getSelfRatings() {
        return selfRatings;
    }

    public List<String> getSelfComments() {
        return selfComments;
    }

    public List<Integer> getLeadRatings() {
        return leadRatings;
    }

    public List<String> getLeadComments() {
        return leadComments;
    }

    public List<Integer> getMultiRatings() {
        return multiRatings;
    }

    public List<String> getMultiComments() {
        return multiComments;
    }

    public String getReceiver() {return receiver;}

    public static class Builder {
        // Required parameters
        private String receiver;

        // Optional parameters - initialized to default values
        private static final Integer[] EMPTY_RATING = new Integer[Row.FEEDBACK_COLUMNS];
        private static final String[] EMPTY_COMMENTS = new String[Row.FEEDBACK_COLUMNS];
        static {
            Arrays.fill(EMPTY_RATING, 0);
            Arrays.fill(EMPTY_COMMENTS, "");
        }

        private List<Integer> selfRatings = Arrays.asList(EMPTY_RATING);
        private List<String>selfComments = Arrays.asList(EMPTY_COMMENTS);
        private List<Integer> leadRatings = Arrays.asList(EMPTY_RATING);
        private List<String>leadComments= Arrays.asList(EMPTY_COMMENTS);
        private List<Integer> multiRatings = Arrays.asList(EMPTY_RATING);
        private List<String>multiComments= Arrays.asList(EMPTY_COMMENTS);

        public Builder(String receiver) {
            this.receiver = receiver;
        }

        public Builder selfRatings(List<Integer> ratings) {
            this.selfRatings = ratings;
            return this;
        }

        public Builder selfComments(List<String> comments) {
            this.selfComments = comments;
            return this;
        }

        public Builder leadRatings(List<Integer> ratings) {
            this.leadRatings = ratings;
            return this;
        }

        public Builder leadComments(List<String> comments) {
            this.leadComments = comments;
            return this;
        }

        public Builder multiRatings(List<Integer> ratings) {
            this.multiRatings = ratings;
            return this;
        }

        public Builder multiComments(List<String> comments) {
            this.multiComments = comments;
            return this;
        }

        public FeedbackResponse build () {
            return new FeedbackResponse(this);
        }

    }
}
