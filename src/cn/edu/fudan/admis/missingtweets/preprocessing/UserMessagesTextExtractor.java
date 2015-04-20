package cn.edu.fudan.admis.missingtweets.preprocessing;

import cn.edu.fudan.admis.missingtweets.util.Util;

import java.io.*;

/**
 * Created by zhengxx on 15/4/20.
 */
public class UserMessagesTextExtractor
{
    public static void main(String[] args) throws IOException
    {
        if (args.length != 2)
        {
            Util.Usage(UserMessagesTextExtractor.class, new String[]{
                    "tweets dir", "tweetsText dir"});
        }

        File tweetsDir = new File(args[0]);
        File[] tweetsFiles = tweetsDir.listFiles();

        File tweetsTextDir = new File(args[1]);
        if (!tweetsTextDir.isDirectory())
        {
            tweetsTextDir.mkdir();
        }

        long totalCount = 0;
        for (File file : tweetsFiles)
        {
            totalCount++;
            System.out.println("extracting " + file.getName() + "...");
            PrintWriter out = new PrintWriter(tweetsTextDir + File.separator
                    + file.getName() + "text");

            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null)
            {
                if (line.startsWith("Text: "))
                {
                    out.println(line.substring(6));
                    // multiple line Text
                    while ((line = in.readLine()) != null
                            && !line.startsWith("URL: "))
                    {
                        out.println(line);
                    }
                    out.flush();
                }
            }
            in.close();
            out.close();
        }
        System.out.println("Done. total count = " + totalCount);
    }
}
