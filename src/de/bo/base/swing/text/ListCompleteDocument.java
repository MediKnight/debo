/*
 *
 */

package de.bo.base.swing.text;

import java.util.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.*;

import de.bo.base.util.*;


/**
 *
 */
public class ListCompleteDocument extends PlainDocument {

    public static final String CLASSNAME = "ListCompleteDocument";

    /**
     *
     */
    protected ListModel listModel;

    /**
     *
     */
    protected boolean enabled;

    /**
     *
     */
    protected JTextComponent textComponent;


    /**
     *
     */
    public ListCompleteDocument( JTextComponent textComponent, ListModel model ) {
	super();

	this.textComponent = textComponent;
	this.listModel = model;
	enabled = true;
    }

    /**
     *
     */
    public void setListModel( ListModel model ) {
	listModel = model;
    }

    /**
     *
     */
    public ListModel getListModel() {
	return listModel;
    }


    /**
     *
     */
    public void setEnabled( boolean state ) {
	enabled = state;
    }

    /**
     *
     */
    public boolean isEnabled() {
	return enabled;
    }

    /**
     *
     */
    public void setTextComponent( JTextComponent textComponent ) {
	this.textComponent = textComponent;
    }

    /**
     *
     */
    public JTextComponent getTextComponent() {
	return textComponent;
    }


    /**
     *
     */
    protected String getListCompletion( String s ) {
	String element = null;

	for( int i = 0; i < listModel.getSize(); i++ ) {
	    element = listModel.getElementAt( i ).toString();

	    if( element.startsWith( s ) ) {
		return element.substring( s.length() );
	    }
	}

	return null;
    }


    /**
     *
     */
    protected String getCompletion( int offset, String s ) {
	if( s.length() == 0 ) {
	    return null;
	}

	//
	// Vervollständigungen aus der Liste nur erlauben, wenn die Eingabe am Ende
	// erfolgt.
	//
	if( listModel != null && enabled && offset == s.length() ) {
	    String completion = getListCompletion( s );

	    if( completion != null ) {
		//
		// Komplettierung aus der History moeglich - fertig.
		//

		return completion;
	    }
	}

	return null;
    }


    /**
     *
     */
    public void insertString( int offset, String s, AttributeSet attributeSet ) 
	throws BadLocationException {

	String currentContent = getText( 0, getLength() );
	StringBuffer contentToCheck = new StringBuffer( currentContent );
	contentToCheck.insert( offset, s );

	String completion = getCompletion( offset + s.length(), contentToCheck.toString() );

	if( completion != null ) {
	    s += completion;
	}

	super.insertString( offset, s, attributeSet );

	if( completion != null ) {
	    //
	    // Markierung des komplettierten Anteils.
	    //

	    textComponent.select( offset + s.length() - completion.length(), offset + s.length() );
	}
    }
}
