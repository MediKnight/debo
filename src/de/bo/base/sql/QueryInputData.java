package de.bo.base.sql;

import java.util.*;

import de.bo.base.sql.*;

public class QueryInputData
{
  protected TableCache cache;
  protected Vector attribute;

  public QueryInputData(TableCache cache,TableAttribute[] ta)
  {
    this.cache = cache;

    if ( ta != null ) {
      attribute = new Vector( ta.length );
      for ( int i=0; i<ta.length; i++ )
	attribute.addElement( ta[i] );
    }
    else
      attribute = null;
  }

  public TableCache getTableCache()
  {
    return cache;
  }
  public TableAttribute getAttribute(int index)
  {
    if ( attribute != null )
      return (TableAttribute)attribute.elementAt( index );
    else
      return null;
  }
  public int getAttributeCount()
  {
    return ( attribute != null ) ? attribute.size() : 0;
  }
}
