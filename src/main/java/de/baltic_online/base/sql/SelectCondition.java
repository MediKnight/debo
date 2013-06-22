package de.baltic_online.base.sql;

public class SelectCondition
{
  public String name;
  public String op;
  public String term;

  public SelectCondition(String name,String term)
  {
    this( name, "=", term );
  }
  public SelectCondition(String name,String op,String term)
  {
    this.name = name;
    this.op = op;
    this.term = term;
  }
}
