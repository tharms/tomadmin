package demo;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import demo.feedback.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by tharms on 26/09/15.
 */
@RestController
public class FeedbackRestController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @RequestMapping("/receiver")
    public List<FeedbackReceiver> receiver() {
        List<Row> persistedFeedbacks = Lists.newArrayList();
        Map<String, FeedbackReceiver> receiver = Maps.newHashMap();
        final UserService userService = UserServiceFactory.getUserService();

        if (userService.isUserLoggedIn()) {
            String leadName = userService.getCurrentUser().getUserId();
            persistedFeedbacks = feedbackRepository.getPersistedFeedbacksOfUser(leadName);
            for (Row row : persistedFeedbacks) {
                if (! receiver.containsKey(row.getReceiver()))
                receiver.put(row.getReceiver(), new FeedbackReceiver(row.getId().intValue(), row.getReceiver()));
            }
        }
        return Lists.newArrayList(receiver.values());
    }

    @RequestMapping(value="/feedback", method= RequestMethod.POST)
    public List<String> receiveFeedbackData(@RequestBody final FeedbackData feedback){
        final UserService userService = UserServiceFactory.getUserService();

        List<String> errorProtocol = Lists.newArrayList();
        if (userService.isUserLoggedIn()) {
            final String userId = userService.getCurrentUser().getUserId();
            final String peopleLead = feedback.getPeopleLead();
            final String feedbackData = feedback.getFeedback();
            errorProtocol = saveFeedback(userId, feedbackData, peopleLead);
        }
        return errorProtocol;
    }

    @RequestMapping(value="/feedback/{rowId}", method= RequestMethod.GET)
    public FeedbackResponse getFeedback(@PathVariable("rowId") final String rowId){
        final UserService userService = UserServiceFactory.getUserService();

        List<Row> feedbackRows = Lists.newArrayList();
        String leadName;
        if (userService.isUserLoggedIn()) {
            leadName = userService.getCurrentUser().getUserId();
            feedbackRows = feedbackRepository.getFeedbackForSampleRow(leadName, Long.parseLong(rowId));
        }
        return FeedbackConverter.toFeedbackResponse(feedbackRows);
    }


    private List<String> saveFeedback(String userId, String feedbackData, String peopleLead) {
        final FeedbackParser feedbackParser = new FeedbackParser();
        final List<String> feedbackLines = readRawData(feedbackData);
        final Map<String, List<String>> parsed = feedbackParser.parse(feedbackLines);
        final FeedbackReceiverParser feedbackReceiverParser = new FeedbackReceiverParser(userId, peopleLead);
        final List<String> errorProtocol = Lists.newArrayList();

        long idOffset = 1L;
        feedbackRepository.removeFeedbackOfUser(userId);
        for (List<String> feedbackPerReceiver : parsed.values()) {
            try {
                final List<Row> rows = feedbackReceiverParser.parse(feedbackPerReceiver, idOffset);
                idOffset += rows.size();
                feedbackRepository.add(rows);
            } catch (Exception e) {
                errorProtocol.add(e + " " + feedbackPerReceiver.get(0).substring(0,40));
            }
        }
        return errorProtocol;
    }

    private List<String> readRawData(String feedback) {
        List<String> rawData = Lists.newArrayList();
        String[] lines = feedback.split("\\n");
        for (String line : lines) {
            if (!line.contains("\"Receiver\",\"Provider\",\"Type\",\"Status\""))
                rawData.add(line);
        }
        return rawData;
    }

}
