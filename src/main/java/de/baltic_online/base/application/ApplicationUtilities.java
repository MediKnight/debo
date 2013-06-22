package de.baltic_online.base.application;

import java.util.*;

/**
 * Useful utilities building applications.
 *
 * @author sml
 */
public class ApplicationUtilities
{
  /**
   * This method returns a <tt>Locale</tt> from the list of available
   * locales which matches with the specification param.
   *
   * @param spec locale specification
   * @return matching locale or <tt>null</tt> if no match
   */
  public static Locale getLocale(String spec) {

    Locale[] la = Locale.getAvailableLocales();
    for ( int i=0; i<la.length; i++ )
      if ( la[i].toString().equals(spec) )
	return la[i];

    return null;
  }
}
