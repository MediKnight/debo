package de.bo.base.swing.text;

import java.text.DecimalFormatSymbols;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Diese Klasse implementiert einen DocumentDecorator, der nur die Eingabe
 * von Fließkommazahlen gestattet.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class IntegerPointFiveDocumentLimiter extends LimitedDocumentDecorator {
	protected	static final DecimalFormatSymbols	dfs = new DecimalFormatSymbols();
	protected	static final char									sep = dfs.getDecimalSeparator();

  /**
   * Standard-Konstruktor
   **/
	public IntegerPointFiveDocumentLimiter() {
  }

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, über dem die
   * Eingabebeschränkung liegen soll.
   * @param d Das Document, über dem die Eingabebeschränkung liegen soll
   **/
	public IntegerPointFiveDocumentLimiter( Document d ) {
		super( d );
	}

  /**
   * Diese Methode überprüft, ob es sich bei der Eingabe um eine ganze
   * Zahl oder um eine ganze Zahl mit der Nachkommastelle ',5' handelt. Ist dies
   * der Fall, so liefert diese Methode <CODE>null</CODE> zurück, ansonsten
   * einen String mit einer Fehlermeldung.
   * @param offs Der Startoffset, ab dem in dem Document die Einfügung
   *		beginnt.
   * @param str Der einzufügende String
   * @param a Die Attribute für den einzufügenden String
   * @return <CODE>null</CODE> wenn die Einfügung gültig ist, ansonsten ein
   *		<CODE>String</CODE> mit einer Fehlerbeschreibung
   **/
	public String checkInsert( int offs, String str, AttributeSet a )
	throws BadLocationException {
		if( str == null ) return null;

		String currentText		= getText( 0, getLength() );
		String beforeOffset		= currentText.substring( 0, offs );
		String afterOffset		= currentText.substring( offs, currentText.length() );
		String proposedResult = beforeOffset + str + afterOffset;
		char[] source					= proposedResult.toCharArray();

		boolean	separatorFound	= false;
		boolean	postDigitFound	= false;
		for( int i = 0; i < source.length; i++ ) {
			if( source[ i ] ==  sep ) {
				if( separatorFound ) {
					return "Nur ein Dezimaltrennzeichen erlaubt.";
				} else {
					separatorFound = true;
				}
			} else if( Character.isDigit( source[ i ] ) ) {
				if( separatorFound ) {
					if( source[ i ] != '5' || postDigitFound ) {
						return "Es sind nur halbe Zimmer erlaubt (Endung '" + sep + "5')";
					}
					postDigitFound = true;
				}
			} else {
				return "Dieses Feld darf nur Zahlen der Form '123' oder '12" +
					sep + "5' enthalten.";
			}
		}
		return null;
	}
}
