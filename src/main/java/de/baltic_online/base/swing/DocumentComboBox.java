package de.baltic_online.base.swing;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import de.baltic_online.base.swing.plaf.basic.DocumentComboBoxEditor;

/**
 * Diese Klasse stellt eine gewöhnliche JComboBox dar, wobei jedoch der Zugriff
 * auf das JTextField möglich ist.
 * Das JTextField liegt im ComboBoxEditor. Da es dort aber als 'protected'
 * markiert ist kann von dieser Klasse aus darauf nicht zugegriffen werden.
 * Der Zugriff wird hergestellt, indem der ComboBoxEditor durch einen
 * DocumentComboBoxEditor ersetzt wird, der diesen Zugriff gestattet.
 * @see DocumentComboBoxEditor
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class DocumentComboBox<E> extends JComboBox<E> {

  private static final long serialVersionUID = 1L;

  /**
   * Initialisierungsroutine. Hier wird der ComboBoxEditor auf den
   * DocumentComboBoxEditor gesetzt. Diese Methode sollte in jedem
   * Konstruktor aufgerufen werden.
	 **/
	private void init() {
		DocumentComboBoxEditor cbe = new DocumentComboBoxEditor();
		setEditor( cbe );
	}

	/**
   * Standard-Konstruktor
	 **/
	public DocumentComboBox() {
		init();
	}

	/**
   * Konstruktor, der als Parameter eine Liste von Objekten bekommt, die
   * initial in die JComboBox eingetragen werden.
   * @param items Eine Liste von Objekten für das Menu der JComboBox
	 **/
	public DocumentComboBox( E[] items ) {
		super( items );
		init();
	}

	/**
   * Konstruktor, der als Parameter eine Liste von Objekten bekommt, die
   * initial in die JComboBox eingetragen werden und ein Document, das
   * in das eingebettete JTextField eingetragen wird.
   * @param items Eine Liste von Objekten für das Menu der JComboBox
   * @param doc Ein Document, das in das JTextField der JComboBox eingetragen
   *		wird.
	 **/
	public DocumentComboBox( E[] items, Document doc ) {
		super( items );
		init();
		setDocument( doc );
	}

	/**
   * Diese Methode liefert als Resultat das JTextField der JComboBox zur�ck.
   * @return Das JTextField dieser JComboBox
	 **/
	public JTextField getTextField() {
		return ((DocumentComboBoxEditor) getEditor()).getTextField();
	}

  /**
   * Diese Methode liefert als Resultat das Document der JComboBox zur�ck.
   * @return Das Document des JTextFields der JComboBox
   **/
	public Document getDocument() {
		return ((DocumentComboBoxEditor) getEditor()).getDocument();
	}

  /**
   * Diese Methode setzt das Document des JTextFields der JComboBox auf
   * den übergebenen Parameter.
   * @param d Das Document, das in das JTextField der JComboBox eingesetzt
   *		werden soll.
   **/
	public void setDocument( Document d ) {
		((DocumentComboBoxEditor) getEditor()).setDocument( d );
	}

  /**
   * Mit dieser Methode kann ein DocumentListener an das Document des
   * JTextFields der JComboBox angehängt werden.
   * @param l Der DocumentListener, der an das Document des JTextFields der
   *		JComboBox angehängt werden soll.
   **/
	public void addDocumentListener( DocumentListener l ) {
		((DocumentComboBoxEditor) getEditor()).addDocumentListener( l );
	}

  /**
   * Diese Methode liefert als Resultat den Text des eingebetteten JTextFields
   * als String zurück.
   * @return Den Text das eingebetteten JTextFields.
   **/
	public String	getText() {
//		return (String) getSelectedItem();
		return (String) getEditor().getItem();
   }

  /**
   * Diese Methode setzt den Text des JTextFields auf den übergebenen
   * Parameter.
   * @param s Der Text, auf den der Inhalt des JTextFields des JComboBox
   *		gesetzt werden soll.
   **/
	public void		setText( String s ) {
  	setSelectedItem( s );
  }
};

