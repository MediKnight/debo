package de.bo.office;

import java.sql.*;

import de.bo.base.dbs.*;

public class EmployeeRecord extends ContainerRecord
{
  protected String office;
  protected String function;
  protected String remark;

  public EmployeeRecord(AbstractTable table) {
    super( table );
  }

  public boolean create()
    throws SQLException {

    office = function = remark = "";

    return super.create();
  }

  protected void makeDataAbstract() {
    record[3] = office;
    record[4] = function;
    record[5] = remark;
  }
  protected void makeDataConcrete() {
    office    = DBUtilities.objectToString( record[3] );
    function  = DBUtilities.objectToString( record[4] );
    remark    = DBUtilities.objectToString( record[5] );
  }

  public String getOffice() {
    return office;
  }
  public String getFunction() {
    return function;
  }
  public String getRemark() {
    return remark;
  }

  public LocationRecord getLocation() {
    LocationTable locationTable =
      new LocationTable( table.getConnection() );
    LocationRecord location = new LocationRecord( locationTable );
    try {
      location.retrieve( getLocationKey() );
      return location;
    }
    catch ( SQLException x ) {
      return null;
    }
  }
  public PersonRecord getPerson() {
    PersonTable personTable =
      new PersonTable( table.getConnection() );
    PersonRecord person = new PersonRecord( personTable );
    try {
      person.retrieve( getPersonKey() );
      return person;
    }
    catch ( SQLException x ) {
      return null;
    }
  }

  public String toString() {
    LocationRecord location = getLocation();
    if ( location != null )
      return location.toString();
    return "";
  }

  public Object getPersonKey() {
    return (record!=null) ? record[1] : null;
  }
  public void setPersonKey(Object key) {
    record[1] = key;
  }
  public Object getLocationKey() {
    return (record!=null) ? record[2] : null;
  }
  public void setLocationKey(Object key) {
    record[2] = key;
  }

  public void set(String office,String function) {
    set( office, function, "" );
  }
  public void set(String office,String function,String remark) {
    this.office   = office;
    this.function = function;
    this.remark   = remark;
  }

  public void setOffice(String office) {
    this.office   = office;
  }
  public void setFunction(String function) {
    this.function = function;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
}
