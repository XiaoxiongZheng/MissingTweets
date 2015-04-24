package cn.edu.fudan.admis.missingtweets.preprocessing;

import cn.edu.fudan.admis.missingtweets.util.Util;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;

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

    public static void cleanData(String oldDirPath, String newDirPath) throws IOException {
        File oldDir = new File(oldDirPath);
        File newDir = new File(newDirPath);

        if (!newDir.isDirectory()) {
            System.out.println("Don't have directory: " + newDirPath);
            newDir.mkdir();
            System.out.println("Create new directory: " + newDirPath);
        }

        CharArraySet stop_words = new CharArraySet(StopWords.stop_words, true);
        Analyzer analyzer = new EnglishAnalyzer(stop_words);
        int total = 0;
        for (File tweet : oldDir.listFiles())
        {
            String name = tweet.getName();
            System.out.println("clean tweet: " + name + " ... ");
            BufferedReader br = new BufferedReader(new FileReader(oldDirPath + "/" + name));
            PrintWriter pw = new PrintWriter(newDirPath + "/" + name);
            String line;
            while ((line = br.readLine()) != null)
            {
                TokenStream stream = analyzer.tokenStream("tweets", line);
                CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
                try{
                    stream.reset();
                    while (stream.incrementToken())
                    {
                        pw.write(term.toString() + " ");
                    }
                    pw.write("\n");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.exit(1);
                }
                finally {
                    stream.close();
                }
            }
            total++;
            br.close();
            pw.close();
        }
        System.out.println("Done!");
        System.out.println("total = " + total);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            Util.Usage(DataProcessing.class, new String[]{"origin tweets dir", "target tweets dir"});
            System.exit(1);
        }
        cleanData(args[0], args[1]);
    }
}
