package de.bo.base.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*; 

/**
 * BoundedTextField ist eine von JTextField abgeleitete Klasse zur
 * begrenzten Eingabe von Text, d.h. nur eine vorgegebene Anzahl von Zeichen
 * wird zugelassen.
 *
 * @author  Sönke Müller-Lund
 */
public class BoundedTextField extends JTextField
{

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
   * <code>getDefaultBound()</code> Zeichen und füllt es mit dem
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
   * als Begrenzung dient und füllt es mit dem
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
   * Begrenzung und füllt es mit dem
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
   * Liefert die Mindestgröße der Komponente.
   *
   * Diese Funktion wurde überschrieben, um ein Zusammenfallen der Komponente
   * zu verhindern, wenn der erforderliche Platz nicht vorhanden ist.
   *
   * @return Mindestgröße der Komponente.
   */
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  protected class BoundedDocument extends PlainDocument
  {
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
