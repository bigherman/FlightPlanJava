/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.bigherman.aero;

import com.bbn.openmap.LatLonPoint;

/**
 * Provides the mathematical engine for calculating geographical information e.g distances, lines of bearing, etc.
 * @author pasa, Mercantec
 * @date 27 November 2011
 * @version 0.1
 */
public class GeoCalc
{
    private static final int EARTH_RADIUS = 3438;  // Earth's radius in nautical miles
    private static final double DEG_TO_RAD = (Math.PI)/180.0;
    
    /**
     * 
     */
    public GeoCalc() 
    {
    }
    
    /**
     * Rhumb Distance is the distance between two points along a line of constant bearing
     * @param from point from which distance along Rhumbline should be calculated, must be converted to radians before use
     * @param to point to which distance along Rhumbline should be calculated, must be converted to radians before use
     * @param rhumbBearing bearing of Rhumbline between two points, along which distance should be calculated, must be provided in dgrees
     * @return
     */
    public double rhumbDistance(LatLonPoint from, LatLonPoint to, double rhumbBearing)
    {
        double distance = 0.0;
        
        //Input lat, long must be coverted into radians, bearing in degrees!!!!!!!

        double deltaCoLat=(to.getLatitude()-from.getLatitude())*DEG_TO_RAD;

        if(rhumbBearing == 90.0 || rhumbBearing == 270.0)
        {
            distance=Math.abs(EARTH_RADIUS*Math.cos(from.getLatitude())*((to.getLongitude()-from.getLongitude())*DEG_TO_RAD));
        }
        else
        {
            distance=Math.abs((deltaCoLat*EARTH_RADIUS)/Math.cos(rhumbBearing*DEG_TO_RAD));
        }
        return distance;
    }
    
    /**
     * NOT YET IMPLEMENTED
     * @param from
     * @param to
     * @return
     */
    public double greatCircleDistance(LatLonPoint from, LatLonPoint to)
    {
        double greatCircleDistance = 0.0;
        return greatCircleDistance;
    }
    
    /**
     * Rhumb Bearing is a line of constant bearing between two points on the Earth
     * @param from point from which bearing along Rhumbline should be calculated
     * @param to point to which bearing along Rhumbline should be calculated
     * @return bearing of Rhumbline between two points
     */
    public double rhumbBearing(LatLonPoint from, LatLonPoint to)
    {
        double rhumbBearing = 0.0;
        
        //Input parameters must be converted into radians!!!!!!!
    
        double deltaLong = to.getLongitude()*DEG_TO_RAD - from.getLongitude()*DEG_TO_RAD;

        double latFactor1 = Math.log(Math.tan(Math.PI/4.0 + (from.getLatitude()*DEG_TO_RAD/2.0)));

        double latFactor2 = Math.log(Math.tan(Math.PI/4.0 + (to.getLatitude()*DEG_TO_RAD/2.0)));

        double deltaLatFactor = latFactor2 - latFactor1;
          
        if (deltaLatFactor==0 && deltaLong==0)
        {
                rhumbBearing = 0;
        }
        else
        {
                rhumbBearing = (360 + (Math.atan2(deltaLong, deltaLatFactor)*180.0/Math.PI))%360;
        }
        return rhumbBearing;
    }  
}
