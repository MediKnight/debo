/*
 *
 */

package de.baltic_online.base.swing;

import java.awt.*;
import java.awt.event.*;

public class BasicWindowMonitor extends WindowAdapter {

    public void windowClosing( WindowEvent e ) {
	Window window = e.getWindow();

	window.setVisible( false );
	window.dispose();
	System.exit( 0 );
    }

}
