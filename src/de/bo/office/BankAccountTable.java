package de.bo.office;

import java.sql.Connection;

import de.bo.base.dbs.AbstractTable;

public class BankAccountTable extends AbstractTable
{
  public BankAccountTable() {
    super();
  }
  public BankAccountTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "bankverbindung";
  }
  protected String getOrderClause() {
    return "blz,ktnr";
  }
}
