package de.bo.base.sql;

import java.util.*;

import de.bo.base.sql.*;

public class QueryOutputData
{
  protected TableCache cache;
  protected Object priKey;
  protected TableAttribute attribute;

  public QueryOutputData(TableCache cache,Object priKey,
			 TableAttribute attribute)
  {
    this.cache = cache;
    this.priKey = priKey;
    this.attribute = attribute;
  }

  public TableCache getTableCache()
  {
    return cache;
  }
  public Object getPriKey()
  {
    return priKey;
  }
  public TableAttribute getAttribute()
  {
    return attribute;
  }
}
