/*
 *
 */

package de.bo.base.util;

import java.util.*;
import java.text.*;


/**
 * Repr&auml;sentierung eines absoluten Datums.
 *
 *
 * @author Jan Bernhardt
 * @version $Id$
 */
public class AbsoluteDate extends AbstractDate {

    public static final String CLASSNAME = "AbsoluteDate";

    /**
     * Das zur Wandelung in einen String verwendete Datumsformat.
     */
    protected DateFormat dateFormat;

    /**
     *
     */
    protected String description;

    /**
     *
     */
    protected Date date;


    /**
     * Erzeugt ein unbestimmtes Datum ohne Formatierungsvorschrift.
     */
    public AbsoluteDate() {
	this( null, null );
    }

    /**
     * Erzeugt ein absolutes Datum ohne Formatierungsvorschrift.
     *
     * @param date	Das absolute Datum
     */
    public AbsoluteDate( Date date ) {
	this( date, null );
    }

    /**
     * Erzeugt ein absolutes Datum mit Formatierungsvorschrift.
     *
     * @param date 	Das absolute Datum
     * @param format	Das zu verwendende Datumsformat
     */
    public AbsoluteDate( Date date, DateFormat format ) {
	this.date = date;
	dateFormat = format;
    }


    /**
     * Erzeugt ein absolutes Datum f&uml;r den heutigen Tag.
     */
    public static AbsoluteDate today() {
	Calendar now = Calendar.getInstance();

	now.clear( Calendar.HOUR_OF_DAY );
	now.clear( Calendar.HOUR );
	now.clear( Calendar.MINUTE );
	now.clear( Calendar.SECOND );
	now.clear( Calendar.MILLISECOND );

	return new AbsoluteDate( now.getTime() );
    }
    

    /**
     * Gibt an <code>Calendar</code>-Objekt mit dem absoluten Datum
     * zur&uuml;ck.
     */
    public Calendar getCalendar() {
	if( date != null ) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime( date );

	    return calendar;
	} else {
	    return null;
	}
    }


    /**
     * Setzt das Datum fest.
     *
     * @param date	Das absolute Datum
     */
    public void setDate( Date date ) {
	this.date = date;
    }


    /**
     * Liefert das absolute Datum.
     */
    public Date getDate() {
	return date;
    }


    /**
     * Setzt das Format zur Umwandlung in einen String fest.
     *
     * @param format	Das zuverwendende Format
     */
    public void setDateFormat( DateFormat format ) {
	dateFormat = format;
    }

    /**
     * Liefert das zur Wandlung in einen String verwendete Format.
     */
    public DateFormat getDateFormat() {
	return dateFormat;
    }


    //
    //
    //
    public boolean equals( Object obj ) {
	//
	// REMARK: Hier sollte besser auf Gleichheit von Tag, Monat und Jahr geprueft werden.
	//

	if( obj == null || ! (obj instanceof UniversalDate ) ) {
	    return false;
	}

	UniversalDate aDate = (UniversalDate) obj;

	if( aDate.getDate() == date ) {
	    return true;
	} else if( aDate.getDate() != null &&  aDate.getDate().equals( date ) ) {
	    return true;
	} else {
	    return false;
	}
    }


    //
    //
    //
    public String toString() {
	if( getDescription() != null ) {
	    return getDescription();
	} else {
	    if( date != null ) {
		if( dateFormat != null ) {
		    return dateFormat.format( date );
		} else {
		    return date.toString();
		}
	    } else {
		return "";
	    }
	}
    }
}
