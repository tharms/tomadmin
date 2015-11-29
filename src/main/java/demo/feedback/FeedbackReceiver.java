package demo.feedback;

/**
 * Created by tharms on 24/11/15.
 */

public class FeedbackReceiver {
    private final int id;
    private final String name;

    public FeedbackReceiver(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
