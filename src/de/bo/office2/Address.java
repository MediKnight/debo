package de.bo.office2;

import de.bo.base.store.StoreKeeper;

/**
 * Darstellung einer Adresse im �blichen Sinne.
 * <p>
 * Diese Klasse behandelt nur die Prim�rdaten dieser Adresse und muss
 * deshalb nicht weiter kommentiert werden.
 *
 * @see AddressRecord
 * @see AddressContainer
 */

public class Address extends AddressRecord
{
  public Address() {
    super();
  }
  public Address(StoreKeeper<Bobo> storeKeeper) {
    super( storeKeeper );
  }

  public String getBuilding() {
    return getStoreToolkit().objectToString( data[4] );
  }
  public String getFloor() {
    return getStoreToolkit().objectToString( data[5] );
  }
  public String getStreet() {
    return getStoreToolkit().objectToString( data[6] );
  }
  public String getNumber() {
    return getStoreToolkit().objectToString( data[7] );
  }
  public String getZip() {
    return getStoreToolkit().objectToString( data[8] );
  }
  public String getCity() {
    return getStoreToolkit().objectToString( data[9] );
  }
  public String getCountry() {
    return getStoreToolkit().objectToString( data[10] );
  }
  public String getRemark() {
    return getStoreToolkit().objectToString( data[11] );
  }

  public void set(String zip,String city) {
    set( "", "", "", "", zip, city, "", "" );
  }
  public void set(String zip,String city,String street,String number) {
    set( "", "", street, number, zip, city, "", "" );
  }
  public void set(String building,String floor,String street,String number,
		  String zip,String city,String country,String remark) {
    setBuilding( building );
    setFloor( floor );
    setStreet( street );
    setNumber( number );
    setCity( city );
    setCountry( country );
    setRemark( remark );
  }

  public void setBuilding(String building) {
    data[4] = getStoreToolkit().stringToObject( building );
  }
  public void setFloor(String floor) {
    data[5] = getStoreToolkit().stringToObject( floor );
  }
  public void setStreet(String street) {
    data[6] = getStoreToolkit().stringToObject( street );
  }
  public void setNumber(String number) {
    data[7] = getStoreToolkit().stringToObject( number );
  }
  public void setZip(String zip) {
    data[8] = getStoreToolkit().stringToObject( zip );
  }
  public void setCity(String city) {
    data[9] = getStoreToolkit().stringToObject( city );
  }
  public void setCountry(String country) {
    data[10] = getStoreToolkit().stringToObject( country );
  }
  public void setRemark(String remark) {
    data[11] = getStoreToolkit().stringToObject( remark );
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    String s = getBuilding();
    if ( s.length() > 0 )
      sb.append( s+", " );
    s = getFloor();
    if ( s.length() > 0 )
      sb.append( s+"; " );
    s = getStreet();
    if ( s.length() > 0 )
      sb.append( s+" " );
    s = getNumber();
    if ( s.length() > 0 )
      sb.append( s+"; " );
    sb.append( getZip()+" "+getCity() );
    s = getCountry();
    if ( s.length() > 0 )
      sb.append( " ("+s+")" );

    return sb.toString();
  }

  public String[] getAttributes() {
    return new String[] { "id", "pid", "mid", "sid",
			    "gebaeude", "stockwerk", "strasse", "hausnummer",
			    "plz", "ort", "land",
			    "bemerkung" };
  }
  public int getColumnCount() {
    return 12;
  }
  public String getIdentifier() {
    return "adresse";
  }
}
