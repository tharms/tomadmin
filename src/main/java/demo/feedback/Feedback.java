package demo.feedback;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import demo.persistence.ClusterGroup;

import static com.google.api.client.util.Preconditions.checkNotNull;

/**
 * Created by tharms on 21/11/15.
 */
@Entity
public class Feedback {
    @Parent
    Key<FeedbackGroup> feedbackGroup;

    @Id
    private final String receiverName;
    @Index
    private final String leadName;
    private final FeedbackSource self;
    private final FeedbackSource lead;
    private final FeedbackSource multi;

    public Feedback(String receiverName, String leadName, FeedbackSource self, FeedbackSource lead, FeedbackSource multi) {
        checkNotNull(receiverName);
        checkNotNull(leadName);
        checkNotNull(self);
        checkNotNull(lead);
        checkNotNull(multi);

        this.feedbackGroup = Key.create(FeedbackGroup.class, leadName); // Creating the Ancestor key

        this.receiverName = receiverName;
        this.leadName = leadName;
        this.self = self;
        this.lead = lead;
        this.multi = multi;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getLeadName() {
        return leadName;
    }

    public FeedbackSource getSelf() {
        return self;
    }

    public FeedbackSource getLead() {
        return lead;
    }

    public FeedbackSource getMulti() {
        return multi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feedback feedback = (Feedback) o;

        if (!feedbackGroup.equals(feedback.feedbackGroup)) return false;
        if (!receiverName.equals(feedback.receiverName)) return false;
        return leadName.equals(feedback.leadName);

    }

    @Override
    public int hashCode() {
        int result = feedbackGroup.hashCode();
        result = 31 * result + receiverName.hashCode();
        result = 31 * result + leadName.hashCode();
        return result;
    }


}
