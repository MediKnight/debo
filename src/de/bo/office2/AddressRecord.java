package de.bo.office2;

import de.bo.base.store.StoreKeeper;

/**
 * Adressen im üblichen Sinne, Telefonnummern, EMails, etc. sind Adressen.
 * <p>
 * In der BO-Datenbank bezieht sich jede Adresse entweder auf eine
 * Person, einen Mitarbeiter oder einen Standort (ausschliesslich).
 *
 * @see AddressContainer
 */

public abstract class AddressRecord extends Bobo
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
    super( storeKeeper );
  }

  /**
   * Liefert Namen des Primärschlüssels der Adresse.
   *
   * (i.A. Spaltenattribute des Primärschlüssels)
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
   * Liefert zugehörigen Mitarbeiter oder <code>null</code>, wenn
   * Adresse zu keinem Mitarbeiter gehört.
   */
  public Employee getEmployee() {
    return (Employee)getParent( 0 );
  }

  /**
   * Liefert zugehörige Person oder <code>null</code>, wenn
   * Adresse zu keiner Person gehört.
   */
  public Person getPerson() {
    return (Person)getParent( 1 );
  }

  /**
   * Liefert zugehörigen Standort oder <code>null</code>, wenn
   * Adresse zu keinem Standort gehört.
   */
  public Location getLocation() {
    return (Location)getParent( 2 );
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
      record.setStoreKeeper( getStoreKeeper() );

    return record;
  }

  /**
   * Liefert "Eltern"-Schlüssel zum gegebenen Index (im Bereich von
   * <code>getParentCount()</code>).
   */
  protected Object getParentKey(int index) {
    return data[index+1];
  }
}
