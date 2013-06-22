package de.baltic_online.office2;

import java.util.*;

import de.baltic_online.base.store.*;

/**
 * Instanzen von Ableitungen dieser Klasse enthalten Adressen.
 * <p>
 * Adressen sind in diesem Sinne auch Telefonnummern und/oder
 * EMails, Web-Pages u.a.
 * <p>
 * Diese Klasse ist auch im semantischen Sinne abstrakt und enth�lt
 * keine Prim�rdaten.
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
  protected ArrayList<LinkedList<Bobo>> addressData;

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
  protected AddressContainer(StoreKeeper<? extends Bobo> storeKeeper) {
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
  public List<Bobo> getAddresses(int index) {
    if ( addressData == null ) {
      addressData = new ArrayList<LinkedList<Bobo>>(ADDRESSTYPES);
      for ( int i=0; i<ADDRESSTYPES; i++ )
	addressData.set(i, null);
    }
    if ( addressData.get(index) == null ) {
      if ( retrieveAddresses(index) )
	return addressData.get(index);
      else
	return null;
    }

    return addressData.get(index);
  }

  /**
   * Liefert <code>ADDRESSTYPES</code>.
   */
  public static final int getCount() {
    return ADDRESSTYPES;
  }

  /**
   * Liefert Adressen im �blichen Sinne.
   */
  public List<Bobo> getAddresses() {
    return getAddresses( 0 );
  }

  /**
   * Liefert Telefonnummern.
   */
  public List<Bobo> getPhones() {
    return getAddresses( 1 );
  }

  /**
   * Liefert EMails.
   */
  public List<Bobo> getEMails() {
    return getAddresses( 2 );
  }

  /**
   * Liefert Web-Pages.
   */
  public List<Bobo> getWWWs() {
    return getAddresses( 3 );
  }

  /**
   * Liefert Bankverbindungen.
   */
  public List<Bobo> getBankAccounts() {
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
   * Laden aller zum Container zugeh�rigen Adressen.
   *
   * @return <code>true</code> bei Erfolg
   */
  protected boolean retrieveAddresses() {
    addressData = new ArrayList<LinkedList<Bobo>>(ADDRESSTYPES);

    for ( int i=0; i<ADDRESSTYPES; i++ )
      if ( !retrieveAddresses( i ) )
	return false;

    return true;
  }


  /**
   * Laden der zum Index zugeh�rigen Adressen.
   *
   * @return <code>true</code> bei Erfolg
   */
  protected synchronized boolean retrieveAddresses(int index) {
    AddressRecord address = createAddress( index );
    if ( address != null )
      address.setStoreKeeper( getStoreKeeper() );

    addressData.set(index, new LinkedList<Bobo>());

    Enumeration<Bobo> e =
      address.getEnumeration( getKeyIdentifier(), getKey(), true );
    if ( e == null )
      return false;

    while ( e.hasMoreElements() )
      addressData.get(index).add( e.nextElement() );

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
