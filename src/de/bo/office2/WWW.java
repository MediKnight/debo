package de.bo.office2;

import de.bo.base.store.StoreKeeper;

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
    super( storeKeeper );
  }

  public String getURL() {
    return getStoreToolkit().objectToString( data[4] );
  }
  public String getRemark() {
    return getStoreToolkit().objectToString( data[5] );
  }

  public String toString() {
    return getURL();
  }

  public void set(String url,String remark) {
    setURL( url );
    setRemark( remark );
  }
 
  public void setURL(String url) {
    data[4] = getStoreToolkit().stringToObject( url );
  }
  public void setRemark(String remark) {
    data[5] = getStoreToolkit().stringToObject( remark );
  }

  public String[] getAttributes() {
    return new String[] { "id", "pid", "mid", "sid",
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
