package cn.edu.fudan.admis.missingtweets.preprocessing;

import cn.edu.fudan.admis.missingtweets.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhengxx on 15/4/20.
 */
public class MentionMessagesTextExtractor
{

    public static void main(String[] args) throws Exception
    {
        if (args.length != 2)
        {
            Util.Usage(MessagesContentExtractor.class, new String[]{
                    "tweetsDir", "atTweetsTextDir"});
        }

        for (String user : Util.sampleUsers)
        {
            System.out.print("extracting " + user + " ... ");
            File file = new File(args[1] + File.separator + user);
            file.mkdir();
            Map<String, String> mentionUsers = new HashMap<>();
            BufferedReader br = new BufferedReader(new FileReader(args[0]
                    + File.separator + user));
            String line;
            StringBuffer text = new StringBuffer();
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("Text: "))
                {
                    text.setLength(0);
                    text.append(line.substring(6));
                    // multiple line Text
                    while ((line = br.readLine()) != null
                            && !line.startsWith("URL: "))
                    {
                        text.append(line);
                    }
                }
                else if (line.startsWith("MentionedEntities: "))
                {
                    String mentions = line.substring(19);
                    if (mentions == null || mentions.equals(""))
                    {
                    }
                    else
                    {
                        String[] uid2s = mentions.split(" ");
                        for (String uid2 : uid2s)
                        {
                            if (uid2 != null && !uid2.equals(""))
                            {
                                if (mentionUsers.containsKey(uid2))
                                {
                                    String s = mentionUsers.get(uid2) + text
                                            + "\n";
                                    mentionUsers.put(uid2, s);
                                }
                                else
                                {
                                    String s = text + "\n";
                                    mentionUsers.put(uid2, s);
                                }
                            }
                        }
                    }
                }
            }
            br.close();
            for (String key : mentionUsers.keySet())
            {
                FileWriter fw = new FileWriter(args[1] + File.separator + user
                        + File.separator + key);
                fw.write(mentionUsers.get(key));
                fw.flush();
                fw.close();
            }
            System.out.println("done with " + mentionUsers.size());
        }
    }
}
