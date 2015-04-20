package cn.edu.fudan.admis.missingtweets.preprocessing;

import cn.edu.fudan.admis.missingtweets.db.MySQLConnection;
import cn.edu.fudan.admis.missingtweets.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by zhengxx on 15/4/20.
 */

public class MessagesContentExtractor
{
    public static void main(String[] args) throws IOException
    {
        if (args.length != 1)
        {
            Util.Usage(MessagesContentExtractor.class,
                    new String[]{"tweetsDir"});
        }

        File tweetsDir = new File(args[0]);
        File[] tweetsFiles = tweetsDir.listFiles();
        MySQLConnection connection = new MySQLConnection();

        int totalCount = 0;
        for (File file : tweetsFiles)
        {
            String uid = file.getName();
            if (Util.sampleUsers.contains(uid))
            {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line, tweet = "", ctime = "", retcount = "", favorite;
                while ((line = br.readLine()) != null)
                {
                    if (line.startsWith("Origin: "))
                    {
                        tweet = line.substring(8).replaceAll("'", "`");
                    }
                    else if (line.startsWith("Time: "))
                    {
                        String time = line.substring(6);
                        String[] timePart = time.split(" ");
                        ctime = timePart[5] + "-"
                                + Util.monthMap.get(timePart[1]) + "-"
                                + timePart[2] + " " + timePart[3];
                    }
                    else if (line.startsWith("RetCount: "))
                    {
                        retcount = line.substring(10);
                    }
                    else if (line.startsWith("Favorite: "))
                    {
                        favorite = line.substring(10);
                        String insert = "insert into contents values('%s','%s','%s','%s',%s)";
                        connection.insert(String.format(insert, uid, tweet,
                                ctime, retcount, favorite));
                        totalCount++;
                    }
                }
                br.close();
            }
        }
        System.out.println("Done. total count = " + totalCount);
    }
}