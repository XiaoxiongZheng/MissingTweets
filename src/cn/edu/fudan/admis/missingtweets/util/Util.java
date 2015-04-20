package cn.edu.fudan.admis.missingtweets.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Util
{
	private Util()
	{
		throw new AssertionError();
	}

    /**
     * read file to an ArrayList
     * @param path is the file's path
     * @param lines is an ArrayList
     */
    public static void readFile(String path, ArrayList<String> lines){
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null)
            {
                lines.add(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * write to a file from an ArrayList
     * @param path is file's path
     * @param lines is an ArrayList
     */
    public static void writeFile(String path, ArrayList<String> lines)
    {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            for (String line : lines)
                bw.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * general usage
     * @param c
     * @param args
     */
	public static void Usage(Class<?> c, String[] args)
	{
		System.err.print("Usage: ");
		System.err.print(c.getSimpleName());
		for (String string : args)
		{
			System.err.print(" [" + string + "]");
		}
		System.err.println();
		System.exit(1);
	}

    // todo month map
    public static HashMap<String, String> monthMap = new HashMap<>();
    static
    {
        monthMap.put("Jan", "01");
        monthMap.put("Feb", "02");
        monthMap.put("Mar", "03");
        monthMap.put("Apr", "04");
        monthMap.put("May", "05");
        monthMap.put("Jun", "06");
        monthMap.put("Jul", "07");
        monthMap.put("Aug", "08");
        monthMap.put("Sep", "09");
        monthMap.put("Oct", "10");
        monthMap.put("Nov", "11");
        monthMap.put("Dec", "12");
    }

    // todo sample users
    public static Set<String> sampleUsers = new HashSet<>();
    static
    {
        sampleUsers.add("30642956");
        sampleUsers.add("230033963");
        sampleUsers.add("101221067");
        sampleUsers.add("191516951");
        sampleUsers.add("228066520");
        sampleUsers.add("107528407");
        sampleUsers.add("259081755");
        sampleUsers.add("126674714");
        sampleUsers.add("256260356");
        sampleUsers.add("194155007");
    }
}
