package de.bo.office2;

import java.util.*;

import de.bo.base.store.StoreKeeper;

/**
 * Baltic-Online Geschäftsobjekt <b>Mitarbeiter</b>.
 * <p>
 * Mitarbeiter sind konkret, aber semantisch abstrakt.
 * Ein Mitarbeiter ist semantisch eine Person, aber eine Person kann
 * Mitarbeiter von verschiedenen Standorten sein.
 * <p>
 * Daher besitzt ein Mitarbeiter genau zwei Eltern-Objekte
 * (Person und Standort) von denen beide existieren müssen.
 *
 * @see Company
 * @see Employee
 * @see EmployeeContainer
 */

public class Employee extends AddressContainer
{

  /**
   * Erzeugt Mitarbeiter mit Default-Storekeeper.
   */
  public Employee() {
    super();
  }

  /**
   * Erzeugt Mitarbeiter mit gegebenen Storekeeper.
   */
  public Employee(StoreKeeper storeKeeper) {
    super( storeKeeper );
  }

  /**
   * Liefert zugehörige Person.
   */
  public Person getPerson() {
    return (Person)getParent( 0 );
  }

  /**
   * Liefert zugehörigen Standort.
   */
  public Location getLocation() {
    return (Location)getParent( 1 );
  }

  /**
   * Erzeugt Eltern-Objekt zum gegebenen Index (im Bereich von
   * <code>getParentCount()</code>).
   */
  protected Bobo createParent(int index) {
    Bobo record = null;
    switch ( index ) {
    case 0:
      record = new Person();
      break;
    case 1:
      record = new Location();
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

  /**
   * Liefert Anzahl der "Eltern".
   *
   * @return <code>2</code>
   */
  public int getParentCount() {
    return 2;
  }

  public String getOffice() {
    return getStoreToolkit().objectToString( data[3] );
  }
  public String getFunction() {
    return getStoreToolkit().objectToString( data[4] );
  }
  public String getRemark() {
    return getStoreToolkit().objectToString( data[5] );
  }

  public String toString() {
    return getOffice()+" "+getFunction();
  }

  public void set(String office,String function) {
    set( office, function, "" );
  }
  public void set(String office,String function,String remark) {
    setOffice( office );
    setFunction( function );
    setRemark( remark );
  }

  public void setOffice(String office) {
    data[3] = getStoreToolkit().stringToObject( office );
  }
  public void setFunction(String function) {
    data[4] = getStoreToolkit().stringToObject( function );
  }
  public void setRemark(String remark) {
    data[5] = getStoreToolkit().stringToObject( remark );
  }

  public String getIdentifier() {
    return "mitarbeiter";
  }
  public String getKeyIdentifier() {
    return "mid";
  }

  public String[] getAttributes() {
    return new String[] { "mid", "pid", "sid",
			    "abteilung", "funktion",
			    "bemerkung" };
  }
  public int getColumnCount() {
    return 6;
  }
}
