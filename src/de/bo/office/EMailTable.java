package de.bo.office;

import java.sql.Connection;

import de.bo.base.dbs.AbstractTable;

public class EMailTable extends AbstractTable
{
  public EMailTable() {
    super();
  }
  public EMailTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "email";
  }
  protected String getOrderClause() {
    return "uname,domain";
  }
}
