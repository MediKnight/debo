package de.bo.office;

import java.sql.*;

import de.bo.base.dbs.*;

public class LocationRecord extends ContainerRecord
{
  protected String name;
  protected String remark;

  public LocationRecord(AbstractTable table) {
    super( table );
  }

  public boolean create()
    throws SQLException {

    name = remark = "";

    return super.create();
  }

  protected void makeDataAbstract() {
    record[2] = name;
    record[3] = remark;
  }
  protected void makeDataConcrete() {
    name   = DBUtilities.objectToString( record[2] ); 
    remark = DBUtilities.objectToString( record[3] );
  }
  
  public String getName() {
    return name;
  }

  public String getRemark() {
    return remark;
  }

  public String toString() {
    return name;
  }

  public void set(String name,String remark) {
    this.name   = name;
    this.remark = remark;
  }
 
  public void setName(String name) {
    this.name = name;
  }
  
  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setCompanyKey(Object key) {
    record[1] = key;
  }
}
