package de.bo.base.swing.glue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import de.bo.base.memento.Glue;
import de.bo.base.memento.BooleanMemento;

/**
 * Diese Klasse stellt eine Verbindung zwischen einer <CODE>JCheckBox</CODE>
 * und einem <CODE>BooleanMemento</CODE> her. Veränderung der Werte werden
 * sofort propagiert.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/
 
public class CheckBoxGlue implements Glue {
	protected	JCheckBox				checkBox;
	protected	BooleanMemento  booleanMemento;

	public	void	load() {
		Boolean	b = booleanMemento.getBooleanValue();
		checkBox.setSelected( b == null ? false : b.booleanValue() );
	}

	public	void	save() {
		booleanMemento.setBooleanValue( checkBox.isSelected() );
	}

	public	CheckBoxGlue( JCheckBox checkBox, BooleanMemento booleanMemento ) {
		this.checkBox				= checkBox;
		this.booleanMemento	= booleanMemento;
		load();
		checkBox.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				save();
			}
		} );
	}
}

