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
public class DateDocument extends PlainDocument {

    public static final String CLASSNAME = "DateDocument";


    /**
     *
     */
    boolean completeDate;

    /**
     *
     */
    protected DateFormatChecker checker;

    /**
     *
     */
    protected ListModel listModel;

    /**
     *
     */
    protected boolean completeFromList;

    /**
     *
     */
    protected boolean validateInput;

    /**
     *
     */
    protected JTextComponent textComponent;


    /**
     *
     */
    public DateDocument() {
	super();

	init();
    }


    /**
     *
     */
    void init() {
	checker = new DateFormatChecker();
	completeFromList = true;
	completeDate = false;
	validateInput = false;
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
    public void setListCompletionEnabled( boolean state ) {
	completeFromList = state;
    }

    /**
     *
     */
    public boolean isListCompletionEnabled() {
	return completeFromList;
    }


    /**
     *
     */
    public void setDateCompletionEnabled( boolean state ) {
	completeDate = state;
    }

    /**
     *
     */
    public boolean isDateCompletionEnabled() {
	return completeDate;
    }


    /**
     *
     */
    public void setDateValidationEnabled( boolean state ) {
	validateInput = state;
    }

    /**
     *
     */
    public boolean isDateValidationEnabled() {
	return validateInput;
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
    protected String getListCompletion( String s ) {
	String element = null;

	for( int i = 0; i < listModel.getSize(); i++ ) {
	    element = listModel.getElementAt( i ).toString();

	    System.out.println( CLASSNAME + ".getListCompletion: Checking against '" + element + "'." );

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
	System.out.println( CLASSNAME + ".getCompletion: listModel = " + listModel );
	System.out.println( CLASSNAME + ".getCompletion: completeFromList = " + completeFromList );

	if( s.length() == 0 ) {
	    return null;
	}

	//
	// Vervollständigungen aus der Liste nur erlauben, wenn die Eingabe am Ende
	// erfolgt.
	//
	if( listModel != null && completeFromList && offset == s.length() ) {
	    String completion = getListCompletion( s );

	    if( completion != null ) {
		//
		// Komplettierung aus der History moeglich - fertig.
		//

		return completion;
	    }
	}

	if( completeDate ) {
	    //
	    // Versuch der Datumskomplettierung, d.h. Monatsnamen oder Jahreszahl
	    // komplettieren.
	    //
	    return checker.completeDate( offset, s );
	} else {
	    return null;
	}
    }


    /**
     *
     */
    protected void errorNotification( String s ) {
	Toolkit.getDefaultToolkit().beep();
    }


    /**
     *
     */
    public void insertString( int offset, String s, AttributeSet attributeSet ) 
	throws BadLocationException {
	System.out.println( CLASSNAME + ".insertString: Entered." );

	String currentContent = getText( 0, getLength() );
	StringBuffer contentToCheck = new StringBuffer( currentContent );
	contentToCheck.insert( offset, s );

	if( validateInput && ! checker.isValid( contentToCheck.toString() ) ) {
	    //
	    // Das Datum, dass sich durch die Eingabe ergibt, ist nicht gueltig.
	    // Dem Benutzer einen Hinweis darauf geben und unveraendert zurueck-
	    // kehren.
	    //

	    errorNotification( contentToCheck.toString() );
	    return;
	}

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


    /**
     *
     */
    public void remove( int offset, int len ) throws BadLocationException {
	if( len == 0 || getLength() == 0 ) {
	    super.remove( offset, len );
	    return;
	}

	StringBuffer contentToCheck = new StringBuffer( getText( 0, getLength() ) );
	contentToCheck.delete( offset, offset + len );

	if( validateInput && ! checker.isValid( contentToCheck.toString() ) ) {
	    //
	    // Das Datum, dass sich durch die Eingabe ergibt, ist nicht gueltig.
	    // Dem Benutzer einen Hinweis darauf geben und unveraendert zurueck-
	    // kehren.
	    //

	    errorNotification( contentToCheck.toString() );
	    return;
	} else {
	    super.remove( offset, len );
	}
    }
}
