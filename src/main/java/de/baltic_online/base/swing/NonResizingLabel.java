package de.baltic_online.base.swing;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * Diese Klasse implementiert ein JLabel, welches bei dem Setzen eines
 * Textes mittels 'setText' keine Geometrie�nderung des Layouts der
 * Root-Komponente verursacht.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class NonResizingLabel extends JLabel {
    private static final long serialVersionUID = 1L;
  
	protected	boolean	setting = false;	// true gdw. im Closure setText aufgerufen worden ist

  /** Konstruktor @see JLabel **/
	public NonResizingLabel( String text, Icon icon, int horizontalAlignment )	{ super( text, icon, horizontalAlignment ); }

  /** Konstruktor @see JLabel **/
	public NonResizingLabel( String text, int horizontalAlignment )							{ super( text, horizontalAlignment ); }

  /** Konstruktor @see JLabel **/
	public NonResizingLabel( String text )																			{ super( text ); }

  /** Konstruktor @see JLabel **/
	public NonResizingLabel( Icon image, int horizontalAlignment )							{ super( image, horizontalAlignment ); }

  /** Konstruktor @see JLabel **/
	public NonResizingLabel( Icon image)																				{ super( image ); }

  /** Konstruktor @see JLabel **/
	public NonResizingLabel()																										{ super(); }

  /**
   * Diese Methode setzt den Text des JLabels auf den �bergebenen Parameter,
   * wobei keine Geometrie�nderung stattfindet.
   * @param s Der Text, auf den das JLabel gesetzt werden soll.
   **/
	public void setText( String s ) {
  	// -- Hinweis zur Implementierung
    // Da in JLabel this.text nur in setText gesetzt wird, this.text aber
    // private in JLabel ist, mu� das revalidate �ber Zunstandsvariable
    // abgefangen werden.
		setting = true;
		super.setText( s );   // Da sind leider zu viele Sachen 'private'
		setting = false;
	}

	public void revalidate() {
  	// Wenn gerade ein setText l�uft, kein revalidate ausf�hren
		if( setting ) {
			return;
		}
		super.revalidate();
	}
}
