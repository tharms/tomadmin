package demo.persistence;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.google.api.client.util.Lists;

import com.google.gdata.data.spreadsheet.ListEntry;

import com.googlecode.objectify.ObjectifyService;

/**
 * Created by tharms on 12/10/15.
 */
@Component
public class ClusterRepository {

    private static Logger logger = Logger.getLogger(ClusterRepository.class.getName());

    public void update(final List<ListEntry> listEntries) {
        final List<Cluster> newCluster = getNewClusters(listEntries);

        logger.warning(newCluster.toString());

        final List<Cluster> oldCluster = getPersistedCluster();

        final List<Cluster> toUpdate = Lists.newArrayList(oldCluster);
        toUpdate.retainAll(newCluster);

        final List<Cluster> toDelete = Lists.newArrayList(oldCluster);
        toDelete.removeAll(newCluster);

        final List<Cluster> toInsert = Lists.newArrayList(newCluster);
        toInsert.removeAll(oldCluster);

        for (Cluster cluster : toDelete) {
            ObjectifyService.ofy().delete().entity(cluster).now();
        }

        ObjectifyService.ofy().save().entities(toUpdate).now();

        ObjectifyService.ofy().save().entities(toInsert).now();
    }

    private List<Cluster> getNewClusters(final List<ListEntry> listEntries) {
        final List<Cluster> newClusters = Lists.newArrayList();

        for (ListEntry listEntry : listEntries) {
            final String clusterGroupName = listEntry.getCustomElements().getValue("group") == null
                ? "default" : listEntry.getCustomElements().getValue("group");
            final String clusterName = listEntry.getCustomElements().getValue("gloalclustername");

            Integer clusterId = null;
            try {
                clusterId = Integer.parseInt(listEntry.getCustomElements().getValue("id"));
            } catch (NumberFormatException e) {
                logger.warning(String.format("Invalid numeric value %s.",
                        listEntry.getCustomElements().getValue("id")));
            }

            if (clusterId != null) {
                newClusters.add(new Cluster(clusterGroupName, clusterName, clusterId));
            }
        }

        return newClusters;
    }

    public List<Cluster> getPersistedCluster() {
        return ObjectifyService.ofy().load().type(Cluster.class).list();
    }
}
