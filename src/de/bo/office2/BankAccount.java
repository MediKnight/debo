package de.bo.office2;

import de.bo.base.store.StoreKeeper;

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
    super( storeKeeper );
  }

  public String getName() {
    return getStoreToolkit().objectToString( data[4] );
  }
  public String getCode() {
    return getStoreToolkit().objectToString( data[5] );
  }
  public String getAccountNumber() {
    return getStoreToolkit().objectToString( data[6] );
  }
  public String getRemark() {
    return getStoreToolkit().objectToString( data[7] );
  }

  public String toString() {
    return getName()+" BLZ: "+getCode()+" KNr: "+getAccountNumber();
  }

  public void set(String name,String accountNumber) {
    set( name, "", accountNumber, "" );
  }
  public void set(String name,String code,String accountNumber,
		  String remark) {
    setName( name );
    setCode( code );
    setAccountNumber( accountNumber );
    setRemark( remark );
  }

  public void setName(String name) {
    data[4] = getStoreToolkit().stringToObject( name );
  }
  public void setCode(String code) {
    data[5] = getStoreToolkit().stringToObject( code );
  }
  public void setAccountNumber(String accountNumber) {
    data[6] = getStoreToolkit().stringToObject( accountNumber );
  }
  public void setRemark(String remark) {
    data[7] = getStoreToolkit().stringToObject( remark );
  }

  public String[] getAttributes() {
    return new String[] { "id", "mid", "sid", "pid",
			    "institut", "blz", "ktnr",
			    "bemerkung" };
  }
  public int getColumnCount() {
    return 8;
  }
  public String getIdentifier() {
    return "bankverbindung";
  }

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
}
