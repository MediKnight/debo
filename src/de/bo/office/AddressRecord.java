package de.bo.office;

import java.sql.*;

import de.bo.base.dbs.*;

public class AddressRecord extends ExternalRecord
{
  protected String building;
  protected String floor;
  protected String street;
  protected String number;
  protected String zip;
  protected String city;
  protected String country;
  protected String remark;

  public AddressRecord(AbstractTable table) {
    super( table );
  }

  public boolean create()
    throws SQLException {

    building = floor = street = number = zip = city = country = remark = "";

    return super.create();
  }

  protected void makeDataAbstract() {
    record[4]  = building;
    record[5]  = floor;
    record[6]  = street;
    record[7]  = number;
    record[8]  = zip;
    record[9]  = city;
    record[10] = country;
    record[11] = remark;
  }
  protected void makeDataConcrete() {
    building = DBUtilities.objectToString( record[4] );
    floor    = DBUtilities.objectToString( record[5] );
    street   = DBUtilities.objectToString( record[6] );
    number   = DBUtilities.objectToString( record[7] );
    zip      = DBUtilities.objectToString( record[8] );
    city     = DBUtilities.objectToString( record[9] );
    country  = DBUtilities.objectToString( record[10] );
    remark   = DBUtilities.objectToString( record[11] );
  }

  protected Object createKey()
    throws SQLException {
    return DBUtilities.createKey( table.getConnection(), "id2" );
  }

  public String getBuilding() {
    return building;
  }
  public String getFloor() {
    return floor;
  }
  public String getStreet() {
    return street;
  }
  public String getNumber() {
    return number;
  }
  public String getZip() {
    return zip;
  }
  public String getCity() {
    return city;
  }
  public String getCountry() {
    return country;
  }
  public String getRemark() {
    return remark;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    if ( building.length() > 0 )
      sb.append( building+", " );
    if ( floor.length() > 0 )
      sb.append( floor+"; " );
    if ( street.length() > 0 )
      sb.append( street+" " );
    if ( number.length() > 0 )
      sb.append( number+"; " );
    sb.append( zip+" "+city );
    if ( country.length() > 0 )
      sb.append( " ("+country+")" );

    return sb.toString();
  }

  public void set(String zip,String city) {
    set( "", "", "", "", zip, city, "", "" );
  }
  public void set(String zip,String city,String street,String number) {
    set( "", "", street, number, zip, city, "", "" );
  }
  public void set(String building,String floor,String street,String number,
		  String zip,String city,String country,String remark) {
    this.building = building;
    this.floor    = floor;
    this.street   = street;
    this.number   = number;
    this.zip      = zip;
    this.city     = city;
    this.country  = country;
    this.remark   = remark;
  }

  public void setBuilding(String building) {
    this.building = building;
  }
  public void setFloor(String floor) {
    this.floor = floor;
  }
  public void setStreet(String street) {
    this.street = street;
  }
  public void setNumber(String number) {
    this.number = number;
  }
  public void setZip(String zip) {
    this.zip = zip;
  }
  public void setCity(String city) {
    this.city = city;
  }
  public void setCountry(String country) {
    this.country = country;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
}
