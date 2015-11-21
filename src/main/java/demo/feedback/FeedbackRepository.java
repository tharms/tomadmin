package demo.feedback;

import com.google.api.client.util.Lists;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.googlecode.objectify.ObjectifyService;
import demo.persistence.ClusterRepository;
import demo.persistence.Goal;
import demo.persistence.Status;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by tharms on 17/10/15.
 */
@Component
public class FeedbackRepository {

    private static Logger logger = Logger.getLogger(FeedbackRepository.class.getName());

    public void update(final List<Feedback> feedbacks) {

        final List<Feedback> oldFeedback = getPersistedFeedbacks();

        final List<Feedback> toUpdate = Lists.newArrayList(oldFeedback);
        toUpdate.retainAll(feedbacks);

        final List<Feedback> toDelete = Lists.newArrayList(oldFeedback);
        toDelete.remove(feedbacks);

        final List<Feedback> toInsert = Lists.newArrayList(feedbacks);
        toInsert.removeAll(oldFeedback);

        for (Feedback f : toDelete) {
            ObjectifyService.ofy().delete().entity(f).now();
        }

        ObjectifyService.ofy().save().entities(toUpdate).now();

        ObjectifyService.ofy().save().entities(toInsert).now();
    }


    public List<Feedback> getPersistedFeedbacks() {
        return ObjectifyService.ofy().load().type(Feedback.class).list();
    }

}
