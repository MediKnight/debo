/*
 *
 */

package de.baltic_online.base.swing;

import java.util.*;


/**
 *
 */
public class DefaultDateSelectionModel extends AbstractDateSelectionModel {

    private static final long serialVersionUID = 1L;
    Calendar date;


    /**
     *
     */
    public DefaultDateSelectionModel() {
	this( null );
    }


    /**
     *
     */
    public DefaultDateSelectionModel( Calendar date ) {
	this.date = date;
    }


    /**
     *
     */
    public void setSelection( Calendar date ) {
	this.date = date;

	fireDateSelectionChanged( date );
    }

    /**
     *
     */
    public Calendar getSelection() {
	return date;
    }
}
