package de.baltic_online.base.store.sql;

import de.baltic_online.base.store.*;

public class SQLToolkit implements StoreToolkit
{
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

  protected SQLToolkit() {}

  public String quote(String s) {
    if ( s == null )
      return getNullString();

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

  public char getQuotationChar() {
    return '\"';
  }
  public String getQuotationReplacement() {
    return "\"\"";
  }
  public char getSingleMatchingChar() {
    return '?';
  }
  public char getMultiMatchingChar() {
    return '*';
  }

  public String getOperatorString(int op) {
    return ops[op];
  }
  public String getLowerFunctionName() {
    return "lower";
  }
  public String getUpperFunctionName() {
    return "upper";
  }
  public String getNullString() {
    return "null";
  }

  public String objectToString(Object o) {
    return (o != null) ? o.toString() : "";
  }
  public Object stringToObject(String s) {
    if ( s.length() != 0 ) return s;
    return null;
  }
  public boolean objectToBoolean(Object o) {
    if ( o == null ) return false;
    return "0nNfF".indexOf(o.toString()) < 0;
  }
  public Object booleanToObject(boolean b) {
    return b ? "y" : "n";
  }
}
