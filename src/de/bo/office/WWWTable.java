package de.bo.office;

import java.sql.*;
import java.util.*;

import de.bo.base.dbs.*;

public class WWWTable extends AbstractTable
{
  public WWWTable() {
    super();
  }
  public WWWTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "www";
  }
  protected String getOrderClause() {
    return "url";
  }
}
