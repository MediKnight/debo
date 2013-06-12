/*
 *
 */

package de.bo.base.event;

import java.util.*;


/**
 *
 * @author Jan Bernhardt
 * @version $Id$
 */
public class ErrorEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    public static final String CLASSNAME = "ErrorEvent";

    /**
     *
     */
    String errorMessage;


    /**
     *
     */
    public ErrorEvent( Object source, String message ) {
	super( source );

	errorMessage = message;
    }


    /**
     *
     */
    public void setErrorMessage( String message ) {
	errorMessage = message;
    }

    /**
     *
     */
    public String getErrorMessage() {
	return errorMessage;
    }
}
