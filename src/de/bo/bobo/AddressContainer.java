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
 * Diese Adressen können direkt (benannt) behandelt werden oder
 * indirekt per Index.
 * <p>
 * Diese Klasse ist auch im semantischen Sinne abstrakt und enthält
 * keine Primärdaten.
 *
 * @see AddressRecord
 * @see #addressClasses
 */

public abstract class AddressContainer extends Bobo
{

  /**
   * Array der möglichen Adressen-Klassen. Dieses Array ist die zentrale
   * Information (und die einzige, die benötigt wird) zur indizierten
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
  protected static Class[] addressClasses = {
    Address.class,
    Telefon.class,
    EMail.class,
    WWW.class,
    BankAccount.class
  };

  /**
   * Der Inhalt des Containers.
   */
  protected Collection[] addressData;

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
   * Der Rückgabe-Typ ist genau, d.h. bei einem Index von <tt>1</tt>
   * (Telefon) ist der Rückgabetyp <tt>Telefon[]</tt>.
   * 
   * @param index Index des internen Adressen-Arrays
   */
  public AddressRecord[] getAddresses(int index)
    throws StoreException {

    if ( addressData == null )
      retrieveAddresses();

    // Diese Zeile erzeugt den genauesten Array-Typ, d.h. der Rückgabetyp
    // ist abhängig vom index.
    return (AddressRecord[])addressData[index].
      toArray((Object[])Array.newInstance(getAddressClass(index),0));
  }

  /**
   * Liefert <code>addressClasses.length</code>.
   *
   * @return <code>addressClasses.length</code>
   */
  public static final int getCount() {
    return addressClasses.length;
  }

  /**
   * Liefert Adressen im üblichen Sinne (nur Lesezugriff).
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
   * Laden aller zum Container zugehörigen Adressen.
   */
  protected void retrieveAddresses()
    throws StoreException {

    int n = getCount();
    addressData = new Collection[n];

    for ( int i=0; i<n; i++ )
      addressData[i] = retrieveAddresses(i);
  }


  /**
   * Laden der zum Index zugehörigen Adressen.
   * <p>
   * Diese Methode wird im Bedarfsfall von allen Zugriffsfunktionen
   * aufgerufen und beinhaltet die Datenbanklogik.
   *
   * @param Index des internen Adressen-Arrays
   * @return gelieferte Adressen in Form einer <tt>Collection</tt>
   * @exception StoreException bei Datenbankfehler
   */
  protected Collection retrieveAddresses(int index)
    throws StoreException {

    AddressRecord address = createAddress(index);
    SQLBase base = (SQLBase)getStoreKeeper();
    address.setStoreKeeper(base);

    Selection sel = new SQLSelection(base.getSQLToolkit(),
				     parentKeyIdentifier,
				     getKey());

    Collection coll = base.retrieve(address,sel);

    for ( Iterator it=coll.iterator(); it.hasNext(); )
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
   * Liefert Adressen-Klasse zum zugehörigen Index.
   *
   * @param index Index des internen Adressen-Arrays
   * @return <tt>addressClasses[index]</tt>
   *
   * @see #getClassIndex(AddressRecord)
   */
  public static Class getAddressClass(int index) {
    return addressClasses[index];
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
    Class ac = address.getClass();
    for ( int i=0; i<addressClasses.length; i++ )
      if ( ac.equals(addressClasses[i]) )
	return i;

    throw new Error("Unknown AddressRecord type");
  }

  /**
   * Fügt Adresse im üblichen Sinne dem Container zu.
   *
   * @param address Adresse, die hinzugefügt werden soll.
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void addAddress(Address address)
    throws StoreException {

    addAddressRecord(address);
  }

  /**
   * Entfernt Adresse im üblichen Sinne aus dem Container.
   *
   * @param address Adresse, die entfernt werden soll.
   * @see #removeAddressRecord(int,AddressRecord)
   */
  public void removeAddress(Address address)
    throws StoreException {

    removeAddressRecord(address);
  }

  /**
   * Fügt Telefon dem Container zu.
   *
   * @param telefon Telefon-Record, das hinzugefügt werden soll.
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
   * Fügt EMail dem Container zu.
   *
   * @param email EMail-Record, das hinzugefügt werden soll.
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
   * Fügt WWW dem Container zu.
   *
   * @param www WWW-Record, das hinzugefügt werden soll.
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
   * Fügt Bankverbindung dem Container zu.
   *
   * @param account Bankverbindung, die hinzugefügt werden soll.
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
   * Fügt die gegebene Adresse dem Container zu und ruft
   * <tt>address.store()</tt> auf.
   *
   * @param address Adresse, die hinzugefügt wird
   * @exception StoreException, falls die Adresse schon einen
   * gültigen Schlüssel besitzt
   *
   * @see #removeAddressRecord(AddressRecord)
   */
  public void addAddressRecord(AddressRecord address)
    throws StoreException {

    addAddressRecord(getClassIndex(address),address);
  }

  /**
   * Fügt die gegebene Adresse dem Container zu und ruft
   * <tt>address.store()</tt> auf.
   *
   * @deprecated ersetzt durch {@link #addAddressRecord(AddressRecord)}
   *
   * @param index Index des internen Adressen-Arrays
   * @param address Adresse, die hinzugefügt wird
   * @exception StoreException, falls die Adresse schon einen
   * gültigen Schlüssel besitzt
   *
   * @see #removeAddressRecord(int,AddressRecord)
   */
  public void addAddressRecord(int index,AddressRecord address)
    throws StoreException {

    if ( address.getKey() != null )
      throw new StoreException(address+ALLREADY_EXISTS);

    address.setStoreKeeper(getStoreKeeper());
    if ( addressData[index] == null )
      retrieveAddresses(index);

    addressData[index].add(address);
    setParent(address);
    address.store();
  }

  /**
   * Kopiert die gegebene Adresse (ohne Schlüsselwert) und addiert
   * die Kopie zum Container.
   * <p>
   * Die gegebene Adresse muss im Container enthalten sein.
   * Die neue Adresse besitzt einen gültigen Schlüssel.
   *
   * @param address Adresse, die kopiert wird
   * @return neue Adresse
   * @exception StoreException, falls die Adresse keinen
   * gültigen Schlüssel besitzt
   */
  public AddressRecord cloneAddressRecord(AddressRecord address)
    throws StoreException {

    int index = getClassIndex(address);

    if ( address.getKey() == null )
      throw new StoreException(address+NOT_CREATED);

    if ( addressData[index] == null )
      retrieveAddresses(index);

    if ( !addressData[index].contains(address) )
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
   * Entfernt die gegebene Adresse aus dem Container und löscht
   * diese.
   *
   * @param address Adresse, die gelöscht wird
   * @exception StoreException, falls die Adresse keinen
   * gültigen Schlüssel besitzt
   *
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void removeAddressRecord(AddressRecord address)
    throws StoreException {

    removeAddressRecord(getClassIndex(address),address);
  }

  /**
   * Entfernt die gegebene Adresse aus dem Container und löscht
   * diese.
   *
   * @deprecated ersetzt durch {@link #removeAddressRecord(AddressRecord)}
   *
   * @param index Index des internen Adressen-Arrays
   * @param address Adresse, die gelöscht wird
   * @exception StoreException, falls die Adresse keinen
   * gültigen Schlüssel besitzt
   *
   * @see #addAddressRecord(int,AddressRecord)
   */
  public void removeAddressRecord(int index,AddressRecord address)
    throws StoreException {

    if ( address.getKey() == null )
      throw new StoreException(address+NOT_CREATED);

    if ( addressData[index] == null )
      retrieveAddresses(index);

    if ( !addressData[index].contains(address) )
      throw new StoreException(address+NO_MEMBER_OF+this);

    addressData[index].remove(address);
    address.delete();
  }

  /**
   * Verschieben der gegebenen Adresse in einen anderen Container.
   *
   * @param destContainer Ziel-Container
   * @param address Adresse, die verschoben wird
   * @exception StoreException, falls die Adresse keinen
   * gültigen Schlüssel besitzt
   */
  public synchronized void moveAddressRecord(AddressContainer destContainer,
					     AddressRecord address)
    throws StoreException {

    int index = getClassIndex(address);
    removeAddressRecord(index,address);

    // Löschen der Parent-Beziehungen
    for ( int i=0; i<3; i++ )
      address.setParent(0,null);

    destContainer.addAddressRecord(index,address);
  }

    synchronized void deleteAddresses(int index)
	throws StoreException {

	AddressRecord[] ar = getAddresses(index);
	for ( int i=0; i<ar.length; i++ )
	    ar[i].delete();
    }

    synchronized void deleteAddresses()
	throws StoreException {

	// Lösche alle Adressen
	for ( int i=0; i<addressClasses.length; i++ )
	    deleteAddresses(i);

	addressData = null;
    }
}
