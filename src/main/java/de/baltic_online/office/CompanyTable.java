package de.baltic_online.office;

import java.sql.Connection;

import de.baltic_online.base.dbs.AbstractTable;

public class CompanyTable extends AbstractTable
{
  public CompanyTable() {
    super();
  }
  public CompanyTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "firma";
  }
  public String getKeyIdentifier() {
    return "fid";
  }
  protected String getOrderClause() {
    return "name";
  }
}
