package de.bo.office2;

import java.util.*;

import de.bo.base.store.StoreKeeper;

/**
 * Instanzen von Ableitungen dieser Klasse enthalten Mitarbeiter
 * (Eltern von Mitarbeiter).
 * <p>
 * Diese Klasse ist auch im semantischen Sinne abstrakt und enth�lt
 * keine Prim�rdaten.
 */

public abstract class EmployeeContainer extends AddressContainer
{
  /**
   * Liste der Mitarbeiter.
   */
  protected LinkedList employeeData;

  /**
   * Erzeugt Container mit Default-Storekeeper
   */
  protected EmployeeContainer() {
    super();

    employeeData = null;
  }

  /**
   * Erzeugt Container mit gegebenen Storekeeper.
   *
   * @param storeKeeper verwendetes Datensystem
   */
  protected EmployeeContainer(StoreKeeper<Bobo> storeKeeper) {
    super( storeKeeper );

    employeeData = null;
  }

  /**
   * Laden des Objekts.
   *
   * Bei Erfolg wird <code>employeeData</code> auf <code>null</code>
   * gesetzt, um korrekte Daten zu garantieren.
   */
  public boolean retrieve(Object key) {
    boolean b = super.retrieve( key );
    if ( b ) employeeData = null;
    return b;
  }

  /**
   * Liefert Liste von Mitarbeitern.
   *
   * Diese Liste wird bei Bedarf (d.h. wenn
   * <code>employeeData == null</code> gilt) geladen.
   */
  public List getEmployees() {
    if ( employeeData == null ) {
      if ( retrieveEmployees() )
	return employeeData;
      else
	return null;
    }
    return employeeData;
  }

  /**
   * Laden aller zum Container zugeh�rigen Mitarbeiter.
   *
   * @return <code>true</code> bei Erfolg
   */
  protected synchronized boolean retrieveEmployees() {
    employeeData = new LinkedList();
    Employee employee = new Employee();
    Enumeration e =
      employee.getEnumeration( getKeyIdentifier(), getKey(), true );
    if ( e == null )
      return false;

    while ( e.hasMoreElements() ) {
      employee = (Employee)e.nextElement();
      if ( this instanceof Person )
	employee.setParent( 0, this );
      if ( this instanceof Location )
	employee.setParent( 1, this );
      employeeData.add( employee );
    }

    return true;
  }
}
