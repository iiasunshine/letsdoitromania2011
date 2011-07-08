/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.radcom.ldir.ldirbackendwebjsf2.tools;

import ro.ldir.dto.Garbage;

/**
 *
 * @author dan.grigore
 */
public class MyGarbage {
    private Garbage garbage;
    private String infoHtml = "";

    public MyGarbage(Garbage garbage){
        this.garbage = garbage;
    }

    public MyGarbage(Garbage garbage, String infoHtml){
        this.garbage = garbage;
        this.infoHtml = infoHtml;
    }

    public String getIdToString(){
        return getGarbage().getGarbageId() != null ? Integer.toString(getGarbage().getGarbageId()) : "";
    }

    public String getCoordXToString(){
        return Double.toString(getGarbage().getX());
    }
    public String getCoordYToString(){
        return Double.toString(getGarbage().getY());
    }

    /**
     * @return the garbage
     */
    public Garbage getGarbage() {
        return garbage;
    }

    /**
     * @param garbage the garbage to set
     */
    public void setGarbage(Garbage garbage) {
        this.garbage = garbage;
    }

    /**
     * @return the infoHtml
     */
    public String getInfoHtml() {
        return infoHtml;
    }

    /**
     * @param infoHtml the infoHtml to set
     */
    public void setInfoHtml(String infoHtml) {
        this.infoHtml = infoHtml;
    }
}
