package de.baltic_online.bobo;

import de.baltic_online.base.store2.StoreKeeper;

/**
 * Darstellung einer Web-Adresse..
 * <p>
 * Diese Klasse behandelt nur die Primärdaten dieser Adresse und muss
 * deshalb nicht weiter kommentiert werden.
 *
 * @see AddressRecord
 * @see AddressContainer
 */

public class WWW extends AddressRecord
{
  public WWW() {
    super();
  }

  public WWW(StoreKeeper storeKeeper) {
    super(storeKeeper);
  }

  public String getURL() {
    return getString(4);
  }

  public String getRemark() {
    return getString(5);
  }

  public String toString() {
    return getURL();
  }

  public void set(String url,String remark) {
    setURL(url);
    setRemark(remark);
  }
 
  public void setURL(String url) {
    setString(4,url);
  }

  public void setRemark(String remark) {
    setString(5,remark);
  }

  public String[] getAttributes() {
    return new String[] { "id", "mid", "pid", "sid",
			    "url",
			    "bemerkung" };
  }

  public int getColumnCount() {
    return 6;
  }

  public String getIdentifier() {
    return "www";
  }
}
