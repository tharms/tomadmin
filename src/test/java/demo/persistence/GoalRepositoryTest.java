package demo.persistence;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.mockito.Mockito;

import com.google.api.client.util.Lists;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import com.google.gdata.data.spreadsheet.ListEntry;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class GoalRepositoryTest {

    private GoalRepository goalRepository;

    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();

        Goal goal1 = new Goal(1L, "Steve Jobs", "Make iPhone", "SJ1", Status.EXECUTION, 23);
        Goal goal2 = new Goal(2L, "Eric Schmidt", "Create google maps", "ES2", Status.EXECUTION, 23);
        Goal goal3 = new Goal(3L, "Jeff Bezos", "Implement AppDynamics", "JB3", Status.EXECUTION, 23);

        final List<Goal> goals = Lists.newArrayList();
        goals.add(goal1);
        goals.add(goal2);
        goals.add(goal3);
        goalRepository = Mockito.mock(GoalRepository.class);

        when(goalRepository.getNewGoals(any(List.class))).thenReturn(goals);

        ObjectifyService.register(GoalGroup.class);
        ObjectifyService.register(Goal.class);
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    @Ignore
    public void testUpdate() throws Exception {
        Objectify ofy = ObjectifyService.factory().begin();

        final List<ListEntry> listEntries = Lists.newArrayList();
        final ListEntry listEntry1 = new ListEntry("1", "1.0");
        listEntry1.getCustomElements().setValueLocal("status", "INITIATION");

        final ListEntry listEntry2 = new ListEntry("2", "1.0");
        listEntries.add(listEntry1);
        listEntries.add(listEntry2);
        goalRepository.update(listEntries);

        assertThat(goalRepository.getPersistedGoals(), entryMatcher(listEntries));
    }

    private Matcher<? super List<Goal>> entryMatcher(final List<ListEntry> listEntries) {
        final Matcher<? super List<Goal>> myMatcher = new TypeSafeMatcher<List<Goal>>() {

            @Override
            public void describeTo(final Description description) {
                description.appendValue(listEntries);
            }

            @Override
            protected boolean matchesSafely(final List<Goal> goals) {
                return goals.equals(listEntries);
            }
        };

        return myMatcher;
    }

    @Test
    public void testGetAllGoals() throws Exception { }
}
