package de.bo.bobo;

import java.util.*;

import de.bo.base.store2.*;

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
    super("mid");
  }

  /**
   * Erzeugt Mitarbeiter mit gegebenen Storekeeper.
   */
  public Employee(StoreKeeper storeKeeper) {
    super(storeKeeper,"mid");
  }

  /**
   * Liefert zugehörige Person.
   */
  public Person getPerson()
    throws StoreException {

    return (Person)getParent(0);
  }

  /**
   * Person zuordnen..
   */
  public void setPerson(Person person)
    throws StoreException {

    if ( person.getKey() == null )
      throw new StoreException(person+NOT_CREATED);

    setParent(0,person);
  }

  /**
   * Liefert zugehörigen Standort.
   */
  public Location getLocation()
    throws StoreException {

    return (Location)getParent(1);
  }

  /**
   * Standort zuordnen..
   */
  public void setLocation(Location location)
    throws StoreException {

    if ( location.getKey() == null )
      throw new StoreException(location+NOT_CREATED);

    setParent(1,location);
  }

  /**
   * Setzen der Eltern-Beziehung.
   *
   * Da ein Mitarbeiter-Datensatz der einzige mit zwei "Eltern" ist, die
   * beide besetzt werden müssen, ist die "Container-Regelung"
   * (Eltern-Datensatz verwaltet Kind-Datensatz) nur bedingt sinnvoll.
   * Deshalb ist diese Methode die "offizielle", um die Relationen
   * zu setzen und zu ändern.
   * (Die Methode muss nicht aufgerufen werden, wenn der Mitarbeiter-Datensatz
   * gelöscht werden soll).
   * <p>
   * Diese Methode ruft <pre>store()</pre> auf.
   *
   * @param person Person
   * @param location Standort
   */
  public void setParents(Person person,Location location)
    throws StoreException {

    if ( person == null || location == null )
      throw new NullPointerException();

    if ( getPerson() != null )
      getPerson().removeEmployee(this);

    if ( getLocation() != null )
      getLocation().removeEmployee(this);

    setParent(0,person);
    setParent(1,location);

    location.addEmployee(this);
    person.addEmployee(this);

    store();
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
      record.setStoreKeeper(getStoreKeeper());

    return record;
  }

  /**
   * Liefert "Eltern"-Schlüssel zum gegebenen Index (im Bereich von
   * <code>getParentCount()</code>).
   */
  protected Object getParentKey(int index) {
    return data[index+1];
  }

  protected void setParentKey(int index,Object key) {
    data[index+1] = key;
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
    return getString(3);
  }

  public String getFunction() {
    return getString(4);
  }

  public String getRemark() {
    return getString(5);
  }

  public String toString() {
    try {
      return getPerson()+" "+getLocation();
    }
    catch ( StoreException x ) {
      return getOffice()+" "+getFunction();
    }
  }

  public void set(String office,String function) {
    set(office,function,"");
  }

  public void set(String office,String function,String remark) {
    setOffice(office);
    setFunction(function);
    setRemark(remark);
  }

  public void setOffice(String office) {
    setString(3,office);
  }

  public void setFunction(String function) {
    setString(4,function);
  }

  public void setRemark(String remark) {
    setString(5,remark);
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

    public synchronized void delete()
	throws StoreException {

	validateDelete();

	if ( getPerson() != null )
	    getPerson().removeEmployee(this);

	if ( getLocation() != null )
	    getLocation().removeEmployee(this);

	deleteAddresses();

	super.delete();
    }
}
