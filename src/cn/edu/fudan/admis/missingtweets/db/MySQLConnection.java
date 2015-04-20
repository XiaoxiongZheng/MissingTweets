package cn.edu.fudan.admis.missingtweets.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    static final Logger logger = LogManager.getLogger(MySQLConnection.class);

    public MySQLConnection()
    {
        try
        {
            conn = DriverManager.getConnection(URL, USER, PW);
            stmt = conn.createStatement();

            logger.debug("MySQL Connection is on");
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

            logger.debug("MySQL Connection is off");
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Column delimited with "***".
     *
     * @param query
     * @return
     */
    public ArrayList<String> queryDelimiter(String query)
    {
        logger.debug("Query: " + query);
        try
        {
            ResultSet rset = stmt.executeQuery(query);
            int col = rset.getMetaData().getColumnCount();
            ArrayList<String> result = new ArrayList<>();
            while (rset.next())
            {
                StringBuffer sb = new StringBuffer();
                for (int i = 1; i <= col; i++)
                {
                    sb.append(rset.getString(i));
                    sb.append("***");
                }
                result.add(sb.toString());
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

    /**
     * One column.
     *
     * @param query
     * @return
     */
    public ArrayList<String> queryOne(String query)
    {
        logger.debug("Query: " + query);
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

    public void insert(String insert)
    {
        logger.debug("Insert: " + insert);
        try
        {
            stmt.executeUpdate(insert);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void set(String set)
    {
        logger.debug("Set: " + set);
        try
        {
            stmt.execute(set);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String userIdToName(String id)
    {
        String query = "select uname from users where uid = " + id;
        ArrayList<String> name = queryOne(query);
        if (name == null || name.size() == 0)
            return id;
        else
            return name.get(0);
    }

    public String userNameToId(String name)
    {
        String query = "select uid from users where uname = '" + name + "'";
        ArrayList<String> id = queryOne(query);
        if (id == null || id.size() == 0)
            return name;
        else
            return id.get(0);
    }
}
