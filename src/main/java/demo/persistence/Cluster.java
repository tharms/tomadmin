package demo.persistence;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Cluster {
    @Parent
    Key<ClusterGroup> clusterGroup;
    @Id
    public Long id;

    @Index
    public Integer externalClusterId;
    public String clusterName;
    public Date date;

    /**
     * Simple constructor just sets the date.
     */
    public Cluster() {
        date = new Date();
    }

    /**
     * Takes all important fields.
     */
    public Cluster(final String clusterGroup, final String clusterName, final Integer externalClusterId) {
        this();
        if (clusterGroup != null) {
            this.clusterGroup = Key.create(ClusterGroup.class, clusterGroup); // Creating the Ancestor key
        } else {
            this.clusterGroup = Key.create(ClusterGroup.class, "default");
        }

        this.clusterName = clusterName;

        this.externalClusterId = externalClusterId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cluster cluster = (Cluster) o;

        if (!externalClusterId.equals(cluster.externalClusterId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return externalClusterId.hashCode();
    }
}
