/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.PrintWriter;

/**
 *
 * @author praveen
 */
public class FormHelper {        
    
    public String makeRow(String label, String type, String name, String value)
    {
        return "<tr>"
                + "<td align='right'>"
                + "<label for='" + name + "'>" + label + "</label>"
                + "</td>"
                + "<td>"
                + "<input type='" + type + "' name='" + name + "' value='" + value + "'/>"
                + "</td>"
                + "</tr>";
    }
}
