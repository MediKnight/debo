package de.bo.base.swing.glue;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import de.bo.base.memento.Glue;
import de.bo.base.memento.StringMemento;

/**
 * Diese Klasse stellt eine Glue-Komponente zwischen einem Icon und einer
 * Datei dar. Das Icon wird dabei durch eine JLabel-Komponente dargestellt und
 * die Datei über den Pfadnamen angegeben. Die Größe des Icons wird
 * automatisch proportional an die Größe des Labels angepaßt und bei
 * Größenänderungen des Labels aktualisiert.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class ImageLabelGlue implements Glue {
	protected JLabel				label;
	protected StringMemento fileName;
	protected Thread				mostRecentIconizer	= null;



	/**
	 * Diese Methode liefert ein proportional skaliertes ImageIcon passend
	 * zur Größe des JLabels für den Dateinamen des StringMementos zurück.
   * @return das passend skalierte Bild als ImageIcon.
	 **/
	protected	ImageIcon	getImageIcon() {
		// ImageIcon fuer die Datei erzeugen
		String name = fileName.getStringValue();
		if( name == null ) { return null; }
		ImageIcon	imageIcon = new ImageIcon( name );

		// Maximale Groesse bestimmen
		Dimension	size		= label.getBounds().getSize();
		Insets	 	insets	= label.getInsets();
		size.width -= insets.left + insets.right;
		size.height -= insets.top + insets.bottom;

		// Bei ueberschreitung der Groesse ImageIcon entsprechend der maximal-
		// groesse proportional skalieren
		if( imageIcon.getIconWidth() > size.width ||
				imageIcon.getIconHeight() > size.height ) {
			if( ((float) size.width / size.height) >
					((float) imageIcon.getIconWidth() / imageIcon.getIconHeight()) ) {
				size.width = -1;
			} else {
				size.height = -1;
			}
			imageIcon = new ImageIcon( imageIcon.getImage().getScaledInstance(
												size.width, size.height, Image.SCALE_DEFAULT ) );
		}
		return imageIcon;
	}

	/**
	 * Diese Methode berechnet das skalierte Icon und setzt dann das Icon
	 * in das JLabel ein. Die Ausfuehrung der Methode geschieht asynchron,
	 * d.h. es wird ein neuer Thread zur Berechnung des skalierten Bildes
	 * abgespalten. Die Berechnung wird auf jedenfall zu Ende gefuehrt.
	 * Zusaetzlich wird der Thread des zuletzt ausgefuehrten load-Aufrufes
	 * gespeichert und nur der Thread des letzten Calls setzt das Icon.
	 * Ansonsten koennte ein Thread angestossen werden, der lange zur
	 * Berechung braucht, ein zweiter Thread wird angestossen, terminiert
	 * gleich und setzt das Icon auf das Resultat des zweiten Threads. Dann
	 * terminiert der erste Thread und setzt das Icon auf das veraltete
	 * Ergebnis. Dieses Szenario wird so vermieden.
	 * Ferner muss die Abfrage von mostRecentIconizer mit dieser Instanz
	 * synchronisiert werden, da sonst ein Zugriff waehrend der Aktualisierung
	 * von mostRecentIconizer auftreten koennte.
	 **/
	synchronized public	void	load() {
		Thread imageIconizer = new Thread() {
			public void run() {
				ImageIcon	imageIcon = getImageIcon();
				synchronized( ImageLabelGlue.this ) {
					if( this == mostRecentIconizer ) {
						label.setIcon( imageIcon );
					}
				}
			}
		};
		mostRecentIconizer = imageIconizer;
		imageIconizer.start();
	}

	/**
	 * Eine Aenderung des Icons bewirkt nichts bei dem StringMemento.
	 **/
	public	void	save() {
	}

  /**
   * Konstruktor für ein <CODE>ImageLabelGlue</CODE>-Objekt, das eine
   * Verbindung zwischen dem JLabel und der durch das StringMemento
   * angegebenen Datei herstellt.
   * @param label Das Label, dem ein Icon zugewiesen werden soll
   * @param fileName Der Name der das Icon enthaltenden Datei
   **/
	public	ImageLabelGlue( JLabel label, StringMemento fileName ) {
		this.label		= label;
		this.fileName	= fileName;

		// Initial das Icon einmal setzen
		load();

		// Aktualisierung bei Aenderung der Groesse des JLabels
		label.addComponentListener( new ComponentListener() {
			public void componentResized(	ComponentEvent e )	{ load(); }
			public void componentMoved(		ComponentEvent e )	{}
			public void componentShown(		ComponentEvent e )	{}
			public void componentHidden(	ComponentEvent e )	{}
		} );
	}
}

