package de.bo.base.sql;

public class TableAttribute
{
  public String name;
  public int order;
  public boolean ascend;

  public TableAttribute()
  {
    this( null, 0, true );
  }
  public TableAttribute(String name)
  {
    this( name, 0, true );
  }
  public TableAttribute(String name,int order)
  {
    this( name, order, true );
  }
  public TableAttribute(String name,int order,boolean ascend)
  {
    this.name = name;
    this.order = order;
    this.ascend = ascend;
  }

  public boolean equals(Object o)
  {
    TableAttribute ta = (TableAttribute)o;

    if ( name == null || ta.name == null )
      return false;

    return name.equalsIgnoreCase( ta.name );
  }
}
