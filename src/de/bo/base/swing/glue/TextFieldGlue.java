package de.bo.base.swing.glue;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import de.bo.base.memento.Glue;
import de.bo.base.memento.StringMemento;

/**
 * Diese Klasse stelle eine Verbindung zwischen einem <CODE>JTextField</CODE>
 * und einem StringMemento her, wobei Veränderungen auf einer der beiden
 * Seiten sofort an die jeweils andere Seite propagiert werden.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class TextFieldGlue implements Glue {
	protected JTextField textField;
	protected StringMemento stringMemento;

	// Über diese Variable soll eine Rückkopplung verhindert werden, bei der
	// eine Veränderung des JTextFields das StringMemento verändert, was
	// wiederum das JTextField ändert...
	protected boolean loadInProgress;

	/**
	 * Diese Methode überträgt die Information von dem <CODE>StringMemento</CODE>
	 * in das <CODE>JTextField</CODE>.
	 **/
	public void load() {
		loadInProgress = true;
		textField.setText(stringMemento.getStringValue());
		loadInProgress = false;
	}

	/**
	 * Diese Methode überträgt die Information von dem <CODE>JTextField</CODE>
	 * in das <CODE>StringMemento</CODE>.
	 **/
	public void save() {
		if (loadInProgress) {
			return;
		}
		stringMemento.setStringValue(textField.getText());
	}

	/**
	 * Konstruktor zur Verbindung eines <CODE>JTextField</CODE> mit einem
	 * <CODE>StringMemento</CODE>. Veränderung in dem <CODE>JTextField</CODE>
	 * werden sofort an das <CODE>StringMemento</CODE> weitergeleitet.
	 * @param textField Das abzugleichende <CODE>JTextField</CODE>
	 * @param stringMemento Das <CODE>StringMemento</CODE>, mit dem abgeglichen
	 *    werden soll.
	 **/
	public TextFieldGlue(JTextField textField, StringMemento stringMemento) {
		this.textField = textField;
		this.stringMemento = stringMemento;
		loadInProgress = false;
		load();
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				save();
			}
			public void insertUpdate(DocumentEvent e) {
				save();
			}
			public void removeUpdate(DocumentEvent e) {
				save();
			}
		});
	}
}
