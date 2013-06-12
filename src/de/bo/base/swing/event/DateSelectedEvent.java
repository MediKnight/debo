/*
 *
 */

package de.bo.base.swing.event;

import java.util.*;


/**
 *
 */
public class DateSelectedEvent extends EventObject {

    private static final long serialVersionUID = 1L;

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
