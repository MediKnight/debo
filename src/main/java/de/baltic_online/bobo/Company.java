package de.baltic_online.bobo;

import java.util.*;

import de.baltic_online.base.store2.*;
import de.baltic_online.base.store2.sql.*;

/**
 * Baltic-Online Geschäftsobjekt <b>Firma</b>.
 * <p>
 * Eine Firma enthält bis auf Name und Rechtsform keine weiteren
 * primären Daten. Diese sind in den verschiedenen Standorten enthalten.
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
  protected Collection<Location> locationData;

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
  public Company(StoreKeeper storeKeeper) {
    super(storeKeeper);
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
   * Liefert "Eltern"-Schlüssel zum gegebenen Index (im Bereich von
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

  protected void setParentKey(int index,Object key) {
  }

  /**
   * Laden des Objekts.
   *
   * Bei Erfolg wird <code>locationData</code> auf <code>null</code>
   * gesetzt, um korrekte Daten zu garantieren.
   */
  public void retrieve(Object key)
    throws StoreException {

    super.retrieve( key );
    locationData = null;
  }

  /**
   * Liefert Liste von Standorten.
   *
   * Diese Liste wird bei Bedarf (d.h. wenn
   * <code>locationData == null</code> gilt) geladen.
   */
  public Location[] getLocations()
    throws StoreException {

    if ( locationData == null )
      retrieveLocations();

    return (Location[])locationData.toArray(new Location[0]);
  }

  /**
   * Laden aller zur Firma zugehörigen Standorte.
   */
  protected void retrieveLocations()
    throws StoreException {

    Location location = new Location();
    SQLBase base = (SQLBase)getStoreKeeper();
    location.setStoreKeeper(base);

    Selection sel = new SQLSelection(base.getSQLToolkit(),
				     "fid",
				     getKey());
    locationData = base.retrieve(location,sel);
    for ( Iterator<Location> it=locationData.iterator(); it.hasNext(); ) {
      location = it.next();
      location.setParent(0,this);
    }
  }

  public void addLocation(Location location)
    throws StoreException {

    if ( location.getKey() != null )
      throw new StoreException(location+ALLREADY_EXISTS);

    location.setStoreKeeper(getStoreKeeper());
    if ( locationData == null )
      retrieveLocations();

    locationData.add(location);
    location.setParent(0,this);
    location.store();
  }

  public void removeLocation(Location location)
    throws StoreException {

    if ( location.getKey() == null )
      throw new StoreException(location+NOT_CREATED);

    if ( locationData == null )
      retrieveLocations();

    if ( !locationData.contains(location) )
      throw new StoreException(location+NO_MEMBER_OF+this);

    locationData.remove(location);
    location.delete();
  }

  /**
   * Liefert Name der Firma.
   */
  public String getName() {
    return getString(1);
  }

  /**
   * Liefert Rechtsform der Firma.
   */
  public String getLegal() {
    return getString(2);
  }

  public boolean isCustomer() {
    return getBoolean(3);
  }

  public boolean isSupplier() {
    return getBoolean(4);
  }

  public boolean isPartner() {
    return getBoolean(5);
  }

  /**
   * Liefert zusätzliche Info zur Firma.
   */
  public String getRemark() {
    return getString(6);
  }

  /**
   * Setzen des Namen der Firma.
   *
   * Alle anderen Daten dieser Firma werden mit neutralen Werten
   * überschrieben.
   */
  public void set(String name) {
    set(name,"",false,false,false,"");
  }

  /**
   * Setzen des Namen und der Rechtsform der Firma.
   *
   * Alle anderen Daten dieser Firma werden mit neutralen Werten
   * überschrieben.
   */
  public void set(String name,String legal) {
    set(name,legal,false,false,false,"");
  }

  /**
   * Setzen aller Firmen-Daten.
   */
  public void set(String name,String legal,
		  boolean customer,boolean supplier,boolean partner,
		  String remark) {
    setName(name);
    setLegal(legal);
    setCustomer(customer);
    setSupplier(supplier);
    setPartner(partner);
    setRemark(remark);
  }

  /**
   * Setzen des Namen.
   */
  public void setName(String name) {
    setString(1,name);
  }

  /**
   * Setzen der Rechtsform.
   */
  public void setLegal(String legal) {
    setString(2,legal);
  }

  public void setCustomer(boolean customer) {
    setBoolean(3,customer);
  }

  public void setSupplier(boolean supplier) {
    setBoolean(4,supplier);
  }

  public void setPartner(boolean partner) {
    setBoolean(5,partner);
  }

  /**
   * Setzen der Zusatzinfo.
   */
  public void setRemark(String remark) {
    setString(6,remark);
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
   * Liefert Namen des Primärschlüssels "Firma".
   *
   * (i.A. Spaltenattribute des Primärschlüssels)
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

    sb.append(getName());

    s = getLegal();
    if ( s.length() > 0 )
      sb.append(" ("+s+")");

    return sb.toString();
  }

    public synchronized void delete()
	throws StoreException {

	validateDelete();

	Location[] l = getLocations();
	for ( int i=0; i<l.length; i++ )
	    l[i].delete();

	locationData = null;

	super.delete();
    }
}
