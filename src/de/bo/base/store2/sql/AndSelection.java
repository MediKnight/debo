package de.bo.base.store2.sql;


public class AndSelection extends SelectionContainer
{
  public AndSelection(SQLToolkit toolkit) {
    super(toolkit);
  }

  protected String getOperatorString() {
    return toolkit.getOperatorString(SQLToolkit.AND);
  }
}
