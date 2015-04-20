package cn.edu.fudan.admis.missingtweets.preprocessing;

import cn.edu.fudan.admis.missingtweets.util.Util;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by zhengxx on 15/4/20.
 */
public class MessagesExtractor
{
    public static void main(String[] args) throws Exception
    {
        if (args.length != 2)
        {
            Util.Usage(MessagesExtractor.class, new String[]{"tweetsDir",
                    "tweets.txt"});
        }

        File tweetsDir = new File(args[0]);
        File[] tweetsFiles = tweetsDir.listFiles();
        FileWriter fw = new FileWriter(args[1]);
        long totalCount = 0;
        for (File file : tweetsFiles)
        {
            String uid = file.getName();
            fw.write(uid);
            fw.write("\n");
            totalCount++;
        }
        fw.close();
        System.out.println("Done. total count = " + totalCount);
    }
}