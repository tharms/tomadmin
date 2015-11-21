package demo.feedback;

import com.googlecode.objectify.annotation.Entity;

/**
 * Created by tharms on 21/11/15.
 */
@Entity
public class FeedbackElement {
    private final Integer rating;
    private final String comment;

    public FeedbackElement(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedbackElement that = (FeedbackElement) o;

        if (rating != null ? !rating.equals(that.rating) : that.rating != null) return false;
        return !(comment != null ? !comment.equals(that.comment) : that.comment != null);

    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public int hashCode() {
        int result = rating != null ? rating.hashCode() : 0;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }


}
