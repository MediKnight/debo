/*
 *
 */

package de.bo.base.swing;

import java.util.*;

import de.bo.base.swing.event.*;


/**
 *
 */
public interface DateSelectionModel {

    /**
     *
     */
    Calendar getSelection();

    /**
     *
     */
    void setSelection( Calendar date );

    
    /**
     *
     */
    void addDateSelectionListener( DateSelectionListener listener );

    /**
     *
     */
    void removeDateSelectionListener( DateSelectionListener listener );
}
