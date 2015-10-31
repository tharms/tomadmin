package demo.persistence;

/**
 * Created by tharms on 18/10/15.
 */
public enum Status {
    INITIATION,
    PREPARATION,
    EXECUTION,
    PARENTAL_LEAVE,
    NOT_INTERESTED,
    PROBATION,
    UNDEFINED;

    public static Status parse(final String s) {
        if (EXECUTION.name().equalsIgnoreCase(s)) {
            return EXECUTION;
        } else if (INITIATION.name().equalsIgnoreCase(s)) {
            return INITIATION;
        } else if (PREPARATION.name().equalsIgnoreCase(s)) {
            return PREPARATION;
        } else if (PARENTAL_LEAVE.name().equalsIgnoreCase(s)) {
            return PARENTAL_LEAVE;
        } else if (NOT_INTERESTED.name().equalsIgnoreCase(s)) {
            return NOT_INTERESTED;
        } else if (PROBATION.name().equalsIgnoreCase(s)) {
            return PROBATION;
        } else {
            return UNDEFINED;
        }
    }
}
