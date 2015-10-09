package demo;

import java.io.IOException;

import java.util.List;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.util.Lists;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

/**
 * Created by tharms on 26/09/15.
 */
@Controller
public class TomAdminController {

    @RequestMapping(value = "/tomadmin", method = RequestMethod.GET)
    public ModelAndView index() {
        final ModelAndView mav = new ModelAndView();
        final SpreadsheetUtil spreadsheetUtil = new SpreadsheetUtil();

        mav.setViewName("tomadmin");

        final StringBuilder stringBuilder = new StringBuilder();

        List<SpreadsheetEntry> spreadsheets = Lists.newArrayList();
        try {
            spreadsheets = spreadsheetUtil.readSpreadsheetEntries();
        } catch (IOException | ServiceException e) {
            e.printStackTrace();
            stringBuilder.append(e.getMessage());
        }

        // Iterate through all of the spreadsheets returned
        for (SpreadsheetEntry spreadsheet : spreadsheets) {

            // Print the title of this spreadsheet to the screen
            stringBuilder.append(spreadsheet.getTitle().getPlainText());
        }

        final UserService userService = UserServiceFactory.getUserService();
        final String logout = userService.createLogoutURL("login");
        mav.addObject("message", stringBuilder.toString());
        mav.addObject("logout", logout);
        return mav;
    }
}
