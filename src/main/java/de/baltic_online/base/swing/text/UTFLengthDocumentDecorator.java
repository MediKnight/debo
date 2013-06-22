package de.baltic_online.base.swing.text;

import javax.swing.text.*;
import de.baltic_online.base.swing.text.UTFUtil;

/**
 * Diese Klasse stellt einen LimitedDocumentDecorator dar, der eine
 * Beschränkung der UTF-kodierten Länge eines Documents erlaubt.
 * Dies ist Sinnvoll z.B. bei der Speicherung des Inhaltes von Eingabefeldern
 * als UTF-kodierte Zeichenketten in Datenbankfeldern.
 * @see DocumentDecorator
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class UTFLengthDocumentDecorator extends LimitedDocumentDecorator {
	// Kodierte Länge, auf die das Document beschränkt werden soll
	protected	int	length;

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, über dem die
   * Längenbeschränkung liegen soll und die Länge, auf die das Document
   * beschränkt werden soll.
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   **/
	public UTFLengthDocumentDecorator( int length ) {
  	this( null, length, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, über dem die
   * Längenbeschränkung liegen soll und die Länge, auf die das Document
   * beschränkt werden soll.
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public UTFLengthDocumentDecorator( int length, Object id ) {
  	this( null, length, id );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, über dem die
   * Längenbeschränkung liegen soll und die Länge, auf die das Document
   * beschränkt werden soll.
   * @param d Das Document, über dem die Längenbeschränkung liegen soll
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   **/
	public UTFLengthDocumentDecorator( Document d, int length ) {
  	this( d, length, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, über dem die
   * Längenbeschränkung liegen soll und die Länge, auf die das Document
   * beschränkt werden soll.
   * @param d Das Document, über dem die Längenbeschränkung liegen soll
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public UTFLengthDocumentDecorator( Document d, int length, Object id ) {
		super( d, id );
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
		if( str == null ||
				UTFUtil.UTFLength( getText( 0, getLength() ) + str ) <= length ) {
			return null;
		} else {
			return "Dieses Feld darf kodiert maximal " + Integer.toString( length )
				+ " Byte enthalten.";
		}
	}
}
