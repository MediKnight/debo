package de.bo.office;

import java.sql.*;
import java.util.*;

import de.bo.base.dbs.*;

public class BankAccountTable extends AbstractTable
{
  public BankAccountTable() {
    super();
  }
  public BankAccountTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "bankverbindung";
  }
  protected String getOrderClause() {
    return "blz,ktnr";
  }
}
