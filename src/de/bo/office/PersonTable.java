package de.bo.office;

import java.sql.*;
import java.util.*;

import de.bo.base.dbs.*;

public class PersonTable extends AbstractTable
{
  public PersonTable() {
    super();
  }
  public PersonTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "person";
  }
  public String getKeyIdentifier() {
    return "pid";
  }
  protected String getOrderClause() {
    return "name,vorname";
  }
}
