package cn.edu.fudan.admis.missingtweets.util;

import java.io.*;
import java.util.ArrayList;

public class Util
{
	private Util()
	{
		throw new AssertionError();
	}

    /**
     * read file
     * @param path is the file's path
     * @param lines is an ArrayList
     * @throws IOException
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
}
