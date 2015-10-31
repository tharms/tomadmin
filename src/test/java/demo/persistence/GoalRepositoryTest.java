package demo.persistence;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.SpringApplicationConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.api.client.util.Lists;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import com.google.gdata.data.spreadsheet.ListEntry;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

import demo.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GoalRepositoryTest {

    @Autowired
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
        // goalRepository = Mockito.mock(GoalRepository.class);

        // when(goalRepository.getNewGoals(any(List.class))).thenReturn(goals);

        ObjectifyService.register(GoalGroup.class);
        ObjectifyService.register(Goal.class);
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testUpdate() throws Exception {
        Objectify ofy = ObjectifyService.factory().begin();

        final List<ListEntry> listEntries = Lists.newArrayList();
        final ListEntry listEntry1 = new ListEntry("1", "1.0");
        listEntry1.getCustomElements().setValueLocal("status", "INITIATION");
        listEntry1.getCustomElements().setValueLocal("id", "1");
        listEntry1.getCustomElements().setValueLocal("internalempid", "BA87");

        final ListEntry listEntry2 = new ListEntry("2", "1.0");
        listEntry1.getCustomElements().setValueLocal("status", "EXECUTION");
        listEntry2.getCustomElements().setValueLocal("id", "2");
        listEntry2.getCustomElements().setValueLocal("internalempid", "DO1");

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
                for (ListEntry listEntry : listEntries) {
                    final Long goalId = Long.parseLong(listEntry.getCustomElements().getValue("id"));
                    final String employeeId = listEntry.getCustomElements().getValue("internalempid");
                    final Status status = Status.parse(listEntry.getCustomElements().getValue("status"));

                    boolean found = false;
                    for (Goal goal : goals) {
                        if (goal.employeeId.equals(employeeId) && goal.id.equals(goalId)
                                && goal.status.equals(status)) {
                            found = true;
                        }
                    }

                    if (!found) {
                        return false;
                    }
                }

                return true;
            }
        };

        return myMatcher;
    }

    @Test
    public void testGetAllGoals() throws Exception { }
}
