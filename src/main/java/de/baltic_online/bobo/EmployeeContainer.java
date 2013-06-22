package de.baltic_online.bobo;

import java.util.*;

import de.baltic_online.base.store2.*;
import de.baltic_online.base.store2.sql.*;

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
    protected Collection<Employee> employeeData;

    /**
     * Erzeugt Container mit Default-Storekeeper
     */
    public EmployeeContainer(String parentKeyIdentifier) {
	super(parentKeyIdentifier);

	employeeData = null;
    }

    /**
     * Erzeugt Container mit gegebenen Storekeeper.
     *
     * @param storeKeeper verwendetes Datensystem
     */
    public EmployeeContainer(StoreKeeper storeKeeper,
			     String parentKeyIdentifier) {
	super(storeKeeper,parentKeyIdentifier);

	employeeData = null;
    }

    /**
     * Laden des Objekts.
     *
     * Bei Erfolg wird <code>employeeData</code> auf <code>null</code>
     * gesetzt, um korrekte Daten zu garantieren.
     */
    public void retrieve(Object key)
	throws StoreException {

	super.retrieve(key);
	employeeData = null;
    }

    /**
     * Liefert Liste von Mitarbeitern.
     */
    public Employee[] getEmployees()
	throws StoreException {

	if ( employeeData == null )
	    retrieveEmployees();

	return (Employee[])employeeData.toArray(new Employee[0]);
    }

    /**
     * Laden aller zum Container zugeh�rigen Mitarbeiter.
     */
    protected void retrieveEmployees()
	throws StoreException {

	Employee employee = new Employee();
	SQLBase base = (SQLBase)getStoreKeeper();
	employee.setStoreKeeper(base);

	Selection sel = new SQLSelection(base.getSQLToolkit(),
					 parentKeyIdentifier,
					 getKey());

	employeeData = base.retrieve(employee,sel);
	
	for ( Iterator<Employee> it=employeeData.iterator(); it.hasNext(); ) {
	    employee = (Employee)it.next();
	    if ( this instanceof Person )
		employee.setParent(0,this);
	    if ( this instanceof Location )
		employee.setParent(1,this);
	}
    }

    /**
     * Einf�gen eines neuen Mitarbeiters, ohne nachtr�gliches Speichern
     * in der Datenbank.
     */
    void addEmployee(Employee employee)
	throws StoreException {

	if ( employeeData == null )
	    retrieveEmployees();

	employeeData.add(employee);
    }

    /**
     * Entfernes eines Mitarbeiters aus dem Container.
     */
    void removeEmployee(Employee employee)
	throws StoreException {

	if ( employeeData == null )
	    retrieveEmployees();

	if ( !employeeData.contains(employee) )
	    throw new StoreException(employee+NO_MEMBER_OF+this);

	employeeData.remove(employee);
    }

    synchronized void deleteEmployees()
	throws StoreException {

	// Delete all employee entries ...
	Employee[] e = getEmployees();
	for ( int i=0; i<e.length; i++ )
	    e[i].delete();

	// Reset employee data
	employeeData = null;
    }

    public synchronized void delete()
	throws StoreException {

	validateDelete();

	deleteEmployees();
	deleteAddresses();

	super.delete();
    }
}
