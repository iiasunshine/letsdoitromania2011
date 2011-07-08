/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects;

import java.util.Comparator;
import ro.ldir.dto.ChartedArea;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.MyGarbage;

/**
 *
 * @author dan.grigore
 */
public class MyAreaComparator implements Comparator<ChartedArea> {
    public int compare(ChartedArea o1, ChartedArea o2) {
        return -o1.getAreaId().compareTo(o2.getAreaId());
    }
}


