package de.bo.office;

import java.sql.*;
import java.util.*;

import de.bo.base.dbs.*;

public class CommitmentTable extends AbstractBillTable
{
  public CommitmentTable() {
    super();
  }
  public CommitmentTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "verbind";
  }
  public String getKeyIdentifier() {
    return "id";
  }
}
