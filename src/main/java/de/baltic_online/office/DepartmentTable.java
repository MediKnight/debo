package de.baltic_online.office;

import java.sql.Connection;

import de.baltic_online.base.dbs.AbstractTable;

public class DepartmentTable extends AbstractTable
{
  public DepartmentTable() {
    super();
  }
  public DepartmentTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "abteilung";
  }
  public String getKeyIdentifier() {
    return "id";
  }
  protected String getOrderClause() {
    return "abk";
  }
}
