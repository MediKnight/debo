package de.bo.bobo;

import de.bo.base.store2.StoreKeeper;

/**
 * Darstellung einer Telefonnummer.
 * <p>
 * Diese Klasse behandelt nur die Primärdaten dieser Adresse und muss
 * deshalb nicht weiter kommentiert werden.
 *
 * @see AddressRecord
 * @see AddressContainer
 */

public class Telefon extends AddressRecord
{
  public Telefon() {
    super();
  }

  public Telefon(StoreKeeper storeKeeper) {
    super(storeKeeper);
  }

  public String getCountry() {
    return getString(4);
  }

  public String getArea() {
    return getString(5);
  }

  public String getNumber() {
    return getString(6);
  }

  public String getExtension() {
    return getString(7);
  }

  public String getType() {
    return getString(8);
  }

  public String getRemark() {
    return getString(9);
  }

  public String getFullNumber() {
    return getCountry()+getArea()+getNumber()+getExtension();
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    String s = getType();
    if ( s.length() > 0 )
      sb.append( s+": " );
    s = getCountry();
    if ( s.length() > 0 )
      sb.append( s+"-" );
    s = getArea();
    if ( s.length() > 0 )
      sb.append( s+"-" );
    sb.append( getNumber() );
    s = getExtension();
    if ( s.length() > 0 )
      sb.append( "-"+s );

    return sb.toString();
  }

  public void set(String area,String number) {
    set("",area,number,"","","");
  }

  public void set(String area,String number,String extension) {
    set("",area,number,extension,"","");
  }

  public void set(String country,String area,String number,String extension) {
    set(country,area,number,extension,"","");
  }

  public void set(String country,String area,String number,String extension,
		  String type,String remark) {
    setCountry(country);
    setArea(area);
    setNumber(number);
    setExtension(extension);
    setType(type);
    setRemark(remark);
  }

  public void setCountry(String country) {
    setString(4,country);
  }

  public void setArea(String area) {
    setString(5,area);
  }

  public void setNumber(String number) {
    setString(6,number);
  }

  public void setExtension(String extension) {
    setString(7,extension);
  }

  public void setType(String type) {
    setString(8,type);
  }

  public void setRemark(String remark) {
    setString(9,remark);
  }

  public String[] getAttributes() {
    return new String[] { "id", "mid", "pid", "sid",
			    "land", "vorwahl", "anschluss", "durchwahl",
			    "art",
			    "bemerkung" };
  }

  public int getColumnCount() {
    return 10;
  }

  public String getIdentifier() {
    return "telefon";
  }
}
