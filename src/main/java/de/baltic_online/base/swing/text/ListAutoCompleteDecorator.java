package de.baltic_online.base.swing.text;

import javax.swing.*;
import javax.swing.text.*;

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
 */

public class ListAutoCompleteDecorator extends AutoCompleteDocumentDecorator {

    public static final String CLASSNAME = "ListAutoCompleteDecorator";

    protected ListModel<Object> model;

    /**
     * Dieser Konstruktor bekommt als Parameter eine DocumenetComboBox, deren
     * Text vervollst�ndigt werden soll.
     * @param documentComboBox Die DocumentComboBox, deren Text vervollst�ndigt
     *  werden soll.
     */
    public ListAutoCompleteDecorator(
        ListModel<Object> model,
        JTextComponent textComponent) {
        super(textComponent);

        this.model = model;
    }

    /**
     * Diese Methode liefert eine <CODE>Enumeration</CODE> mit den m�glichen
     * Vervollst�ndigungen. Da die Vervollst�ndigungen f�r alle Prefixe gleich
     * sind, werden die Parameter hier ignoriert.
     * @param beforeText Der Text vor der einzuf�genden Stelle.
     * @param str Der einzuf�gende Text.
     * @return Eine <CODE>Enumeration</CODE> von <CODE>String</CODE> die
     *  die m�glichen Vervollst�ndigungen angibt.
     */
    public String getCompletion(String s) {
        String element = null;
        String elementWithCase = null;

        if (isCaseInsensitive()) {
            s = s.toLowerCase();
        }

        for (int i = 0; i < model.getSize(); i++) {
            elementWithCase = model.getElementAt(i).toString();

            if (isCaseInsensitive()) {
                element = elementWithCase.toLowerCase();
            } else {
                element = elementWithCase;
            }

            if (element.startsWith(s)) {
                if (element.length() > s.length()) {
                    return elementWithCase.substring(s.length());
                }
            }
        }

        return null;
    }
}
