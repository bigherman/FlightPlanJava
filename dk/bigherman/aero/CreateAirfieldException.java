/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.bigherman.aero;

/**
 * Provides a container to report failed initialization of an Airfield Class
 * @author pasa, Mercantec
 * @date 27 November 2011
 * @version 0.1
 */
public class CreateAirfieldException extends RuntimeException
{
    private String message = null;
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    @Override
    public String getMessage()
    {
        return message;
    }
}
