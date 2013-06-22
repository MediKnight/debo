package de.baltic_online.base.application;

import java.util.*;

import java.awt.Dimension;
import java.io.IOException;

/**
 * Useful utilities handling properties in applications.
 *
 * @author sml
 */
public class PropertyManager
{
    /**
     * Merges two <tt>Properties</tt> together.
     * <p>
     * If the two <tt>Properties</tt> have a none-zero cut (e.g. have
     * common keys) the common key of the first <tt>Properties</tt> object
     * has the higher priority.
     *
     * @param higher <tt>Properties</tt> of higher priority
     * @param lower <tt>Properties</tt> of lower priority
     * @return new <tt>Properties</tt> object
     */
    public static Properties mergeProperties(Properties higher,
					     Properties lower) {
	Properties props = (Properties)higher.clone();

	for ( Enumeration<Object> e=lower.keys(); e.hasMoreElements(); ) {
	    String key = e.nextElement().toString();
	    if ( props.getProperty(key) == null )
		props.setProperty(key,lower.getProperty(key));
	}

	return props;
    }

    /**
     * Merges an array of <tt>Properties</tt> together
     * (in order from higher to lower).
     * <p>
     * If the <tt>Properties</tt> have a none-zero cut (e.g. have
     * common keys) the common key of the previous <tt>Properties</tt> object
     * in the array has the higher priority.
     *
     * @param pa array of <tt>Properties</tt>
     * @return new <tt>Properties</tt> object
     */
    public static Properties mergeProperties(Properties[] pa) {
	int n = pa.length;

	if ( n == 0 )
	    return null;
	
	Properties props = (Properties)pa[n-1].clone();
	for ( int i=n-2; i>=0; i-- )
	    props = mergeProperties(pa[i],props);

	return props;
    }

    public static UserProperties retrieveUserProperties(String path) {
	UserProperties up = new UserProperties(path);
	try {
	    up.load();
	}
	catch ( IOException x ) { // ignore (return empty props)
	}
	return up;
    }

    public static ApplicationProperties
	retrieveApplicationProperties(String path) {

	ApplicationProperties ap = new ApplicationProperties(path);
	try {
	    ap.load();
	}
	catch ( IOException x ) { // ignore (return empty props)
	}
	return ap;
    }

    public static Dimension getDimension(Properties props,String key) {
	String value = props.getProperty(key);
	if ( value != null ) {
	    StringTokenizer st = new StringTokenizer(value,",;xX");
	    try {
		return new Dimension(Integer.parseInt(st.nextToken()),
				     Integer.parseInt(st.nextToken()));
	    }
	    catch ( RuntimeException x ) {
		return null;
	    }
	}
	return null;
    }
}
