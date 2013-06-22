package de.baltic_online.office;

import java.sql.*;

import de.baltic_online.base.dbs.*;

public abstract class ContainerRecord extends AbstractRecord
{
  protected AbstractTable[] childTable;

  public ContainerRecord(AbstractTable table) {
    super( table );

    Connection conn = table.getConnection();

    childTable = new AbstractTable[] {
      new TelefonTable( conn ),
	new AddressTable( conn ),
	new EMailTable( conn ),
	new WWWTable( conn ),
	new BankAccountTable( conn )
	};
  }

  protected Object createKey()
    throws SQLException {
    return DBUtilities.createKey( table.getConnection(), "id" );
  }

  public boolean retrieve()
    throws SQLException {

    boolean b = super.retrieve();
    if ( b ) arrangeSelections();

    return b;
  }
  public boolean retrieve(Object key)
    throws SQLException {

    boolean b = super.retrieve( key );
    if ( b ) arrangeSelections();

    return b;
  }

  public boolean retrieve(Object[] data) {
    boolean b = super.retrieve( data );

    try {
      if ( b ) arrangeSelections();
    }
    catch ( SQLException x ) {
      return false;
    }

    return b;
  }

  public boolean store()
    throws SQLException {

    boolean c = created;
    boolean b = super.store();

    if ( b && c ) arrangeSelections();

    return b;
  }

  protected void arrangeSelections()
    throws SQLException {

    TableSelection sel = new
      TableSelection( table.getKeyIdentifier(), getKey() );

    for ( int i=0; i<childTable.length; i++ ) {
      AbstractTable t = childTable[i];
      t.removeSelection();
      t.addSelection( sel );
      t.invalidate();
    }
  }

  public TelefonTable getTelefonTable() {
    return (TelefonTable)childTable[0];
  }
  public AddressTable getAddressTable() {
    return (AddressTable)childTable[1];
  }
  public EMailTable getEMailTable() {
    return (EMailTable)childTable[2];
  }
  public WWWTable getWWWTable() {
    return (WWWTable)childTable[3];
  }
  public BankAccountTable getBankAccountTable() {
    return (BankAccountTable)childTable[4];
  }

  public int getChildCount() {
    return childTable.length;
  }
  public AbstractTable getChildTable(int n) {
    return childTable[n];
  }
  public AbstractRecord getChildRecord(int n) {
    AbstractTable table = childTable[n];
    switch ( n ) {
    case 0:
      return new TelefonRecord( table );
    case 1:
      return new AddressRecord( table );
    case 2:
      return new EMailRecord( table );
    case 3:
      return new WWWRecord( table );
    case 4:
      return new BankAccountRecord( table );
    }
    return null;
  }
}
