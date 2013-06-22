package de.baltic_online.base.swing.text;

import javax.swing.text.*;

/**
 * DocumentDecorator, der als Basisklassse für Ausnahmen in der Decoratorkette
 * verwendet werden kann.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class AllowLimitedDocumentDecorator extends LimitedDocumentDecorator {
	protected	LimitedDocumentDecorator	d1;

	public AllowLimitedDocumentDecorator( Document d, LimitedDocumentDecorator d1 ) {
		super( d );
		this.d1 = d1;
	}

	public String checkInsert( int offs, String str, AttributeSet a )
	throws BadLocationException {
		return d1.checkInsert( offs, str, a );
	}
}
