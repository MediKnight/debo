package de.bo.base.store;

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
   * <code>StoreToolkit</code> für diesen "Keeper" festlegen.
   */
  public void setStoreToolkit(StoreToolkit storeToolkit);

  /**
   * Verwendeten <code>StoreToolkit</code> abfragen.
   */
  public StoreToolkit getStoreToolkit();

  /**
   * Eindeutigen Schlüssel für ein Objekt erzeugen.
   *
   * @param object das Objekt, für den der Schlüssel erzeugt werden soll
   * (meistens <code>this</code>)
   *
   * @return Schlüssel
   */
  public Object createKey(Storable object);

  /**
   * Daten vom StoreKeeper beziehen.
   * 
   * @param object das Objekt, das die Daten erhalten soll
   * (meistens <code>this</code>)
   * @param data Ziel-Array
   * @param key Passender Schlüssel
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean retrieve(Storable object,Object[] data,Object key);

  /**
   * Daten dem StoreKeeper hinzufügen.
   * 
   * @param object das Objekt, das die Daten liefern soll
   * (meistens <code>this</code>)
   * @param data Quell-Array
   * @param key Passender Schlüssel
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
   * @param key Passender Schlüssel
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean store(Storable object,Object[] data,Object key);

  /**
   * Daten aus dem StoreKeeper entfernen.
   * 
   * @param object das Objekt, das gelöscht werden soll
   * (meistens <code>this</code>)
   * @param key Passender Schlüssel
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean delete(Storable object,Object key);

  /**
   * Liefert Enumeration aller Objekte vom Typ der Klasse des Objekts
   * <code>object</code>, deren Schlüssel <code>identifier</code> mit
   * dem gegebenen Schlüssel übereinstimmt.
   *
   * @param object Quell-Objekttyp
   * @param identifier Name des Schlüssels
   * @param key Schlüsselwert, der übereinstimmen muss
   * @param order wenn <code>true</code>, wird Enumeration sortiert.
   */
  public Enumeration getEnumeration(Storable object,
				    String identifier,
				    Object key,
				    boolean order);

  /**
   * Liefert Enumeration aller Objekte vom Typ der Klasse des Objekts
   * <code>object</code>, die die Bedingungen der gegeben Selektion
   * erfüllen.
   *
   * @param object Quell-Objekttyp
   * @param selection Selektion
   * @param order wenn <code>true</code>, wird Enumeration sortiert.
   *
   * @see Selection
   */
  public Enumeration getEnumeration(Storable object,
				    Selection selection,
				    boolean order);
}
