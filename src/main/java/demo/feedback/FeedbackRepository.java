package demo.feedback;

import com.googlecode.objectify.ObjectifyService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by tharms on 17/10/15.
 */
@Component
public class FeedbackRepository {

    private static Logger logger = Logger.getLogger(FeedbackRepository.class.getName());

    public void add(final List<Row> feedback) {

        for (Row row : feedback) {
            ObjectifyService.ofy().save().entity(row).now();
        }
    }

    public void removeFeedbackOfUser(String userId) {
        final List<Row> oldFeedback = getPersistedFeedbacksOfUser(userId);

        for (Row row : oldFeedback) {
            ObjectifyService.ofy().delete().entity(row).now();
        }
    }


    public List<Row> getPersistedFeedbacksOfUser(String user) {
        return ObjectifyService.ofy().load().type(Row.class).filter("userId", user).list();
    }

    public List<Row> getPersistedFeedbacksOfReceiver(String user, String receiver) {
        return ObjectifyService.ofy().load().type(Row.class).filter("userId", user).filter("receiver", receiver).list();
    }

    public List<Row> getFeedbackForSampleRow(String user, Long sampleRow) {
        final List<Row> persistedFeedbacksOfLead = getPersistedFeedbacksOfUser(user);
        String receiver = null;
        for (Row row : persistedFeedbacksOfLead) {
            if (row.getId().equals(sampleRow)) {
                receiver = row.getReceiver();
                break;
            }
        }
        if (receiver == null) {
            throw new IllegalArgumentException("No feedback receiver found for rowId: " + sampleRow);
        }
        return getPersistedFeedbacksOfReceiver(user, receiver);
    }
}
