package de.bo.base.store2;

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
   * Liefert Schlüssel des Objekts.
   */
  public Object getKey();

  /**
   * Laden eines Objekts zum passenden Schlüssel.
   */
  public void retrieve(Object key)
    throws StoreException;

  /**
   * Speichert aktuelles Objekt.
   */
  public void store()
    throws StoreException;

  /**
   * Entfernt Objekt aus dem Storekeeper.
   */
  public void delete()
    throws StoreException;
}
