package de.bo.base.swing.text;

import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Diese Klasse stellt ein <CODE>Document</CODE> dar, was an eine
 * editierbare <CODE>DocumentComboBox</CODE> gehängt werden kann und dann alle
 * Eingaben in dem Textfeld der ComboBox entsprechend den
 * Listeneinträgen in der <CODE>DocumentComboBox</CODE> vervollständigt.
 *
 * Anwendung:
 * <PRE>
 * DocumentComboBox cb = new DocumentComboBox();
 * DocumentDecorator.updateDocumentDecorator( cb.getTextField(), "eineId",
 *   new DocumentComboBoxCompleteDecorator( cb ) );
 * </PRE>
 * oder (falls keine weiteren DocumentDecorator verwendet werden sollen):
 * <PRE>
 * cb.setDocument( new DocumentComboBoxCompleteDecorator( cb.getDocument(), cb );
 * </PRE>
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 */

public class ListAutoCompleteDecorator extends AutoCompleteDocumentDecorator {

    public static final String CLASSNAME = "ListAutoCompleteDecorator";

    protected ListModel model;

    /**
     * Dieser Konstruktor bekommt als Parameter eine DocumenetComboBox, deren
     * Text vervollständigt werden soll.
     * @param documentComboBox Die DocumentComboBox, deren Text vervollständigt
     *  werden soll.
     */
    public ListAutoCompleteDecorator( ListModel model, JTextComponent textComponent ) {
	super( textComponent );

	this.model = model;
    }


    /**
     * Diese Methode liefert eine <CODE>Enumeration</CODE> mit den möglichen
     * Vervollständigungen. Da die Vervollständigungen für alle Prefixe gleich
     * sind, werden die Parameter hier ignoriert.
     * @param beforeText Der Text vor der einzufügenden Stelle.
     * @param str Der einzufügende Text.
     * @return Eine <CODE>Enumeration</CODE> von <CODE>String</CODE> die
     *  die möglichen Vervollständigungen angibt.
     */
    public String getCompletion( String s ) {
	String element = null;
	String elementWithCase = null;

	if( isCaseInsensitive() ) {
	    s = s.toLowerCase();
	}

	for( int i = 0; i < model.getSize(); i++ ) {
	    elementWithCase = model.getElementAt( i ).toString();

	    if( isCaseInsensitive() ) {
		element = elementWithCase.toLowerCase();
	    } else {
		element = elementWithCase;
	    }

	    if( element.startsWith( s ) ) {
		if( element.length() > s.length() ) {
		    return elementWithCase.substring( s.length() );
		}
	    }
	}
	
	return null;
    }
}
