package de.bo.office;

import java.sql.*;
import java.util.*;

import de.bo.base.dbs.*;

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
