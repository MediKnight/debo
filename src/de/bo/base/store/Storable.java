package de.bo.base.store;

/**
 * Ein Storeable Objekt ist ein Objekt, das durch einen StoreKeeper
 * verwaltet wird.
 * <p>
 * Gewöhnlicherweise ist ein Storable ein Datensatz einer Datenbank-Tabelle
 * oder ein Objekt aus einem Space.
 *
 * @see StoreKeeper
 */

public interface Storable
{
  /**
   * Setzen des aktuellen Storekeepers.
   */
  public void setStoreKeeper(StoreKeeper storeKeeper);

  /**
   * Liefert aktuellen Storekeeper.
   */
  public StoreKeeper getStoreKeeper();

  /**
   * Liefert Schlüssel des Objekts oder <code>null</code> bei Misserfolg.
   */
  public Object getKey();

  /**
   * Erzeugt neues Objekt im Storekeeper mit neuem Schlüssel.
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean create();

  /**
   * Laden des Objekts mit den Daten, die zum gegebenen Schlüssel passen.
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean retrieve(Object key);

  /**
   * Speichert aktuelles Objekt.
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean store();

  /**
   * Entfernt Objekt aus dem Storekeeper.
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean delete();

  /**
   * Liefert Anzahl der Spalten oder Attribute dieses Objekts.
   */
  public int getColumnCount();
}
