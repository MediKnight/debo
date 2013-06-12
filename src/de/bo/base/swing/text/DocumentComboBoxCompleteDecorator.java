package de.bo.base.swing.text;

import java.util.Enumeration;

import javax.swing.text.Document;

import de.bo.base.swing.DocumentComboBox;

/**
 * Diese Klasse stellt ein <CODE>Document</CODE> dar, was an eine
 * editierbare <CODE>DocumentComboBox</CODE> geh�ngt werden kann und dann alle
 * Eingaben in dem Textfeld der ComboBox entsprechend den
 * Listeneintr�gen in der <CODE>DocumentComboBox</CODE> vervollst�ndigt.
 *
 * Anwendung:
 * <PRE>
 * DocumentComboBox cb = new DocumentComboBox();
 * DocumentDecorator.updateDocumentDecorator( cb.getTextField(), "eineId",
 *   new DocumentComboBoxCompleteDecorator( cb ) );
 * </PRE>
 * oder (falls keine weiteren DocumentDecorator verwendet werden sollen):
 * <PRE>
 * cb.setDocument( new DocumentComboBoxCompleteDecorator( cb.getDocument(), cb );
 * </PRE>
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class DocumentComboBoxCompleteDecorator extends DagoAutoCompleteDocumentDecorator {
	protected DocumentComboBox<String> documentComboBox;

  /**
   * Dieser Konstruktor bekommt als Parameter eine DocumenetComboBox, deren
   * Text vervollst�ndigt werden soll.
   * @param documentComboBox Die DocumentComboBox, deren Text vervollst�ndigt
   *		werden soll.
   **/
	public DocumentComboBoxCompleteDecorator( DocumentComboBox<String> documentComboBox ) {
    this( null, documentComboBox );
	}

  /**
   * Dieser Konstruktor bekommt als Parameter ein Document, auf dem die
   * Vervollst�ndigung angewendet werden soll und eine DocumentComboBox,
   * deren JTextField den zu vervollst�ndigenden Text enth�lt.
   **/
	public DocumentComboBoxCompleteDecorator( Document document,
			DocumentComboBox<String> documentComboBox ) {
		super( document, documentComboBox.getTextField() );
		this.documentComboBox = documentComboBox;
	}

	/**
	 * Diese Methode liefert eine <CODE>Enumeration</CODE> mit den m�glichen
	 * Vervollst�ndigungen. Da die Vervollst�ndigungen f�r alle Prefixe gleich
   * sind, werden die Parameter hier ignoriert.
   * @param beforeText Der Text vor der einzuf�genden Stelle.
   * @param str Der einzuf�gende Text.
	 * @return Eine <CODE>Enumeration</CODE> von <CODE>String</CODE> die
	 *		die m�glichen Vervollst�ndigungen angibt.
	 **/
	public Enumeration<String> getCompletions( String beforeText, String str ) {
		return new Enumeration<String>() {
			int index = 0;
			public	boolean	hasMoreElements() { return index < documentComboBox.getItemCount(); }
			public	String	nextElement()     { return documentComboBox.getItemAt( index++ ); }
		};
	}
}
