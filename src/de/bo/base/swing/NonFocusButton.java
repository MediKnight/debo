package de.bo.base.swing;

import java.awt.event.*;

import javax.swing.*;

/**
 * Instanzen dieser Button-Klasse erhalten niemals den Fokus.
 *
 * @author Sönke Müller-Lund
 */

public class NonFocusButton extends JButton
{
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
   * den Fokus erhält.
   *
   * @return <code>false</code>
   */
  public boolean isFocusable() {
    return false;
  }
}
