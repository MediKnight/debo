package de.bo.base.swing.plaf.basic;

import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.plaf.basic.BasicComboBoxEditor;


/**
 * Diese Klasse stellt einen BasicComboBoxEditor dar, der um einige
 * Methoden erweitert ist. Der normale BasicComboBoxEditor enthält
 * ein JTextField, was aber 'protected' ist. Um trotzdem an das JTextField
 * zu kommen kann die Methode getTextField() aus dieser Klasse verwendet
 * werden. Außerdem sind noch einige Methoden von JTextField direkt
 * herausgeführt.
 * Das Herausführen des Textfeldes ist notwendig, falls Listener an
 * das Textfeld gehängt werden sollen (z.B. fuer DocumentChange-Events)
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/
 
public class DocumentComboBoxEditor extends BasicComboBoxEditor {

	/**
   * Diese Methode liefert als Resultat das JTextField des Editors zurück.
   * @return Das JTextField des Editors.
   **/
	public JTextField getTextField() {
  	return editor;
  }

  /**
   * Diese Methode liefert als Resultat das Document des JTextFields des
   * Editors zurück.
   * @return Das Documentd es JTextFields des Editors.
   **/
	public Document getDocument() {
  	return editor.getDocument();
  }

  /**
   * Diese Methode setzt das Document des JTextFields des Editors auf
   * den übergebenen Parameter.
   * @param d Das Document, das in das JTextField eingesetzt werden soll.
   **/
	public void	setDocument( Document d ) {
  	editor.setDocument( d );
  }

  /**
   * Mit dieser Methode kann direkt ein DocumentListener an das Document des
   * JTextFields des Editors angehängt werden.
   * @param l Der DocumentListener, der an das Document des JTextFields
   *		gehaengt werden soll.
   **/
	public void addDocumentListener( DocumentListener l ) {
		editor.getDocument().addDocumentListener( l );
	}
};

