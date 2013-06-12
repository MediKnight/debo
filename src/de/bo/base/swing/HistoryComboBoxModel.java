/*
 *
 */

package de.bo.base.swing;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;


/**
 * Ein ComboBoxModel, das eine History-Liste implementiert. Insbesondere
 * k&ouml;nnen Einf&uuml;gungen in die Liste unterbunden werden und
 * Mehrfachvorkommnisse in der Liste verhindert werden.
 *
 *
 * @version $Id$
 * @author Jan Bernhardt
 */
public class HistoryComboBoxModel<E> extends DefaultComboBoxModel<E> implements HistoryListModel<E> {

    private static final long serialVersionUID = 1L;

    public static final String CLASSNAME = "HistoryComboBoxModel";

    /**x
     * Legt fest, ob Dupletten in der History erlaubt sind.
     */
    protected boolean allowDuplicates;

    /**
     * Legt fest, ob die History eingefroren ist. Eingefroren bedeutet, dass
     * <code>addElement</code>-Aufrufe ignoriert werden.
     */
    protected boolean historyFrozen;


    /**
     * Erzeugt eine leere History-Liste.
     */
    public HistoryComboBoxModel() {
	super();
	init();
    }


    /**
     * Erzeugt eine mit Objekten aus einem Array initialisierte History.
     */
    public HistoryComboBoxModel( final E items[] ) {
	super( items );
	init();
    }


    /**
     * Erzeugt eine mit Objekten aus einem Vector initialisierte History.
     */
    public HistoryComboBoxModel( Vector<E> v ) {
	super( v );
	init();
    }


    //
    //
    //
    protected void init() {
	allowDuplicates = false;
	historyFrozen = false;
    }


    /**
     * Friert die History ein. Es k&ouml;nnen keine Elemente mehr in die
     * History aufgenommen werden.
     */
    public void setHistoryFrozen( boolean state ) {
	historyFrozen = state;
    }

    /**
     * Gibt an, ob die History eingefroren ist.
     */
    public boolean isHistoryFrozen() {
	return historyFrozen;
    }

    /**
     * Legt fest, ob Dupletten erlaubt sind.
     *
     * @param state	Wenn <code>true</code> sind Dupletten erlaubt, andernfalls nicht.
     */
    public void setAllowDuplicatesEnabled( boolean state ) {
	allowDuplicates = state;
    }

    /**
     * Gibt an, ob Dupletten erlaubt sind.
     */
    public boolean isAllowDuplicatesEnabled() {
	return allowDuplicates;
    }
  

    /**
     * F&uuml;gt ein Element zu der History hinzu. Falls keine Dupletten erlaubt sind, kann es
     * sein, dass das Element nicht nochmal hinzugef&uuml;gt wird, sofern das Element bereits
     * in der History enthalten ist. Um dieses zu &uuml;berpr&uuml;fen, wird die
     * <code>equals()</code>-Methode verwendet.
     * 
     * @param item	Element, das in die History aufgenommen werden soll
     */
    public void addElement( E item ) {
	if( historyFrozen || item == null ) {
	    return;
	}

	if( ! allowDuplicates ) {
	    for( int i = 0; i < getSize(); i++ ) {
		if( item.equals( getElementAt( i ) ) ) {
		    //
		    // Das Element ist ein Duplikat, es wird nicht
		    // in die History aufgenommen.
		    //

		    return;
		}
	    }
	}

	super.addElement( item );
    }
}


