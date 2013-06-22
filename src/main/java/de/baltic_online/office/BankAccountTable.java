package de.baltic_online.office;

import java.sql.Connection;

import de.baltic_online.base.dbs.AbstractTable;

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
