package de.bo.base.swing.text;

import java.awt.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import de.bo.base.memento.StringMemento;

/**
 * Diese Klasse ist eine Basisklasse f�r alle DocumentDecorator, die die
 * Eingabe in einem Document begrenzen. Dazu mu� die abgeleitete Klasse
 * die Methode <CODE>checkInsert</CODE> �berschreiben, welche die
 * �berpr�fung vornimmt. Die Methode sollte in Abh�ngigkeit von ihren
 * Parametern entweder <CODE>null</CODE> (bei Erfolg) oder einen String
 * mit einer Fehlermeldung zur�ckliefern. Dieser Fehlerstring wird bei
 * gesetztem <CODE>statusMemento</CODE> an selbiges ausgegeben.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public abstract class LimitedDocumentDecorator extends DocumentDecorator {
	protected	long					lastBeep			= 0;		// Zeit des letzten Beeps in ms
	protected	StringMemento	statusMemento = null;	// F�r Fehlermeldungen

	protected long getTime() {
		return System.currentTimeMillis();
	}

  /**
   * Standard-Konstruktor
   **/
  public LimitedDocumentDecorator() {
  	this( null, null );
  }

  /**
   * Konstruktor, der als Parameter ein Document entgegennimmt und dieses
   * direkt f�r die Initialisierung des DocumentDecorator verwendet.
   * @param document Das durch den DocumentDecorator einzuschalende Document
   **/
	public LimitedDocumentDecorator( Document document ) {
		this( document, null );
	}

  /**
   * Konstruktor, der als Parameter ein Document entgegennimmt und dieses
   * direkt f�r die Initialisierung des DocumentDecorator verwendet.
   * @param document Das durch den DocumentDecorator einzuschalende Document
   * @param id Die neue Id dieses Decorators
   **/
	public LimitedDocumentDecorator( Document document, Object id ) {
		super( document, id );
	}

  /**
   * Diese Methode wird bei einer Einf�gung zur �berpr�fung der G�ltigkeit
   * der Einf�gung aufgerufen werden und von der abgeleiteten Klasse
   * �berschrieben werden.
   * @param offs Der Startoffset, ab dem in dem Document die Einf�gung
   *		beginnt.
   * @param str Der einzuf�gende String
   * @param a Die Attribute f�r den einzuf�genden String
   * @return <CODE>null</CODE> wenn die Einf�gung g�ltig ist, ansonsten ein
   *		<CODE>String</CODE> mit einer Fehlerbeschreibung
   **/
	abstract public String checkInsert( int offs, String str, AttributeSet a )
	throws BadLocationException;

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
			if( getTime() - lastBeep < 1000 ) {
				if( statusMemento != null ) {
					statusMemento.setStringValue( err );
				} else {
        	// Drei Spaces hinter 'Achtung' f�r den Popper, um den Dialog immer
          // nach vorne zu bringen.
					JOptionPane.showMessageDialog( null, err, "Achtung   ",
						JOptionPane.WARNING_MESSAGE );
				}
			}
			lastBeep = getTime();
		} else {
			super.insertString( offs, str, a );
		}
	}

  /**
   * Diese Methode setzt das ein <CODE>StringMemento</CODE>, durch das
   * Fehlermeldungen ausgegeben werden sollen. Ist es nicht gesetzt, so
   * werden Fehlermeldungen in einem eigenen Fenster ausgegeben.
   * @param statusMemento Das <CODE>StringMemento</CODE>, das bei
   *		Fehlermeldungen aufgerufen wird.
   **/
	public	void	setStatusMemento( StringMemento statusMemento ) {
		this.statusMemento = statusMemento;
	}

  /**
   * Mit dieser Methode kann bei geschachtelten
   * <CODE>DocumentDecorator</CODE>-Ketten das StatusMemento von allen
   * <CODE>LimitedDocumentDecorator</CODE>-Objekten gesetzt werden.
   * @param d Das oberste Document der <CODE>DocumentDecorator</CODE>-Kette
   * @param statusMemento Das StringMemento, in dem Fehlermeldungen ausgegeben
   *		werden sollen und das f�r jeden LimitedDocumentDecorator in der Kette
   *		ausgehend von <IT>d</IT> gesetzt werden soll.
   **/
	public	static	void setStatusMemento( Document d, StringMemento statusMemento ) {
		while( d instanceof DocumentDecorator ) {
			if( d instanceof LimitedDocumentDecorator ) {
				((LimitedDocumentDecorator) d).setStatusMemento( statusMemento );
			}
			d = ((DocumentDecorator) d).getEnclosedDocument();
		}
	}
}
