package de.bo.office;

import java.sql.*;

import de.bo.base.dbs.*;

public class TelefonRecord extends ExternalRecord
{
  protected String country;
  protected String area;
  protected String number;
  protected String extension;
  protected String type;
  protected String remark;

  public TelefonRecord(AbstractTable table) {
    super( table );
  }

  public boolean create()
    throws SQLException {

    country = area = number = extension = type = remark = "";

    return super.create();
  }

  protected void makeDataAbstract() {
    record[4] = country;
    record[5] = area;
    record[6] = number;
    record[7] = extension;
    record[8] = type;
    record[9] = remark;
  }
  protected void makeDataConcrete() {
    country   = DBUtilities.objectToString( record[4] );
    area      = DBUtilities.objectToString( record[5] );
    number    = DBUtilities.objectToString( record[6] );
    extension = DBUtilities.objectToString( record[7] );
    type      = DBUtilities.objectToString( record[8] );
    remark    = DBUtilities.objectToString( record[9] );
  }

  protected Object createKey()
    throws SQLException {
    return DBUtilities.createKey( table.getConnection(), "id2" );
  }

  public String getCountry() {
    return country;
  }
  public String getArea() {
    return area;
  }
  public String getNumber() {
    return number;
  }
  public String getExtension() {
    return extension;
  }
  public String getType() {
    return type;
  }
  public String getRemark() {
    return remark;
  }
  public String getFullNumber() {
    return country+area+number+extension;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    if ( type.length() > 0 )
      sb.append( type+": " );
    if ( country.length() > 0 )
      sb.append( country+"-" );
    if ( area.length() > 0 )
      sb.append( area+"-" );
    sb.append( number );
    if ( extension.length() > 0 )
      sb.append( "-"+extension );

    return sb.toString();
  }

  public void set(String area,String number) {
    set( "", area, number, "", "", "" );
  }
  public void set(String area,String number,String extension) {
    set( "", area, number, extension, "", "" );
  }
  public void set(String country,String area,String number,String extension) {
    set( country, area, number, extension, "", "" );
  }
  public void set(String country,String area,String number,String extension,
		  String type,String remark) {
    this.country   = country;
    this.area      = area;
    this.number    = number;
    this.extension = extension;
    this.type      = type;
    this.remark    = remark;
  }

  public void setCountry(String country) {
    this.country = country;
  }
  public void setArea(String area) {
    this.area = area;
  }
  public void setNumber(String number) {
    this.number = number;
  }
  public void setExtension(String extension) {
    this.extension = extension;
  }
  public void setType(String type) {
    this.type = type;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
}
