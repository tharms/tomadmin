package demo.persistence;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * The @Entity tells Objectify about our entity. We also register it in OfyHelper.java -- very important. This is never
 * actually created, but gives a hint to Objectify about our Ancestor key.
 */
@Entity
public class GoalGroup {
    @Id
    public String clusterGroupName;
}
