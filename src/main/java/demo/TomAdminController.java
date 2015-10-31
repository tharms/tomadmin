package demo;

import java.io.IOException;

import java.net.URL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.util.Lists;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import demo.persistence.ClusterRepository;
import demo.persistence.GoalRepository;

/**
 * Created by tharms on 26/09/15.
 */
@Controller
public class TomAdminController {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private ClusterRepository clusterRepository;
    @Autowired
    private SpreadsheetUtil spreadsheetUtil;

    final String tomGoalRepo =
        "https://spreadsheets.google.com/feeds/worksheets/1bRO_aI_FBudyAMZWFuDWr-V7l_rofAjMVd2_dJRzPb4/private/full";

    @RequestMapping(value = "/tomadmin", method = RequestMethod.GET)
    public ModelAndView index() {
        final ModelAndView mav = new ModelAndView();

        spreadsheetUtil.refreshCredentials();

        final StringBuilder stringBuilder = new StringBuilder();

        List<WorksheetEntry> worksheets = Lists.newArrayList();
        try {
            final URL spreadsheetFeedUrl = new URL(tomGoalRepo);
            worksheets = spreadsheetUtil.readWorksheetEntries(spreadsheetFeedUrl);
        } catch (IOException | ServiceException e) {
            e.printStackTrace();
            stringBuilder.append(e.getMessage());
        }

        final WorksheetEntry clusterWorksheet = worksheets.get(2);
        final WorksheetEntry goalWorksheet = worksheets.get(1);

        List<ListEntry> clusterEntries = null;
        List<ListEntry> goalEntries = null;
        try {
            clusterEntries = saveClusterEntries(spreadsheetUtil, clusterWorksheet);
            goalEntries = saveGoalEntries(spreadsheetUtil, goalWorksheet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
            stringBuilder.append(e.getMessage());
        }

        generateDataImportMessage(stringBuilder, clusterWorksheet, clusterEntries, goalWorksheet, goalEntries);

        final UserService userService = UserServiceFactory.getUserService();
        final String logout = userService.createLogoutURL("login");

        mav.setViewName("tomadmin");
        mav.addObject("message", stringBuilder.toString());
        mav.addObject("logout", logout);
        return mav;
    }

    private void generateDataImportMessage(final StringBuilder stringBuilder, final WorksheetEntry clusterWorksheet,
            final List<ListEntry> clusterEntries, final WorksheetEntry goalWorksheet,
            final List<ListEntry> goalEntries) {

        // Print the title of this clusterWorksheet to the screen
        stringBuilder.append(clusterWorksheet.getTitle().getPlainText()).append("<br/>");

        // Iterate through each row, printing its cell values.
        for (ListEntry row : clusterEntries) {

            // Iterate over the remaining columns, and print each cell value
            for (String tag : row.getCustomElements().getTags()) {
                stringBuilder.append(tag + ":&nbsp;" + row.getCustomElements().getValue(tag) + "&nbsp;");
            }

            stringBuilder.append("<br/>");
        }

        stringBuilder.append("<br/>").append(goalWorksheet.getTitle().getPlainText()).append("<br/>");

        // Iterate through each row, printing its cell values.
        for (ListEntry row : goalEntries) {

            // Iterate over the remaining columns, and print each cell value
            for (String tag : row.getCustomElements().getTags()) {
                stringBuilder.append(tag + ":&nbsp;" + row.getCustomElements().getValue(tag) + "&nbsp;");
            }

            stringBuilder.append("<br/>");
        }
    }

    private List<ListEntry> saveGoalEntries(final SpreadsheetUtil spreadsheetUtil, final WorksheetEntry goalWorksheet)
        throws IOException, ServiceException {

        final URL listFeedUrl = goalWorksheet.getListFeedUrl();
        List<ListEntry> listEntries = listEntries = spreadsheetUtil.getListEntries(listFeedUrl);

        goalRepository.update(listEntries);
        return listEntries;
    }

    private List<ListEntry> saveClusterEntries(final SpreadsheetUtil spreadsheetUtil,
            final WorksheetEntry clusterWorksheet) throws IOException, ServiceException {
        final URL listFeedUrl = clusterWorksheet.getListFeedUrl();

        final List<ListEntry> listEntries = spreadsheetUtil.getListEntries(listFeedUrl);

        clusterRepository.update(listEntries);
        return listEntries;
    }
}
