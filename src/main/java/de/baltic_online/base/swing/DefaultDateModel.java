/*
 *
 */

package de.baltic_online.base.swing;

import java.util.*;


/**
 *
 */
public class DefaultDateModel extends AbstractDateModel {

    private static final long serialVersionUID = 1L;
    Calendar date;


    /**
     *
     */
    public DefaultDateModel() {
	this( Calendar.getInstance() );
    }

    /**
     *
     */
    public DefaultDateModel( Calendar date ) {
	this.date = date;
    }


    /**
     *
     */
    public void setDate( Calendar date ) {
	if( ! date.equals( this.date ) ) {
	    Calendar oldDate = this.date;
	    this.date = date;

	    fireDateChanged( oldDate );
	}
    }


    /**
     *
     */
    public Calendar getDate() {
	return date;
    }


    /**
     *
     */
    public void setYear( int year ) {
	if( year != date.get( Calendar.YEAR ) ) {
	    Calendar newDate = (Calendar) date.clone();
	    Calendar oldDate = date;

	    newDate.set( Calendar.YEAR, year );
	    date = newDate;

	    fireDateChanged( oldDate );
	}
    }


    /**
     *
     */
    public void setMonth( int month ) {
	if( month != date.get( Calendar.MONTH ) ) {
	    Calendar newDate = (Calendar) date.clone();
	    Calendar oldDate = date;

	    newDate.set( Calendar.MONTH, month );
	    date = newDate;

	    fireDateChanged( oldDate );
	}
    }


    /**
     *
     */
    public void incrementMonth( int delta ) {
	if( delta != 0 ) {
	    Calendar newDate = (Calendar) date.clone();
	    Calendar oldDate = date;

	   newDate.add( Calendar.MONTH, delta );
	    date = newDate;
	    
	    fireDateChanged( oldDate );
	}
    }


    /**
     *
     */
    public void incrementYear( int delta ) {
	if( delta != 0 ) {
	    Calendar newDate = (Calendar) date.clone();
	    Calendar oldDate = date;

	    newDate.add( Calendar.YEAR, delta );
	    date = newDate;
	    
	    fireDateChanged( oldDate );
	}
    }
}
