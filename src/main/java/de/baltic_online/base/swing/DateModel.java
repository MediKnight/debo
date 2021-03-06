/*
 *
 */

package de.baltic_online.base.swing;

import java.util.*;

import de.baltic_online.base.swing.event.*;


/**
 *
 */
public interface DateModel {

    /**
     *
     */
    void setDate( Calendar date );

    /**
     *
     */
    Calendar getDate();

    /**
     *
     */
    void setYear( int year );

    /**
     *
     */
    void setMonth( int month );

    /**
     *
     */
    void incrementMonth( int delta );

    /**
     *
     */
    void incrementYear( int delta );

    /**
     *
     */
    void addDateListener( DateListener listener );

    /**
     *
     */
    void removeDateListener( DateListener listener );
}
