package de.bo.base.swing.text;

import javax.swing.text.*;

/**
 * Diese Klasse stellt einen LimitedDocumentDecorator dar, der eine
 * Beschränkung der Länge eines Documents erlaubt.
 * @see DocumentDecorator
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class LengthLimitedDocumentDecorator extends LimitedDocumentDecorator {
	private	int	length;

  /**
   * Dieser Konstruktor nimmt als Parameter die Länge, auf die das Document
   * beschränkt werden soll.
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   **/
	public LengthLimitedDocumentDecorator( int length ) {
  	this( null, length, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter die Länge, auf die das Document
   * beschränkt werden soll.
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public LengthLimitedDocumentDecorator( int length, Object id ) {
  	this( null, length, id );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, über dem die
   * Längenbeschränkung liegen soll und die Länge, auf die das Document
   * beschränkt werden soll.
   * @param d Das Document, über dem die Längenbeschränkung liegen soll
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   **/
	public LengthLimitedDocumentDecorator( Document document, int length ) {
  	this( document, length, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, über dem die
   * Längenbeschränkung liegen soll und die Länge, auf die das Document
   * beschränkt werden soll.
   * @param d Das Document, über dem die Längenbeschränkung liegen soll
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public LengthLimitedDocumentDecorator( Document document, int length,
  		Object id ) {
		super( document, id );
		this.length = length;
	}

  /**
   * Diese Methode überprüft die Länge des bei der Einfügung entstehenden
   * Documents und liefert eine Fehlermeldung, falls neue Länge den
   * im Konstruktor gesetzten maximalwert übersteigt.
   * @param offs Der Startoffset, ab dem in dem Document die Einfügung
   *		beginnt.
   * @param str Der einzufügende String
   * @param a Die Attribute für den einzufügenden String
   * @return <CODE>null</CODE> wenn die Einfügung gültig ist, ansonsten ein
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
   * Diese Methode liefert als Resultat die Länge zurück, auf die dieses
   * Objekt das Document begrenzt.
   * @return Die Länge, auf die dieses Objekt das Document begrenzt.
   **/
	public	int	getLengthLimit() {
		return length;
	}
}
