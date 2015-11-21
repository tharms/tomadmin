package demo.feedback;

import com.google.api.client.util.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by tharms on 21/11/15.
 */
public class FeedbackSourceTest {
    @Test
    public void testAverageSimple() {
        final List<FeedbackElement> data = Lists.newArrayList();
        for (int i=0; i<18; i++) data.add(new FeedbackElement(50, ""));
        final Row row1 = new Row(data);
        List<Row> rows = Lists.newArrayList();
        rows.add(row1);
        final FeedbackSource feedbackSource = new FeedbackSource(rows);
        assertEquals(feedbackSource.getRow(), row1);
    }

    @Test
    public void testAverageMulti() {
        final List<FeedbackElement> data1 = Lists.newArrayList();
        for (int i=0; i<18; i++) data1.add(new FeedbackElement(50, ""));

        final List<FeedbackElement> data2 = Lists.newArrayList();
        for (int i=0; i<18; i++) data2.add(new FeedbackElement(70, ""));

        final List<FeedbackElement> expeectedData = Lists.newArrayList();
        for (int i=0; i<18; i++) data2.add(new FeedbackElement(60, ""));

        final Row row1 = new Row(data1);
        final Row row2 = new Row(data2);
        final Row expected = new Row(expeectedData);
        List<Row> rows = Lists.newArrayList();
        rows.add(row1);
        rows.add(row2);
        final FeedbackSource feedbackSource = new FeedbackSource(rows);
        assertEquals(feedbackSource.getRow(), expected);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAverageDifferentSize() {
        final List<FeedbackElement> data1 = Lists.newArrayList();
        for (int i=0; i<18; i++) data1.add(new FeedbackElement(50, ""));

        final List<FeedbackElement> data2 = Lists.newArrayList();
        for (int i=0; i<17; i++) data2.add(new FeedbackElement(70, ""));

        final Row row1 = new Row(data1);
        final Row row2 = new Row(data2);
        List<Row> rows = Lists.newArrayList();
        rows.add(row1);
        rows.add(row2);
        final FeedbackSource feedbackSource = new FeedbackSource(rows);
    }

}