/*
 *
 */

package de.bo.base.swing.event;

import java.util.*;


/**
 *
 */
public class DateSelectedEvent extends EventObject {

    Calendar selectedDate;


    /**
     *
     */
    public DateSelectedEvent( Object source, Calendar selectedDate ) {
	super( source );

	this.selectedDate = selectedDate;
    }


    /**
     *
     */
    public Calendar getDate() {
	return selectedDate;
    }
}
