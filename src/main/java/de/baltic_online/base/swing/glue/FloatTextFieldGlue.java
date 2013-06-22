package de.baltic_online.base.swing.glue;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.baltic_online.base.memento.FloatMemento;
import de.baltic_online.base.memento.Glue;

/**
 * Diese Klasse stellt eine Verbindung zwischen einem <CODE>JTextField</CODE>
 * und einem <CODE>FloatMemento</CODE> her. Dabei sollte das
 * <CODE>JTextField</CODE> von der Eingabe her so beschränkt sein, daß nur
 * Fließkommazahlen eingebbar sind. Die Umsetzung geschieht Locale-spezifisch.
 * @see java.text.NumberFormat.getNumberInstance.parse
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/
 
public class	FloatTextFieldGlue implements Glue {
	protected JTextField		textField;
	protected FloatMemento	floatMemento;

	public void load() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		Float f = floatMemento.getFloatValue();
		textField.setText( f == null ? "" : nf.format( f ) );
	}

	public void save() {
		Number f = null;
		try {
			String text = textField.getText();
			if( !text.equals( "" ) ) {
				f = NumberFormat.getNumberInstance().parse( text );
			}
		} catch( ParseException e ) {
			e.printStackTrace();
		}
		if( f == null ) {
			floatMemento.setFloatValue( null );
		} else if( f instanceof Float ) {
			floatMemento.setFloatValue( (Float) f );
		} else {
			floatMemento.setFloatValue( new Float( f.floatValue() ) );
		}
	}

	public	FloatTextFieldGlue( JTextField textField, FloatMemento floatMemento ) {
		this.textField			= textField;
		this.floatMemento		= floatMemento;
		load();
		textField.getDocument().addDocumentListener(
			new DocumentListener() {
				public void changedUpdate( DocumentEvent e )	{ save(); }
				public void insertUpdate( DocumentEvent e )		{ save(); }
				public void removeUpdate( DocumentEvent e )		{ save(); }
			}
		);
	}
}

