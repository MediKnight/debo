package de.bo.base.swing.text;

import javax.swing.text.*;

/**
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/
public class AllowStringDocumentDecorator extends AllowLimitedDocumentDecorator {
	protected	String allowed;

	public AllowStringDocumentDecorator( LimitedDocumentDecorator d1,
			String allowed ) {
		this( null, d1, allowed );
	}

	public AllowStringDocumentDecorator( Document d, LimitedDocumentDecorator d1,
			String allowed ) {
		super( d, d1 );
		this.allowed = allowed;
	}

	public String checkInsert( int offs, String str, AttributeSet a )
	throws BadLocationException {
		if( str == null ) {
			return null;
		}

		String currentText		= getText( 0, getLength() );
		String beforeOffset		= currentText.substring( 0, offs );
		String afterOffset		= currentText.substring( offs, currentText.length() );
		String proposedResult	= beforeOffset + str + afterOffset;
		if( proposedResult.equals( allowed ) ) {
			return null;
		} else {
			return super.checkInsert( offs, str, a );
		}
	}
}
