package de.bo.base.dbs;

import java.sql.*;
import java.util.*;
import java.text.*;

import de.bo.base.util.*;

public class DBUtilities
{
  public static final int ORACLE = 0;
  public static final int MYSQL = 1;

  public static final int EQUALS = 0;
  public static final int NEQUALS = 1;
  public static final int LESS = 2;
  public static final int GREATER = 3;
  public static final int LESSTHAN = 4;
  public static final int GREATERTHAN = 5;
  public static final int LIKE = 6;
  public static final int AND = 7;
  public static final int OR = 8;

  private static String[] ops = new String[] {
    "=", "<>", "<", ">", "<=", ">=", "like", "and", "or" };

  private static String[] nops = new String[] { "is null", "is not null" };

  private static int model = ORACLE;

  public static void setModel(int m) {
    model = m;
  }

  public static String quote(String s) {
    int n = s.length();
    StringBuffer sb = new StringBuffer( n*2 );
    char qc = getQuotationChar();
    String qr = getQuotationReplacement();

    sb.append( qc );
    for ( int i=0; i<n; i++ ) {
      char c = s.charAt( i );
      if ( c == qc )
	sb.append( qr );
      else
	sb.append( c );
    }
    sb.append( qc );

    return sb.toString();
  }

  public static char getQuotationChar() {
    switch ( model ) {
    case ORACLE:
      return '\'';
    }

    return '\"';
  }
  public static String getQuotationReplacement() {
    switch ( model ) {
    case ORACLE:
      return "''";
    }

    return "\"\"";
  }

  public static char getSingleMatchingChar() {
    switch ( model ) {
    case ORACLE:
      return '_';
    }

    return '?';
  }
  public static char getMultiMatchingChar() {
    switch ( model ) {
    case ORACLE:
      return '%';
    }

    return '*';
  }

  public static String getOperatorString(int op) {
    return ops[op];
  }

  public static String getNullOperatorString(int op) {
    return nops[op];
  }

  public static String getLowerFunctionName() {
    return "lower";
  }
  public static String getUpperFunctionName() {
    return "upper";
  }

  public static String objectToString(Object o) {
    return (o != null) ? o.toString() : "";
  }
  public static boolean objectToBoolean(Object o) {
    if ( o == null ) return false;

    return "0nNfF".indexOf(o.toString()) < 0;
  }
  public static Object booleanToObject(boolean b) {
    return b ? "y" : "n";
  }
  public static Integer objectToInteger(Object o) {
    Integer i = new Integer( 0 );
    if ( o == null ) return i;

    try {
      return new Integer( o.toString() );
    }
    catch ( NumberFormatException x ) {
    }
    return i;
  }
  public static Long objectToLong(Object o) {
    Long l = new Long( 0L );
    if ( o == null ) return l;

    try {
      return new Long( o.toString() );
    }
    catch ( NumberFormatException x ) {
    }
    return l;
  }
  public static Object integerToObject(int x) {
    return new Integer( x );
  }
  public static Object longToObject(long x) {
    return new Long( x );
  }
  public static CurrencyNumber objectToCurrencyNumber(Object o) {
    if ( o == null ) return new CurrencyNumber( 0L );

    long l = 0L;
    try {
      l = Long.parseLong( o.toString() );
    }
    catch ( NumberFormatException x ) {
    }
    return new CurrencyNumber( l );
  }

  public static Object createKey(Connection conn,Object param)
    throws SQLException {

    Object x = null;
    String query;
    Statement sm;
    ResultSet rset;

    switch ( model ) {
    case ORACLE:
      query = "select "+param.toString()+".nextval from dual";
      sm = conn.createStatement();
      rset = sm.executeQuery( query );

      if ( rset.next() )
	x = rset.getObject( 1 );

      rset.close();
      sm.close();
    }

    return x;
  }
  public static String dateToString(Calendar cal) {
    return dateToString( cal, Locale.getDefault() );
  }
  public static String dateToString(Calendar cal,Locale locale) {
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd", locale );
    return sdf.format( cal.getTime() );
  }

  public static GregorianCalendar stringToDate(String ds) {
    if ( ds == null || ds.length() != 8 ) return null;
    GregorianCalendar cal = new GregorianCalendar();
    try {
      cal.set( Integer.parseInt(ds.substring(0,4)),
	       Integer.parseInt(ds.substring(4,6))-1,
	       Integer.parseInt(ds.substring(6,8)) );
      return cal;
    }
    catch ( Exception x ) {
      return null;
    }
  }
}
