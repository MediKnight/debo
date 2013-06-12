/*
 *
 */

package de.bo.base.swing.event;

import java.util.*;


/**
 *
 */
public class DateEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    Calendar oldValue;


    /**
     *
     */
    public DateEvent( Object source, Calendar oldValue ) {
	super( source );

	this.oldValue = oldValue;
    }


    /**
     *
     */
    public Calendar getOldValue() {
	return oldValue;
    }
}
