/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects;

import java.util.Comparator;
import ro.ldir.dto.Garbage;

/**
 *
 * @author dan.grigore
 */
public class GarbageComparator implements Comparator<Garbage> {
    public int compare(Garbage o1, Garbage o2) {
        return o1.getGarbageId().compareTo(o2.getGarbageId());
    }
}


