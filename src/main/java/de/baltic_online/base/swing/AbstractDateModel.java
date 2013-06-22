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
public abstract class AbstractDateModel implements DateModel, Serializable {

    private static final long serialVersionUID = 1L;
  
    protected EventListenerList listenerList = new EventListenerList();


    /**
     *
     */
    public void addDateListener( DateListener listener ) {
	listenerList.add( DateListener.class, listener );
    }


    /**
     *
     */
    public void removeDateListener( DateListener listener ) {
	listenerList.remove( DateListener.class, listener );
    }


    /**
     *
     */
    protected void fireDateChanged( Calendar oldValue ) {
	Object[] listeners = listenerList.getListenerList();
	DateEvent event = null;

	for( int i = listeners.length - 2; i >=0; i -= 2 ) {
	    if( listeners[i] == DateListener.class ) {
		if( event == null ) {
		    event = new DateEvent( this, oldValue );
		}

		( (DateListener) listeners[i+1]).dateChanged( event );
	    }
	}
    }
}
