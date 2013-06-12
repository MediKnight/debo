package de.bo.base.store2;

import java.util.*;

import de.bo.base.store2.sql.SQLRecord;

/**
 * Ein StoreKeeper ("Lagerhalter") ist ein Objekt, das andere Objekte
 * lagern und liefern kann.
 * <p>
 * Ein StoreKeeper kann aber keine Objekte erzeugen (das m�ssen die
 * Objekte schon selbst tun), sondern kann neuen Objekten nur
 * einen Schl�ssel liefern.
 * <p>
 * Typische Beispiele f�r einen "StoreKeeper" ist eine Tabelle einer
 * Datenbank, eine Datenbank selbst oder ein "Java-Space".
 * <p>
 * Objekte, die von einem StoreKeeper verwaltet werden sollen, m�ssen
 * das Interface <code>Storable</code> implementieren.
 *
 * @see Storable
 */

public interface StoreKeeper
{
  /**
   * Eindeutigen Schl�ssel f�r ein Objekt erzeugen.
   *
   * @param object das Objekt, f�r den der Schl�ssel erzeugt werden soll
   * (meistens <code>this</code>)
   *
   * @return Schl�ssel
   */
  public Object createKey(Storable object)
    throws StoreException;

  /**
   * Daten vom StoreKeeper beziehen.
   * 
   * @param object das Objekt, das die Daten erhalten soll
   * (meistens <code>this</code>)
   * @param key Passender Schl�ssel
   *
   * @return Array
   */
  public Object[] retrieve(Storable object,Object key)
    throws StoreException;

  /**
   * Daten dem StoreKeeper hinzuf�gen.
   * 
   * @param object das Objekt, das die Daten liefern soll
   * (meistens <code>this</code>)
   * @param data Quell-Array
   * @param key Neuer Schl�ssel (kann <tt>null</tt> sein).
   */
  public void insert(Storable object,Object[] data,Object newKey)
    throws StoreException;

  /**
   * Daten im StoreKeeper ersetzen.
   * 
   * @param object das Objekt, das die Daten liefern soll
   * (meistens <code>this</code>)
   * @param data Quell-Array
   * @param key Passender Schl�ssel
   */
  public void store(Storable object,Object[] data,Object key)
    throws StoreException;

  /**
   * Daten aus dem StoreKeeper entfernen.
   * 
   * @param object das Objekt, das gel�scht werden soll
   * (meistens <code>this</code>)
   * @param key Passender Schl�ssel
   */
  public void delete(Storable object,Object key)
    throws StoreException;


  /**
   * Liefert Collection aller Objekte vom Typ der Klasse des Objekts
   * <code>object</code>, die die Bedingungen der gegeben Selektion
   * erf�llen.
   *
   * @param object Quell-Objekttyp
   * @param selection Selektion
   *
   * @see Selection
   */
  public <E extends SQLRecord> Collection<E> retrieve(E object,Selection selection)
    throws StoreException;

  public <E extends SQLRecord> Collection<E> retrieve(E object,
			     Selection selection,
			     ObjectFilter filter)
    throws StoreException;
}
