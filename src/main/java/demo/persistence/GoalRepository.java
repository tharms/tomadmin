package demo.persistence;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.google.api.client.util.Lists;

import com.google.gdata.data.spreadsheet.ListEntry;

import com.googlecode.objectify.ObjectifyService;

/**
 * Created by tharms on 17/10/15.
 */
@Component
public class GoalRepository {

    private static Logger logger = Logger.getLogger(ClusterRepository.class.getName());

    public void update(final List<ListEntry> listEntries) {
        final List<Goal> newGoals = getNewGoals(listEntries);

        final List<Goal> oldGoals = getPersistedGoals();

        final List<Goal> toUpdate = Lists.newArrayList(oldGoals);
        toUpdate.retainAll(newGoals);

        final List<Goal> toDelete = Lists.newArrayList(oldGoals);
        toDelete.remove(newGoals);

        final List<Goal> toInsert = Lists.newArrayList(newGoals);
        toInsert.removeAll(oldGoals);

        for (Goal g : toDelete) {
            ObjectifyService.ofy().delete().entity(g).now();
        }

        ObjectifyService.ofy().save().entities(toUpdate).now();

        ObjectifyService.ofy().save().entities(toInsert).now();
    }

    protected List<Goal> getNewGoals(final List<ListEntry> listEntries) {
        final List<Goal> newGoals = Lists.newArrayList();

        for (ListEntry listEntry : listEntries) {
            final String goal = listEntry.getCustomElements().getValue("goal");
            final String peopleLead = listEntry.getCustomElements().getValue("peoplelead");
            Integer goalClusterId = null;

            try {
                goalClusterId = Integer.parseInt(listEntry.getCustomElements().getValue("clusterid"));
            } catch (NumberFormatException e) {
                logger.warning(String.format("Invalid numeric value for cluster: %s.",
                        listEntry.getCustomElements().getValue("clusterid")));
            }

            Long goalId = null;
            try {
                goalId = Long.parseLong(listEntry.getCustomElements().getValue("id"));
            } catch (NumberFormatException e) {
                logger.warning(String.format("Invalid numeric value for id: %s.",
                        listEntry.getCustomElements().getValue("id")));
            }

            final String employeeId = listEntry.getCustomElements().getValue("internalempid");

            final Status status = Status.parse(listEntry.getCustomElements().getValue("status"));

            if (goalId != null) {
                newGoals.add(new Goal(goalId, peopleLead, goal, employeeId, status, goalClusterId));
            }
        }

        return newGoals;
    }

    public List<Goal> getPersistedGoals() {
        return ObjectifyService.ofy().load().type(Goal.class).list();
    }

}
