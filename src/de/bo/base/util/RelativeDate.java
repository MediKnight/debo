/*
 *
 */

package de.bo.base.util;

import java.util.*;
import java.text.*;

/**
 * <code>RelativeDate</code> erm&ouml;glicht es Datumsangaben relativ zu
 * einem Zeitpunkt auszudr&uuml;cken. Diese Angaben lassen sich durch
 * Addition bzw. Substraktion (negative Werte) von Tagen, Monaten oder
 * Jahren vom Zeitpunkt der Auswertung mittels <code>getDate()</code>
 * angeben.<p>
 *
 * Beispiele:
 * <blockquote>
 * <pre>
 * RelativeDate now = new RelativeDate( "Jetzt" );
 * RelativeDate tomorrow = new RelativeDate( "Morgen", RelativeDate.DAY_OF_MONTH, 1 );
 * RelativeDate yesterday = new RelativeDate( "Gestern", RelativeDate.DAY_OF_MONTH, -1 );
 * RelativeDate inAMonth = new RelativeDate( "in einem Monat", RelativeDate.MONTH, 1 );
 * RelativeDate aYearAgo = new RelativeDate( "vor einem Jahr", RelativeDate.YEAR, -1 );
 * </pre>
 * </blockquote>
 *
 * @author Jan Bernhardt
 * @version $Id$
 */
public class RelativeDate extends AbstractDate {

    public static final String CLASSNAME = "RelativeDate";

    public static final int FIELD_COUNT = 3;

    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY_OF_MONTH = 2;

    int fields[];

    int fieldMap[] = { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH };
    String fieldDescription[] = { "Jahr", "Monat", "Tag" };


    /**
     * Erzeugt ein relatives Datum mit Beschreibung und Offset in einer
     * Komponente.
     *
     * @param description	Textuelle Beschreibung des Datums
     * @param field		Komponente, zu der ein Offset angegeben werden
     *				soll
     * @param amount		Gr&ouml;&szlig;e des Offsets
     */
    public RelativeDate( String description, int field, int amount ) {
	super();
	init();
	if( field != -1 && amount != 0 ) {
	    add( field, amount );
	}
	setDescription( description );
    }

    /**
     * Erzeugt ein einfaches relatives Datum. Liefert bei <code>getDate()</code>
     * den aktuellen Zeitpunkt im Moment der Auswertung.
     */
    public RelativeDate() {
	this( null, -1, 0 );
    }

    /**
     * Erzeugt ein einfaches relatives Datums mit Beschreibung. Liefert bei
     * <code>getDate()</code> den aktuellen Zeitpunkt im Moment der Auswertung.
     *
     * @param description	Textuelle Beschreibung des Datums
     */
    public RelativeDate( String description ) {
	this( description, -1, 0 );
    }

    /**
     * Erzeugt eine relatives Datum mit einfachem Offset in einer Komponente.
     *
     * @param field	Komponente, zu der ein Offset angegeben werden soll
     * @param amount	Gr&ouml;&szlig;e des Offsets
     */
    public RelativeDate( int field, int amount ) {
	this( null, field, amount );
    }


    //
    //
    //
    protected void init() {
	fields = new int[ FIELD_COUNT ];

	for( int i = 0; i < FIELD_COUNT; i++ ) {
	    fields[i] = 0;
	}
    }


    /**
     * Addiert einen Betrag zu einer Komponente hinzu.
     *
     * @param field	Die gew&uuml;nschte Komponente
     * @param amount	Die zu addierende Gr&ouml;&szlig;e. Negative
     *			Betr&auml;ge entsprechen einer Subtraktion
     */
    public void add( int field, int amount ) {
	fields[ field ] += amount;
    }


    /**
     * Konvertiert das relative Datum in einen absoluten Zeitpunkt.
     *
     * @return <code>Date</code>, das dem relativen Datum in Bezug auf
     * 		den Auswertungszeitpunkt entspricht
     */
    public Date getDate() {
	Calendar calendar = Calendar.getInstance( TimeZone.getDefault(), Locale.getDefault() );
	calendar.set( Calendar.HOUR_OF_DAY, 0 );
	calendar.set( Calendar.MINUTE, 0 );
	calendar.set( Calendar.SECOND, 0 );
	calendar.set( Calendar.MILLISECOND, 0 );

	for( int i = 0; i < FIELD_COUNT; i++ ) {
	    if( fields[i] != 0 ) {
		calendar.add( fieldMap[i], fields[i] );
	    }
	}

	return calendar.getTime();
    }


    //
    //
    //
    public String getDescription() {
	if( description != null ) {
	    return super.getDescription();
	} else {
	    String result = new String();
	    
	    boolean isFirst = true;
	    
	    for( int i = 0; i < FIELD_COUNT; i++ ) {
		if( fields[i] != 0 ) {
		    if( ! isFirst ) {
			result += " ";
		    } else {
			isFirst = false;
		    }
		    
		    if( fields[i] < 0 ) {
			result += "- ";
		    } else {
			result += "+ ";
		    }
		    
		    result += Math.abs( fields[i] ) + " " + fieldDescription[i];
		    if( Math.abs( fields[i] ) > 1 ) {
			result += "e";
		    }
		}
	    }

	    if( isFirst ) {
		return "Heute";
	    }

	    return result;
	}
    }


    //
    //
    //
    private static int getFieldIndex( String s ) {
	if( s.equalsIgnoreCase( "jahr" ) || s.equalsIgnoreCase( "jahre" ) ) {
	    return YEAR;
	} else if( s.equalsIgnoreCase( "monat" ) || s.equalsIgnoreCase( "monate" ) ) {
	    return MONTH;
	} else if( s.equalsIgnoreCase( "tag" ) || s.equalsIgnoreCase( "tage" ) ) {
	    return DAY_OF_MONTH;
	} else {
	    return -1;
	}
    }


    //
    //
    //
    private static RelativeDate evaluateConstants( String format ) {
	RelativeDate date = new RelativeDate();

	if( format.equalsIgnoreCase( "morgen" ) ) {
	    date.add( DAY_OF_MONTH, 1 );
	    return date;
	} else if( format.equalsIgnoreCase( "heute" ) || format.equalsIgnoreCase( "jetzt" ) ) {
	    return date;
	} else if( format.equalsIgnoreCase( "gestern" ) ) {
	    date.add( DAY_OF_MONTH, -1 );
	    return date;
	} else {
	    return null;
	}
    }


    /**
     * Wandelt einen String in ein relatives Datum um.
     *
     * @param s	Der verarbeitende Ausdruck
     */
    public static RelativeDate parse( String s ) throws ParseException {

	RelativeDate date = new RelativeDate();

	StringTokenizer tokenizer = new StringTokenizer( s, " +-", true );
	String token = null;

	final int UNKNOWN = 0;
	final int PLUS = 1;
	final int MINUS = 2;

	final int NORMAL = 1;
	final int GOT_OPERATOR = 2;
	final int GOT_VALUE = 3;

	int state = NORMAL;
	boolean atStart = true;

	int operator = UNKNOWN;

	int value = -1;
	int field = -1;

	while( tokenizer.hasMoreElements() ) {

	    token = (String) tokenizer.nextElement();

	    //
	    // Whitespace ueberspringen.
	    //
	    if( token.equals( " " ) ) {
		continue;
	    }

	    switch( state ) {

	    case NORMAL:
		if( atStart ) {
		    //
		    // Ueberpruefen von Konstanten wie: Jetzt, Heute, Morgen, Gestern, ...
		    //

		    atStart = false;

		    date = evaluateConstants( token );

		    if( date == null ) {
			date = new RelativeDate();
		    } else {
			break;
		    }
		}

		if( token.equals( "+" ) ) {
		    operator = PLUS;
		} else if( token.equals( "-" ) ) {
		    operator = MINUS;
		} else {
		    throw new ParseException( "", -1 );
		}

		state = GOT_OPERATOR;
		break;

	    case GOT_OPERATOR:
		try {
		    value = Integer.parseInt( token );
		} catch( NumberFormatException e ) {
		    throw new ParseException( "Integer value expected", -1 );
		}

		state = GOT_VALUE;
		break;

	    case GOT_VALUE:
		field = getFieldIndex( token );

		if( field == -1 ) {
		    throw new ParseException( "Period identifier expected", -1 );
		} else {
		    if( operator == MINUS ) {
			value = -value;
		    }

		    date.add( field, value );
		}

		state = NORMAL;
		break;
	    }
	}

	if( state != NORMAL || atStart ) {
	    throw new ParseException( "Somethings wrong here", -1 );
	}

	return date;
    }


    /**
     *
     */
    public boolean equals( Object obj ) {
	return obj != null && obj instanceof RelativeDate && equals((RelativeDate) obj );
    }

    
    /**
     *
     */
    public boolean equals( RelativeDate date ) {
	for( int i = 0; i < FIELD_COUNT; i++ ) {
	    if( fields[i] != date.fields[i] ) {
		return false;
	    }
	}

	return true;
    }
}
