package de.baltic_online.office;

import java.sql.Connection;

import de.baltic_online.base.dbs.AbstractTable;

public class AddressTable extends AbstractTable
{
  public AddressTable() {
    super();
  }
  public AddressTable(Connection conn) {
    super( conn );
  }

  public String getIdentifier() {
    return "adresse";
  }
  protected String getOrderClause() {
    return "land,plz,ort,strasse,hausnummer";
  }
}
