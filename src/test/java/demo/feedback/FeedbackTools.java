package demo.feedback;

import com.google.api.client.util.Lists;

import java.io.*;
import java.util.List;

/**
 * Created by tharms on 21/11/15.
 */
public class FeedbackTools {
    public static List<String> readFile(String file) {
        final List<String> rawData = Lists.newArrayList();
        final File f = new File(file);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line;
            while((line = br.readLine()) != null) {
                if (line.startsWith("\"Feedback Receiver\"")) {
                    rawData.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData;
    }

    public static String readFeedbackAsString(String filename) {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

    public static List<String> readComplexFile(String file) {
        List<String> rawData = Lists.newArrayList();
        File f = new File(file);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line;
            boolean firstLine = true;
            while((line = br.readLine()) != null) {
                if(firstLine) {
                    firstLine = false;
                } else {
                    rawData.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData;
    }

}
