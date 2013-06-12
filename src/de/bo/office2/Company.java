package de.bo.office2;

import java.util.*;

import de.bo.base.store.StoreKeeper;

/**
 * Baltic-Online Gesch�ftsobjekt <b>Firma</b>.
 * <p>
 * Eine Firma enth�lt bis auf Name und Rechtsform keine weiteren
 * prim�ren Daten. Diese sind in den verschiedenen Standorten enthalten.
 * <p>
 * Eine Firma kann wie eine Person Kunde, Lieferant oder Partner sein
 * und ist deshalb eine <code>BusinessRelation</code>.
 *
 * @see Person
 * @see BusinessRelation
 */

public class Company extends Bobo
  implements BusinessRelation
{
  /**
   * Liste der Standorte.
   */
  protected LinkedList locationData;

  /**
   * Erzeugt Firma mit Default-Storekeeper
   */
  public Company() {
    super();

    locationData = null;
  }

  /**
   * Erzeugt Firma mit gegebenen Storekeeper.
   *
   * @param storeKeeper verwendetes Datensystem
   */
  public Company(StoreKeeper<Bobo> storeKeeper) {
    super( storeKeeper );
  }

  /**
   * Liefert Anzahl der "Eltern".
   *
   * Eine Firma hat keine "Eltern" im Sinne der BO-Hierarchie und
   * deshalb liefert diese Funktion <code>0</code>.
   *
   * @return <code>0</code>
   */
  public int getParentCount() {
    return 0;
  }

  /**
   * Erzeugt "Eltern"-Objekt zum gegebenen Index (im Bereich von
   * <code>getParentCount()</code>).
   *
   * Eine Firma hat keine "Eltern" im Sinne der BO-Hierarchie und
   * deshalb liefert diese Funktion <code>null</code>.
   *
   * @return <code>null</code>
   */
  protected Bobo createParent(int index) {
    return null;
  }

  /**
   * Liefert "Eltern"-Schl�ssel zum gegebenen Index (im Bereich von
   * <code>getParentCount()</code>).
   *
   * Eine Firma hat keine "Eltern" im Sinne der BO-Hierarchie und
   * deshalb liefert diese Funktion <code>null</code>.
   *
   * @return <code>null</code>
   */
  protected Object getParentKey(int index) {
    return null;
  }

  /**
   * Laden des Objekts.
   *
   * Bei Erfolg wird <code>locationData</code> auf <code>null</code>
   * gesetzt, um korrekte Daten zu garantieren.
   */
  public boolean retrieve(Object key) {
    boolean b = super.retrieve( key );
    if ( b ) locationData = null;
    return b;
  }

  /**
   * Liefert Liste von Standorten.
   *
   * Diese Liste wird bei Bedarf (d.h. wenn
   * <code>locationData == null</code> gilt) geladen.
   */
  public List getLocations() {
    if ( locationData == null ) {
      if ( retrieveLocations() )
	return locationData;
      else
	return null;
    }
    return locationData;
  }

  /**
   * Laden aller zur Firma zugeh�rigen Standorte.
   *
   * @return <code>true</code> bei Erfolg
   */
  protected synchronized boolean retrieveLocations() {
    locationData = new LinkedList();
    Location location = new Location();
    Enumeration e =
      location.getEnumeration( getKeyIdentifier(), getKey(), true );
    if ( e == null )
      return false;

    while ( e.hasMoreElements() ) {
      location = (Location)e.nextElement();
      location.setParent( 0, this );
      locationData.add( location );
    }

    return true;
  }

  /**
   * Liefert Name der Firma.
   */
  public String getName() {
    return getStoreToolkit().objectToString( data[1] );
  }

  /**
   * Liefert Rechtsform der Firma.
   */
  public String getLegal() {
    return getStoreToolkit().objectToString( data[2] );
  }

  public boolean isCustomer() {
    return getStoreToolkit().objectToBoolean( data[3] );
  }
  public boolean isSupplier() {
    return getStoreToolkit().objectToBoolean( data[4] );
  }
  public boolean isPartner() {
    return getStoreToolkit().objectToBoolean( data[5] );
  }

  /**
   * Liefert zus�tzliche Info zur Firma.
   */
  public String getRemark() {
    return getStoreToolkit().objectToString( data[6] );
  }

  /**
   * Setzen des Namen der Firma.
   *
   * Alle anderen Daten dieser Firma werden mit neutralen Werten
   * �berschrieben.
   */
  public void set(String name) {
    set( name, "", false, false, false, "" );
  }

  /**
   * Setzen des Namen und der Rechtsform der Firma.
   *
   * Alle anderen Daten dieser Firma werden mit neutralen Werten
   * �berschrieben.
   */
  public void set(String name,String legal) {
    set( name, legal, false, false, false, "" );
  }

  /**
   * Setzen aller Firmen-Daten.
   */
  public void set(String name,String legal,
		  boolean customer,boolean supplier,boolean partner,
		  String remark) {
    setName( name );
    setLegal( legal );
    setCustomer( customer );
    setSupplier( supplier );
    setPartner( partner );
    setRemark( remark );
  }

  /**
   * Setzen des Namen.
   */
  public void setName(String name) {
    data[1] = getStoreToolkit().stringToObject( name );
  }

  /**
   * Setzen der Rechtsform.
   */
  public void setLegal(String legal) {
    data[2] = getStoreToolkit().stringToObject( legal );
  }
  public void setCustomer(boolean customer) {
    data[3] = getStoreToolkit().booleanToObject( customer );
  }
  public void setSupplier(boolean supplier) {
    data[4] = getStoreToolkit().booleanToObject( supplier );
  }
  public void setPartner(boolean partner) {
    data[5] = getStoreToolkit().booleanToObject( partner );
  }

  /**
   * Setzen der Zusatzinfo.
   */
  public void setRemark(String remark) {
    data[6] = getStoreToolkit().stringToObject( remark );
  }

  /**
   * Liefert Namen des Objekttyps "Firma".
   *
   * (i.A. Name der Tabelle "firma")
   *
   * @return <code>"firma"</code>
   */
  public String getIdentifier() {
    return "firma";
  }

  /**
   * Liefert Namen des Prim�rschl�ssels "Firma".
   *
   * (i.A. Spaltenattribute des Prim�rschl�ssels)
   *
   * @return <code>"fid"</code>
   */
  public String getKeyIdentifier() {
    return "fid";
  }

  /**
   * Liefert Attribute des Objekttyps "Firma".
   *
   * (i.A. Spaltenattribute der Tabelle "firma")
   */
  public String[] getAttributes() {
    return new String[] { "fid", "name", "rechtsform",
			    "kunde", "lieferant", "partner",
			    "bemerkung" };
  }

  /**
   * Liefert Anzahl der Attribute.
   */
  public int getColumnCount() {
    return 7;
  }

  /**
   * Liefert Name und Rechtsform der Firma.
   */
  public String toString() {
    String s;
    StringBuffer sb = new StringBuffer();

    sb.append( getName() );

    s = getLegal();
    if ( s.length() > 0 )
      sb.append( " ("+s+")" );

    return sb.toString();
  }
}
