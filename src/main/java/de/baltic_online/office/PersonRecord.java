package de.baltic_online.office;

import java.sql.*;

import de.baltic_online.base.dbs.*;

public class PersonRecord extends ContainerRecord
  implements BusinessRelation
{
  protected String name;
  protected String firstName;
  protected String init;
  protected String title;
  protected String address;
  protected boolean customer,supplier,partner;
  protected String remark;

  protected EmployeeTable employeeTable;
  protected boolean isEmployee;

  public PersonRecord(AbstractTable table) {
    super( table );

    Connection conn = table.getConnection();
    employeeTable = new EmployeeTable( conn );

    isEmployee = false;
  }

  public boolean create()
    throws SQLException {

    name = firstName = init = title = address = remark = "";
    customer = supplier = partner = false;

    return super.create();
  }

  protected void arrangeSelections()
    throws SQLException {

    if ( isEmployee ) {
      employeeTable.removeSelection();
      TableSelection sel = new
	TableSelection( table.getKeyIdentifier(), getKey() );
      employeeTable.addSelection( sel );
      employeeTable.invalidate();

      for ( int i=0; i<childTable.length; i++ ) {
	AbstractTable t = childTable[i];
	t.removeSelection();
	t.addSelection( TableSelection.FALSE );
	t.invalidate();
      }
    }
    else
      super.arrangeSelections();
  }

  public void setEmployeeRelation(boolean b) {
    isEmployee = b;
  }

  public boolean reArrange() {
    try {
      arrangeSelections();
      return true;
    }
    catch ( SQLException x ) {
      return false;
    }
  }
  protected void makeDataAbstract() {
    record[1] = name;
    record[2] = firstName;
    record[3] = init;
    record[4] = title;
    record[5] = address;
    record[6] = DBUtilities.booleanToObject( customer );
    record[7] = DBUtilities.booleanToObject( supplier );
    record[8] = DBUtilities.booleanToObject( partner );
    record[9] = remark;
  }
  protected void makeDataConcrete() {
    name      = DBUtilities.objectToString( record[1] );
    firstName = DBUtilities.objectToString( record[2] );
    init      = DBUtilities.objectToString( record[3] );
    title     = DBUtilities.objectToString( record[4] );
    address   = DBUtilities.objectToString( record[5] );
    customer  = DBUtilities.objectToBoolean( record[6] );
    supplier  = DBUtilities.objectToBoolean( record[7] );
    partner   = DBUtilities.objectToBoolean( record[8] );
    remark    = DBUtilities.objectToString( record[9] );
  }

  public EmployeeTable getEmployeeTable() {
    return employeeTable;
  }

  public String getName() {
    return name;
  }
  public String getFirstName() {
    return firstName;
  }
  public String getFullName() {
    if ( firstName.length() > 0 )
      return name+", "+firstName;

    return name;
  }
  public String getInit() {
    return init;
  }
  public String getTitle() {
    return title;
  }
  public String getAddress() {
    return address;
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

  public void set(String name,String firstName) {
    set( name, firstName, "", "", "", false, false, false, "" );
  }
  public void set(String name,String firstName,
		  String init,String title,String address,
		  boolean customer, boolean supplier,boolean partner,
		  String remark) {
    this.name      = name;
    this.firstName = firstName;
    this.init      = init;
    this.title     = title;
    this.address   = address;
    this.customer  = customer;
    this.supplier  = supplier;
    this.partner   = partner;
    this.remark    = remark;
  }

  public void setName(String name) {
    this.name = name;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public void setInit(String init) {
    this.init = init;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public void setAddress(String address) {
    this.address = address;
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
    if ( firstName.length() > 0 )
      return name+", "+firstName;
    return name;
  }
}
