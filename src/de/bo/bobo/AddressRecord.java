package de.bo.bobo;

import de.bo.base.store2.*;

/**
 * Adressen im �blichen Sinne, Telefonnummern, EMails, etc. sind Adressen.
 * <p>
 * In der BO-Datenbank bezieht sich jede Adresse entweder auf eine
 * Person, einen Mitarbeiter oder einen Standort (ausschliesslich).
 *
 * @see AddressContainer
 */

public abstract class AddressRecord extends Bobo
implements Cloneable
{
  /**
   * Erzeugt Adresse mit Default-Storekeeper
   */
  protected AddressRecord() {
    super();
  }

  /**
   * Erzeugt Adresse mit gegebenen Storekeeper.
   *
   * @param storeKeeper verwendetes Datensystem
   */
  protected AddressRecord(StoreKeeper storeKeeper) {
    super(storeKeeper);
  }

  /**
   * Liefert Namen des Prim�rschl�ssels der Adresse.
   *
   * (i.A. Spaltenattribute des Prim�rschl�ssels)
   *
   * @return <code>"id"</code>
   */
  public String getKeyIdentifier() {
    return "id";
  }

  /**
   * Liefert Anzahl der "Eltern".
   *
   * @return <code>3</code>
   */
  public int getParentCount() {
    return 3;
  }

  /**
   * Liefert zugeh�rigen Mitarbeiter oder <code>null</code>, wenn
   * Adresse zu keinem Mitarbeiter geh�rt.
   */
  public Employee getEmployee()
    throws StoreException {

    return (Employee)getParent(0);
  }

  /**
   * Liefert zugeh�rige Person oder <code>null</code>, wenn
   * Adresse zu keiner Person geh�rt.
   */
  public Person getPerson()
    throws StoreException {

    return (Person)getParent(1);
  }

  /**
   * Liefert zugeh�rigen Standort oder <code>null</code>, wenn
   * Adresse zu keinem Standort geh�rt.
   */
  public Location getLocation()
    throws StoreException {

    return (Location)getParent(2);
  }

  /**
   * Erzeugt Eltern-Objekt zum gegebenen Index (im Bereich von
   * <code>getParentCount()</code>).
   */
  protected Bobo createParent(int index) {
    Bobo record = null;
    switch ( index ) {
    case 0:
      record = new Employee();
      break;
    case 1:
      record = new Person();
      break;
    case 2:
      record =  new Location();
      break;
    }

    if ( record != null )
      record.setStoreKeeper(getStoreKeeper());

    return record;
  }

  /**
   * Liefert "Eltern"-Schl�ssel zum gegebenen Index (im Bereich von
   * <code>getParentCount()</code>).
   */
  protected Object getParentKey(int index) {
    return data[index+1];
  }

  protected void setParentKey(int index,Object key) {
    data[index+1] = key;
  }

    public String getSequenceIdentifier() {
	return "id2";
    }

  /**
   * Erzeugen einer (flachen) Kopie der Adresse.
   * <p>
   * Diese Kopie wird nicht auf die zugeh�rige Datenbank abgebildet,
   * deshalb besitzt die Kopie keinen Schl�ssel.
   */
  protected Object clone() throws CloneNotSupportedException {
    Object o = super.clone();
    ((AddressRecord)o).setKey(null);
    return o;
  }
}
