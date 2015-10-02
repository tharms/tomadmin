package demo;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by tharms on 26/09/15.
 */
@Controller
public class TomAdminController {
    @RequestMapping(value = "/tomadmin", method = RequestMethod.GET)
    public ModelAndView hello() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("tomadmin");

        String str = "Hello World!";
        mav.addObject("message", str);
        return mav;
    }
}
