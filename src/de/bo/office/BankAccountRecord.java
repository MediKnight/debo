package de.bo.office;

import java.sql.*;

import de.bo.base.dbs.*;

public class BankAccountRecord extends ExternalRecord
{
  protected String name;
  protected String code;
  protected String accountNumber;
  protected String remark;

  public BankAccountRecord(AbstractTable table) {
    super( table );
  }

  public boolean create()
    throws SQLException {

    name = code = accountNumber = remark = "";

    return super.create();
  }

  protected void makeDataAbstract() {
    record[4] = name;
    record[5] = code;
    record[6] = accountNumber;
    record[7] = remark;
  }
  protected void makeDataConcrete() {
    name           = DBUtilities.objectToString( record[4] );
    code           = DBUtilities.objectToString( record[5] );
    accountNumber  = DBUtilities.objectToString( record[6] );
    remark         = DBUtilities.objectToString( record[7] );
  }

  protected Object createKey()
    throws SQLException {
    return DBUtilities.createKey( table.getConnection(), "id" );
  }

  public String getName() {
    return name;
  }
  public String getCode() {
    return code;
  }
  public String getAccountNumber() {
    return accountNumber;
  }
  public String getRemark() {
    return remark;
  }

  public String toString() {
    return name+" BLZ: "+code+" KNr: "+accountNumber;
  }

  public void set(String name,String accountNumber) {
    set( name, "", accountNumber, "" );
  }
  public void set(String name,String code,String accountNumber,
		  String remark) {
    this.name           = name;
    this.code           = code;
    this.accountNumber  = accountNumber;
    this.remark         = remark;
  }

  public void setName(String name) {
    this.name = name;
  }
  public void setCode(String bc) {
    this.code = bc;
  }
  public void setAccountNumber(String an) {
    this.accountNumber = an;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setPersonKey(Object key) {
    record[3] = key;
  }
  public void setLocationKey(Object key) {
    record[2] = key;
  }
}
