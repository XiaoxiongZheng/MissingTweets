package cn.edu.fudan.admis.missingtweets.preprocessing;

import cn.edu.fudan.admis.missingtweets.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by zhengxx on 15/4/20.
 */
public class MessagesMentionExtractor
{
    public static void main(String[] args) throws Exception
    {
        if (args.length != 2)
        {
            Util.Usage(MessagesMentionExtractor.class, new String[]{
                    "tweetsDir", "tweetsMentions.txt"});
        }

        File tweetsDir = new File(args[0]);
        File[] tweetsFiles = tweetsDir.listFiles();
        FileWriter fw = new FileWriter(args[1]);
        long totalCount = 0;
        for (File file : tweetsFiles)
        {
            long count = 0;
            long mention = 1;
            String uid1 = file.getName();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String time = "";
            String mentions;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("Time: "))
                {
                    time = line.substring(6);
                }
                else if (line.startsWith("MentionedEntities: "))
                {
                    mentions = line.substring(19);
                    if (mentions == null || mentions.equals(""))
                    {
                        continue;
                    }
                    else
                    {
                        String[] uid2s = mentions.split(" ");
                        for (String uid2 : uid2s)
                        {
                            if (uid2 != null && !uid2.equals(""))
                            {
                                String[] timePart = time.split(" ");
                                String timeFormat = timePart[5] + "-"
                                        + Util.monthMap.get(timePart[1]) + "-"
                                        + timePart[2] + " " + timePart[3];
                                fw.write(uid1 + "\t");
                                fw.write(uid2 + "\t");
                                fw.write(timeFormat + "\t");
                                fw.write(mention + "\n");
                                count++;
                            }
                        }
                        mention++;
                        fw.flush();
                    }
                }
                else
                {
                    continue;
                }
            }
            br.close();
            System.out.println(file.getName() + " extracted... count = "
                    + count);
            totalCount += count;
        }
        fw.close();
        System.out.println("Done. total count = " + totalCount);
    }
}
