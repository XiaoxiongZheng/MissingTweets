package cn.edu.fudan.admis.missingtweets.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLConnection
{
	private static final String HOST = "localhost";
	private static final int PORT = 3306;
	private static final String DB = "tweetpeople";
	private static final String USER = "root";
	private static final String PW = "root";
	private static final String URL = String.format("jdbc:mysql://%s:%d/%s",
			HOST, PORT, DB);
	private Connection conn;
	private Statement stmt;

	public MySQLConnection()
	{
		try
		{
			conn = DriverManager.getConnection(URL, USER, PW);
			stmt = conn.createStatement();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			stmt.close();
			conn.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * One column.
	 * 
	 * @param query
	 * @return
	 */
	public ArrayList<String> queryOne(String query)
	{
		try
		{
			ResultSet rset = stmt.executeQuery(query);
			ArrayList<String> result = new ArrayList<>();
			while (rset.next())
			{
				result.add(rset.getString(1));
			}
			return result;
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
