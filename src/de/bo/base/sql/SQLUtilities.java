package de.bo.base.sql;

import java.util.*;
import java.net.*;

public class SQLUtilities
{
  public static String quote(String s)
  {
    int n = s.length();
    StringBuffer sb = new StringBuffer( n*2 );
    for ( int i=0; i<n; i++ ) {
      char c = s.charAt( i );
      if ( c == '\'' )
	sb.append( "''" );
      else
	sb.append( c );
    }
    return "'"+sb.toString()+"'";
  }
  public static int getLocalHostValue()
  {
    try {
      return InetAddress.getLocalHost().hashCode();
    }
    catch ( Exception x ) {
      return 0;
    }
  }
  public static Object createPseudoKey()
  {
    Date date = new Date();
    int secs = (int)(date.getTime() / 1000L);
    int hv = getLocalHostValue();

    return new Integer( secs^hv );
  }
}
