package de.bo.base.swing.text;

import java.text.*;
import javax.swing.text.*;

/**
 * Diese Klasse implementiert einen DocumentDecorator, der nur die Eingabe
 * von Fließkommazahlen gestattet.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class RealLimitedDocumentDecorator extends LimitedDocumentDecorator {
	protected	static final DecimalFormatSymbols	dfs = new DecimalFormatSymbols();
	protected	static final char	sep		= dfs.getDecimalSeparator();	// Dezimalkomma
	protected	static final char	ksep	= dfs.getGroupingSeparator();	// Tausenderpunkt

  /**
   * Standard-Konstruktor
   **/
	public RealLimitedDocumentDecorator() {
		this( null );
	}

  /**
   * Konstruktor, der als Parameter das ainzuschalende Document bekommt.
   * @param document Das einzuschalende Document
   **/
	public RealLimitedDocumentDecorator( Document document ) {
		super( document );
	}

  /**
   * Diese Methode wird bei einer Einfügung aufgerufen und überprüft,
   * ob der eingegebene Text eine Flißkommazahl darstellt. Ist dies
   * der Fall, so wird <CODE>null</CODE> zurückgeliefert, ansonsten ein
   * String mit der Fehlerbeschreibung.
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

		String	currentText			= getText( 0, getLength() );
		String	beforeText			= currentText.substring( 0, offs );
		String	afterText				= currentText.substring( offs );
		String	proposedResult	= beforeText + str + afterText;
		char[]	source					= proposedResult.toCharArray();

		boolean	separatorFound	= false;
		for( int i = 0; i < source.length; i++ ) {
			if( source[ i ] == ksep ) {
				// Tausenderzeichen ignorieren
			} else if( source[ i ] == sep ) {
				if( separatorFound ) {
					return "Nur ein Dezimaltrennzeichen erlaubt.";
				} else {
					separatorFound = true;
				}
			} else if( !Character.isDigit( source[ i ] ) ) {
				return "Dieses Feld darf nur Zahlen der Form '123' oder '12" +
					sep + "345' enthalten.";
			}
		}
		return null;
	}
}
