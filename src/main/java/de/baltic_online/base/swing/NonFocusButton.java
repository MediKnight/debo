package de.baltic_online.base.swing;

import java.awt.event.*;

import javax.swing.*;

/**
 * Instanzen dieser Button-Klasse erhalten niemals den Fokus.
 *
 * @author S�nke M�ller-Lund
 */

public class NonFocusButton extends JButton
{
  private static final long serialVersionUID = 1L;

  public NonFocusButton() {
    super();
  }
  public NonFocusButton(Icon icon) {
    super( icon );
  }
  public NonFocusButton(String text) {
    super( text );
  }
  public NonFocusButton(String text,Icon icon) {
    super( text, icon );
  }

  /**
   * Diese Funktion ist leer.
   */
  protected void processFocusEvent(FocusEvent e) {
  }

  /**
   * Liefert stets <code>false</code>, damit der Schalter niemals
   * den Fokus erh�lt.
   *
   * @return <code>false</code>
   */
  public boolean isFocusable() {
    return false;
  }
}
