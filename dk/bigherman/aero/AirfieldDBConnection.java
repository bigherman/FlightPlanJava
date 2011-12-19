/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.bigherman.aero;

import java.sql.*;

/**
 * Wraps a database connection to the underlying airfield database
 * @author pasa, Mercantec
 * @date 27 November 2011
 * @version 0.1
 */
public class AirfieldDBConnection
{
    AirfieldDBConnection()
    {
        
    }
    
    /**
     * Generates a connection to the underlying airfield database
     * @param connString the Connection String for the database to be opened
     * @return the established database connection
     */
    public Connection getConnection(String connString)
    {
        Connection conn = null;
        
        try
        {
          // load the driver into memory
          Class.forName("org.relique.jdbc.csv.CsvDriver");

          // create a connection. The first command line parameter is assumed to
          //  be the directory in which the .csv files are held
          conn = DriverManager.getConnection(connString);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
        return conn;
    }
    
    /**
     * Closes the given connection to the underlying airfield database
     * @param conn an existing database connection
     */
    public void closeConnection(Connection conn)
    {
        try
        {
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }        
    }
}
