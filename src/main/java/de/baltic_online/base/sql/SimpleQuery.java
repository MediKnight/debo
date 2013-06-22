package de.baltic_online.base.sql;


public class SimpleQuery extends AbstractQuery
{
  public SimpleQuery(TableCache cache)
  {
    super( cache );
  }
  public SimpleQuery(TableCache cache,TableAttribute[] ta)
  {
    super( cache, ta );
  }

  public boolean find(Object toFind)
  {
    return true;
  }
}
