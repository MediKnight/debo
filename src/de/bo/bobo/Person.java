package de.bo.bobo;

import java.util.*;

import de.bo.base.store2.*;

/**
 * Baltic-Online Geschäftsobjekt <b>Person</b>.
 * <p>
 * Eine Person enthält mehrere Adressen und mehrere Mitarbeiter-Einträge und
 * ist daher ein <code>EmployeeContainer</code>.
 * <p>
 * Eine Person kann wie eine Firma Kunde, Lieferant oder Partner sein
 * und ist deshalb eine <code>BusinessRelation</code>.
 *
 * @see Company
 * @see BusinessRelation
 */

public class Person extends EmployeeContainer
  implements BusinessRelation
{
  /**
   * Erzeugt Person mit Default-Storekeeper.
   */
  public Person() {
    super("pid");
  }

  /**
   * Erzeugt Person mit gegebenen Storekeeper.
   *
   * @param storeKeeper verwendetes Datensystem
   */
  public Person(StoreKeeper storeKeeper) {
    super(storeKeeper,"pid");
  }

  /**
   * Liefert Anzahl der "Eltern".
   *
   * Eine Person hat keine "Eltern" im Sinne der BO-Hierarchie und
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
   * Eine Person hat keine "Eltern" im Sinne der BO-Hierarchie und
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
   * Eine Person hat keine "Eltern" im Sinne der BO-Hierarchie und
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
   * Liefert Name der Person.
   */
  public String getName() {
    return getString(1);
  }

  /**
   * Liefert Vorname der Person.
   */
  public String getFirstName() {
    return getString(2);
  }

  /**
   * Liefert vollständigen Namen der Person.
   */
  public String getFullName() {
    String firstName = getFirstName();
    String name = getName();
    if ( firstName.length() > 0 )
      return name+", "+firstName;

    return name;
  }

  /**
   * Liefert Initialen der Person.
   */
  public String getInit() {
    return getString(3);
  }

  /**
   * Liefert Titel der Person.
   */
  public String getTitle() {
    return getString(4);
  }

  /**
   * Liefert Anrede der Person.
   */
  public String getSalutation() {
    return getString(5);
  }

  public boolean isCustomer() {
    return getBoolean(6);
  }

  public boolean isSupplier() {
    return getBoolean(7);
  }

  public boolean isPartner() {
    return getBoolean(8);
  }

  /**
   * Liefert zusätzliche Info zur Person.
   */
  public String getRemark() {
    return getString(9);
  }

  /**
   * Setzen von Name und Vorname der Person.
   *
   * Alle anderen Daten dieser Person werden mit neutralen Werten
   * überschrieben.
   */
  public void set(String name,String firstName) {
    set(name,firstName,"","","",false,false,false,"");
  }

  /**
   * Setzen aller Personen-Daten.
   */
  public void set(String name,String firstName,
		  String init,String title,String salutation,
		  boolean customer, boolean supplier,boolean partner,
		  String remark) {
    setName(name);
    setFirstName(firstName);
    setInit(init);
    setTitle(title);
    setSalutation(salutation);
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
   * Setzen des Vornamen.
   */
  public void setFirstName(String firstName) {
    setString(2,firstName);
  }

  /**
   * Setzen der Initialen.
   */
  public void setInit(String init) {
    setString(3,init);
  }

  /**
   * Setzen des Titels der Personen.
   */
  public void setTitle(String title) {
    setString(4,title);
  }

  /**
   * Setzen der Anrede der Person.
   */
  public void setSalutation(String salutation) {
    setString(5,salutation);
  }

  public void setCustomer(boolean customer) {
    setBoolean(6,customer);
  }

  public void setSupplier(boolean supplier) {
    setBoolean(7,supplier);
  }

  public void setPartner(boolean partner) {
    setBoolean(8,partner);
  }

  /**
   * Setzen der Zusatzinfo der Person.
   */
  public void setRemark(String remark) {
    setString(9,remark);
  }

  /**
   * Liefert Namen des Objekttyps "Person".
   *
   * (i.A. Name der Tabelle "person")
   *
   * @return <code>"person"</code>
   */
  public String getIdentifier() {
    return "person";
  }

  /**
   * Liefert Namen des Primärschlüssels "Person".
   *
   * (i.A. Spaltenattribute des Primärschlüssels)
   *
   * @return <code>"pid"</code>
   */
  public String getKeyIdentifier() {
    return "pid";
  }

  /**
   * Liefert Attribute des Objekttyps "Person".
   *
   * (i.A. Spaltenattribute der Tabelle "person")
   */
  public String[] getAttributes() {
    return new String[] { "pid", "name", "vorname", "initialen", "titel",
			    "anrede", "kunde", "lieferant", "partner",
			    "bemerkung" };
  }

  /**
   * Liefert Anzahl der Attribute.
   */
  public int getColumnCount() {
    return 10;
  }

  /**
   * Liefert alle primären Daten zur Person (ohne Zusatzinfo).
   */
  public String toString() {
    String s;
    StringBuffer sb = new StringBuffer();

    s = getSalutation();
    if ( s.length() > 0 )
      sb.append(s+" ");

    sb.append(getFullName());

    s = getTitle();
    if ( s.length()>0)
      sb.append(" ("+s+")");

    return sb.toString();
  }
}
