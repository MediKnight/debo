package de.bo.base.dbs;

import java.sql.*;
import java.util.Stack;
import java.util.Vector;

/**
 * Instanzen von Ableitungen dieser Klasse repr�sentieren Tabellen
 * einer Datenbank. Einzelne Datens�tze werden durch Objekte der
 * Klasse AbstractRecord dargestellt.
 *
 * Tabellen, die durch diese Klasse dargestellt werden sollen,
 * <b>m�ssen</b> einen Prim�rschl�ssel besitzen.
 *
 * @see AbstractRecord
 * @author S�nke M�ller-Lund
 */

public abstract class AbstractTable
  implements SelectionStack
{
  public final static int FIRST = -2;
  public final static int PREV = -1;
  public final static int NEXT = 1;
  public final static int LAST = 2;

  /**
   * Default-Connection.
   *
   * Diese Verbindungsinstanz wird benutzt, wenn der Konstruktor dieser Klasse
   * ohne Verbindungsinstanz aufgerufen wird (Default-Konstruktor).
   *
   * @see #setDefaultConnection
   * @see #AbstractTable()
   */
  protected static Connection defaultConnection;

  /**
   * Aktuelle Verbindung.
   *
   * Verbindungsinstanz zur Datenbank.
   *
   * @see #defaultConnection
   */
  protected Connection connection;

  /**
   * Instanzen dieser Klasse speichern nicht die gesamte Tabelle,
   * sondern nur die Werte der Prim�rschl�ssel.
   *
   * @see #getKey
   */
  protected Vector<Object> keyTable;

  /*
   * Cache f�r Zeilen der Tabelle.
   */
  // protected Vector cache;

  /**
   * Stack f�r angewandte Selektionen auf die Tabelle. Selektionen
   * nehmen Einfluss auf die Funktion <code>getWhereClause</code>.
   *
   * @see #getWhereClause
   */
  protected Stack<TableSelection> selection;

  /**
   * Attribut-Namen der Tabelle.
   */
  protected String[] attribute;

  /**
   * G�ltigkeits-Flag der Tabelle.
   *
   * Dieses Flag kennzeichnet, ob die Tabelle (bzgl. der Selektionen)
   * neu ausgelesen werden muss.
   *
   * @see #invalidate
   * @see #validate
   */
  protected boolean valid;

  /**
   * Anzahl der Datens�tze in der Tabelle bez�glich Selektionen.
   */
  protected int records;

  /**
   * Aktuelle Zeilenposition in der Tabelle.
   */
  protected int position;

  /**
   * Anzahl der Spalten in der Tabelle.
   */
  protected int columns;

  /**
   * Spaltenposition des Prim�rschl�ssels.
   */
  protected int keyColumn;

  /**
   * Setzen der Default-Verbindung.
   */
  public static void setDefaultConnection(Connection conn) {
    defaultConnection = conn;
  }

  /**
   * Erzeugen eines Tabellen-Objekts mit Default-Verbindung.
   *
   * Nach dem Erzeugen ist die Tabelle "ung�ltig".
   *
   * @see #valid
   * @see #invalidate
   * @see #validate
   */
  public AbstractTable() {
    this( null );
  }

  /**
   * Erzeugen eines Tabellen-Objekts mit gegebener Verbindung.
   *
   * Nach dem Erzeugen ist die Tabelle "ung�ltig".
   *
   * @see #valid
   * @see #invalidate
   * @see #validate
   */
  public AbstractTable(Connection connection) {
    this.connection = (connection!=null) ? connection : defaultConnection;

    keyTable = null;

    valid = false;

    records = position = columns = 0;
    keyColumn = -1;

    selection = new Stack<TableSelection>();

    attribute = null;
  }

  /**
   * Liefert die initiale Gr��e des Schl�ssel-Vektors (2048).
   *
   * @return initiale Gr��e des Schl�ssel-Vektors
   */
  protected int getInitKeyTableSize() {
    return 2048;
  }

  /**
   * Liefert die inkrementelle Gr��e des Schl�ssel-Vektors (1024).
   *
   * @return inkrementelle Gr��e des Schl�ssel-Vektors
   */
  protected int getIncrementKeyTableSize() {
    return 1024;
  }

  /**
   * Liefert die Gr��e des Zeilen-Cache (256).
   *
   * @return Gr��e des Zeilen-Cache
   */
  protected int getCacheSize() {
    return 256;
  }

  /**
   * Instanzierbare Klassen m�ssen den Namen der Tabelle zur�ckgeben.
   *
   * @return Tabellen-Name
   */
  public abstract String getIdentifier();

  /**
   * Instanzierbare Klassen m�ssen den Namen des Prim�rschl�ssels zur�ckgeben.
   *
   * @return Name des Prim�rschl�ssels
   */
  public String getKeyIdentifier() {
    return "id";
  }

  /**
   * Liefert Verbindungsinstanz.
   *
   * @return <code>connection</code>
   *
   * @see #connection
   */
  public Connection getConnection() {
    return connection;
  }

  /**
   * Setzt das Flag <code>valid</code> auf <code>false</code>.
   *
   * @see #valid
   * @see #validate
   */
  public void invalidate() {
    valid = false;
  }

  /**
   * Auslesen des Inhalts der Tabelle, wenn diese "ung�ltig" ist.
   *
   * Ist die Tabelle hingegen g�ltig, wird sofort zur�ckgekehrt.
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #valid
   * @see #invalidate
   * @see #getQueryString
   */
  public void validate()
    throws SQLException {
    validate( null );
  }

  /**
   * Wird von <code>validate</code> oder von <code>consume</code> aufgerufen.
   * 
   * Die Funktion liest den Inhalt der Tabelle gem�� der angewandten
   * Selektionen aus und speichert die Schl�sselspalte.
   *
   * Ein Consumer kann in die Einlese-Schleife eingeh�ngt werden, so dass
   * die Zeilen der Tabelle direkt und schnell verwertet werden k�nnen.
   *
   * @param tc Consumer
   *
   * @see #validate()
   * @see TableConsumer
   */
  protected void validate(TableConsumer tc)
    throws SQLException {

    // nothing to do if valid
    if ( valid ) return;

    // neuen Schl�sssel-Vektor
    keyTable = new Vector<Object>( getInitKeyTableSize(),
			   getIncrementKeyTableSize() );

    // und neuen Cache erzeugen
    // cache = new Vector( getCacheSize() );

    // JDBC-Part ...
    String qs = getQueryString();
//    StdApplication.debugMsg( qs );
    Statement sm = connection.createStatement();
    ResultSet rset = sm.executeQuery( qs );
    ResultSetMetaData rsmd = rset.getMetaData();

    // Anzahl der Datens�tze und aktuelle Position zur�cksetzen ...
    records = position = 0;

    // Anzahl der Spalten (inkl. Schl�ssel) ermitteln ...
    columns = rsmd.getColumnCount();

    // Attribute speichern
    attribute = new String[columns];
    for ( int i=0; i<columns; i++ ) {
      attribute[i] = rsmd.getColumnName( i+1 );
    }

    // Spaltenposition des Prim�rschl�ssels suchen ...
    keyColumn = -1;
    for ( int i=0; i<columns; i++ )
      if ( attribute[i].equalsIgnoreCase( getKeyIdentifier() ) ) {
	keyColumn = i;
	break;
      }

    // No chance, wenn Prim�rschl�ssel nicht existiert!
    if ( keyColumn < 0 )
      throw new SQLException( "No primary key in table" );

    // Abfrageergebnisse Zeile f�r Zeile holen und Schl�ssel speichern ...
    // int cs = getCacheSize();
    int cs = 0;

    if ( tc != null )
      tc.startConsume();

    while ( rset.next() ) {
      Object[] oa = new Object[columns];
	for ( int i=0; i<columns; i++ )
	  oa[i] = rset.getObject( i+1 );

	keyTable.addElement( oa[keyColumn] );

	if ( tc != null )
	  tc.consume( oa, records );

// 	if ( records < cs ) {
// 	  RecordCache rc = new RecordCache( oa );
// 	  cache.addElement( rc );
// 	}
	records++;
    }

    // SQL-Statements abschlie�en
    // (records ist nun richtig gesetzt).
    rset.close();
    sm.close();

    if ( tc != null )
      tc.endConsume();

    // Tabelle ist nun g�ltig
    valid = true;
  }

  /**
   * Wie <code>validate</code>, jedoch wird das Neuladen der Tabelle
   * erzwungen, damit der Consumer seine Zeilen-Daten bekommen kann.
   *
   * @param tc Consumer
   *
   * @see #validate()
   * @see #validate(TableConsumer)
   * @see TableConsumer
   */
  public void consume(TableConsumer tc)
    throws SQLException {

    valid = false;
    validate( tc );
  }

  /**
   * Liefert den String des SQL "select"-Kommandos, der f�r die Auswahl
   * von Zeilen in <code>validate</code> und
   * <code>retrieve</code> verwendet wird.
   *
   * @return SQL "select"-String
   *
   * @see #validate
   * @see #retrieve(Object)
   * @see #getWhereClause
   * @see #getOrderClause
   */
  protected String getQueryString() {
    String s;
    StringBuffer sb = new StringBuffer();

    sb.append( "select * from " );
    sb.append( getIdentifier() );

    s = getWhereClause();
    if ( s.length() > 0 )
      sb.append( " where "+s );

    s = getOrderClause();
    if ( s.length() > 0 )
      sb.append( " order by "+s );

    return sb.toString();
  }

  /**
   * Liefert "where"-Klausel im "select"-String.
   *
   * Das Ergebnis ist ein jeweils mit "and" verkn�pfter String alle
   * verwendeten Selektionen.
   *
   * @return "where"-Klausel im "select"-String
   *
   * @see #getQueryString
   * @see TableSelection
   * @see #addSelection
   * @see #removeSelection
   */
  protected String getWhereClause() {
    String and = DBUtilities.getOperatorString(DBUtilities.AND);
    StringBuffer sb = new StringBuffer();
    for ( int i=0; i<selection.size(); i++ ) {
      TableSelection sel = selection.elementAt( i );
      if ( i > 0 ) sb.append( " "+and+" " );
      sb.append( sel.toString() );
    }
    return sb.toString();
  }

  /**
   * Liefert "order"-Klausel im "select"-String.
   *
   * Per Default liefert diese Funktion den leeren String.
   *
   * @return "order"-Klausel im "select"-String
   *
   * @see #getQueryString
   */
  protected String getOrderClause() {
    return "";
  }

  /**
   * Liefert die Anzahl der Datens�tze in der Tabelle.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @return Anzahl der Datens�tze in der Tabelle
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   */
  public int size()
    throws SQLException {
    validate();
    return records;
  }

  /**
   * Liefert den "end of data" Zustand in der Tabelle.
   *
   * Der "end of data" Zustand ist erreicht, wenn die aktuelle
   * Datensatzposition gr��er oder gleich der Anzahl der Datens�tze ist.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @return <code>position >= records</code>
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #position
   * @see #records
   */
  public boolean isEOD()
    throws SQLException {
    validate();
    return position >= records;
  }

  /**
   * Liefert die aktuelle Datensatz-Position in der Tabelle.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @return aktuelle Datensatz-Position
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #position
   * @see #setPosition
   */
  public int getPosition()
    throws SQLException {
    validate();
    return position;
  }

  /**
   * Setzen der aktuellen Datensatz-Position in der Tabelle.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @param position neue Datensatz-Position
   * @return <code>true</code>, falls die neue Datensatz-Position
   * von der aktuellen verschieden ist.
   *
   * @exception IndexOutOfBoundsException wenn <code>position</code>
   * negativ oder echt gr��er als die Anzahl der Datens�tze ist
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #position
   * @see #movePosition
   * @see #getPosition
   */
  public boolean setPosition(int pos)
    throws SQLException {

    validate();

    if ( pos < 0 || pos > records )
      throw new IndexOutOfBoundsException();

    boolean b = position != pos;
    position = pos;

    return b;
  }

  /**
   * Bewegen der aktuellen Datensatz-Position in der Tabelle.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @param dir Bewegungsrichtung
   * @return <code>true</code>, falls die neue Datensatz-Position
   * von der aktuellen verschieden ist.
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #position
   * @see #getPosition
   * @see #setPosition
   */
  public boolean movePosition(int dir)
    throws SQLException {

    validate();

    int newpos = position;
    if ( dir == FIRST )
      newpos = 0;
    if ( dir == LAST )
      newpos = records - 1;
    if ( dir == PREV )
      newpos--;
    if ( dir == NEXT )
      newpos++;

    if ( newpos >= records ) newpos--;
    if ( newpos < 0 ) newpos = 0;

    boolean b = newpos != position;
    position = newpos;

    return b;
  }

  /**
   * Liefert Wert des Prim�rschl�ssels in Abh�ngigkeit der aktuellen
   * Datensatz-Position.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @return <code>(isEOD()) ? null : keyTable.elementAt( position )</code>
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #position
   * @see #isEOD
   * @see #keyTable
   */
  public Object getKey()
    throws SQLException {
    validate();

    return (isEOD()) ? null : keyTable.elementAt( position );
  }

  /**
   * Liefert <code>true</code>, falls der gegebene Schl�ssel im
   * Schl�ssel-Vektor enthalten ist.
   *
   * Da Schl�ssel potentiell von verschiedenen Klassen sein k�nnen,
   * wird beim Test ein String-Vergleich durchgef�hrt.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @param key zu suchender Schl�ssel
   *
   * @return <code>true</code>, falls Schl�ssel vorhanden ist
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #keyTable
   */
  public boolean hasKey(Object key)
    throws SQLException {
    validate();

    for ( int i=0; i<keyTable.size(); i++ )
      if ( keyTable.elementAt(i).toString().equals( key.toString() ) )
	return true;

    return false;
  }

  /**
   * Liefert Spalte des Prim�rschl�ssels.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @return Spalte des Prim�rschl�ssels
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #keyColumn
   */
  protected int getKeyColumn()
    throws SQLException {
    validate();

    return keyColumn;
  }

  /**
   * Liefert die Anzahl der Spalten der Tabelle.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @return <code>columns</code>
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #columns
   */
  public int getColumns()
    throws SQLException {
    validate();

    return columns;
  }

  /**
   * Liefert Attribut-Namen der Tabelle.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @param column Spaltenposition
   *
   * @return <code>attribute[column]</code>
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #attribute
   */
  public String getAttribute(int column)
    throws SQLException {
    validate();
    return attribute[column];
  }

  /**
   * Erzeugt ein leeres Object-Array.
   *
   * Diese Funktion wird von korrespondierenden Record-Objekten
   * ben�tigt.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @return <code>new Object[columns]</code>
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   */
  protected Object[] createRecord()
    throws SQLException {
    validate();

    return new Object[columns];
  }

  /**
   * Lesen des aktuellen Datensatzes.
   *
   * Diese Funktion wird von korrespondierenden Record-Objekten
   * ben�tigt.
   * Sie ruft <code>retrieve( getKey() )</code> auf und liefert
   * somit den Record an der aktuellen Datensatz-Position.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @return <code>retrieve( getKey() )</code>
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #retrieve(Object)
   * @see #getKey
   * @see AbstractRecord
   */
  public Object[] retrieve()
    throws SQLException {

    return retrieve( getKey() );
  }

  /**
   * Lesen des zum gegebenen Schl�ssels zugeh�rigen Datensatzes.
   *
   * Diese Funktion wird von korrespondierenden Record-Objekten
   * ben�tigt.
   * Sie sucht den zum Schl�ssel zugeh�rigen Datensatz zun�chst im
   * Cache und wenn dort nicht vorhanden im Schl�ssel-Vektor.
   * Sie liefert den zugeh�rigen Datensatz oder <code>null</code>,
   * falls der Schl�ssel nicht vorhanden ist.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * @return zugeh�riger Datensatz
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see AbstractRecord
   */
  public synchronized Object[] retrieve(Object key)
    throws SQLException {

    validate();

    // taugt der Schl�ssel?
    if ( key == null )
      return null;

//     for ( int i=0; i<cache.size(); i++ ) {
//       RecordCache rc = (RecordCache)cache.elementAt( i );
//       if ( rc.data[keyColumn].toString().equals( key.toString() ) )
// 	return rc.data;
//     }

    // Unn�tige Einschr�nkung, deshalb entfernt.
    //      if ( !hasKey( key ) )
    //        return null;

    TableSelection oldsel = removeSelection();

    // Diese Selektion liefert genau den zum Schl�ssel passenden Datensatz
    addSelection( new TableSelection( getKeyIdentifier(), key ) );

    // JDBC-Part ...
    String qs = getQueryString();
//    StdApplication.debugMsg( qs );
    Statement sm = connection.createStatement();
    ResultSet rset = sm.executeQuery( qs );

    Object[] oa = null;
    if ( rset.next() ) {
      oa = new Object[columns];
      for ( int i=0; i<columns; i++ )
	oa[i] = rset.getObject( i+1 );
    }

    rset.close();
    sm.close();

    // Selektion aufheben
    removeSelection();

    if ( oldsel != null )
      addSelection( oldsel );

    return oa;
  }

  /**
   * �ndert gegebenen Datensatz zum passenden Schl�ssel in der Tabelle.
   *
   * Diese Funktion wird von korrespondierenden Record-Objekten
   * ben�tigt.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * <b>Nach erfolgreicher Durchf�hrung geht eine evtl. Sortierung
   * m�glicherweise verloren! Um die richtige Sortierung wieder herzustellen,
   * muss <code>invalidate</code> aufgerufen werden.</b>
   *
   * @param key Schl�ssel des zu �ndernen Datensatzes
   * @param record der Datensatz
   *
   * @return <code>true</code> bei Erfolg, sonst <code>false</code>
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #invalidate
   * @see AbstractRecord
   */
  public synchronized boolean store(Object key,Object[] record)
    throws SQLException {
    validate();

    if ( key == null || record == null || !hasKey(key) )
      return false;

    addSelection( new TableSelection( getKeyIdentifier(), key ) );

    String us = getUpdateString( record );
//    StdApplication.debugMsg( us );
    Statement sm = connection.createStatement();
    sm.executeUpdate( us );
    sm.close();

    removeSelection();

    adjustPosition( key );

    return true;
  }

  /**
   * Liefert SQL-"update"-String zum passenden Datensatz.
   *
   * @param record Datensatz
   *
   * @return SQL-"update"-String
   *
   * @see #attribute
   * @see #getWhereClause
   */
  protected String getUpdateString(Object[] record) {
    StringBuffer sb = new StringBuffer();

    sb.append( "update " );
    sb.append( getIdentifier() );
    sb.append( " set " );

    boolean first = true;
    for ( int i=0; i<columns; i++ )
      if ( record[i] != null ) {
	if ( first ) first = false;
	else sb.append( ',' );

	sb.append( attribute[i] );
	sb.append( '=' );
	sb.append( DBUtilities.quote(record[i].toString()) );
      }
    sb.append( " where " );
    sb.append( getWhereClause() );

    return sb.toString();
  }

  /**
   * F�gt neuen Datensatz mit neuen Schl�ssel in die Tabelle ein.
   *
   * Diese Funktion wird von korrespondierenden Record-Objekten
   * ben�tigt.
   *
   * Diese Funktion ruft <code>validate</code> auf.
   *
   * <b>Nach erfolgreicher Durchf�hrung geht eine evtl. Sortierung
   * m�glicherweise verloren! Um die richtige Sortierung wieder herzustellen,
   * muss <code>invalidate</code> aufgerufen werden.</b>
   *
   * Bei Erfolg wird der Schl�ssel-Vektor erg�nzt und <code>record</code>
   * um 1 erh�ht.
   *
   * @param newKey neuer Schl�ssel
   * @param record Datensatz
   *
   * @return <code>true</code> bei Erfolg, sonst <code>false</code>
   *
   * @exception SQLException wenn betreffende Datenbank einen Fehler
   * erzeugt
   *
   * @see #validate
   * @see #invalidate
   * @see AbstractRecord
   */
  public synchronized boolean insert(Object newKey,Object[] record)
    throws SQLException {
    validate();

    if ( newKey == null || record == null )
      return false;

    String is = getInsertString( newKey, record );
//    StdApplication.debugMsg( is );
    Statement sm = connection.createStatement();
    sm.executeUpdate( is );
    sm.close();

    adjustPosition( newKey );

//     keyTable.addElement( newKey );
//     records++;

    return true;
  }

  /**
   * Liefert SQL-"insert"-String zum passenden Schl�ssel und Datensatz.
   *
   * @param newKey neuer Schl�ssel
   * @param record Datensatz
   *
   * @return SQL-"insert"-String
   *
   * @see #attribute
   */
  protected String getInsertString(Object newKey,Object[] record) {
    StringBuffer sb = new StringBuffer();

    sb.append( "insert into " );
    sb.append( getIdentifier() );
    sb.append( " (" );

    boolean first = true;
    for ( int i=0; i<columns; i++ )
      if ( i == keyColumn || record[i] != null ) {
	if ( first ) first = false;
	else sb.append( ',' );
	sb.append( attribute[i] );
      }
    sb.append( ") values (" );

    first = true;
    for ( int i=0; i<columns; i++ )
      if ( i == keyColumn || record[i] != null ) {
	if ( first ) first = false;
	else sb.append( ',' );
	if ( i == keyColumn )
	  sb.append( newKey.toString() );
	else
	  sb.append( DBUtilities.quote(record[i].toString()) );
      }
    sb.append( ")" );

    return sb.toString();
  }

  /**
   * Justiert <code>position</code> passend zu <code>key</code>.
   *
   * Diese Funktion wird von <code>update</code> und
   * <code>insert</code> aufgerufen.
   *
   * Vor der Justierung wird ein Neuladen der Schl�sseltabelle
   * erzwungen, damit bei �nderungen die Sortierung erhalten bleibt.
   *
   * @param key der Schl�ssel, auf den <code>position</code> zeigen
   * soll.
   *
   * @see #validate
   * @see #position
   */
  protected void adjustPosition(Object key)
    throws SQLException {

    valid = false;

    setPosition( key );
  }

  public void setPosition(Object key)
    throws SQLException {

    validate();

    int oldpos = position;
    for ( position=0; position<records; position++ )
      if ( keyTable.elementAt(position).toString().
	   equalsIgnoreCase(key.toString()) )
	return;
    position = oldpos;
  }

  public synchronized boolean delete(Object key)
    throws SQLException {
    validate();

    if ( key == null || !hasKey(key) )
      return false;

    addSelection( new TableSelection( getKeyIdentifier(), key ) );

    String ds = getDeleteString();
//    StdApplication.debugMsg( ds );
    Statement sm = connection.createStatement();
    sm.executeUpdate( ds );
    sm.close();

    removeSelection();

    for ( int i=0; i<keyTable.size(); i++ )
      if ( keyTable.elementAt(i).toString().equals( key.toString() ) ) {
	keyTable.removeElementAt( i );
	break;
      }

    records--;
    if ( records > 0 && position == records ) position--;

    return true;
  }

  protected String getDeleteString() {
    StringBuffer sb = new StringBuffer();

    sb.append( "delete from " );
    sb.append( getIdentifier() );
    sb.append( " where " );
    sb.append( getWhereClause() );

    return sb.toString();
  }

  public void addSelection(TableSelection sel) {
    selection.push( sel );
  }
  public TableSelection removeSelection() {
    if ( !selection.isEmpty() )
      return selection.pop();
    return null;
  }
  public void removeSelections() {
    while ( !selection.isEmpty() )
      selection.pop();
  }
}

// class RecordCache
// {
//   java.util.Date access;
//   Object[] data;

//   RecordCache(Object[] data) {
//     this.data = data;
//     this.access = new java.util.Date();
//   }
// }
