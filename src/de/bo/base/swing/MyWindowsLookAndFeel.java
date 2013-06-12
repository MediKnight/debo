package de.bo.base.swing;

import com.sun.java.swing.plaf.windows.*;

/**
 *
 */
public class MyWindowsLookAndFeel extends WindowsLookAndFeel {

    private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public String getID() {
		return "A modified Windows Look and Feel";
	}

	/**
	 *
	 */
	public String getName() {
		return "A modified Windows Look and Feel";
	}

	/**
	 *
	 */
	public String getDescription() {
		return "A modified Windows Look and Feel";
	}

	/**
	 *
	 */
	public boolean isNativeLookAndFeel() {
		return false;
	}

	/**
	 *
	 */
	public boolean isSupportedLookAndFeel() {
		return true;
	}
}
