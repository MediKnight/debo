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
   * @param date Die L�nge, auf die das Document eingeschr�nkt werden soll.
   **/
	public DateDocumentDecorator( Date d ) {
  	this( null, d, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   * @param id Die neue Id dieses Decorators
   **/
	public DateDocumentDecorator( Date d, Object id ) {
  	this( null, d, id );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, �ber dem die
   * L�ngenbeschr�nkung liegen soll und die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param d Das Document, �ber dem die L�ngenbeschr�nkung liegen soll
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
   **/
	public DateDocumentDecorator( Document document, Date d) {
  	this( document, d, null );
	}

  /**
   * Dieser Konstruktor nimmt als Parameter das Document, �ber dem die
   * L�ngenbeschr�nkung liegen soll und die L�nge, auf die das Document
   * beschr�nkt werden soll.
   * @param d Das Document, �ber dem die L�ngenbeschr�nkung liegen soll
   * @param length Die L�nge, auf die das Document eingeschr�nkt werden soll.
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
   * Diese Methode liefert als Resultat die L�nge zur�ck, auf die dieses
   * Objekt das Document begrenzt.
   * @return Die L�nge, auf die dieses Objekt das Document begrenzt.
   **/
	public	Date	getDate() {
		return date;
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
//		if( str == null || getLength() + str.length() <= length ) {
			return null;
//		} else {
//			return "Dieses Feld darf maximal " + length + " Zeichen enthalten.";
//		}
	}

	/**
   * Diese Methode wird bei jeder Einf�gung aufgerufen. Die �berpr�fung wird
   * an <CODE>checkInsert</CODE> delegiert. Die Fehlermeldung von
   * <CODE>checkInsert</CODE> wird nur dann an das StatusMemento weitergeleitet,
   * wenn der letzte Fehler vor weniger als einer Sekunde stattgefunden hat.
   * @param offs Der Startoffset, ab dem in dem Document die Einf�gung
   *		beginnt.
   * @param str Der einzuf�gende String
   * @param a Die Attribute f�r den einzuf�genden String
	 **/
	public void insertString( int offs, String str, AttributeSet a )
	throws BadLocationException {
		if( str == null ) return;

		String err = checkInsert( offs, str, a );
		if( err != null ) {
			Toolkit.getDefaultToolkit().beep();
     	// Drei Spaces hinter 'Achtung' f�r den Popper, um den Dialog immer
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
