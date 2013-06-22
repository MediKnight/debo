package de.baltic_online.office;

import java.sql.*;

import de.baltic_online.base.dbs.*;

public class WWWRecord extends ExternalRecord
{
  protected String url;
  protected String remark;

  public WWWRecord(AbstractTable table) {
    super( table );
  }

  public boolean create()
    throws SQLException {

    url = remark = "";
    return super.create();
  }

  protected void makeDataAbstract() {
    record[4] = url;
    record[5] = remark;
  }
  protected void makeDataConcrete() {
    url        = DBUtilities.objectToString( record[4] );
    remark     = DBUtilities.objectToString( record[5] );
  }

  protected Object createKey()
    throws SQLException {
    return DBUtilities.createKey( table.getConnection(), "id" );
  }

  public String getURL() {
    return url;
  }
  public String getRemark() {
    return remark;
  }

  public String toString() {
    return url;
  }

  public void set(String url,String remark) {
    this.url = url;
    this.remark = remark;
  }
 
  public void setURL(String url) {
    this.url = url;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
}
