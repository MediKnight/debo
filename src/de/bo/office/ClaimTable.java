package de.bo.office;

import java.sql.*;
import java.util.*;

import de.bo.base.dbs.*;

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
