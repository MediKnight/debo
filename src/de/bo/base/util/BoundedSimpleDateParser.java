/*
 *
 */

package de.bo.base.util;

import java.util.*;
import java.text.*;


/**
 * <code>BoundedSimpleDateParser</code> ist ein auf <code>SimpleDateParser</code>
 * aufbauender <code>DateParser</code>, der verlangt, da&szlig; die Eingabe
 * eine bestimmte L&auml;nge hat.
 */
public class BoundedSimpleDateParser extends SimpleDateParser {

    public static final String CLASSNAME = "BoundedSimpleDateParser";

    int formatLength;

    /**
     * Erzeugt einen <code>SimpleDateParser</code> mit angegebenem
     * Format und Spezifikation der durch das <code>SimpleDateFormat</code>
     * gesetzten Datumsteile.
     *
     * @param format	 Das f&uuml;r das <code>SimpleDateFormat</code> verwendete
     *			 Format
     * @param segmentSet Flagvariable, die angibt, welche Teile des Datums durch
     *			 das <code>SimpleDateFormat</code> gesetzt werden
     */
    public BoundedSimpleDateParser( String format, int segmentSet ) {
	super( format, segmentSet );
	
	formatLength = format.length();
    }


    /**
     *
     */
    public Date parse( String s ) {
	if( s.trim().length() != formatLength ) {
	    return null;
	} else {
	    return super.parse( s );
	}
    }
}
