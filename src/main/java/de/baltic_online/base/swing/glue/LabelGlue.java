package de.baltic_online.base.swing.glue;

import javax.swing.JLabel;
import de.baltic_online.base.memento.Glue;
import de.baltic_online.base.memento.StringMemento;

/**
 * Diese Klasse stellt eine Verbindung zwischen einem JLabel und einem
 * StringMemento her, wobei der Inhalt des StringMementos als Text in das
 * JLabel eingesetzt wird. Ist der Text des StringMementos leer, so wird
 * ein Leerzeichen (' ') als Text gesetzt um die Darstellung des JLabels
 * zu erzwingen.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/
 
public class LabelGlue implements Glue {
	protected JLabel				label;
	protected StringMemento	stringMemento;

  /**
   * Überträgt den Text aus dem StringMemento als Text in das JLabel.
   **/
	public	void	load() {
		String	s = stringMemento.getStringValue();
		label.setText( s == null ? " " : s );
	}

  /**
   * Der Text des JLabels ändert sich i.A. nicht, so daß Änderungen nicht
   * an das StringMemento propagiert werden.
   **/
	public	void	save() {
	}

  /**
   * Konstruktor für ein LabelGlue-Objekt.
   * @param label Das JLabel, von dem der Text umgesetzt werden soll
   * @param stringMemento Die Quelle, aus der der Text für das JLabel
   *    bezogen werden soll
   **/
	public	LabelGlue( JLabel label, StringMemento stringMemento ) {
		this.label					= label;
		this.stringMemento	= stringMemento;
		load();
	}
}


