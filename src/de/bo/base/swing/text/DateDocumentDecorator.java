package de.bo.base.swing.text;

import java.awt.Toolkit;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class DateDocumentDecorator extends DocumentDecorator
{
  private Date date;

  public DateDocumentDecorator()
  {
     this(null, null, null);
  }

  /**
   * Dieser Konstruktor nimmt als Parameter das Datum, auf die das Document
   * blah werden soll.
   * @param date Die Länge, auf die das Document eingeschränkt werden soll.
   **/
	public DateDocumentDecorator( Date d ) {
  	this( null, d, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter die Länge, auf die das Document
   * beschränkt werden soll.
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public DateDocumentDecorator( Date d, Object id ) {
  	this( null, d, id );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, über dem die
   * Längenbeschränkung liegen soll und die Länge, auf die das Document
   * beschränkt werden soll.
   * @param d Das Document, über dem die Längenbeschränkung liegen soll
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   **/
	public DateDocumentDecorator( Document document, Date d) {
  	this( document, d, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, über dem die
   * Längenbeschränkung liegen soll und die Länge, auf die das Document
   * beschränkt werden soll.
   * @param d Das Document, über dem die Längenbeschränkung liegen soll
   * @param length Die Länge, auf die das Document eingeschränkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public DateDocumentDecorator( Document document, Date d,
  		Object id ) {
		super( document, id );
		date = d;
    try
    {
      document.insertString( 0, "  .  .", null);
    }
    catch (BadLocationException ex)
    {
      ex.printStackTrace();
    }
	}

  /**
   * Diese Methode liefert als Resultat die Länge zurück, auf die dieses
   * Objekt das Document begrenzt.
   * @return Die Länge, auf die dieses Objekt das Document begrenzt.
   **/
	public	Date	getDate() {
		return date;
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
//		if( str == null || getLength() + str.length() <= length ) {
			return null;
//		} else {
//			return "Dieses Feld darf maximal " + length + " Zeichen enthalten.";
//		}
	}

	/**
   * Diese Methode wird bei jeder Einfügung aufgerufen. Die Überprüfung wird
   * an <CODE>checkInsert</CODE> delegiert. Die Fehlermeldung von
   * <CODE>checkInsert</CODE> wird nur dann an das StatusMemento weitergeleitet,
   * wenn der letzte Fehler vor weniger als einer Sekunde stattgefunden hat.
   * @param offs Der Startoffset, ab dem in dem Document die Einfügung
   *		beginnt.
   * @param str Der einzufügende String
   * @param a Die Attribute für den einzufügenden String
	 **/
	public void insertString( int offs, String str, AttributeSet a )
	throws BadLocationException {
		if( str == null ) return;

		String err = checkInsert( offs, str, a );
		if( err != null ) {
			Toolkit.getDefaultToolkit().beep();
     	// Drei Spaces hinter 'Achtung' für den Popper, um den Dialog immer
      // nach vorne zu bringen.
  		JOptionPane.showMessageDialog( null, err, "Achtung   ",
			JOptionPane.WARNING_MESSAGE );
    }
    else
    {
			super.insertString( offs, str, a );
		}
	}
}
