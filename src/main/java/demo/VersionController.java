package demo;

import com.google.api.client.util.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tharms on 26/09/15.
 */
@RestController
public class VersionController {

    @RequestMapping("/version")
    public String getVersion() {
        return "v1.0";
    }
}
