/*
 *
 */

package de.bo.base.swing;

import java.util.*;


/**
 *
 */
public class DefaultDateSelectionModel extends AbstractDateSelectionModel {

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
