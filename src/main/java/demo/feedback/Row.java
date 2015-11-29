package demo.feedback;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by tharms on 21/11/15.
 */
@Entity
public class Row {
    public static final int FEEDBACK_COLUMNS = 18;
    public static final int SELF_FEEDBACK = 1;
    public static final int LEAD_FEEDBACK = 2;
    public static final int MULTI_FEEDBACK = 3;

    @Parent
    private Key<FeedbackGroup> feedbackGroup;
    @Id
    private Long id;
    @Index
    private String receiver;
    @Index
    private String userId;
    private String lead;
    private String giver;
    private List<Integer> ratings;
    private List<String> comments;
    private Integer feedbackType;

    public Row() {
    }

    public Row(Long id, String userId, String receiver, String giver, String lead, List<Integer> ratings, List<String> comments, Integer feedbackType) {
        this.id = id;
        this.userId = userId;
        this.giver = giver;
        this.receiver = receiver;
        this.lead = lead;
        this.ratings = ratings;
        this.comments = comments;
        this.feedbackGroup = Key.create(FeedbackGroup.class, lead); // Creating the Ancestor key
        this.feedbackType = feedbackType;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getLead() {
        return lead;
    }

    public String getGiver() {
        return giver;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public List<String> getComments() {
        return comments;
    }

    public Long getId() { return id; }

    public Key<FeedbackGroup> getFeedbackGroup() {
        return feedbackGroup;
    }

    public Integer getFeedbackType() { return feedbackType; }

    public String getUserId() {return userId; }
}
