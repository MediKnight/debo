package de.bo.base.swing.glue;

import javax.swing.JComponent;
import de.bo.base.memento.Glue;
import de.bo.base.memento.StringMemento;

/**
 * Diese Klasse stellt eine Verbindung zwischen einem <CODE>StringMemento</CODE>
 * und dem ToolTipText einer <CODE>JComponent</CODE> her.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class	ToolTipGlue implements Glue {
	protected JComponent		component;
	protected StringMemento stringMemento;

  /**
   * �bertragen des Wertes des <CODE>StringMementos</CODE> auf den
   * ToolTipText des gew�hlten <CODE>JComponent</CODE>.
   **/
	public	void	load() {
		component.setToolTipText( stringMemento.getStringValue() );
	}

  /**
   * Ver�nderungen des ToolTipTextes werden nicht an das <CODE>JTextField</CODE>
   * propagiert.
   **/
	public	void	save() {
	}

  /**
   * Konstruktor, der eine Verbindung zwischen einer <CODE>JComponent</CODE>
   * und einem <CODE>StringMemento</CODE> herstellt.
   * @param component Die <CODE>JComponent</CODE>, deren ToolTipText gesetzt
   *    werden soll.
   * @param stringMemento Das <CODE>StringMemento</CODE>, dessen Inhalt als
   *    Vorlage f�r den ToolTipText verwendet werden soll.
   **/
	public	ToolTipGlue( JComponent component, StringMemento stringMemento ) {
		this.component			= component;
		this.stringMemento	= stringMemento;
		load();
	}
}

