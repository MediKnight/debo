package de.bo.base.swing.glue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import de.bo.base.memento.Glue;
import de.bo.base.memento.StringMemento;

public class ComboBoxGlue<E> implements Glue {
	protected	JComboBox<E>			comboBox;
	protected	StringMemento stringMemento;
  protected boolean       loadInProgress;

	public	void	load() {
    loadInProgress = true;
		comboBox.setSelectedItem( stringMemento.getStringValue() );
    loadInProgress = false;
	}

	public	void	save() {
 		stringMemento.setStringValue( (String) comboBox.getSelectedItem() );
	}

	public	ComboBoxGlue( JComboBox<E> comboBox, StringMemento stringMemento ) {
		this.comboBox				= comboBox;
		this.stringMemento	= stringMemento;
		load();
		comboBox.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
        if( !loadInProgress ) {
		  		save();
        }
			}
		} );
	}
}

