package de.bo.base.swing.text;

import javax.swing.text.*;
import de.bo.base.swing.text.UTFUtil;

/**
 * Diese Klasse stellt einen LimitedDocumentDecorator dar, der eine
 * Beschr�nkung der UTF-kodierten L�nge eines Documents erlaubt.
 * Dies ist Sinnvoll z.B. bei der Speicherung des Inhaltes von Eingabefeldern
 * als UTF-kodierte Zeichenketten in Datenbankfeldern.
 * @see DocumentDecorator
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class UTFLengthDocumentDecorator extends LimitedDocumentDecorator {
	// Kodierte L�nge, auf die das Document beschr�nkt werden soll
	protected	int	length;

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, �ber dem die
   * L�ngenbeschr�nkung liegen soll und die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   **/
	public UTFLengthDocumentDecorator( int length ) {
  	this( null, length, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, �ber dem die
   * L�ngenbeschr�nkung liegen soll und die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public UTFLengthDocumentDecorator( int length, Object id ) {
  	this( null, length, id );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, �ber dem die
   * L�ngenbeschr�nkung liegen soll und die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param d Das Document, �ber dem die L�ngenbeschr�nkung liegen soll
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   **/
	public UTFLengthDocumentDecorator( Document d, int length ) {
  	this( d, length, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, �ber dem die
   * L�ngenbeschr�nkung liegen soll und die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param d Das Document, �ber dem die L�ngenbeschr�nkung liegen soll
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public UTFLengthDocumentDecorator( Document d, int length, Object id ) {
		super( d, id );
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
		if( str == null ||
				UTFUtil.UTFLength( getText( 0, getLength() ) + str ) <= length ) {
			return null;
		} else {
			return "Dieses Feld darf kodiert maximal " + Integer.toString( length )
				+ " Byte enthalten.";
		}
	}
}
