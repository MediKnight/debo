/*
 *
 */

package de.bo.base.swing;

import java.io.Serializable;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;


/**
 *
 */
public abstract class AbstractBillAmountModel implements BillAmountModel, Serializable {

    protected EventListenerList listenerList = new EventListenerList();


    /**
     *
     */
    public void addChangeListener( ChangeListener listener ) {
	listenerList.add( ChangeListener.class, listener );
    }


    /**
     *
     */
    public void removeChangeListener( ChangeListener listener ) {
	listenerList.remove( ChangeListener.class, listener );
    }


    /**
     *
     */
    protected void fireStateChanged() {
	Object[] listeners = listenerList.getListenerList();
	ChangeEvent event = null;

	for( int i = listeners.length - 2; i >=0; i -= 2 ) {
	    if( listeners[i] == ChangeListener.class ) {
		if( event == null ) {
		    event = new ChangeEvent( this );
		}

		( (ChangeListener) listeners[i+1]).stateChanged( event );
	    }
	}
    }
}
