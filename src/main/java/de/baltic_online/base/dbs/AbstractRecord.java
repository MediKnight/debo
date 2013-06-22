package de.baltic_online.base.dbs;

import java.sql.SQLException;

/**
 * Darstellung eines Datensatzes einer Tabelle.
 *
 * @see AbstractTable
 */

public abstract class AbstractRecord implements DBStoreable
{
  /**
   * Korrespondierendes Tabellen-Objekt.
   *
   * @see AbstractTable
   */
  protected AbstractTable table;

  /**
   * Abstrakte Darstellung des Datensatzes.
   */
  protected Object[] record;

  /**
   * Erzeugung-Zustand des Datensatzes.
   *
   * Ein Datensatz ist "created", wenn er durch die Funktion
   * <code>create</code> erzeugt wurde.
   * Ein Datensatz ist solange "created", bis er mit der Funktion
   * <code>store</code> erfolgreich gespeichert wird.
   *
   * Per Default ist ein Datensatz nicht "created".
   *
   * @see #create
   * @see #store
   */
  protected boolean created;

  /**
   * L�sch-Zustand des Datensatzes.
   *
   * Ein Datensatz ist "deleted", wenn er ung�ltig ist.
   * Ein Datensatz wird ung�ltig, wenn er mit der Funktion
   * <code>delete</code> erfolgreich gel�scht wurde.
   * Ein Datensatz ist solange "deleted", bis er mit der Funktion
   * <code>retrieve</code> einen g�ltigen Zustand erh�lt.
   *
   * Per Default ist ein Datensatz ung�ltig, d.h. "deleted".
   *
   * @see #delete
   * @see #retrieve
   */
  protected boolean deleted;

  /**
   * Erzeugt einen neuen (aber ung�ltigen) Datensatz mit der Verbindung
   * zur zugeh�rigen Tabelle.
   *
   * Der Konstruktor setzt <code>created</code> auf <code>false</code>
   * und <code>deleted</code> auf <code>true</code>
   *
   * @param table zugeh�rige Tabelle
   *
   * @see #created
   * @see #deleted
   */
  public AbstractRecord(AbstractTable table) {
    this.table = table;
    record = null;
    created = false;
    deleted = true;
  }

  /**
   * Speichert Datensatz, wenn g�ltig.
   *
   * Ist das Flag <code>created</code> gesetzt, dann wird auf die
   * korrespondierende Tabelle ein <pre>INSERT</pre>, sonst ein
   * <pre>UPDATE</pre> ausgef�hrt.
   *
   * Es kann nur dann gespeichert werden, wenn das Flag
   * <code>deleted</code> nicht gesetzt ist.
   *
   * Bei Erfolg wird <code>created</code> auf <code>false</code> gesetzt.
   *
   * @return <code>true</code> bei Erfolg
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   */
  public boolean store()
    throws SQLException {
    //    debugCall( "store()" );

    // gel�schte Objekte k�nnen nicht gespeichert werden!
    if ( deleted ) return false;

    // record f�llen ...
    makeDataAbstract();

    // Bei created table.insert() aufrufen ...
    if ( created ) {
      Object newKey = createKey();
      if ( !table.insert( newKey, record ) )
	return false;

      record[table.getKeyColumn()] = newKey;
      created = false;
    }
    // ... sonst table.store()
    else {
      if ( !table.store( getKey(), record ) )
	return false;
    }

    return true;
  }

  /**
   * L�schen des gegebenen Datensatzes.
   *
   * Wenn m�glich, wird der Datensatz aus der korrespondierenden Quelle
   * gel�scht.
   *
   * Das Flag <code>deleted</code> wird gesetzt, wenn das L�schen erfolgreich
   * war.
   * Ein Datensatz kann nicht gel�scht werden, wenn das Flag
   * <code>deleted</code> gesetzt ist.
   *
   * @return <code>true</code> bei Erfolg
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt, oder der Datensatz aufgrund relationaler Abh�ngigkeiten nicht
   * gel�scht werden kann.
   */
  public boolean delete()
    throws SQLException {
    //    debugCall( "delete()" );

    created = false;

    // gel�schte Objekte k�nnen nicht noch einmal gel�scht werden!
    if ( deleted ) return false;

    if ( !table.delete( getKey() ) )
      return false;

    deleted = true;

    return true;
  }

  /**
   * Liefert <tt>retrieve()</tt> zur�ck, ohne Datensatz auszulesen.
   * <p>
   * Diese Methode bezieht sich nicht auf die <tt>retrieve()</tt>-Methoden
   * mit Parameter.
   *
   * @see #retrieve()
   */
  public boolean canRetrieve()
    throws SQLException {
    return !created && !table.isEOD();
  }

  /**
   * Liest Datensatz.
   *
   * Liest Datensatz von zugeh�riger Tabelle entsprechend der
   * Datensatzposition in der Tabelle.
   *
   * Es kann nicht gelesen werden, wenn das Flag <code>created</code>
   * gesetzt ist.
   *
   * Bei Erfolg wird das Flag <code>deleted</code> gel�scht.
   *
   * @return <code>true</code> bei Erfolg
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   */
  public boolean retrieve()
    throws SQLException {
    //    debugCall( "retrieve()" );

    if ( created || table.isEOD() )
      return false;

    record = table.retrieve();
    if ( record != null ) {
      deleted = false;
      makeDataConcrete();
      return true;
    }

    return false;
  }

  /**
   * Liest Datensatz.
   *
   * Liest Datensatz von zugeh�riger Tabelle entsprechend des
   * gegebenen Schl�ssels.
   *
   * Es kann nicht gelesen werden, wenn das Flag <code>created</code>
   * gesetzt ist.
   *
   * Bei Erfolg wird das Flag <code>deleted</code> gel�scht.
   *
   * @param key Schl�ssel
   * @return <code>true</code> bei Erfolg
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   */
  public boolean retrieve(Object key)
    throws SQLException {
    //    debugCall( "retrieve("+key.toString()+")" );

    if ( created ) return false;

    record = table.retrieve( key );
    if ( record != null ) {
      deleted = false;
      makeDataConcrete();
      return true;
    }
    return false;
  }

  /**
   * Liest Datensatz.
   *
   * Liest Datensatz aus gegebenen Array.
   *
   * Es kann nicht gelesen werden, wenn das Flag <code>created</code>
   * gesetzt ist.
   *
   * Bei Erfolg wird das Flag <code>deleted</code> gel�scht.
   *
   * Es wird vorausgesetzt, dass die Quelldaten zum Datensatz passen.
   *
   * Diese Funktion erzeugt keine Exceptions und wird von
   * <code>TableConsumer</code> bevorzugt eingesetzt.
   *
   * @param data Quell-Daten
   * @return <code>true</code> bei Erfolg
   *
   * @see TableConsumer
   */
  public boolean retrieve(Object[] data) {
    //    debugCall( "retrieve("+data+")" );

    if ( created ) return false;

    record = data;
    if ( record != null ) {
      deleted = false;
      makeDataConcrete();
      return true;
    }
    return false;
  }

  /**
   * Erzeugt einen Datensatz.
   *
   * Erzeugt einen Datensatz in dem Sinne, dass das Array
   * <code>record</code> von der zugeh�rigen Tabelle gesetzt wird,
   * da diese weiss, wieviel Spalten der Datensatz besitzen soll.
   *
   * Ein Datensatz kann nicht erzeugt werden, wenn <code>created</code>
   * gesetzt ist.
   *
   * Bei Erfolg wird <code>created</code> auf <code>true</code> und
   * <code>deleted</code> auf <code>false</code> gesetzt.
   *
   * @return <code>true</code> bei Erfolg
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   */
  public boolean create()
    throws SQLException {
    //    debugCall( "create()" );

    if ( created ) return false;

    record = table.createRecord();

    created = true;
    deleted = false;

    return true;
  }

  /**
   * Liefert Schl�ssel zum Datensatz.
   *
   * Liefert Schl�ssel, wenn <code>deleted</code> auf <code>false</code>
   * ist, sonst <code>null</code>.
   *
   * @return zugeh�riger Schl�ssel
   */
  public Object getKey() {
    try {
      if ( record == null || deleted )
	return null;

      return record[table.getKeyColumn()];
    }
    catch ( SQLException x ) {
      return null;
    }
  }

//   public boolean match(String pattern) {
//     return match( pattern, true );
//   }
//   public boolean match(String pattern,boolean ignoreCase) {
//     for ( int i=0; i<record.length; i++ )
//       if ( record[i] != null ) {
// 	String txt = record[i].toString();
// 	String pat = pattern;
// 	if ( ignoreCase ) {
// 	  txt = txt.toLowerCase();
// 	  pat = pat.toLowerCase();
// 	}
// 	if ( Pattern.match( pat, txt ) ) return true;
//       }
//     return false;
//   }

  /**
   * Pr�ft Datensatz auf Inhalt.
   *
   * @param toFind Suchstring
   * @return <code>hasContents( toFind, false, true )</code>
   *
   * @see #hasContents(String,boolean,boolean)
   */
  public boolean hasContents(String toFind) {
    return hasContents( toFind, false, true );
  }

  /**
   * Pr�ft Datensatz auf Inhalt.
   *
   * Diese Funktion kann benutz werden, um alle Felder des Datensatzes
   * nach einem String zu durchsuchen.
   * Bei mehreren Datens�tzen ist es allerdings g�nstiger,
   * Selektionen auf die Tabelle anzuwenden und dann einen Consumer
   * auf die Tabelle loszulassen.
   *
   * @param toFind Suchstring
   * @param exact gesetzt, wenn <b>keine</b> Teilstring-Suche erfolgen soll.
   * @param ignoreCase gesetzt, wenn Gro�/Kleinschreibung nicht
   * ber�cksichtigt werden soll.
   * @return <code>true</code>, wenn Suchstring gefunden wurde.
   *
   * @see TableSelection
   * @see TableOrSelection
   * @see TableConsumer
   */
  public boolean hasContents(String toFind,boolean exact,
			     boolean ignoreCase) {
    if ( deleted ) return false;

    String text = ignoreCase ? toFind.toLowerCase() : toFind;

    for ( int i=0; i<record.length; i++ )
      if ( record[i] != null ) {
	String s = record[i].toString().trim();
	if ( ignoreCase ) s = s.toLowerCase();

	if ( exact && text.equals(s) )
	  return true;

	if ( !exact && s.indexOf(text) >= 0 )
	  return true;
      }
    return false;
  }

  /**
   * Liefert das Flag <code>created</code>.
   *
   * @return <code>created</code>
   */
  public boolean isCreated() {
    return created;
  }


  public void setCreated(boolean state) {
    created = state;
  }

  /**
   * Liefert das Flag <code>deleted</code>.
   *
   * @return <code>deleted</code>
   */
  public boolean isDeleted() {
    return deleted;
  }

  /**
   * Liefert eindeutigen Schl�ssel, wenn der Datensatz neu erzeugt
   * wird.
   *
   * Wie ein Schl�ssel erzeugt werden kann, h�ngt i.A. von dem verwendeten
   * DBMS ab. Derartige Abh�ngigkeiten k�nnen von der Klasse
   * <code>DBUtilities</code> kompensiert werden.
   *
   * Eine typische Implementierung:
   * <pre>
   * protected Object createKey()
   *   throws SQLException {
   *   return DBUtilities.createKey( table.getConnection(), "id" );
   * }
   * </pre>
   *
   * @return neuer Schl�ssel
   *
   * @see AbstractTable
   * @see DBUtilities
   */
  protected abstract Object createKey() throws SQLException;

  /**
   * �berf�hrt benannte (konkrete) Elemente eines Datensatzes in die
   * abstrakte Darstellung durch das Array <code>record</code>.
   *
   * Diese Funktion wird von <code>store</code> aufgerufen.
   *
   * Beispiel:
   * <pre>
   *  protected void makeDataAbstract() {
   *    record[1] = name;
   *    record[2] = firstName;
   *    record[3] = init;
   *    record[4] = title;
   *    record[5] = address;
   *    record[6] = DBUtilities.booleanToObject( customer );
   *    record[7] = DBUtilities.booleanToObject( supplier );
   *    record[8] = DBUtilities.booleanToObject( partner );
   *    record[9] = remark;
   *  }
   * </pre>
   *
   * @see #store
   * @see DBUtilities
   */
  protected abstract void makeDataAbstract();

  /**
   * Besetzt benannte (konkrete) Elemente eines Datensatzes aus den
   * abstrakten Daten des Arrays <code>record</code>.
   *
   * Diese Funktion wird von <code>retrieve</code> aufgerufen.
   * <pre>
   *  protected void makeDataConcrete() {
   *    name      = DBUtilities.objectToString( record[1] );
   *    firstName = DBUtilities.objectToString( record[2] );
   *    init      = DBUtilities.objectToString( record[3] );
   *    title     = DBUtilities.objectToString( record[4] );
   *    address   = DBUtilities.objectToString( record[5] );
   *    customer  = DBUtilities.objectToBoolean( record[6] );
   *    supplier  = DBUtilities.objectToBoolean( record[7] );
   *    partner   = DBUtilities.objectToBoolean( record[8] );
   *    remark    = DBUtilities.objectToString( record[9] );
   *  }
   * </pre>
   *
   * @see #retrieve
   * @see DBUtilities
   */
  protected abstract void makeDataConcrete();

  protected void debugCall(String msg) {
//    String s = "calling "+msg+" from ";
//    s += getClass().getName();
//    StdApplication.debugMsg( s );
  }
}
