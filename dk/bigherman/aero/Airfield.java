/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.bigherman.aero;

//import org.relique.jdbc.csv.*;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import com.bbn.openmap.LatLonPoint;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a Class to manage Airfield information, including weather.  Inlcudes positional information and methods for manipulating it. 
 * @author pasa, Mercantec
 * @date 27 November 2011
 * @version 0.1
 */
public class Airfield
{
    private String icaoCode = null;
    private String name;
    
    private String country = null;
    
    private LatLonPoint position = new LatLonPoint(0.0, 0.0);
    //private double latitude = 0.0;
    //private double longitude = 0.0;
    private int tileRow = 0;
    private int tileCol = 0;
    
    private GeoCalc geoCalc = new GeoCalc();
    
    private boolean isHomeBase = false;
    
    private String actualWeatherReport;

    /**
     * An enumerated type allowing programmers to work with actual weather colour state names
     */
    public enum ColourStates {

        /**
         * Visibility less than 0.8 km and cloudbase lower than 200 feet
         */
        RED,
        /**
         * Visibility better than 0.8 km and cloudbase higher than 200 feet
         */
        AMB,
        /**
         * Visibility better than 1.6 km and cloudbase higher than 300 feet
         */
        YLO,
        /**
         * Visibility better than 3.7 km and cloudbase higher than 700 feet
         */
        GRN,
        /**
         * Visibility better than 5 km and cloudbase higher than 1500 feet
         */
        WHT,
        /**
         * Visibility better than 8 km and cloudbase higher than 2500 feet
         */
        BLU,
        /**
         * Airfield closed for non-weather related reason
         */
        BLK
    };
    private ColourStates colourState;
    
    private String initMessage = null;
    
    /**
     * Initializes an airfield instance, by reference to an ICAO code.
     * @throws CreateAirfieldException when the requested ICAO code cannot be found/used to lookup an entry in the database
     * @param icaoCode - the ICAO code of the airfield to be generated 
     * @param dbConn - a connection the the airfield database
     * @throws SQLException 
     */
    public Airfield(String icaoCode, Connection dbConn) throws SQLException
    {  
        CreateAirfieldException ex = new CreateAirfieldException();
        
        this.icaoCode = icaoCode;
        
        try 
        {
            name = new String("".getBytes(), "UTF-16");
        } 
        catch (UnsupportedEncodingException ex1) 
        {
            Logger.getLogger(Airfield.class.getName()).log(Level.SEVERE, null, ex1);
        }
        
        // create a Statement object to execute the query with
        Statement stmt = dbConn.createStatement();

        // Select all information from relevant DB entry
        ResultSet results = stmt.executeQuery("SELECT name, country, lat, long, tileRow, tileCol FROM airfields WHERE icao = '" + icaoCode +"'");

        // dump out the results
        if (!results.next())
        {              
            ex.setMessage("Failed to create airfield " + icaoCode);
            throw ex;
        }
        else
        {
            name = results.getString("name");
            country = results.getString("country");
            position.setLatLon(results.getDouble("lat"), results.getDouble("long"));
            tileRow = results.getInt("tileRow");
            tileCol = results.getInt("tileCol");

            initMessage = icaoCode + "(" + name + "): " + "initialized";
        }

        // clean up
        results.close();
        stmt.close();
    }
       
    /**
     * Provides access to the airfield position
     * @return the airfield position as Lat and Long
     */
    public LatLonPoint getPosition()
    {
        return position;
    }
       
    /**
     * Calculates the airfield's distance froma provided reference point
     * @param referencePoint the point from which the airfield's distance is to be reported
     * @return the airfield's distance from the input reference point
     */
    public double distanceFrom(LatLonPoint referencePoint)
    {
        double distance = 0.0;
        double bearing = 0.0;
        
        bearing = geoCalc.rhumbBearing(referencePoint, position);
        distance = geoCalc.rhumbDistance(referencePoint, position, bearing);
        
        return distance;
    }
    
    /**
     * Calculates whether the airfield qualifies as "local" to the provided reference point
     * @param referencePoint the point from which an assessment of whether the airfield is local is made 
     * @return a boolean indicating whether the airfield is local to the reference point
     */
    public boolean isLocal(LatLonPoint referencePoint)
    {
        int refTileRow = (int)Math.floor(referencePoint.getLatitude()) + 90;
        int refTileCol = (int)Math.floor(referencePoint.getLongitude()) + 180;
        
        if(Math.abs(refTileRow-tileRow)<5 && Math.abs(refTileCol-tileCol)<10)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Provides access to the airfield's given name
     * @return the airfield's actual name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Provides the means to associate a weather report with the airfield instance 
     * @param actualWeatherReport a METAR actual weather report to be associated with the airfield
     */
    public void setWeather(String actualWeatherReport)
    {
        this.actualWeatherReport = actualWeatherReport;
    }
    
    /**
     * Provides access to the airfield's associated actual weather information
     * @return the METAR actual weather report associated with the airfield
     */
    public String getWeather()
    {
        return actualWeatherReport;
    }
    
    /**
     * Provides the means to associate a weather colour state with the airfield instance
     * @param colourState the weather colour state to be associated with the airfield
     */
    public void setColourState(ColourStates colourState)
    {
        this.colourState = colourState;
    }
    
    /**
     * Provides access to the airfield's associated actual weather colour state information
     * @return the current weather colour state associated with the airfield
     */
    public ColourStates getColourState()
    {
        return colourState;
    }
}
