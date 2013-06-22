package de.baltic_online.bobo;

import de.baltic_online.base.store2.StoreKeeper;

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
    super(storeKeeper);
  }

  public String getName() {
    return getString(4);
  }

  public String getDomain() {
    return getString(5);
  }

  public String getRemark() {
    return getString(6);
  }

  public String toString() {
    return getName()+"@"+getDomain();
  }

  public void set(String name,String domain) {
    set(name,domain,"");
  }

  public void set(String name,String domain,String remark) {
    setName(name);
    setDomain(domain);
    setRemark(remark);
  }

  public void setName(String name) {
    setString(4,name);
  }

  public void setDomain(String domain) {
    setString(5,domain);
  }

  public void setRemark(String remark) {
    setString(6,remark);
  }

  public String[] getAttributes() {
    return new String[] { "id", "mid", "pid", "sid",
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
