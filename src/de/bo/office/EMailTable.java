package de.bo.office;

import java.sql.*;
import java.util.*;

import de.bo.base.dbs.*;

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
