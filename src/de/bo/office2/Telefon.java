package de.bo.office2;

import de.bo.base.store.StoreKeeper;

/**
 * Darstellung einer Telefonnummer.
 * <p>
 * Diese Klasse behandelt nur die Prim�rdaten dieser Adresse und muss
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
  public Telefon(StoreKeeper<Bobo> storeKeeper) {
    super( storeKeeper );
  }

  public String getCountry() {
    return getStoreToolkit().objectToString( data[4] );
  }
  public String getArea() {
    return getStoreToolkit().objectToString( data[5] );
  }
  public String getNumber() {
    return getStoreToolkit().objectToString( data[6] );
  }
  public String getExtension() {
    return getStoreToolkit().objectToString( data[7] );
  }
  public String getType() {
    return getStoreToolkit().objectToString( data[8] );
  }
  public String getRemark() {
    return getStoreToolkit().objectToString( data[9] );
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
    set( "", area, number, "", "", "" );
  }
  public void set(String area,String number,String extension) {
    set( "", area, number, extension, "", "" );
  }
  public void set(String country,String area,String number,String extension) {
    set( country, area, number, extension, "", "" );
  }
  public void set(String country,String area,String number,String extension,
		  String type,String remark) {
    setCountry( country );
    setArea( area );
    setNumber( number );
    setExtension( extension );
    setType( type );
    setRemark( remark );
  }

  public void setCountry(String country) {
    data[4] = getStoreToolkit().stringToObject( country );
  }
  public void setArea(String area) {
    data[5] = getStoreToolkit().stringToObject( area );
  }
  public void setNumber(String number) {
    data[6] = getStoreToolkit().stringToObject( number );
  }
  public void setExtension(String extension) {
    data[7] = getStoreToolkit().stringToObject( extension );
  }
  public void setType(String type) {
    data[8] = getStoreToolkit().stringToObject( type );
  }
  public void setRemark(String remark) {
    data[9] = getStoreToolkit().stringToObject( remark );
  }

  public String[] getAttributes() {
    return new String[] { "id", "pid", "mid", "sid",
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
