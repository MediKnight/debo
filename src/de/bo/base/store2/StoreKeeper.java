package de.bo.base.store2;

import java.util.*;

/**
 * Ein StoreKeeper ("Lagerhalter") ist ein Objekt, das andere Objekte
 * lagern und liefern kann.
 * <p>
 * Ein StoreKeeper kann aber keine Objekte erzeugen (das müssen die
 * Objekte schon selbst tun), sondern kann neuen Objekten nur
 * einen Schlüssel liefern.
 * <p>
 * Typische Beispiele für einen "StoreKeeper" ist eine Tabelle einer
 * Datenbank, eine Datenbank selbst oder ein "Java-Space".
 * <p>
 * Objekte, die von einem StoreKeeper verwaltet werden sollen, müssen
 * das Interface <code>Storable</code> implementieren.
 *
 * @see Storable
 */

public interface StoreKeeper
{
  /**
   * Eindeutigen Schlüssel für ein Objekt erzeugen.
   *
   * @param object das Objekt, für den der Schlüssel erzeugt werden soll
   * (meistens <code>this</code>)
   *
   * @return Schlüssel
   */
  public Object createKey(Storable object)
    throws StoreException;

  /**
   * Daten vom StoreKeeper beziehen.
   * 
   * @param object das Objekt, das die Daten erhalten soll
   * (meistens <code>this</code>)
   * @param key Passender Schlüssel
   *
   * @return Array
   */
  public Object[] retrieve(Storable object,Object key)
    throws StoreException;

  /**
   * Daten dem StoreKeeper hinzufügen.
   * 
   * @param object das Objekt, das die Daten liefern soll
   * (meistens <code>this</code>)
   * @param data Quell-Array
   * @param key Neuer Schlüssel (kann <tt>null</tt> sein).
   */
  public void insert(Storable object,Object[] data,Object newKey)
    throws StoreException;

  /**
   * Daten im StoreKeeper ersetzen.
   * 
   * @param object das Objekt, das die Daten liefern soll
   * (meistens <code>this</code>)
   * @param data Quell-Array
   * @param key Passender Schlüssel
   */
  public void store(Storable object,Object[] data,Object key)
    throws StoreException;

  /**
   * Daten aus dem StoreKeeper entfernen.
   * 
   * @param object das Objekt, das gelöscht werden soll
   * (meistens <code>this</code>)
   * @param key Passender Schlüssel
   */
  public void delete(Storable object,Object key)
    throws StoreException;


  /**
   * Liefert Collection aller Objekte vom Typ der Klasse des Objekts
   * <code>object</code>, die die Bedingungen der gegeben Selektion
   * erfüllen.
   *
   * @param object Quell-Objekttyp
   * @param selection Selektion
   *
   * @see Selection
   */
  public Collection retrieve(Storable object,Selection selection)
    throws StoreException;

  public Collection retrieve(Storable object,
			     Selection selection,
			     ObjectFilter filter)
    throws StoreException;
}
