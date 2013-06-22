package de.baltic_online.base.store2.sql;

import java.sql.*;

import de.baltic_online.base.store2.*;

public class SQLToolkit
{
  public static final int EQUALS = 0;
  public static final int NEQUALS = 1;
  public static final int LESS = 2;
  public static final int GREATER = 3;
  public static final int LESSTHAN = 4;
  public static final int GREATERTHAN = 5;
  public static final int IS = 6;
  public static final int ISNOT = 7;
  public static final int LIKE = 8;
  public static final int AND = 9;
  public static final int OR = 10;
  public static final int NOT = 11;

  private static String[] ops = new String[] {
    "=", "<>", "<", ">", "<=", ">=", "is", "is not", "like",
      "and", "or", "not" };

  protected SQLToolkit() {}

  public String quote(String s) {
    if ( s == null )
      return getNullString();

    int n = s.length();
    StringBuffer sb = new StringBuffer(n*2);
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
    return '\'';
  }

  public String getQuotationReplacement() {
    return "''";
  }

  public char getSingleMatchingChar() {
    return '_';
  }

  public char getMultiMatchingChar() {
    return '%';
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

  public Object createKey(Connection connection,Object param)
    throws StoreException {

    return null;
  }
}
