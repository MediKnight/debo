package de.bo.base.dbs;

/**
 */

public class TableAndSelection extends BooleanTableSelection
{
  public String getOperatorString() {
    return DBUtilities.getOperatorString( DBUtilities.AND );
  }
}
