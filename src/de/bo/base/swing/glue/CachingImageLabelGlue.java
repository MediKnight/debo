package de.bo.base.swing.glue;

import java.awt.*;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import de.bo.base.memento.StringMemento;
import java.util.Hashtable;

/**
 * Diese Klasse stellt eine Verbindung zwischen einem <CODE>JLabel</CODE>
 * und einem <CODE>StringMemento</CODE> her, wobei die dem Inhalt
 * des StringMementos entsprechende Datei als ImageIcon umgesetzt und
 * gecached wird.
 *
 * Achtung: Die Implementierung ist potentiell Fehlerhaft, da hier
 * nur auf die Groesse des JLabels geschaut wird. In ImageLabelGlue
 * wird die Groesse des Icons aber wesentlich aufwendiger bestimmt.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class CachingImageLabelGlue extends ImageLabelGlue {
	protected Hashtable	sizeCache				= new Hashtable();
	protected Hashtable	imageIconCache	= new Hashtable();

	protected	ImageIcon	getImageIcon() {
		Dimension		size			= label.getBounds().getSize();
		String			name			= fileName.getStringValue();
		if( name == null ) { return null; }

		Dimension	cachedSize = (Dimension) sizeCache.get( name );
		if( cachedSize != null ) {
			if( cachedSize.equals( size ) ) {
				return (ImageIcon) imageIconCache.get( name );
			}
		}

		ImageIcon imageIcon = super.getImageIcon();
		sizeCache.put( name, size );
		imageIconCache.put( name, imageIcon );
		return imageIcon;
	}

	public	CachingImageLabelGlue( JLabel label, StringMemento fileName ) {
		super( label, fileName );
	}
}

