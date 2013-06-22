package de.baltic_online.office;

import java.sql.Connection;

import de.baltic_online.base.dbs.AbstractTable;

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
