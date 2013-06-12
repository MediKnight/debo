/*
 *
 */

package de.bo.base.util;

import java.util.*;
import java.text.*;

/**
 *
 */
public class DateFormatChecker {

    String[] months;

    String separators = "./ ";

    /**
     *
     */
    boolean allowIncompleteDates;

    /**
     *
     */
    boolean numericOnly;


    /**
     *
     */
    public DateFormatChecker() {
	allowIncompleteDates = false;

	months = new DateFormatSymbols().getMonths();
	numericOnly = true;
    }


    /**
     *
     */
    public void setNumericOnly( boolean state ) {
	numericOnly = state;
    }

    /**
     *
     */
    public boolean isNumericOnly() {
	return numericOnly;
    }


    /**
     *
     */
    public boolean isValid( String dateString ) {

	String[] elements = splitDateString( dateString );

	if( elements.length == 0 ) {
	    return true;
	}

	if( elements.length > 3 ) {
	    return false;
	}

	if( elements[0].length() > 2 ) {
	    //
	    // Ungueltiger Tag.
	    //

	    return false;
	} else {
	    try {
		int d = Integer.parseInt( elements[0] );

		if( d < 1 || d > 31 ) {
		    return false;
		}
	    } catch( NumberFormatException e ) {
		return false;
	    }
	}

	if( elements.length == 1 ) {
	    return true;
	}

	try {
	    int m = Integer.parseInt( elements[1] );

	    if( m < 1 || m > 12 ) {
		return false;
	    }
	} catch( NumberFormatException e ) {
	    if( numericOnly ) {
		return false;
	    } else if( getMonthName( elements[1], false ) == null ) {
		return false;
	    }
	}

	if( elements.length == 2 ) {
	    return true;
	}

	try {
	    int y = Integer.parseInt( elements[2] );

	    if( y < 0 ) {
		return false;
	    }
	} catch( NumberFormatException e ) {
	    return false;
	}

	return true;
    }


    /**
     *
     */
    public String getMonthName( String s, boolean unique ) {
	s = s.toLowerCase();

	int index = -1;

	for( int i = 0; i < months.length; i++ ) {
	    if( months[i].toLowerCase().startsWith( s ) ) {
		if( ! unique ) {
		    return months[i];
		} else {
		    if( index == -1 ) {
			index = i;
		    } else {
			return null;
		    }
		}
	    }
	}
	
	if( index != -1 ) {
	    return months[ index ];
	} else {
	    return null;
	}
    }


    /**
     *
     */
    protected String[] splitDateString( String dateString ) {
	StringTokenizer tokenizer = new StringTokenizer( dateString, separators );
	String token = null;
	Vector<String> elements = new Vector<String>();

	while( tokenizer.hasMoreTokens() ) {
	    token = tokenizer.nextToken();

	    elements.addElement( token );
	}

	Object[] result = elements.toArray();
	String[] strings = new String[ result.length ];

	for( int i = 0; i < result.length; i++ ) {
	    strings[i] = (String) result[i];
	}

	return strings;
    }


    /**
     *
     */
    public boolean isSeparator( char c ) {
	return separators.indexOf( c ) != -1;
    }


    /**
     *
     */
    public String completeDate( int offset, String s ) {
	if( offset < s.length() && ! isSeparator( s.charAt( offset ) ) ) {
	    return null;
	}

	String result = null;

	String[] elements = splitDateString( s.substring( 0, offset ) );

	switch( elements.length ) {
	case 2:
	    result = getMonthName( elements[1], true );

	    if( result != null ) {
		result = result.substring( elements[1].length() );
	    }

	    break;

	case 3:
	    break;
	}

	return result;
    }
}
