package demo.feedback;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by tharms on 21/11/15.
 */
public class FeedbackParser {
    public Map<String, List<String>> parse(List<String> rawData) {
        return parseInternal(rawData);
    }

    private Map<String, List<String>> parseInternal(List<String> rawData) {

        if(rawData.isEmpty()) {
            return Maps.newHashMap();
        }

        Map<String, List<String>> repaired = Maps.newHashMap();
        List<String> brokenLines = Lists.newArrayList();

        boolean brokenLineMerged = true;
        for(String line : rawData) {
            String[] split = splitAndClean(line);

            if (split.length == FeedbackReceiverParser.LINE_ELEMENTS) {
                final String receiver = split[0].trim();

                if (repaired.get(receiver) == null) {
                    List<String> receiverFeedback = Lists.newArrayList();
                    receiverFeedback.add(line);
                    repaired.put(receiver, receiverFeedback);
                }
                else {
                    repaired.get(receiver).add(line);
                }
            } else {
                if (brokenLineMerged) {
                    brokenLines.add(line);
                    brokenLineMerged = false;
                } else {
                    String brokenLine = brokenLines.get(brokenLines.size()-1);
                    String newAttempt = brokenLine + "\n" + line;
                    brokenLines.set(brokenLines.size()-1, newAttempt);
                    String[] newLine = splitAndClean(newAttempt);
                    if (newLine.length >= FeedbackReceiverParser.LINE_ELEMENTS){
                        brokenLineMerged = true;
                    } else {
                        brokenLineMerged = false;
                    }

                }
            }
        }

        if (rawData.size() > brokenLines.size()) { // try to repair as long as we get better

            Map<String, List<String>> newRepaired = parseInternal(brokenLines);
            for (Map.Entry<String, List<String>> entry : newRepaired.entrySet()) {
                if (repaired.containsKey(entry.getKey())) {
                    repaired.get(entry.getKey()).addAll(entry.getValue());
                } else {
                    repaired.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return repaired;
    }

    private String[] splitAndClean(String line) {
        String[] splits = line.split("\\\",");
        for (int i=0; i<splits.length; i++) {
            splits[i] = splits[i].replace("\\\"","").replace("\\","");
        }
        return splits;
    }
}
