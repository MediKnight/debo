package de.bo.office;

import java.sql.Connection;

import de.bo.base.dbs.AbstractTable;

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
