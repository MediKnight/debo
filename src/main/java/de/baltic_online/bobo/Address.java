package de.baltic_online.bobo;

import de.baltic_online.base.store2.StoreKeeper;

/**
 * Darstellung einer Adresse im üblichen Sinne.
 * <p>
 * Diese Klasse behandelt nur die Primärdaten dieser Adresse und muss
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

  public Address(StoreKeeper storeKeeper) {
    super(storeKeeper);
  }

  public String getBuilding() {
    return getString(4);
  }

  public String getFloor() {
    return getString(5);
  }

  public String getStreet() {
    return getString(6);
  }

  public String getNumber() {
    return getString(7);
  }

  public String getZip() {
    return getString(8);
  }

  public String getCity() {
    return getString(9);
  }

  public String getCountry() {
    return getString(10);
  }

  public String getRemark() {
    return getString(11);
  }

  public void set(String zip,String city) {
    set("","","","",zip,city,"","");
  }

  public void set(String zip,String city,String street,String number) {
    set("", "", street, number, zip, city, "", "" );
  }

  public void set(String building,String floor,String street,String number,
		  String zip,String city,String country,String remark) {
    setBuilding(building);
    setFloor(floor);
    setStreet(street);
    setNumber(number);
    setZip(zip);
    setCity(city);
    setCountry(country);
    setRemark(remark);
  }

  public void setBuilding(String building) {
    setString(4,building);
  }

  public void setFloor(String floor) {
    setString(5,floor);
  }

  public void setStreet(String street) {
    setString(6,street);
  }

  public void setNumber(String number) {
    setString(7,number);
  }

  public void setZip(String zip) {
    setString(8,zip);
  }

  public void setCity(String city) {
    setString(9,city);
  }

  public void setCountry(String country) {
    setString(10,country);
  }

  public void setRemark(String remark) {
    setString(11,remark);
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
    return new String[] { "id", "mid", "pid", "sid",
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
