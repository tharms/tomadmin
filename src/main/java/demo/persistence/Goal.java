package demo.persistence;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Goal {
    @Parent
    Key<GoalGroup> goalGroup;
    @Id
    public Long id;

    @Index
    public String employeeId;
    public String goalName;
    public Integer externalClusterId;
    public Status status;
    public String peopleLead;
    public Date date;

    /**
     * Simple constructor just sets the date.
     */
    public Goal() {
        date = new Date();
    }

    /**
     * Takes all important fields.
     */
    public Goal(final Long goalId, final String peopleLead, final String goalName, final String employeeId,
            final Status status, final Integer externalClusterId) {
        this();
        this.goalGroup = Key.create(GoalGroup.class, "default");

        this.id = goalId;
        this.goalName = goalName;
        this.employeeId = employeeId;
        this.externalClusterId = externalClusterId;
        this.peopleLead = peopleLead;
        this.status = status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Goal cluster = (Goal) o;

        if (!id.equals(cluster.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return employeeId.hashCode();
    }
}
