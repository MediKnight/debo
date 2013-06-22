package de.baltic_online.office;

import java.sql.Connection;

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
