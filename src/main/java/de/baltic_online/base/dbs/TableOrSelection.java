package de.baltic_online.base.dbs;

/**
 */

public class TableOrSelection extends BooleanTableSelection
{
  public String getOperatorString() {
    return DBUtilities.getOperatorString( DBUtilities.OR );
  }
}
