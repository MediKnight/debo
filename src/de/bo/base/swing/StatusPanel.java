package de.bo.base.swing;

import java.awt.GridLayout;
import javax.swing.*;
import de.bo.base.memento.StringMemento;

/**
 * Diese Klasse stellt ein JPanel dar, in dem Statusmeldungen angezeigt
 * werden können und die nach einer definierten Zeit automatisch wieder
 * gelöscht werden. Das setzen des angezeigten Textes geschieht über
 * ein StringMemento.
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class StatusPanel extends JPanel {
	private		long		timeToClear = 3000;	// Zeit bis zum löschen des Statustextes in Millisekunden
	private		long		lastUpdated;				// Zeitpunkt des letzten Setzen eines Statustextes
	protected	JLabel	statusLabel;
	protected	Thread	clearStatusThread;

	protected long getTime() {
		return System.currentTimeMillis();
	}

  /**
   * Diese Methode setzt die Zeit, die bis zum löschen des angezeigten Textes
   * in der Statusbar vergehen muß.
   * @param timeToClear Zeit in Millisekunden bis zum löschen des Textes
   *		in der Statusbar.
   **/
	public	synchronized	void	setTimeToClear( long timeToClear ) {
		this.timeToClear = timeToClear;
	}

  /**
   * Diese Methode liefert als Ergebnis die Zeit, die bis zum Löschen von
   * Meldungen vergeht in Millisekunden.
   * @return Zeit bis zum Löschen von Meldungen in Millisekunden.
   **/
  public	synchronized	long	getTimeToClear() {
  	return timeToClear;
  }

  /**
   * Standard-Konstruktor
   **/
	public StatusPanel() {
		GridLayout gridLayout = new GridLayout();
		setLayout( gridLayout );
		setBorder( BorderFactory.createLoweredBevelBorder() );

		statusLabel = new NonResizingLabel( " " );	// Darstellung erzwingen

		add( statusLabel );
		clearStatusThread = null;
	}

  /**
   * Diese Methode liefert die Zeit zurück, an dem zuletzt eine Meldung
   * angezeigt worden ist.
   * @return Der Zeitpunkt, an dem zuletzt eine Meldung angezeigt worden ist.
   **/
	protected	synchronized long	getLastUpdated() {
  	return lastUpdated;
  }

  /**
   * Diese Methode setzt den Text der Statuszeile auf den übergebenen
   * Parameter.
   * @param s Der Text, der in die Statuszeile geschrieben werden soll.
   **/
	protected synchronized void setText( String s ) {
	  // -- Hinweise zur Implementierung
		// Das Löschen des Statustextes nach einer gewissen Zeitspanne geschieht
    // in einem extra Thread (nämlich clearStatusThread). Wird der Text
    // gesetzt und existiert noch kein claerStatusThread, so wird ein neuer
    // Thread erzeugt. Dieser legt sich schlafen bis zu dem Zeitpunkt, wo der
    // Text in der Statusbar gelöscht werden soll. Falls aber in der
    // Zwischenzeit der Text der Statusbar neu gesetzt worden ist, so soll
    // der neue Text wiederum nach Ablauf der Zeit ab dem Setzen des neuen
    // Textes gelöscht werden. Es muß in diesem Fall also erneut gewartet
    // werden, bis die Zeit nach dem letzten Setzen abgelaufen ist. Dieses
    // leistet die while-Schleife.
    // Der nachfolgende synchronized-Block auf das StatusPanel-Objekt ist
    // notwendig, da dort claerStatusThread umgesetzt wird, was in einem
    // anderen Thread bei setText auch gerade neu gesetzt werden könnte.
		statusLabel.setText( s );
		lastUpdated = getTime();
		if( clearStatusThread == null ) {
			clearStatusThread = new Thread() {
				public	void run() {
					// lastUpdated kann sich waehrend der Wartezeit aendern, deshalb
					// eine while-Schleife darumlegen.
					while( getLastUpdated() + timeToClear > getTime() ) {
						try {
							sleep( getLastUpdated() + timeToClear - getTime() );
						} catch( InterruptedException e ) {
						}
					}
					synchronized( StatusPanel.this ) {	// wg. Zuweisung an clearStatusThread
						statusLabel.setText( " " );
						clearStatusThread = null;
					}
				}
			};
			clearStatusThread.start();
		}
	}

  /**
   * Diese Methode liefert als Resultat den aktuellen Text der Statuszeile
   * zurück.
   * Achtung: Der Text wird automatisch zurückgesetzt, so daß hier nicht
   * unbedingt der Text des letzten setText zurückgeliefert wird.
   * @return Der aktuelle Text der Statuszeile
   **/
	protected synchronized String getText() {
		return statusLabel.getText();
	}

  /**
   * Diese Methode liefert als Resultat ein StringMemento, mit dem der
   * Inhalt der Statuszeile gesetzt und ausgelesen werden kann.
   * @return Ein StringMemento, mit dem der Text der Statuszeile gesetzt
   *		werden kann.
   **/
	public	StringMemento	getStatusMemento() {
		return new StringMemento() {
			public	void		setStringValue( String s )  { setText( s ); }
			public	String  getStringValue()            { return getText(); }
		};
	}
}
