package de.bo.bobo;

import de.bo.base.store2.StoreKeeper;

/**
 * Darstellung einer Bankverbindung.
 * <p>
 * Diese Klasse behandelt nur die Primärdaten dieser Adresse und muss
 * deshalb nicht weiter kommentiert werden.
 *
 * @see AddressRecord
 * @see AddressContainer
 */

public class BankAccount extends AddressRecord
{
  public BankAccount() {
    super();
  }

  public BankAccount(StoreKeeper storeKeeper) {
    super(storeKeeper);
  }

  public String getName() {
    return getString(4);
  }

  public String getCode() {
    return getString(5);
  }

  public String getAccountNumber() {
    return getString(6);
  }

  public String getRemark() {
    return getString(7);
  }

  public String toString() {
    return getName()+" BLZ: "+getCode()+" KNr: "+getAccountNumber();
  }

  public void set(String name,String accountNumber) {
    set(name,"",accountNumber,"");
  }

  public void set(String name,String code,String accountNumber,
		  String remark) {
    setName(name);
    setCode(code);
    setAccountNumber(accountNumber);
    setRemark(remark);
  }

  public void setName(String name) {
    setString(4,name);
  }

  public void setCode(String code) {
    setString(5,code);
  }

  public void setAccountNumber(String accountNumber) {
    setString(6,accountNumber);
  }

  public void setRemark(String remark) {
    setString(7,remark);
  }

  public String[] getAttributes() {
    return new String[] { "id", "mid", "pid", "sid",
			    "institut", "blz", "ktnr",
			    "bemerkung" };
  }

  public int getColumnCount() {
    return 8;
  }

  public String getIdentifier() {
    return "bankverbindung";
  }

  /*
  protected Object getParentKey(int index) {
    switch ( index ) {
    case 0:
      return data[1];
    case 1:
      return data[3];
    case 2:
      return data[2];
    }
    return null;
  }

  protected void setParentKey(int index,Object key) {
    switch ( index ) {
    case 0:
      data[1] = key;
      break;
    case 1:
      data[3] = key;
      break;
    case 2:
      data[2] = key;
      break;
    }
  }
  */
}
