package de.bo.office2;

import java.util.*;

import de.bo.base.store.*;

/**
 * Instanzen von Ableitungen dieser Klasse enthalten Adressen.
 * <p>
 * Adressen sind in diesem Sinne auch Telefonnummern und/oder
 * EMails, Web-Pages u.a.
 * <p>
 * Diese Klasse ist auch im semantischen Sinne abstrakt und enthält
 * keine Primärdaten.
 *
 * @see AddressRecord
 */

public abstract class AddressContainer extends Bobo
{
  /**
   * Anzahl der verschiedenen Adress-Typen.
   */
  protected final static int ADDRESSTYPES = 5;

  /**
   * Listen der Adressen.
   */
  protected LinkedList[] addressData;

  /**
   * Erzeugt Container mit Default-Storekeeper
   */
  protected AddressContainer() {
    super();

    addressData = null;
  }

  /**
   * Erzeugt Container mit gegebenen Storekeeper.
   *
   * @param storeKeeper verwendetes Datensystem
   */
  protected AddressContainer(StoreKeeper storeKeeper) {
    super( storeKeeper );

    addressData = null;
  }

  /**
   * Liefert Liste von Adressen mit gegebenen Index
   * (im Bereich bis <code>ADDRESSTYPES</code>.
   *
   * Alle Listen werden bei Bedarf (d.h. wenn
   * <code>addressData == null</code> gilt) geladen.
   */
  public List getAddresses(int index) {
    if ( addressData == null ) {
      addressData = new LinkedList[ADDRESSTYPES];
      for ( int i=0; i<ADDRESSTYPES; i++ )
	addressData[i] = null;
    }
    if ( addressData[index] == null ) {
      if ( retrieveAddresses(index) )
	return addressData[index];
      else
	return null;
    }

    return addressData[index];
  }

  /**
   * Liefert <code>ADDRESSTYPES</code>.
   */
  public static final int getCount() {
    return ADDRESSTYPES;
  }

  /**
   * Liefert Adressen im üblichen Sinne.
   */
  public List getAddresses() {
    return getAddresses( 0 );
  }

  /**
   * Liefert Telefonnummern.
   */
  public List getPhones() {
    return getAddresses( 1 );
  }

  /**
   * Liefert EMails.
   */
  public List getEMails() {
    return getAddresses( 2 );
  }

  /**
   * Liefert Web-Pages.
   */
  public List getWWWs() {
    return getAddresses( 3 );
  }

  /**
   * Liefert Bankverbindungen.
   */
  public List getBankAccounts() {
    return getAddresses( 4 );
  }

  /**
   * Laden des Objekts.
   *
   * Bei Erfolg wird <code>addressData</code> auf <code>null</code>
   * gesetzt, um korrekte Daten zu garantieren.
   */
  public boolean retrieve(Object key) {
    boolean b = super.retrieve( key );
    if ( b ) addressData = null;
    return b;
  }

  /**
   * Laden aller zum Container zugehörigen Adressen.
   *
   * @return <code>true</code> bei Erfolg
   */
  protected boolean retrieveAddresses() {
    addressData = new LinkedList[ADDRESSTYPES];

    for ( int i=0; i<ADDRESSTYPES; i++ )
      if ( !retrieveAddresses( i ) )
	return false;

    return true;
  }


  /**
   * Laden der zum Index zugehörigen Adressen.
   *
   * @return <code>true</code> bei Erfolg
   */
  protected synchronized boolean retrieveAddresses(int index) {
    AddressRecord address = createAddress( index );
    if ( address != null )
      address.setStoreKeeper( getStoreKeeper() );

    addressData[index] = new LinkedList();

    Enumeration e =
      address.getEnumeration( getKeyIdentifier(), getKey(), true );
    if ( e == null )
      return false;

    while ( e.hasMoreElements() )
      addressData[index].add( e.nextElement() );

    return true;
  }

  /**
   * Erzeugen eines Adressen-Objekts zum passenden Index.
   */
  protected static AddressRecord createAddress(int index) {
    AddressRecord record = null;
    switch ( index ) {
    case 0:
      record = new Address();
      break;
    case 1:
      record = new Telefon();
      break;
    case 2:
      record = new EMail();
      break;
    case 3:
      record = new WWW();
      break;
    case 4:
      record = new BankAccount();
      break;
    }

    return record;
  }
}
