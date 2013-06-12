/*
 *
 */

package de.bo.base.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 *
 */
public class LookAndFeelPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    LookAndFeelType metal, motif, windows, mywindows;

    Frame frame;
    
    public LookAndFeelPanel( Frame frame ) {
	this.frame = frame;

	setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
	
	metal = new LookAndFeelType( "Metal", "javax.swing.plaf.metal.MetalLookAndFeel", frame );
	motif = new LookAndFeelType( "Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel", frame );
	windows = new LookAndFeelType( "Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", frame );
//	mywindows = new LookAndFeelType( "MyWindows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", frame );
	mywindows = new LookAndFeelType( "MyWindows", "de.bo.base.swing.MyWindowsLookAndFeel", frame );

	add( metal.getButton() );
	add( motif.getButton() );
	add( windows.getButton() );
	add( mywindows.getButton() );
    }
}

    
class LookAndFeelActionListener implements ActionListener {

    String className;
    Frame frame;

    public LookAndFeelActionListener( String className, Frame frame ) {
	this.className = className;
	this.frame = frame;
    }


    public void actionPerformed( ActionEvent event ) {
	try {
	    UIManager.setLookAndFeel( className );
	    SwingUtilities.updateComponentTreeUI( frame );
	    frame.pack();
	} catch( Exception e ) {
	    System.err.println( "Exception: " + e.getMessage() );
	}
    }
}


class LookAndFeelType {

    String text;
    String className;
    Frame frame;

    JButton button;
    ActionListener listener;

    public LookAndFeelType( String text, String className, Frame frame ) {
	this.text = text;
	this.className = className;
	this.frame = frame;

	button = new JButton( text );
	listener = new LookAndFeelActionListener( className, frame );
	button.addActionListener( listener );
    }


    public JButton getButton() {
	return button;
    }
}

