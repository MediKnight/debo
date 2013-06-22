package de.baltic_online.office;

import java.sql.Connection;

import de.baltic_online.base.dbs.AbstractTable;

public class EmployeeTable extends AbstractTable
{
  public EmployeeTable() {
    super();
  }
  public EmployeeTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "mitarbeiter";
  }
  public String getKeyIdentifier() {
    return "mid";
  }
  protected String getOrderClause() {
    return "abteilung,funktion";
  }
}
