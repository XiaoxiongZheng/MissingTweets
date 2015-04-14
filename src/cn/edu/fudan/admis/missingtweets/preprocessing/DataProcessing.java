package cn.edu.fudan.admis.missingtweets.preprocessing;

import java.util.ArrayList;

/**
 * Created by zhengxx on 15/4/14.
 */
public class DataProcessing {
    public static void processLine(String line, ArrayList<String> words)
    {
        String[] wordsList = line.split("[ '.!|\\n\\r\\t    \"]");
        for (String word : wordsList)
        {
            if (word != null && StopWords.isStopWords(word))
                words.add(word);
        }
    }
}
