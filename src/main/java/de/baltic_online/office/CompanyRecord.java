package de.baltic_online.office;

import java.sql.*;

import de.baltic_online.base.dbs.*;

public class CompanyRecord extends AbstractRecord
  implements BusinessRelation
{
  protected String name;
  protected String legal;
  protected boolean customer,supplier,partner;
  protected String remark;

  protected LocationTable locationTable;

  public CompanyRecord(AbstractTable table) {
    super( table );

    Connection conn = table.getConnection();
    locationTable = new LocationTable( conn );
  }

  public boolean create()
    throws SQLException {

    name = legal = remark = "";
    customer = supplier = partner = false;

    return super.create();
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

    locationTable.removeSelection();
    locationTable.addSelection( sel );
    locationTable.invalidate();
  }

  protected void makeDataAbstract() {
    record[1] = name;
    record[2] = legal;
    record[3] = DBUtilities.booleanToObject( customer );
    record[4] = DBUtilities.booleanToObject( supplier );
    record[5] = DBUtilities.booleanToObject( partner );
    record[6] = remark;
  }
  protected void makeDataConcrete() {
    name      = DBUtilities.objectToString( record[1] );
    legal     = DBUtilities.objectToString( record[2] );
    customer  = DBUtilities.objectToBoolean( record[3] );
    supplier  = DBUtilities.objectToBoolean( record[4] );
    partner   = DBUtilities.objectToBoolean( record[5] );
    remark    = DBUtilities.objectToString( record[6] );
  }

  protected Object createKey()
    throws SQLException {
    return DBUtilities.createKey( table.getConnection(), "id" );
  }

  public void set(String name,String legal) {
    set( name, legal, false, false, false, "" );
  }
  public void set(String name,String legal,
		  boolean customer, boolean supplier,boolean partner,
		  String remark) {
    this.name      = name;
    this.legal     = legal;
    this.customer  = customer;
    this.supplier  = supplier;
    this.partner   = partner;
    this.remark    = remark;
  }

  public LocationTable getLocationTable() {
    return locationTable;
  }

  public String getName() {
    return name;
  }
  public String getLegal() {
    return legal;
  }
  public boolean isCustomer() {
    return customer;
  }
  public boolean isSupplier() {
    return supplier;
  }
  public boolean isPartner() {
    return partner;
  }
  public String getRemark() {
    return remark;
  }

  public void setName(String name) {
    this.name = name;
  }
  public void setLegal(String legal) {
    this.legal = legal;
  }
  public void setCustomer(boolean customer) {
    this.customer = customer;
  }
  public void setSupplier(boolean supplier) {
    this.supplier = supplier;
  }
  public void setPartner(boolean partner) {
    this.partner = partner;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String toString() {
    return name;
  }
}
