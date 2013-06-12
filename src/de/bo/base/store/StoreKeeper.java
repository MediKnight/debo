package de.bo.base.store;

import java.util.*;

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
   * <code>StoreToolkit</code> f�r diesen "Keeper" festlegen.
   */
  public void setStoreToolkit(StoreToolkit storeToolkit);

  /**
   * Verwendeten <code>StoreToolkit</code> abfragen.
   */
  public StoreToolkit getStoreToolkit();

  /**
   * Eindeutigen Schl�ssel f�r ein Objekt erzeugen.
   *
   * @param object das Objekt, f�r den der Schl�ssel erzeugt werden soll
   * (meistens <code>this</code>)
   *
   * @return Schl�ssel
   */
  public Object createKey(Storable object);

  /**
   * Daten vom StoreKeeper beziehen.
   * 
   * @param object das Objekt, das die Daten erhalten soll
   * (meistens <code>this</code>)
   * @param data Ziel-Array
   * @param key Passender Schl�ssel
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean retrieve(Storable object,Object[] data,Object key);

  /**
   * Daten dem StoreKeeper hinzuf�gen.
   * 
   * @param object das Objekt, das die Daten liefern soll
   * (meistens <code>this</code>)
   * @param data Quell-Array
   * @param key Passender Schl�ssel
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean insert(Storable object,Object[] data,Object key);

  /**
   * Daten im StoreKeeper ersetzen.
   * 
   * @param object das Objekt, das die Daten liefern soll
   * (meistens <code>this</code>)
   * @param data Quell-Array
   * @param key Passender Schl�ssel
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean store(Storable object,Object[] data,Object key);

  /**
   * Daten aus dem StoreKeeper entfernen.
   * 
   * @param object das Objekt, das gel�scht werden soll
   * (meistens <code>this</code>)
   * @param key Passender Schl�ssel
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean delete(Storable object,Object key);

  /**
   * Liefert Enumeration aller Objekte vom Typ der Klasse des Objekts
   * <code>object</code>, deren Schl�ssel <code>identifier</code> mit
   * dem gegebenen Schl�ssel �bereinstimmt.
   *
   * @param object Quell-Objekttyp
   * @param identifier Name des Schl�ssels
   * @param key Schl�sselwert, der �bereinstimmen muss
   * @param order wenn <code>true</code>, wird Enumeration sortiert.
   */
  public <E extends Storable> Enumeration<E> getEnumeration(E object,
				    String identifier,
				    Object key,
				    boolean order);

  /**
   * Liefert Enumeration aller Objekte vom Typ der Klasse des Objekts
   * <code>object</code>, die die Bedingungen der gegeben Selektion
   * erf�llen.
   *
   * @param object Quell-Objekttyp
   * @param selection Selektion
   * @param order wenn <code>true</code>, wird Enumeration sortiert.
   *
   * @see Selection
   */
  public <E extends Storable> Enumeration<E> getEnumeration(E object,
				    Selection selection,
				    boolean order);
}
