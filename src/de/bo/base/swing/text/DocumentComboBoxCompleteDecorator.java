package de.bo.base.swing.text;

import java.util.Enumeration;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import de.bo.base.swing.DocumentComboBox;

/**
 * Diese Klasse stellt ein <CODE>Document</CODE> dar, was an eine
 * editierbare <CODE>DocumentComboBox</CODE> gehängt werden kann und dann alle
 * Eingaben in dem Textfeld der ComboBox entsprechend den
 * Listeneinträgen in der <CODE>DocumentComboBox</CODE> vervollständigt.
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
	protected	DocumentComboBox	documentComboBox;

  /**
   * Dieser Konstruktor bekommt als Parameter eine DocumenetComboBox, deren
   * Text vervollständigt werden soll.
   * @param documentComboBox Die DocumentComboBox, deren Text vervollständigt
   *		werden soll.
   **/
	public DocumentComboBoxCompleteDecorator( DocumentComboBox documentComboBox ) {
    this( null, documentComboBox );
	}

  /**
   * Dieser Konstruktor bekommt als Parameter ein Document, auf dem die
   * Vervollständigung angewendet werden soll und eine DocumentComboBox,
   * deren JTextField den zu vervollständigenden Text enthält.
   **/
	public DocumentComboBoxCompleteDecorator( Document document,
			DocumentComboBox documentComboBox ) {
		super( document, documentComboBox.getTextField() );
		this.documentComboBox = documentComboBox;
	}

	/**
	 * Diese Methode liefert eine <CODE>Enumeration</CODE> mit den möglichen
	 * Vervollständigungen. Da die Vervollständigungen für alle Prefixe gleich
   * sind, werden die Parameter hier ignoriert.
   * @param beforeText Der Text vor der einzufügenden Stelle.
   * @param str Der einzufügende Text.
	 * @return Eine <CODE>Enumeration</CODE> von <CODE>String</CODE> die
	 *		die möglichen Vervollständigungen angibt.
	 **/
	public Enumeration getCompletions( String beforeText, String str ) {
		return new Enumeration() {
			int index = 0;
			public	boolean	hasMoreElements() { return index < documentComboBox.getItemCount(); }
			public	Object	nextElement()			{ return documentComboBox.getItemAt( index++ ); }
		};
	}
}
