package de.bo.office2;

import de.bo.base.store.StoreKeeper;

/**
 * Darstellung einer EMail-Adresse.
 * <p>
 * Diese Klasse behandelt nur die Primärdaten dieser Adresse und muss
 * deshalb nicht weiter kommentiert werden.
 *
 * @see AddressRecord
 * @see AddressContainer
 */

public class EMail extends AddressRecord
{
  public EMail() {
    super();
  }
  public EMail(StoreKeeper storeKeeper) {
    super( storeKeeper );
  }

  public String getName() {
    return getStoreToolkit().objectToString( data[4] );
  }
  public String getDomain() {
    return getStoreToolkit().objectToString( data[5] );
  }
  public String getRemark() {
    return getStoreToolkit().objectToString( data[6] );
  }
  public String toString() {
    return getName()+"@"+getDomain();
  }

  public void set(String name,String domain,String remark) {
    setName( name );
    setDomain( domain );
    setRemark( remark );
  }

  public void setName(String name) {
    data[4] = getStoreToolkit().stringToObject( name );
  }
  public void setDomain(String domain) {
    data[5] = getStoreToolkit().stringToObject( domain );
  }
  public void setRemark(String remark) {
    data[6] = getStoreToolkit().stringToObject( remark );
  }

  public String[] getAttributes() {
    return new String[] { "id", "pid", "mid", "sid",
			    "uname", "domain",
			    "bemerkung" };
  }
  public int getColumnCount() {
    return 7;
  }
  public String getIdentifier() {
    return "email";
  }
}
