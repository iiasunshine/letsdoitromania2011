/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.radcom.ldir.ldirbackendwebjsf2.tools;

/**
 *
 * @author dan.grigore
 */
public class CustomFn {
    public static java.lang.String parseDoubleToString(java.lang.Double value){
        String result = value != null ? String.valueOf(value) : "";
        return result;
    }

}
