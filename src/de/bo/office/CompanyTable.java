package de.bo.office;

import java.sql.*;
import java.util.*;

import de.bo.base.dbs.*;

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
