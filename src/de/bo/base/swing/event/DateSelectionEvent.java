/*
 *
 */

package de.bo.base.swing.event;

import java.util.*;


/**
 *
 */
public class DateSelectionEvent extends EventObject {

    Calendar selectedDate;


    /**
     *
     */
    public DateSelectionEvent( Object source, Calendar selectedDate ) {
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
