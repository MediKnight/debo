package de.baltic_online.office;

import java.sql.*;

import de.baltic_online.base.dbs.*;

public class DepartmentRecord extends AbstractRecord
{
  protected String name;
  protected String acronym;
  protected String remark;

  public DepartmentRecord(AbstractTable table) {
    super( table );
  }

  public boolean create()
    throws SQLException {

    name = acronym = remark = "";

    return super.create();
  }

  protected void makeDataAbstract() {
    record[1] = name;
    record[2] = acronym;
    record[3] = remark;
  }
  protected void makeDataConcrete() {
    name      = DBUtilities.objectToString( record[1] );
    acronym   = DBUtilities.objectToString( record[2] );
    remark    = DBUtilities.objectToString( record[3] );
  }

  protected Object createKey()
    throws SQLException {
    return DBUtilities.createKey( table.getConnection(), "id2" );
  }

  public void set(String name,String acronym) {
    set( name, acronym, "" );
  }
  public void set(String name,String acronym,String remark) {
    this.name      = name;
    this.acronym   = acronym;
    this.remark    = remark;
  }

  public String getName() {
    return name;
  }
  public String getAcronym() {
    return acronym;
  }
  public String getRemark() {
    return remark;
  }

  public void setName(String name) {
    this.name = name;
  }
  public void setAcronym(String acronym) {
    this.acronym = acronym;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String toString() {
    return acronym+" ("+name+")";
  }
}
