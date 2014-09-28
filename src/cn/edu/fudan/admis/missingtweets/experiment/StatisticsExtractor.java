package cn.edu.fudan.admis.missingtweets.experiment;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.edu.fudan.admis.missingtweets.db.MySQLConnection;
import cn.edu.fudan.admis.missingtweets.util.Util;

public class StatisticsExtractor
{
	// "select frcount from users where uid = %s"
	private static String uid2 = "select uid2 from network where uid1 = %s";
	private static String uid2Mutual = "select uid2 from network where uid1 = %s and uid2 in ( select uid1 from network where uid2 = %s)";
	private static String uid2Single = "select uid2 from network where uid1 = %s and uid2 not in ( select uid1 from network where uid2 = %s)";

	public static void main(String[] args) throws Exception
	{
		if (args.length != 2)
		{
			Util.Usage(StatisticsExtractor.class, new String[] { "tweetsDir",
					"statistics.txt" });
		}

		File tweetsDir = new File(args[0]);
		File[] tweetsFile = tweetsDir.listFiles();
		Set<String> idSet = new HashSet<>();
		System.out.println("init idSet ...");
		for (File f : tweetsFile)
		{
			String id = f.getName();
			idSet.add(id);
		}
		System.out.println("init idSet done");

		int total = idSet.size();
		int count = 0;
		int per = 10;
		MySQLConnection connection = new MySQLConnection();
		FileWriter fw = new FileWriter(args[1]);
		fw.write("id");
		fw.write("\t");
		fw.write("total");
		fw.write("\t");
		fw.write("mutual");
		fw.write("\t");
		fw.write("mutualT");
		fw.write("\t");
		fw.write("single");
		fw.write("\t");
		fw.write("singleT");
		fw.write("\n");

		System.out.println("extract statistics ...");
		for (String id : idSet)
		{
			count++;
			if (count % per == 0)
				System.out.println(count + "/" + total);

			fw.write(id);
			fw.write("\t");

			String query = String.format(uid2, id);
			ArrayList<String> list = connection.queryOne(query);
			if (list.size() == 0)
				continue;
			fw.write("" + list.size());
			fw.write("\t");

			query = String.format(uid2Mutual, id, id);
			list = connection.queryOne(query);
			fw.write("" + list.size());
			fw.write("\t");

			int mutualT = 0;
			for (String string : list)
				if (idSet.contains(string))
					mutualT++;
			fw.write("" + mutualT);
			fw.write("\t");

			query = String.format(uid2Single, id, id);
			list = connection.queryOne(query);
			fw.write("" + list.size());
			fw.write("\t");

			int singleT = 0;
			for (String string : list)
				if (idSet.contains(string))
					singleT++;
			fw.write("" + singleT);

			fw.write("\n");
			fw.flush();
		}
		System.out.println("extract statistics done");
		fw.close();
		connection.close();
	}
}
