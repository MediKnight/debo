
package de.bo.base.util;

import java.util.*;
import java.io.File;

/**
 * Einige (hoffentlich nützliche) String-Routinen.
 */

public class StringTools
{
  /**
   * Konvertiert String Zahlenliste in ein <code>int</code>-Array.
   *
   * Die Zahlen müssen mit Kommata getrennt sein.
   *
   * @param data String Zahlenliste
   * @return <code>int</code>-Array
   */
  public static int[] stringListToIntArray(String data) {
    return stringListToIntArray( data, "," );
  }

  /**
   * Konvertiert String Zahlenliste in ein <code>int</code>-Array.
   *
   * @param data String Zahlenliste
   * @param delim Trennzeichen
   * @return <code>int</code>-Array
   */
  public static int[] stringListToIntArray(String data,String delim) {

    StringTokenizer st = new StringTokenizer( data, delim );
    int n = 0;
    while ( st.hasMoreTokens() ) {
      st.nextToken();
      n++;
    }
    st = new StringTokenizer( data, "," );
    int[] ret = new int[n];
    for ( int i=0; i<n; i++ )
      ret[i] = Integer.parseInt( st.nextToken() );

    return ret;
  }

  /**
   * Konvertiert <code>int</code>-Array in eine String-Liste.
   *
   * Die String-Liste ist durch Kommata getrennt.
   *
   * @param data int Daten
   * @return String-Liste
   */
  public static String intArrayToStringList(int[] data) {
    return intArrayToStringList( data, ',' );
  }

  /**
   * Konvertiert <code>int</code>-Array in eine String-Liste.
   *
   * @param data int Daten
   * @param delim Trennzeichen
   * @return String-Liste
   */
  public static String intArrayToStringList(int[] data,char delim) {
    StringBuffer sb = new StringBuffer();
    int n = data.length;
    for ( int i=0; i<n; i++ ) {
      if ( i > 0 ) sb.append( delim );
      sb.append( new Integer(data[i]).toString() );
    }
    return sb.toString();
  }

  public static String[] getTokens(String text) {
    return getTokens( text, null );
  }

  public static String[] getTokens(String text,String delim) {

    StringTokenizer st = (delim!=null) ?
      new StringTokenizer( text, delim ) :
      new StringTokenizer( text );

    int n = st.countTokens();
    String[] sa = new String[n];

    for ( int i=0; i<n; i++ )
      sa[i] = st.nextToken();

    return sa;
  }

  public static String stringArrayToStringList(String[] sa) {
    return stringArrayToStringList( sa, ',' );
  }

  public static String stringArrayToStringList(String[] sa,char delim) {
    if ( sa == null )
      return null;

    StringBuffer sb = new StringBuffer();
    for ( int i=0; i<sa.length; i++ ) {
      if ( i > 0 )
	sb.append( delim );
      sb.append( sa[i] );
    }

    return sb.toString();
  }

  public static String translatePath(String pointedPath) {
    StringBuffer sb = new StringBuffer();
    StringTokenizer st = new StringTokenizer( pointedPath, "." );
    while ( st.hasMoreTokens() ) {
      sb.append( st.nextToken() );
      if ( st.hasMoreTokens() )
	sb.append( File.separator );
    }
    return sb.toString();
  }
}
