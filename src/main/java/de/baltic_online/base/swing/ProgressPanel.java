package de.baltic_online.base.swing;

import java.awt.*;
import javax.swing.*;

/**
 * Diese Klasse implementiert eine Fortschrittsanzeige in einem Fenster.
 * Sie ist geeignet, wenn das Aufbauen der GUI-Komponenten eine l�ngere
 * Zeit erfordert.
 * Anwendung:<PRE>
 * ProgressPanel pProgress = new ProgressPanel( 1, 2 );
 * add( pProgress );
 * int progress = 0;
 *   <Aktionen>
 * pProgress.setValue( ++progress );
 *   <Aktionen>
 * pProgress.setValue( ++progress );
 * remove( pProgress );
 * add( <Andere Komponenten> );</PRE>
 *
 * @version 0.01 1.8.1999
 * @author Dagobert Michelsen
 **/

public class ProgressPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	protected	JLabel				lProgress;
	protected	JProgressBar	pbProgress;

  /**
   * Konstruktor, der die untere und obere Grenze f�r die Darstellung von
   * dem Fortschritt entsprechenden Werten �bergeben bekommt.
   * @param low Die untere Grenze f�r Werte der Fortschrittsanzeige
   * @param high Die obere Grenze f�r Werte der Fortschrittsanzeige
   **/
	public ProgressPanel( int low, int high ) {
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );

		lProgress = new JLabel( "Initialisiere Dialog" );
		lProgress.setFont( new Font( "Dialog", Font.BOLD, 18 ) );
		lProgress.setAlignmentX( CENTER_ALIGNMENT );

		add( lProgress );
		add( Box.createRigidArea( new Dimension( 1, 20 ) ) );

		pbProgress = new JProgressBar( low, high );
		lProgress.setLabelFor( pbProgress );
		pbProgress.setAlignmentX( CENTER_ALIGNMENT );
		add( pbProgress );
	}

  /**
   * Diese Methode setzt den Fortschrittsanzeiger auf den als Parameter
   * �bergebenen Wert, der zwischen den im Konstruktor �bergebenen Werten
   * low und high liegen mu�.
   * @param i Der Wert, auf den die Fortschrittsanzeige gesetzt werden soll.
   **/
	public	void	setValue( int i ) {
		pbProgress.setValue( i );
	}
}
