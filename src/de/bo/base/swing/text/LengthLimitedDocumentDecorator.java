package de.bo.base.swing.text;

import javax.swing.text.*;

/**
 * Diese Klasse stellt einen LimitedDocumentDecorator dar, der eine
 * Beschr�nkung der L�nge eines Documents erlaubt.
 * @see DocumentDecorator
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class LengthLimitedDocumentDecorator extends LimitedDocumentDecorator {
	private	int	length;

  /**
   * Dieser Konstruktor nimmt als Parameter die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   **/
	public LengthLimitedDocumentDecorator( int length ) {
  	this( null, length, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public LengthLimitedDocumentDecorator( int length, Object id ) {
  	this( null, length, id );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, �ber dem die
   * L�ngenbeschr�nkung liegen soll und die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param d Das Document, �ber dem die L�ngenbeschr�nkung liegen soll
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   **/
	public LengthLimitedDocumentDecorator( Document document, int length ) {
  	this( document, length, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, �ber dem die
   * L�ngenbeschr�nkung liegen soll und die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param d Das Document, �ber dem die L�ngenbeschr�nkung liegen soll
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public LengthLimitedDocumentDecorator( Document document, int length,
  		Object id ) {
		super( document, id );
		this.length = length;
	}

  /**
   * Diese Methode �berpr�ft die L�nge des bei der Einf�gung entstehenden
   * Documents und liefert eine Fehlermeldung, falls neue L�nge den
   * im Konstruktor gesetzten maximalwert �bersteigt.
   * @param offs Der Startoffset, ab dem in dem Document die Einf�gung
   *		beginnt.
   * @param str Der einzuf�gende String
   * @param a Die Attribute f�r den einzuf�genden String
   * @return <CODE>null</CODE> wenn die Einf�gung g�ltig ist, ansonsten ein
   *		<CODE>String</CODE> mit einer Fehlerbeschreibung
   **/
	public String checkInsert( int offs, String str, AttributeSet a )
	throws BadLocationException {
		if( str == null || getLength() + str.length() <= length ) {
			return null;
		} else {
			return "Dieses Feld darf maximal " + length + " Zeichen enthalten.";
		}
	}

  /**
   * Diese Methode liefert als Resultat die L�nge zur�ck, auf die dieses
   * Objekt das Document begrenzt.
   * @return Die L�nge, auf die dieses Objekt das Document begrenzt.
   **/
	public	int	getLengthLimit() {
		return length;
	}
}
