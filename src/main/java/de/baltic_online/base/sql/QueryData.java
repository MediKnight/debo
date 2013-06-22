package de.baltic_online.base.sql;

import java.util.*;

public class QueryData implements Cloneable
{
  public String term;
  public int method;
  public boolean ignoreCase;
  public int selection;

  protected boolean valid;

  public QueryData()
  {
    term = "";
    method = selection = 0;
    ignoreCase = true;

    valid = true;
  }

  public QueryData parseQuery()
  {
    valid = true;
    String t = term.trim();

    if ( t.length() == 0 || selection == 0 ) {
      valid = false;
      return this;
    }

    QueryData q = (QueryData)clone();

    StringTokenizer st = new StringTokenizer( t );
    StringBuffer sb = new StringBuffer( t.length() );

    while ( st.hasMoreTokens() ) {
      if ( sb.length() > 0 )
	sb.append( ' ' );
      sb.append( st.nextToken() );
    }

    q.term = new String( sb.toString() );

    return q;
  }

  public boolean isValid()
  {
    return valid;
  }

  public Object clone()
  {
    QueryData q = new QueryData();

    q.term = new String( term );
    q.method = method;
    q.ignoreCase = ignoreCase;
    q.selection = selection;

    q.valid = valid;

    return q;
  }
}
