/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.bigherman.aero;

import java.sql.*;
import com.bbn.openmap.LatLonPoint;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Test Harness, to test all aspects of the dk.bigherman.aero package 
 * @author pasa
 */
public class AirfieldTest 
{ 
    public static void main(String[] argv)
    {
        HashMap <String, Integer> icaoLookUp = new HashMap<String, Integer>();
        int mapIndex = 0;
        
        LatLonPoint leemingPosition = new LatLonPoint(54.25, -1.52);
        
        AirfieldDBConnection airfieldDBConn = new AirfieldDBConnection();
        Connection dbConn = airfieldDBConn.getConnection("jdbc:relique:csv:c:/airfields");
        
        Airfield airfield1 = null;
        Airfield airfield2 = null;
        Airfield airfield3 = null;
        try 
        {
            airfield1 = new Airfield("EGOO", dbConn);
            icaoLookUp.put("EGOO", (Integer)mapIndex);
            mapIndex++;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(AirfieldTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (CreateAirfieldException ex)
        {   
            System.out.println(ex.getMessage());
        }
        
        try 
        {
            airfield2 = new Airfield("EKBI", dbConn);
            icaoLookUp.put("EKBI", (Integer)mapIndex);
            mapIndex++;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(AirfieldTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (CreateAirfieldException ex)
        {   
            System.out.println(ex.getMessage());
        }
        
        try 
        {
            airfield3 = new Airfield("EKAH", dbConn);
            icaoLookUp.put("EKAH", (Integer)mapIndex);
            mapIndex++;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(AirfieldTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (CreateAirfieldException ex)
        {   
            System.out.println(ex.getMessage());
        }

        LatLonPoint position = new LatLonPoint(airfield2.getPosition());
        System.out.print("Lat: " + position.getLatitude() + " ");
        System.out.println("Long: " + position.getLongitude());

        airfield2.setColourState(Airfield.ColourStates.YLO);

        GeoCalc geoCalc = new GeoCalc();

        double bearing = geoCalc.rhumbBearing(leemingPosition, airfield2.getPosition());
        System.out.println("Rhumb bearing from Leeming = " + bearing);
        System.out.println("Rhumb distance from Leeming = " + geoCalc.rhumbDistance(leemingPosition, airfield2.getPosition(), bearing));
        System.out.println("Rhumb distance from Leeming = " + airfield2.distanceFrom(leemingPosition));
        System.out.println("Airfield 2: " + airfield2.getColourState());
        
        airfieldDBConn.closeConnection(dbConn);
        
        System.out.println(icaoLookUp.containsKey("EGOO"));
        System.out.println(icaoLookUp.containsKey("EKBI"));
        
        //System.out.println("Leeming close to: " + airfield1.getName() + " is " + airfield1.isLocal(leemingPosition));
        System.out.println("Leeming close to " + airfield2.getName() + " is: " + airfield2.isLocal(leemingPosition));
        System.out.println("Leeming close to " + airfield3.getName() + " is: " + airfield3.isLocal(leemingPosition));
    }
}
