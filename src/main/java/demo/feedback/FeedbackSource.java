package demo.feedback;

import com.google.api.client.util.Lists;
import com.googlecode.objectify.annotation.Entity;

import java.util.List;

/**
 * Created by tharms on 21/11/15.
 */
@Entity
public class FeedbackSource {
    public static final int FEEDBACK_COLUMNS = 18;
    private Row row;

    public FeedbackSource(List<Row> rows) {
        final List<FeedbackElement> tmpValues = Lists.newArrayList();
        for (int i = 0; i< FEEDBACK_COLUMNS; i++) {
            Integer sum = 0;
            StringBuilder comments = new StringBuilder();
            int numValues = 0;

            for (Row row: rows) {
                FeedbackElement value = row.getValues().get(i);
                if (value.getRating() >= 0) {
                    sum += value.getRating();
                    comments.append(" | ").append(value.getComment());
                    numValues ++;
                }
            }
            if (numValues > 0) {
                tmpValues.add(new FeedbackElement(sum / numValues, comments.toString()));
            } else {
                tmpValues.add(new FeedbackElement(0, ""));
            }
        }

        this.row = new Row(tmpValues);
    }


    public Row getRow() {
        return row;
    }

}
