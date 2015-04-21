package cn.edu.fudan.admis.missingtweets.preprocessing;

import cn.edu.fudan.admis.missingtweets.util.Util;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by zhengxx on 15/4/14.
 */

// todo log
public class DataProcessing {

    public static void processLine(String line, ArrayList<String> words)
    {
        String[] wordsList = line.split("[ '.!|\\n\\r\\t@    \"]");
        for (String word : wordsList)
        {
            if (word != null && StopWords.isStopWords(word))
                words.add(word);
        }
    }
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            Util.Usage(DataProcessing.class, new String[]{"origin tweets dir", "target tweets dir"});
            System.exit(1);
        }
        File oldTweetsDir = new File(args[0]);

        for (File tweet : oldTweetsDir.listFiles())
        {
            String name = tweet.getName();
            System.out.println(name + " is processing ... ");
            BufferedReader br = new BufferedReader(new FileReader(tweet));
            BufferedWriter bw = new BufferedWriter(new FileWriter(args[1] + "/" + name));
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] wordsList = line.split("[\\~`,<>; '.!|\\n\\r\\t@:；·，。《》、【】？?￥……（）_ /  \"#$%^&*()-+\\{\\}\\[\\]1234567890]");
                for (String word : wordsList)
                {
                    try{
                    if (word != null && !StopWords.isStopWords(word) && !word.equals(""))
                        bw.write(word + " ");
                    }
                    catch(Exception e) {
                        System.out.println(word);
                        e.printStackTrace();
                    }
                }
                bw.write("\n");
            }
            br.close();
            bw.close();
        }
        System.out.println("Done!");
    }
}
