package de.baltic_online.base.store;

public abstract class AbstractStorable
  implements Storable
{
  protected static StoreKeeper<?> defaultStoreKeeper;

  protected Object[] data;
  protected StoreKeeper<? extends Storable> storeKeeper;

  /**
   * Erzeugung-Zustand des Objekts.
   *
   * Ein Objekt ist "created", wenn er durch die Funktion
   * <code>create</code> erzeugt wurde.
   * Ein Objekt ist solange "created", bis er mit der Funktion
   * <code>store</code> erfolgreich gespeichert wird.
   *
   * Per Default ist ein Objekt nicht "created".
   *
   * <pre>
   * created | deleted | Bedeutung
   * -----------------------------
   *  false  |  false  | Trifft zu, wenn ein vorausgegangendes "retrieve"
   *         |         | erfolgreich war. Objekt ist "g�ltig".
   *  false  |  true   | Dies ist der Initialisierungszustand. Er tritt auch
   *         |         | ein, wenn ein ein vorausgegangendes "delete"
   *         |         | erfolgreich war. Objekt ist "ung�ltig".
   *  true   |  false  | Trifft zu, wenn ein vorausgegangendes "create"
   *         |         | erfolgreich war. Objekt ist "g�ltig".
   *  true   |  true   | Dieser Zustand darf gar nicht auftreten.
   * </pre>
   *
   * @see #create
   * @see #deleted
   * @see #store
   */
  protected boolean created;

  /**
   * L�sch-Zustand des Objekts.
   *
   * Ein Objekt ist "deleted", wenn er ung�ltig ist.
   * Ein Objekt wird ung�ltig, wenn er mit der Funktion
   * <code>delete</code> erfolgreich gel�scht wurde.
   * Ein Objekt ist solange "deleted", bis er mit der Funktion
   * <code>retrieve</code> einen g�ltigen Zustand erh�lt.
   *
   * Per Default ist ein Objekt ung�ltig, d.h. "deleted".
   *
   * @see #delete
   * @see #created
   * @see #retrieve
   */
  protected boolean deleted;

  /**
   * Setzen des Default-Storekeepers.
   */
  public static void setDefaultStoreKeeper(StoreKeeper<? extends Storable> storeKeeper) {
    defaultStoreKeeper = storeKeeper;
  }

  /**
   * Liefert Default-Storekeeper.
   */
  public static StoreKeeper<? extends Storable> getDefaultStoreKeeper() {
    return defaultStoreKeeper;
  }

  /**
   * Erzeugt Storable mit dem durch <code>setDefaultStoreKeeper</code>
   * gesetzten Objekt.
   */
  public AbstractStorable() {
    this( null );
  }

  /**
   * Erzeugt Storable mit gegebenen Storekeeper.
   */
  public AbstractStorable(StoreKeeper<? extends Storable> storeKeeper) {
    this.storeKeeper = (storeKeeper==null) ?
      defaultStoreKeeper : storeKeeper;

    data = null;
    created = false;
    deleted = true;
  }

  /**
   * Setzen des aktuellen Storekeepers.
   */
  public void setStoreKeeper(StoreKeeper<? extends Storable> storeKeeper) {
    this.storeKeeper = storeKeeper;
  }

  /**
   * Liefert aktuellen Storekeeper.
   */
  public StoreKeeper<? extends Storable> getStoreKeeper() {
    return storeKeeper;
  }

  /**
   * Liefert aktuellen Store-Toolkit (das vom Storekeeper).
   */
  public StoreToolkit getStoreToolkit() {
    return storeKeeper.getStoreToolkit();
  }

  /**
   * Erzeugt neuen Schl�ssel f�r das Objekt.
   *
   * @return <code>true</code> bei Erfolg
   */
  protected boolean createKey() {
    Object key = storeKeeper.createKey( this );
    if ( key != null ) {
      setKey( key );
      return true;
    }
    return false;
  }

  /**
   * Erzeugt neues Objekt im Storekeeper mit neuem Schl�ssel.
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean create() {
    if ( created ) return false;
    if ( createKey() ) {
      created = true;
      deleted = false;
      return true;
    }
    return false;
  }

  /**
   * Laden des Objekts mit den Daten, die zum gegebenen Schl�ssel passen.
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean retrieve(Object key) {
    if ( created ) return false;

    if ( data == null )
      data = new Object[getColumnCount()];

    if ( storeKeeper.retrieve( this, data, key ) ) {
      deleted = false;
      return true;
    }

    data = null;
    return false;
  }

  /**
   * Speichert aktuelles Objekt.
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean store() {
    // gel�schte Objekte k�nnen nicht gespeichert werden!
    if ( deleted ) return false;

    Object key = getKey();

    if ( created ) {
      if ( !storeKeeper.insert( this, data, key ) )
	return false;

      created = false;
    }
    else {
      if ( !storeKeeper.store( this, data, key ) )
	return false;
    }

    return true;
  }

  /**
   * Entfernt Objekt aus dem Storekeeper.
   *
   * @return <code>true</code> bei Erfolg
   */
  public boolean delete() {
    // gel�schte Objekte k�nnen nicht noch einmal gel�scht werden!
    if ( deleted ) return false;

    if ( created || storeKeeper.delete( this, getKey() ) ) {
      created = false;
      deleted = true;
      data = null;
      return true;
    }

    return false;
  }

  /**
   * Setzen des Prim�rschl�ssels.
   */
  protected abstract void setKey(Object priKey);

  /**
   * Liefert Schl�ssel des Objekts oder <code>null</code> bei Misserfolg.
   */
  public abstract Object getKey();

  /**
   * Liefert Anzahl der Spalten oder Attribute dieses Objekts.
   */
  public abstract int getColumnCount();
}
