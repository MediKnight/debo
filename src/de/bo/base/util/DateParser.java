/*
 *
 */

package de.bo.base.util;

import java.util.*;


/**
 * Das Interface <code>DateParser</code> hat die Aufgabe, einen String in ein
 * Datum zu wandeln.
 *
 * @author Jan Bernhardt
 * @version $Id$
 */
public interface DateParser {

    /**
     * Wandelt einen String in ein Datum.
     *
     * @return <code>null</code>, wenn das Format des Datums nicht erkannt
     * 		wurde, ein entsprechendes </code>Date<code>-Objekt sonst
     */
    Date parse( String s );
}
