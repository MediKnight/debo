package de.baltic_online.office;

import java.sql.Connection;

public class ClaimTable extends AbstractBillTable
{

  public ClaimTable() {
    super();
  }

  public ClaimTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "forderung";
  }

  public String getKeyIdentifier() {
    return "id";
  }


}
