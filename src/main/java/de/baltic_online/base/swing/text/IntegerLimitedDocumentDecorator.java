package de.baltic_online.base.swing.text;

import javax.swing.text.*;

/**
 * Diese Klasse stellt einen DocumentDecorator dar, der die Eingabe auf ganze
 * Zahlen beschränkt.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/
public class IntegerLimitedDocumentDecorator extends LimitedDocumentDecorator {

	/**
   * Standard-Konstruktor
   **/
	public IntegerLimitedDocumentDecorator() {
		this( null, null );
	}

	/**
   * Standard-Konstruktor
   **/
	public IntegerLimitedDocumentDecorator( Object id ) {
  	this( null, id );
	}

  /**
   * Konstruktor, der als Parameter das Document üebergeben bekommt, auf dem
   * die Eingabe eingeschränkt werden soll.
   * @param document Das Document, auf dem die Eingabe eingeschränkt werden
   *		soll.
   **/
	public IntegerLimitedDocumentDecorator( Document document ) {
  	this( document, null );
	}

  /**
   * Konstruktor, der als Parameter das Document üebergeben bekommt, auf dem
   * die Eingabe eingeschränkt werden soll.
   * @param document Das Document, auf dem die Eingabe eingeschränkt werden
   *		soll.
   **/
	public IntegerLimitedDocumentDecorator( Document document, Object id ) {
		super( document, id );
	}

  /**
   * Diese Methode überprüft, ob es sich bei der Eingabe um eine ganze
   * Zahl handelt. Ist dies der Fall, so liefert die Methode
   * <CODE>null</CODE> zurück, ansonsten einen String mit einer Fehlermeldung.
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

		char[] source = str.toCharArray();
		for( int i = 0; i < source.length; i++ ) {
			if( !Character.isDigit( source[ i ] ) ) {
				return "Dieses Feld darf nur ganze Zahlen enthalten.";
			}
		}
		return null;
	}
}
