/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects;

import java.util.Comparator;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.MyGarbage;

/**
 *
 * @author dan.grigore
 */
public class MyGarbageComparator implements Comparator<MyGarbage> {
    public int compare(MyGarbage o1, MyGarbage o2) {
        return -o1.getGarbage().getGarbageId().compareTo(o2.getGarbage().getGarbageId());
    }
}


