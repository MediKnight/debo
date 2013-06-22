/*
 *
 */

package de.baltic_online.base.swing;

import java.awt.*;

import javax.swing.*;

public class ArrowIconFactory {

    public static final String CLASSNAME = "ArrowIconFactory";

    public static Icon getLeftFilledArrowIcon() {
	return new LeftFilledArrowIcon( 10, 17, new Insets( 2, 2, 2, 2 ) );
    }

    public static Icon getRightFilledArrowIcon() {
	return new RightFilledArrowIcon( 10, 17, new Insets( 2, 2, 2, 2 ) );
    }


    private static class LeftFilledArrowIcon implements Icon {
	int height;
	int width;

	Insets insets;

	public LeftFilledArrowIcon( int width, int height, Insets insets ) {
	    this.width = width;
	    this.height = height;
	    this.insets = insets;
	}

	public void paintIcon( Component c, Graphics g, int x, int y ) {
	    int innerWidth = width - insets.left - insets.right;
	    int innerHeight = height - insets.top - insets.bottom;

	    int neededHeight = (innerWidth - 1) * 2 + 1;

	    int actualHeight = Math.min( neededHeight, innerHeight );
	    int actualWidth = actualHeight / 2 + 1;

	    int middle = actualHeight / 2;

	    g.translate( x + insets.left, y + insets.top );

	    g.setColor( c.getForeground() );

	    for( int i = 0; i < actualWidth; i++ ) {
		g.drawLine( i, middle - i, i, middle + i );
	    }

	    g.translate( -(x + insets.left), -(y + insets.top) );

	}

	public int getIconWidth() {
	    return width;
	}

	public int getIconHeight() {
	    return height;
	}
    }


    private static class RightFilledArrowIcon implements Icon {
	int height;
	int width;
	Insets insets;

	public RightFilledArrowIcon( int width, int height, Insets insets ) {
	    this.width = width;
	    this.height = height;
	    this.insets = insets;
	}

	public void paintIcon( Component c, Graphics g, int x, int y ) {
	    int innerWidth = width - insets.left - insets.right;
	    int innerHeight = height - insets.top - insets.bottom;

	    int neededHeight = (innerWidth - 1) * 2 + 1;

	    int actualHeight = Math.min( neededHeight, innerHeight );
	    int actualWidth = actualHeight / 2 + 1;

	    int middle = actualHeight / 2;

	    g.translate( x + insets.left, y + insets.top );

	    g.setColor( c.getForeground() );

	    for( int i = 0; i < actualWidth; i++ ) {
		g.drawLine( innerWidth - i - 1, middle - i, innerWidth - i - 1, middle + i );
	    }

	    g.translate( -(x + insets.left), -(y + insets.top) );

	}

	public int getIconWidth() {
	    return width;
	}

	public int getIconHeight() {
	    return height;
	}
    }

}
