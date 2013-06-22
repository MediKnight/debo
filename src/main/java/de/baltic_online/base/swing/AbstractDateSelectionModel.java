/*
 *
 */

package de.baltic_online.base.swing;

import java.util.*;
import java.io.*;

import javax.swing.event.*;

import de.baltic_online.base.swing.event.*;


/**
 *
 */
public abstract class AbstractDateSelectionModel implements DateSelectionModel, Serializable {

    private static final long serialVersionUID = 1L;

    protected EventListenerList listenerList = new EventListenerList();


    /**
     *
     */
    public void addDateSelectionListener( DateSelectionListener listener ) {
	listenerList.add( DateSelectionListener.class, listener );
    }


    /**
     *
     */
    public void removeDateSelectionListener( DateSelectionListener listener ) {
	listenerList.remove( DateSelectionListener.class, listener );
    }


    /**
     *
     */
    public void fireDateSelectionChanged( Calendar oldValue ) {
	Object[] listeners = listenerList.getListenerList();
	DateSelectionEvent event = null;

	for( int i = listeners.length - 2; i >=0; i -= 2 ) {
	    if( listeners[i] == DateSelectionListener.class ) {
		if( event == null ) {
		    event = new DateSelectionEvent( this, oldValue );
		}

		( (DateSelectionListener) listeners[i+1]).dateSelectionChanged( event );
	    }
	}

    }

}
