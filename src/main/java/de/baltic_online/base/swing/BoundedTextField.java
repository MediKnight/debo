package de.baltic_online.base.swing;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * BoundedTextField ist eine von JTextField abgeleitete Klasse zur
 * begrenzten Eingabe von Text, d.h. nur eine vorgegebene Anzahl von Zeichen
 * wird zugelassen.
 *
 * @author  S�nke M�ller-Lund
 */
public class BoundedTextField extends JTextField
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Erzeugt ein leeres Textfeld mit einer Begrenzung von
   * <code>getDefaultBound()</code> Zeichen.
   *
   * @see BoundedTextField#getDefaultBound
   */
  public BoundedTextField() {
    this( null, 0, getDefaultBound() );
  }

  /**
   * Erzeugt ein leeres Textfeld mit der angegebenen Breite, die auch
   * als Begrenzung dient.
   *
   * @param cols Breite und Begrenzung des Feldes in Spalten.
   */
  public BoundedTextField(int cols) {
    this( null, cols, cols );
  }

  /**
   * Erzeugt ein leeres Textfeld mit der angegebenen Breite und
   * Begrenzung.
   *
   * @param cols Breite des Feldes in Spalten.
   * @param bound Begrenzung.
   */
  public BoundedTextField(int cols,int bound) {
    this( null, cols, bound );
  }

  /**
   * Erzeugt ein Textfeld mit einer Begrenzung von
   * <code>getDefaultBound()</code> Zeichen und f�llt es mit dem
   * angegebenen Text auf.
   *
   * @param text Text des Eingabefeldes.
   * @see BoundedTextField#getDefaultBound
   */
  public BoundedTextField(String text) {
    this( text, 0, getDefaultBound() );
  }

  /**
   * Erzeugt ein Textfeld mit der angegebenen Breite, die auch
   * als Begrenzung dient und f�llt es mit dem
   * angegebenen Text auf.
   *
   * @param text Text des Eingabefeldes.
   * @param cols Breite und Begrenzung des Feldes in Spalten.
   * @see BoundedTextField#getDefaultBound
   */
  public BoundedTextField(String text,int cols) {
    this( text, cols, cols );
  }

  /**
   * Erzeugt ein Textfeld mit der angegebenen Breite und
   * Begrenzung und f�llt es mit dem
   * angegebenen Text auf.
   *
   * @param text Text des Eingabefeldes.
   * @param cols Breite und Begrenzung des Feldes in Spalten.
   * @param bound Begrenzung.
   * @see BoundedTextField#getDefaultBound
   */
  public BoundedTextField(String text,int cols,int bound) {
    super( cols );

    setDocument( new BoundedDocument(bound) );

    if ( text != null )
      setText( text );
  }

  /**
   * Liefert die Default-Begrenzung.
   *
   * @return Default-Begrenzung.
   */
  protected static int getDefaultBound() {
    return 10;
  }

  /**
   * Liefert die Mindestgr��e der Komponente.
   *
   * Diese Funktion wurde �berschrieben, um ein Zusammenfallen der Komponente
   * zu verhindern, wenn der erforderliche Platz nicht vorhanden ist.
   *
   * @return Mindestgr��e der Komponente.
   */
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  protected class BoundedDocument extends PlainDocument
  {
    private static final long serialVersionUID = 1L;
    int bound;

    BoundedDocument(int bound) {
      this.bound = bound;
    }

    public void insertString(int offs,String str,AttributeSet a) 
      throws BadLocationException {
      if ( getLength()+str.length() > bound )
	Toolkit.getDefaultToolkit().beep();
      else
	super.insertString( offs, str, a );
    }
  }
}
