package de.bo.base.swing.text;

import javax.swing.text.*;

/**
 * Diese Klasse stellt einen DocumentDecorator dar, der die Eingabe auf ganze
 * Zahlen beschr�nkt.
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
   * Konstruktor, der als Parameter das Document �ebergeben bekommt, auf dem
   * die Eingabe eingeschr�nkt werden soll.
   * @param document Das Document, auf dem die Eingabe eingeschr�nkt werden
   *		soll.
   **/
	public IntegerLimitedDocumentDecorator( Document document ) {
  	this( document, null );
	}

  /**
   * Konstruktor, der als Parameter das Document �ebergeben bekommt, auf dem
   * die Eingabe eingeschr�nkt werden soll.
   * @param document Das Document, auf dem die Eingabe eingeschr�nkt werden
   *		soll.
   **/
	public IntegerLimitedDocumentDecorator( Document document, Object id ) {
		super( document, id );
	}

  /**
   * Diese Methode �berpr�ft, ob es sich bei der Eingabe um eine ganze
   * Zahl handelt. Ist dies der Fall, so liefert die Methode
   * <CODE>null</CODE> zur�ck, ansonsten einen String mit einer Fehlermeldung.
   * @param offs Der Startoffset, ab dem in dem Document die Einf�gung
   *		beginnt.
   * @param str Der einzuf�gende String
   * @param a Die Attribute f�r den einzuf�genden String
   * @return <CODE>null</CODE> wenn die Einf�gung g�ltig ist, ansonsten ein
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
