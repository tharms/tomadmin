package demo.feedback;

import com.googlecode.objectify.annotation.Entity;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by tharms on 21/11/15.
 */
@Entity
public class Row {
    private List<FeedbackElement> values;

    public Row(List<FeedbackElement> values) {
        this.values = values;
    }

    public List<FeedbackElement> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Row row = (Row) o;

        if (row.values.size() != values.size()) {
            return false;
        }
        for (int i=0; i<values.size(); i++) {
            if (values.get(i) != row.getValues().get(i)) {
                return false;
            }
        }
        return true;

    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
