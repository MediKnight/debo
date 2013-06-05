package de.bo.bobo;

import java.lang.reflect.*;

import java.util.*;

import de.bo.base.store2.*;
import de.bo.base.store2.sql.*;

/**
 * Instanzen von Ableitungen dieser Klasse enthalten Adressen.
 * <p>
 * Adressen sind in diesem Sinne auch Telefonnummern und/oder
 * EMails, Web-Pages u.a.
 * Diese Adressen k�nnen direkt (benannt) behandelt werden oder
 * indirekt per Index.
 * <p>
 * Diese Klasse ist auch im semantischen Sinne abstrakt und enth�lt
 * keine Prim�rdaten.
 *
 * @see AddressRecord
 * @see #addressClasses
 */

public abstract class AddressContainer extends Bobo
{

  /**
   * Array der m�glichen Adressen-Klassen. Dieses Array ist die zentrale
   * Information (und die einzige, die ben�tigt wird) zur indizierten
   * Bestimmung von Adressen.<p>
   * Implementierung:<p><pre>
   *   protected static Class[] addressClasses = {
   *     Address.class,
   *     Telefon.class,
   *     EMail.class,
   *     WWW.class,
   *     BankAccount.class
   *   };
   * </pre>
   */
  protected static ArrayList<Class<? extends AddressRecord>> addressClasses = new ArrayList<Class<? extends AddressRecord>>() {
    private static final long serialVersionUID = 1L;
  {
    add(Address.class);
    add(Telefon.class);
    add(EMail.class);
    add(WWW.class);
    add(BankAccount.class);
  }};

  /**
   * Der Inhalt des Containers.
   */
  protected ArrayList<Collection<AddressRecord>> addressData;

  /**
   * Bezeichner des "Eltern"-Keys.
   */
  protected String parentKeyIdentifier;

  /**
   * Erzeugt Container mit Default-Storekeeper
   *
   * @param parentKeyIdentifier Bezeichner des "Eltern"-Keys
   */
  protected AddressContainer(String parentKeyIdentifier) {
    super();

    this.parentKeyIdentifier = parentKeyIdentifier;
    addressData = null;
  }

  /**
   * Erzeugt Container mit gegebenen Storekeeper.
   *
   * @param storeKeeper verwendetes Datensystem
   * @param parentKeyIdentifier Bezeichner des "Eltern"-Keys
   */
  protected AddressContainer(StoreKeeper storeKeeper,
			     String parentKeyIdentifier) {
    super(storeKeeper);

    this.parentKeyIdentifier = parentKeyIdentifier;
    addressData = null;
  }

  /**
   * Liefert Array von Adressen mit gegebenen Index
   * <p>
   * Alle Listen werden bei Bedarf (d.h. wenn
   * <code>addressData == null</code> gilt) geladen.
   * <p>
   * Der R�ckgabe-Typ ist genau, d.h. bei einem Index von <tt>1</tt>
   * (Telefon) ist der R�ckgabetyp <tt>Telefon[]</tt>.
   * 
   * @param index Index des internen Adressen-Arrays
   */
  public AddressRecord[] getAddresses(int index)
    throws StoreException {

    if ( addressData == null )
      retrieveAddresses();

    // Diese Zeile erzeugt den genauesten Array-Typ, d.h. der R�ckgabetyp
    // ist abh�ngig vom index.
    return (AddressRecord[])addressData.get(index).
      toArray((Object[])Array.newInstance(getAddressClass(index),0));
  }

  /**
   * Liefert <code>addressClasses.length</code>.
   *
   * @return <code>addressClasses.length</code>
   */
  public static final int getCount() {
    return addressClasses.size();
  }

  /**
   * Liefert Adressen im �blichen Sinne (nur Lesezugriff).
   *
   * @return <tt>(Address[])getAddresses(0)</tt>
   */
  public Address[] getAddresses()
    throws StoreException {

    return (Address[])getAddresses(0);
  }

  /**
   * Liefert Telefonnummern (nur Lesezugriff).
   *
   * @return <tt>(Telefon[])getAddresses(1)</tt>
   */
  public Telefon[] getTelefons()
    throws StoreException {

    return (Telefon[])getAddresses(1);
  }

  /**
   * Liefert EMails (nur Lesezugriff).
   *
   * @return <tt>(EMail[])getAddresses(2)</tt>
   */
  public EMail[] getEMails()
    throws StoreException {

    return (EMail[])getAddresses(2);
  }

  /**
   * Liefert Web-Pages (nur Lesezugriff).
   *
   * @return <tt>(WWW[])getAddresses(3)</tt>
   */
  public WWW[] getWWWs()
    throws StoreException {

    return (WWW[])getAddresses(3);
  }

  /**
   * Liefert Bankverbindungen (nur Lesezugriff).
   *
   * @return <tt>(BankAccount[])getAddresses(4)</tt>
   */
  public BankAccount[] getBankAccounts()
    throws StoreException {

    return (BankAccount[])getAddresses(4);
  }

  /**
   * Laden des Objekts.
   *
   * <code>addressData</code> wird auf <code>null</code>
   * gesetzt, um korrekte Daten zu garantieren.
   */
  public void retrieve(Object key)
    throws StoreException {

    super.retrieve(key);
    addressData = null;
  }

  /**
   * Laden aller zum Container zugeh�rigen Adressen.
   */
  protected void retrieveAddresses()
    throws StoreException {

    int n = getCount();
    addressData = new ArrayList<Collection<AddressRecord>>(n);

    for ( int i=0; i<n; i++ )
      addressData.add(retrieveAddresses(i));
  }


  /**
   * Laden der zum Index zugeh�rigen Adressen.
   * <p>
   * Diese Methode wird im Bedarfsfall von allen Zugriffsfunktionen
   * aufgerufen und beinhaltet die Datenbanklogik.
   *
   * @param Index des internen Adressen-Arrays
   * @return gelieferte Adressen in Form einer <tt>Collection</tt>
   * @exception StoreException bei Datenbankfehler
   */
  protected Collection<AddressRecord> retrieveAddresses(int index)
    throws StoreException {

    AddressRecord address = createAddress(index);
    SQLBase base = (SQLBase)getStoreKeeper();
    address.setStoreKeeper(base);

    Selection sel = new SQLSelection(base.getSQLToolkit(),
				     parentKeyIdentifier,
				     getKey());

    Collection<AddressRecord> coll = base.retrieve(address,sel);

    for ( Iterator<AddressRecord> it=coll.iterator(); it.hasNext(); )
      setParent((AddressRecord)it.next());

    return coll;
  }

  /**
   * Diese Methode setzt den Eltern-Key der gegebenen Adresse.
   *
   * @param address gegebene Adresse
   */
  protected void setParent(AddressRecord address) {
    if ( this instanceof Employee )
      address.setParent(0,this);
    if ( this instanceof Person )
      address.setParent(1,this);
    if ( this instanceof Location )
      address.setParent(2,this);
  }

  /**
   * Erzeugen eines Adressen-Objekts zum passenden Index.
   *
   * @param index Index des internen Adressen-Arrays
   * @return Neue Instanz aller direkten Sub-Klassen des Typs
   * <tt>AddressRecord</tt>.
   */
  public static AddressRecord createAddress(int index) {
    try {
      return (AddressRecord)getAddressClass(index).newInstance();
    }
    catch ( Exception x ) {
      throw new Error(x.getMessage());
    }
  }

  /**
   * Liefert Adressen-Klasse zum zugeh�rigen Index.
   *
   * @param index Index des internen Adressen-Arrays
   * @return <tt>addressClasses[index]</tt>
   *
   * @see #getClassIndex(AddressRecord)
   */
  public static Class<? extends AddressRecord> getAddressClass(int index) {
    return addressClasses.get(index);
  }

  /**
   * Liefert Index zur gegebenen Adresse.
   *
   * @param address Adressen-Record
   * @return Index zu <tt>address</tt>
   *
   * @see #getAddressClass(int)
   */
  public static int getClassIndex(AddressRecord address) {
    Class<? extends AddressRecord> ac = address.getClass();
    for ( int i=0; i<addressClasses.size(); i++ )
      if ( ac.equals(addressClasses.get(i)) )
	return i;

    throw new Error("Unknown AddressRecord type");
  }

  /**
   * F�gt Adresse im �blichen Sinne dem Container zu.
   *
   * @param address Adresse, die hinzugef�gt werden soll.
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void addAddress(Address address)
    throws StoreException {

    addAddressRecord(address);
  }

  /**
   * Entfernt Adresse im �blichen Sinne aus dem Container.
   *
   * @param address Adresse, die entfernt werden soll.
   * @see #removeAddressRecord(int,AddressRecord)
   */
  public void removeAddress(Address address)
    throws StoreException {

    removeAddressRecord(address);
  }

  /**
   * F�gt Telefon dem Container zu.
   *
   * @param telefon Telefon-Record, das hinzugef�gt werden soll.
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void addTelefon(Telefon telefon)
    throws StoreException {

    addAddressRecord(telefon);
  }

  /**
   * Entfernt Telefon aus dem Container.
   *
   * @param address Telefon-Record, das entfernt werden soll.
   * @see #removeAddressRecord(int,AddressRecord)
   */
  public void removeTelefon(Telefon telefon)
    throws StoreException {

    removeAddressRecord(telefon);
  }

  /**
   * F�gt EMail dem Container zu.
   *
   * @param email EMail-Record, das hinzugef�gt werden soll.
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void addEMail(EMail email)
    throws StoreException {

    addAddressRecord(email);
  }

  /**
   * Entfernt EMail aus dem Container.
   *
   * @param email EMail-Record, das entfernt werden soll.
   * @see #removeAddressRecord(int,AddressRecord)
   */
  public void removeEMail(EMail email)
    throws StoreException {

    removeAddressRecord(email);
  }

  /**
   * F�gt WWW dem Container zu.
   *
   * @param www WWW-Record, das hinzugef�gt werden soll.
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void addWWW(WWW www)
    throws StoreException {

    addAddressRecord(www);
  }

  /**
   * Entfernt WWW aus dem Container.
   *
   * @param www WWW-Record, das entfernt werden soll.
   * @see #removeAddressRecord(int,AddressRecord)
   */
  public void removeWWW(WWW www)
    throws StoreException {

    removeAddressRecord(www);
  }

  /**
   * F�gt Bankverbindung dem Container zu.
   *
   * @param account Bankverbindung, die hinzugef�gt werden soll.
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void addBankAccount(BankAccount account)
    throws StoreException {

    addAddressRecord(account);
  }

  /**
   * Entfernt Bankverbindung aus dem Container.
   *
   * @param account Bankverbindung, die entfernt werden soll.
   * @see #removeAddressRecord(int,AddressRecord)
   */
  public void removeBankAccount(BankAccount account)
    throws StoreException {

    removeAddressRecord(account);
  }

  /**
   * F�gt die gegebene Adresse dem Container zu und ruft
   * <tt>address.store()</tt> auf.
   *
   * @param address Adresse, die hinzugef�gt wird
   * @exception StoreException, falls die Adresse schon einen
   * g�ltigen Schl�ssel besitzt
   *
   * @see #removeAddressRecord(AddressRecord)
   */
  public void addAddressRecord(AddressRecord address)
    throws StoreException {

    addAddressRecord(getClassIndex(address),address);
  }

  /**
   * F�gt die gegebene Adresse dem Container zu und ruft
   * <tt>address.store()</tt> auf.
   *
   * @deprecated ersetzt durch {@link #addAddressRecord(AddressRecord)}
   *
   * @param index Index des internen Adressen-Arrays
   * @param address Adresse, die hinzugef�gt wird
   * @exception StoreException, falls die Adresse schon einen
   * g�ltigen Schl�ssel besitzt
   *
   * @see #removeAddressRecord(int,AddressRecord)
   */
  public void addAddressRecord(int index,AddressRecord address)
    throws StoreException {

    if ( address.getKey() != null )
      throw new StoreException(address+ALLREADY_EXISTS);

    address.setStoreKeeper(getStoreKeeper());
    if ( addressData.get(index) == null )
      retrieveAddresses(index);

    addressData.get(index).add(address);
    setParent(address);
    address.store();
  }

  /**
   * Kopiert die gegebene Adresse (ohne Schl�sselwert) und addiert
   * die Kopie zum Container.
   * <p>
   * Die gegebene Adresse muss im Container enthalten sein.
   * Die neue Adresse besitzt einen g�ltigen Schl�ssel.
   *
   * @param address Adresse, die kopiert wird
   * @return neue Adresse
   * @exception StoreException, falls die Adresse keinen
   * g�ltigen Schl�ssel besitzt
   */
  public AddressRecord cloneAddressRecord(AddressRecord address)
    throws StoreException {

    int index = getClassIndex(address);

    if ( address.getKey() == null )
      throw new StoreException(address+NOT_CREATED);

    if ( addressData.get(index) == null )
      retrieveAddresses(index);

    if ( !addressData.get(index).contains(address) )
      throw new StoreException(address+NO_MEMBER_OF+this);

    try {
      AddressRecord newAddress = (AddressRecord)address.clone();
      addAddressRecord(newAddress);

      return newAddress;
    }
    catch ( CloneNotSupportedException x ) {
      x.printStackTrace(System.err);
      throw new Error("Programers Error");
    }
  }

  /**
   * Entfernt die gegebene Adresse aus dem Container und l�scht
   * diese.
   *
   * @param address Adresse, die gel�scht wird
   * @exception StoreException, falls die Adresse keinen
   * g�ltigen Schl�ssel besitzt
   *
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void removeAddressRecord(AddressRecord address)
    throws StoreException {

    removeAddressRecord(address);
  }

  /**
   * Entfernt die gegebene Adresse aus dem Container und l�scht
   * diese.
   *
   * @deprecated ersetzt durch {@link #removeAddressRecord(AddressRecord)}
   *
   * @param index Index des internen Adressen-Arrays
   * @param address Adresse, die gel�scht wird
   * @exception StoreException, falls die Adresse keinen
   * g�ltigen Schl�ssel besitzt
   *
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void removeAddressRecord(int index,AddressRecord address)
    throws StoreException {

    if ( address.getKey() == null )
      throw new StoreException(address+NOT_CREATED);

    if ( addressData.get(index) == null )
      retrieveAddresses(index);

    if ( !addressData.get(index).contains(address) )
      throw new StoreException(address+NO_MEMBER_OF+this);

    addressData.get(index).remove(address);
    address.delete();
  }

  /**
   * Verschieben der gegebenen Adresse in einen anderen Container.
   *
   * @param destContainer Ziel-Container
   * @param address Adresse, die verschoben wird
   * @exception StoreException, falls die Adresse keinen
   * g�ltigen Schl�ssel besitzt
   */
  public synchronized void moveAddressRecord(AddressContainer destContainer,
					     AddressRecord address)
    throws StoreException {

    removeAddressRecord(address);

    // L�schen der Parent-Beziehungen
    for ( int i=0; i<3; i++ )
      address.setParent(0,null);

    destContainer.addAddressRecord(address);
  }

    synchronized void deleteAddresses(int index)
	throws StoreException {

	AddressRecord[] ar = getAddresses(index);
	for ( int i=0; i<ar.length; i++ )
	    ar[i].delete();
    }

    synchronized void deleteAddresses()
	throws StoreException {

	// L�sche alle Adressen
	for ( int i=0; i<addressClasses.size(); i++ )
	    deleteAddresses(i);

	addressData = null;
    }
}
