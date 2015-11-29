package demo;

import com.google.api.client.util.Lists;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import demo.feedback.FeedbackParser;
import demo.feedback.FeedbackReceiverParser;
import demo.feedback.FeedbackRepository;
import demo.feedback.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by tharms on 26/09/15.
 */
@Controller
public class FeedbackController {

    @RequestMapping(value="/index")
    public ModelAndView index() {
        final ModelAndView mav = new ModelAndView();

        mav.setViewName("feedback");

        final UserService userService = UserServiceFactory.getUserService();
        final String logout = userService.createLogoutURL("login");
        final String name = userService.getCurrentUser().getEmail();
        mav.addObject("logout", logout);
        mav.addObject("userName", name);

        return mav;
    }
}
