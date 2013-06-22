package de.baltic_online.base.sql;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * Diese Klasse bildet eine (Teil-)Tabelle im Speicher ab.
 * Alle Zugriffe auf eine Tabelle und deren Referenz-Tabellen
 * k�nnen somit �ber diese Klasse erfolgen.
 * <p>
 * Jede Tabelle, die durch diese Klasse dargestellt werden soll,
 * muss einen Prim�rschl�ssel besitzen, denn ohne Prim�rschl�ssel
 * k�nnen einzelne Datens�tze nicht eindeutig identifiziert werden.
 * <p>
 * Bei Tabellen, die sich durch eine <code>1:n</code> Relation auf eine
 * (oder mehrere) �bergeordnete Tabellen beziehen, wird angenommen, dass
 * der Name des Bezugsschl�ssel dem Namen des Prim�rschl�ssels der
 * aktuellen �bergeordneten Tabelle entspricht. �bergeordnete Tabellen
 * m�ssen dann verschiedene Namen f�r den Prim�rschl�ssel besitzen.
 * <p>
 * Per Default ist <code>"id"</code> der Prim�rschl�ssel. Die Funktion
 * <code>getKeyName</code> muss �berschrieben werden, wenn ein anderer
 * Prim�rschl�ssel verwendet werden soll.
 * <p>
 * <b>Beispiel:</b>
 * <p>
 * Gibt es Tabellen <i>Mitarbeiter</i> und <i>Standort</i>, die jeweils
 * der Tabelle <i>Telefon</i> �bergeordnet sind, dann m�ssen diese Tabellen
 * ihren Prim�rschl�ssel unterschiedlich benennen (z.B. <code>mid</code>
 * und <code>sid</code>) und die Tabelle <i>Telefon</i> muss die Felder
 * (Bezugsschl�ssel) <code>mid</code> und <code>sid</code> besitzen.
 * <p>
 * Die Beziehungen der einzelnen Tabellen m�ssen mit <code>
 * addReference</code> hergestellt werden.
 * <p>
 * Weder Prim�r- noch Bezugsschl�ssel m�ssen durch die Attribute angegeben
 * werden.
 * <p>
 * Nahezu s�mtliche Zugriffsfunktionen f�hren die Funktion
 * <code>reload</code> aus (Neuladen, aber nur, wenn <code>useReload</code>
 * gesetzt ist) um abzusichern, dass der Inhalt des Cache stets auf dem
 * aktuellen Stand ist.
 *
 * @see #addReference
 * @see #reload
 *
 * @see TableAttribute
 *
 * @version 0.9 09/27/99
 * @author S�nke M�ller-Lund
 */

public abstract class TableCache implements Observer
{
  /**
   * Navigation zum ersten Datensatz in der Tabelle.
   */
  public final static int FIRST = -2;

  /**
   * Navigation zum vorherigen Datensatz in der Tabelle.
   */
  public final static int PREV = -1;

  /**
   * Navigation zum aktuellen Datensatz in der Tabelle.
   */
  public final static int CURRENT = 0;

  /**
   * Navigation zum n�chsten Datensatz in der Tabelle.
   */
  public final static int NEXT = 1;

  /**
   * Navigation zum letzten Datensatz in der Tabelle.
   */
  public final static int LAST = 2;

  /**
   * SQL-Verbindung.
   */
  protected static Connection connection;

  /**
   * Meta-Daten der Datenbank
   */
  protected static DatabaseMetaData metaData;

  /**
   * Schema.
   *
   * Dieser Wert sollt mit <code>setSchema</code> auf den Namen des
   * Datenbank-Benutzers gesetzt werden.
   *
   * @see #setSchema
   */
  protected static String schema;

  protected static RollbackObservable rollbacker =
    new RollbackObservable();

  protected static BufferedWriter debugWriter = null;

  /**
   * Inhalt des Cache.
   */
  protected Vector<Object[]> data;

  /**
   * Gibt an, ob ein Neuladen der Tabelle notwendig ist.
   */
  protected boolean useReload;

  /**
   * Name der zugeh�rigen SQL-Tabelle.
   */
  protected String tableName;

  /**
   * Anzahl der Datens�tze in der Tabelle.
   */
  protected int records;

  /**
   * Position des aktuellen Datensatzes.
   */
  protected int currentRecord;

  /**
   * Hilfs-Array zur Bestimmung der Reihenfolge in die Sortierung
   * eingehender Attribute.
   */
  protected int[] orderArray;

  /**
   * Anfangsgr��e des Daten-Vektors.
   */
  protected int initialRecordBufferSize;

  /**
   * Erweiterungsgr��e des Daten-Vektors.
   */
  protected int incrementRecordBufferSize;

  /**
   * Anzahl der Spalten der zugeh�rigen Abfrage.
   */
  protected int columns;

  /**
   * Tabellen-Attribute.
   *
   * @see TableAttribute
   */
  protected TableAttribute[] attribute;

  /**
   * M�gliche abh�ngige Tabellen.
   */
  protected Vector<TableCache> reference;

  /**
   * M�gliche �bergeordnete Tabellen.
   */
  protected Vector<TableCache> parent;

  /**
   * Wenn �bergeordnete "Parent"-Tabellen gegeben sind, k�nnen diese
   * als "schwach" deklariert werden. Weak Parents werden von der Selektion
   * ausgeschlossen.
   */
  protected Vector<TableCache> weakParent;

  /**
   * Wert des Prim�rschl�ssels.
   *
   * Dieser Wert wird nur tempor�r beim Erzeugen neuer Datens�tze ben�tigt,
   * da der Prim�rschl�ssel sonst durch die Tabelle ermittelt wird.
   *
   * @see #getKeyName
   * @see #getKeyValue
   */
  protected Object keyValue;

  /**
   * Ausdrucks-Vektor zur Einschr�nkung der Selektion
   * (Teil-Tabellen).
   */
  protected Vector<Vector<SelectCondition>> superSet;

  static {
  }

  /**
   * Zuweisung der Verbindungs-Instanz zu allen Tabellen.
   *
   * Diese Funktion muss aufgerufen werden, <b>bevor</b> eine Instanz der
   * Klasse <code>TableCache</code> erzeugt wird.
   *
   * Diese Funktion setzt ausserdem das Element <code>metaData</code>.
   *
   * @param conn Verdindsdaten.
   */
  public static void setConnection(Connection conn)
  {
    connection = conn;
    try {
      metaData = conn.getMetaData();
    }
    catch ( SQLException x ) { // sollte niemals geschehen.
    }
  }

  public static void commit()
    throws SQLException
  {
    connection.commit();
  }

  public static void rollback()
    throws SQLException
  {
    connection.rollback();
    rollbacker.notifyTables();
  }

  /**
   * Zuweisung des Schemas zu allen Tabellen.
   *
   * Das Schema wir z.Z. nicht benutzt.
   *
   */
  public static void setSchema(String sname)
  {
    schema = sname;
  }


  public static void startDebug(String fileName)
  {
    try {
      debugWriter = new BufferedWriter(new FileWriter(fileName));
    }
    catch ( IOException x ) {
      System.err.println( x.toString() );
    }
  }

  public static void endDebug()
  {
    try {
      if ( debugWriter != null ) {
	debugWriter.close();
	debugWriter = null;
      }
    }
    catch ( IOException x ) {
      System.err.println( x.toString() );
    }
  }

  protected static void debugLine(String line)
  {
    try {
      if ( debugWriter != null ) {
	debugWriter.write( "TC: "+line );
	debugWriter.newLine();
      }
    }
    catch ( IOException x ) {
      System.err.println( x.toString() );
    }
  }

  /**
   * Erzeugt einen Tabellen-Cache, f�llt ihn aber noch nicht mit Daten.
   *
   * Der Konstruktor ruft die Funktionen <code>setSpace</code> und
   * <code>setAttributes</code> auf.
   * Sollen Daten aus der Tabelle gelesen werden, muss die Funktion
   * <code>reload</code> aufgerufen werden.
   *
   * @param tableName Name der Tabelle
   *
   * @see #setSpace
   * @see #setAttributes
   * @see #reload
   */
  @SuppressWarnings("unused")
  protected TableCache(String tableName)
  {
    debugLine( "creating table-cache for \""+tableName+"\"." );

    this.tableName = tableName;

    rollbacker.addObserver( this );

    columns = records = currentRecord = 0;
    data = null;

    reference = new Vector<TableCache>( 10, 5 );
    parent = new Vector<TableCache>( 4, 2 );
    weakParent = new Vector<TableCache>( 4, 2 );

    superSet = new Vector<Vector<SelectCondition>>( 10, 5 );

    setSpace();
    setAttributes();

    createOrderArray();

    useReload = true;

    // Erzwinge Objekt-Implementierung
    if ( false ) {
      new SimpleQuery( this );
    }
  }

  /**
   * Setzen der Vektor-Dimensionen durch vordefinierte Werte.
   *
   * Implementierung:
   *
   * <pre>
   * protected void setSpace()
   * {
   *   initialRecordBufferSize = 100;
   *   incrementRecordBufferSize = 50;
   * }
   * </pre>
   */
  protected void setSpace()
  {
    initialRecordBufferSize = 100;
    incrementRecordBufferSize = 50;
  }

  /**
   * Wird vom Konstruktor aufgerufen.
   *
   * Nach diesem Aufruf ist garantiert, dass das Array orderArray die
   * Spalten enth�lt, die f�r eine Sortierung ausschlaggebend sind,
   * und zwar in der Reihenfolge der gegebenen Attribute.
   */
  private void createOrderArray()
  {
    orderArray = null;
    int oc = 0;
    for ( int i=0; i<attribute.length; i++ )
      if ( attribute[i].order > 0 )
	oc++;

    if ( oc == 0 ) return;

    orderArray = new int[oc];
    oc = 0;
    for ( int i=1; i<=attribute.length; i++ )
      for ( int j=0; j<attribute.length; j++ )
	if ( attribute[j].order == i )
	  orderArray[oc++] = j;
  }

  /**
   * Setzen der Relation zwischen zwei Tabellen.
   *
   * @param refCache Referenz-Tabelle
   *
   * @see #addParent
   */
  public void addReference(TableCache refCache)
  {
    addReference( refCache, false );
  }

  public void addWeakReference(TableCache refCache)
  {
    addReference( refCache, true );
  }

  public void addReference(TableCache refCache,boolean weak)
  {
    if ( reference.indexOf( refCache ) < 0 ) {
      reference.addElement( refCache );
      refCache.addParent( this );
      if ( weak )
	refCache.setWeakParent( this );

      useReload = true;
    }
  }

  /**
   * Hinzuf�gen der �bergeordneten Tabelle.
   *
   * Wird von <code>addReference</code> aufgerufen.
   *
   * @param parentCache �bergeordnete Tabelle
   *
   * @see #addReference
   */
  protected void addParent(TableCache parentCache)
  {
    if ( parent.indexOf( parentCache ) < 0 )
      parent.addElement( parentCache );
  }

  /**
   * Entfernen einer Referenz-Tabelle.
   *
   * @param refCache die zu entfernende Tabelle
   *
   * @see #addReference
   */
  public void removeReference(TableCache refCache)
  {
    int i = reference.indexOf( refCache );
    if ( i >= 0 ) {
      reference.removeElementAt( i );
      refCache.removeParent( this );
      refCache.setWeakParent( this, false );
      useReload = true;
    }
  }

  /**
   * Entfernen einer Parent-Tabelle.
   *
   * @see #removeReference
   */
  protected void removeParent(TableCache parentCache)
  {
    int i = parent.indexOf( parentCache );
    if ( i >= 0 )
      parent.removeElementAt( i );
  }

  /**
   * Entfernen aller Referenz-Tabellen.
   *
   * @see #removeReference
   * @see #addReference
   */
  public void removeReferences()
  {
    useReload = reference.size() > 0;
    for ( int i=0; i<reference.size(); i++ ) {
      TableCache tc = reference.elementAt( i );
      tc.removeParent( this );
      tc.setWeakParent( this, false );
    }
    reference.clear();
  }

  protected void setWeakParent(TableCache parentCache)
  {
    setWeakParent( parentCache, true );
  }

  protected void setWeakParent(TableCache parentCache,boolean weak)
  {
    if ( weak && weakParent.indexOf(parentCache) < 0 )
      weakParent.addElement( parentCache );

    if ( !weak )
      weakParent.removeElement( parentCache );
  }

  public boolean isWeak(TableCache parentCache)
  {
    return weakParent.indexOf( parentCache ) >= 0;
  }

  /**
   * Findet den zu den angegebenen Namen zugeh�rigen
   * Cache im Referenz-Baum.
   *
   * @return Zugeh�riger Cache oder <code>null</code>
   */
  public TableCache getReference(String name)
  {
    if ( tableName.equalsIgnoreCase( name ) )
      return this;

    for ( int i=0; i<reference.size(); i++ ) {
      TableCache tc = reference.elementAt(i);
      tc = tc.getReference( name );
      if ( tc != null )
	return tc;
    }

    return null;
  }

  public TableCache getParent(String name)
  {
    if ( tableName.equalsIgnoreCase( name ) )
      return this;

    for ( int i=0; i<parent.size(); i++ ) {
      TableCache tc = parent.elementAt(i);
      tc = tc.getParent( name );
      if ( tc != null )
	return tc;
    }

    return null;
  }

  public int getParentCount()
  {
    return parent.size();
  }

  public int getReferenceCount()
  {
    return reference.size();
  }

  public TableAttribute getAttribute(int index)
  {
    return attribute[index];
  }

  /**
   * Tabellen-Objekte gelten als gleich, wenn sie auf die gleiche
   * Tabelle im DBMS abbilden.
   *
   * @return <code>true</code>, wenn Tabellennamen ohne Ber�cksichtigung
   * der Gro�- und Kleinschreibung �bereinstimmen.
   */
  public boolean equals(Object o)
  {
    TableCache tc = (TableCache)o;

    return tableName.equalsIgnoreCase( tc.tableName );
  }

  /**
   * Liefert den Namen des Prim�rschl�ssels.
   *
   * @return Name des Prim�rschl�ssels
   */
  public String getKeyName()
  {
    return "id";
  }

  /**
   * Liefert den Wert des Prim�rschl�ssels.
   *
   * Dieser Wert wird aus den Tabellen-Daten ermittelt, sofern
   * <code>keyValue==null</code> ist.
   * Andernfalls wird <code>keyValue</code> zur�ckgegeben.
   *
   * @return Wert des Prim�rschl�ssels
   *
   * @exception SQLException bei einem DBMS-Fehler
   */
  public Object getKeyValue()
    throws SQLException
  {
    if ( keyValue != null )
      return keyValue;

    Object[] oa = getCurrentData();
    if ( oa != null )
      return oa[0];

    return null;
  }

  public Object getParentKeyValue(String name)
    throws SQLException
  {
    reload();

    for ( int i=0; i<parent.size(); i++ ) {
      TableCache p = parent.elementAt(i);
      if ( p.tableName.equalsIgnoreCase( name ) ) {
	Object[] oa = data.elementAt( currentRecord );
	return oa[columns+i];
      }
    }
    return null;
  }

  /**
   * Gibt Namen der Tabelle zur�ck.
   *
   * @return Tabellenname
   *
   */
  public String getTableName()
  {
    return tableName;
  }

  /**
   * Erzeugt einen SQL-Abfrage-String aus den gegebenen Attributen.
   *
   * @return SQL-Abfrage-String
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   */
  public String createQueryString()
    throws SQLException
  {
    // Attribute m�ssen gesetzt sein!
    if ( attribute == null )
      throw new NullPointerException( "in createQueryString()" );

    // Aus Performance-Gr�nden StringBuffer statt String benutzen.
    StringBuffer sb = new StringBuffer();

    sb.append( "select " );
    sb.append( getKeyName() );

    // Auswahl zusammenstellen ...
    for ( int i=0; i<attribute.length; i++ ) {
      TableAttribute ta = attribute[i];
      sb.append( ',' );
      sb.append( ta.name );
    }
    for ( int i=0; i<parent.size(); i++ ) {
      TableCache p = parent.elementAt( i );
      sb.append( ',' );
      sb.append( p.getKeyName() );
    }

    sb.append( " from " );
    sb.append( tableName );

    // Ist "where appended" ?
    boolean wa = false;

    // Selektiere auf einen oder mehrere nichtschwache Parents ...
    if ( parent.size() > 0 ) {
      boolean first = true;
      for ( int i=0; i<parent.size(); i++ ) {
	TableCache p = parent.elementAt( i );
	if ( !isWeak(p) ) {
	  String keyName = p.getKeyName();
	  Object keyValue = p.getKeyValue();
	  if ( keyValue != null ) {
	    if ( first ) {
	      sb.append( " where " );
	      wa = true;
	    }
	    else
	      sb.append( " and " );

	    sb.append( keyName );
	    sb.append( '=' );
	    sb.append( keyValue.toString() );
	    first = false;
	  }
	}
      }
    }

    // Bilden der Restriktionen wenn Tabelle eine Teil-Tabelle ist.
    if ( superSet.size() > 0 ) {
      if ( wa )
	sb.append( " and (" );
      else
	sb.append( " where " );
      for ( int i=0; i<superSet.size(); i++ ) {
	if ( i > 0 )
	  sb.append( " or " );
	Vector<SelectCondition> subSet = superSet.elementAt( i );
	sb.append( "(" );
	for ( int j=0; j<subSet.size(); j++ ) {
	  SelectCondition cond = subSet.elementAt( j );
	  if ( j > 0 )
	    sb.append( " and " );

	  sb.append( cond.name );
	  sb.append( " "+cond.op+" " );
	  sb.append( cond.term );
	}
	sb.append( ")" );
      }
      if ( wa )
	sb.append( ")" );
    }

    // "order"-Klausel bei Bedarf hinzuf�gen ...
    if ( orderArray != null ) {
      // Wird f�r die ','-Setzung ben�tigt.
      boolean firstOrder = true;
      sb.append( " order by " );
      // Attribute, die in die Sortierung einbezogen werden, haben
      // order>0. Sortierung prim�r nach order==1, dann order==2 usw.

      for ( int i=0; i<orderArray.length; i++ ) {
	TableAttribute ta = attribute[orderArray[i]];
	if ( !firstOrder )
	  sb.append( ',' );
	else
	  firstOrder = false;
	sb.append( ta.name );
	// Muss Feld absteigend sortiert werden?
	if ( !ta.ascend )
	  sb.append( " desc" );
      }
    }
    // Fertig!

    return sb.toString();
  }

  /**
   * Auslesen der Tabelle.
   *
   * Ruft <code>reload( false, false )</code> auf.
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   *
   * @see #reload(boolean,boolean)
   */
  public void reload()
    throws SQLException
  {
    reload( false, false );
  }

  public void reload(boolean loadReferences)
    throws SQLException
  {
    reload( loadReferences, false );
  }

  /**
   * Auslesen der Tabelle und aller untergeordneten Tabellen
   * (sofern angegeben).
   *
   * Das Neuladen wird nur ausgef�hrt, wenn <code>useReload</code>
   * gesetzt ist. In diesem Fall wird <code>currentRecord</code>
   * auf <code>0</code> gesetzt.
   *
   * @param loadReference gibt an, ob untergeordnete Tabellen
   * ebenfalls ausgelesen werden sollen.
   * @param keepRecord gibt an, ob <code>currentRecord</code>
   * unver�ndert bleiben soll.
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   *
   * @see #useReload
   * @see #currentRecord
   */
  public void reload(boolean loadReferences,boolean keepRecord)
    throws SQLException
  {
    if ( useReload ) {
      // Initialisieren des Datenbereichs ...
      if ( data == null )
	data = new Vector<Object[]>( initialRecordBufferSize,
			   incrementRecordBufferSize );
      else
	data = new Vector<Object[]>( data.size(),
			   incrementRecordBufferSize );

      String sqlQuery = createQueryString();
      debugLine( "executing query" );
      debugLine( ">> "+sqlQuery );

      // SQL-Abfrage ausf�hren ...
      Statement sm = connection.createStatement();
      ResultSet rset = sm.executeQuery( sqlQuery );
      ResultSetMetaData rsmd = rset.getMetaData();

      // Anzahl der Datens�tze zur�cksetzen ...
      records = 0;
      // ... und normalerweise auch die Datensatz-Position
      if ( !keepRecord ) currentRecord = 0;

      // Anzahl der Spalten ermitteln (enth�lt PriKey) ...
      columns = rsmd.getColumnCount() - parent.size();

      int allColumns = columns + parent.size();

      // Abfrageergebnisse Zeile f�r Zeile holen
      // und speichern ...
      while ( rset.next() ) {
	Object[] oa = new Object[allColumns];
	for ( int i=0; i<allColumns; i++ )
	  oa[i] = rset.getObject( i+1 );

	data.addElement( oa );
	records++;
      }

      // SQL-Statements abschlie�en
      // (records ist nun richtig gesetzt).
      rset.close();
      sm.close();

      // reload ist nun nicht mehr notwendig.
      useReload = false;
    }

    // Ggf. reload auf die Child-Tabellen anwenden ...
    if ( loadReferences )
      for ( int i=0; i<reference.size(); i++ )
	reference.elementAt(i).reload( true, keepRecord );
  }

  public void update(Observable o,Object arg)
  {
    try {
      if ( parent.size() == 0 ) {
	useReload = true;
	reload();
	forceReferenceReload();
      }
    }
    catch ( SQLException x ) {
    }
  }

  /**
   * Liefert die aktuelle Zeile in der Tabelle.
   *
   * Das erste Feld ist stets der Prim�rschl�ssel, die restlichen Felder
   * repr�sentieren den Inhalt, welcher surch die Attribute gegeben ist.
   *
   * Die aktuelle Zeile ist durch den Wert <code>currentRecord</code>
   * gegeben. Dieser Wert kann durch die Funktionen
   * <code>moveToRecord</code> und <code>goToRecord</code>
   * ge�ndert werden.
   *
   * Gilt <code>currentRecord==records</code> (also z.B. bei einer
   * leeren Tabelle), dann liefert die Funktion <code>null</code>.
   *
   * @return aktuelle Zeile
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   */
  public Object[] getCurrentData()
    throws SQLException
  {
    reload();

    if ( isEOD() ) return null;

    Object[] oa = data.elementAt( currentRecord );
    Object[] tmp = new Object[columns];
    for ( int i=0; i<columns; i++ )
      tmp[i] = oa[i];

    return tmp;
  }

  /**
   * Liefert gesamten Inhalt.
   *
   * Gilt <code>currentRecord==records</code> (also z.B. bei einer
   * leeren Tabelle), dann liefert die Funktion <code>null</code>.
   *
   * @return gesamter Inhalt
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   */
  public Object[] getData()
    throws SQLException
  {
    reload();

    if ( isEOD() ) return null;

    return data.toArray();
  }

  /**
   * Liefert den Index der aktuellen Zeile (<code>currentRecord</code>)
   * zur�ck.
   *
   * Diese Funktion ruft <b>nicht</b> <code>reload</code> auf
   * (h�tte auch absolut keinen Sinn, da im Falle eines Neuladens der
   * Wert von <code>currentRecord</code> auf <code>0</code> gesetzt wird.
   *
   * @return Index der aktuellen Zeile
   */
  public int getRecordIndex()
  {
    return currentRecord;
  }

  /**
   * Anzahl der Zeilen in der (Teil-)Tabelle.
   *
   * @return <code>records</code>
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   */
  public int getRecords()
    throws SQLException
  {
    reload();

    return records;
  }

  public int getColumns()
    throws SQLException
  {
    reload();

    return columns;
  }


  /**
   * Liefert <code>true</code>, wenn die Tabelle leer ist.
   *
   * @return <code>records==0</code>
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   */
  public boolean isEmpty()
    throws SQLException
  {
    reload();

    return records == 0;
  }

  /**
   * Liefert <code>true</code>, wenn <code>currentRecord==records</code>
   * gilt (End Of Data).
   *
   * @return <code>currentRecord>=records</code>
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   */
  public boolean isEOD()
    throws SQLException
  {
    reload();

    return currentRecord >= records;
  }

  /**
   * �ndern der aktuellen Zeile (Tabellen-Navigation)
   *
   * Falls der neue Zeilen-Index verschieden vom aktuellen ist,
   * wird bei allen referenzierten Tabellen ein <code>reload</code>
   * erzwungen. In diesem Fall wird <code>true</code> zur�ckgegeben,
   * sonst <code>false</code>.
   *
   * @param newRecord neuer Zeilenindex
   *
   * @return �nderungszustand
   *
   * @exception ArrayIndexOutOfBoundsException falls
   * <code>newRecord&lt;0</code> oder <code>newRecord&gt;records</code>
   * gilt (der Fall <code>newRecord==records</code> ist erlaubt, um
   * <code>goToRecord(0)</code> in einer leeren Tabelle zu erlauben.
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   * 
   */
  public boolean goToRecord(int newRecord)
    throws SQLException
  {
    reload();

    if ( newRecord == currentRecord )
      return false;

    if ( newRecord < 0 || newRecord > records )
      throw new ArrayIndexOutOfBoundsException( newRecord );

    currentRecord = newRecord;

    if ( currentRecord < records )
      forceReferenceReload();

    return true;
  }

  /**
   * Relative Navigation im Cache.
   *
   * Liefert <code>true</code>, falls der aktuelle Zeilenindex
   * ge�ndert wurde oder falls <code>dir==CURRENT</code> gilt,
   * sonst <code>false</code>.
   *
   * @param dir Richtungs-Konstante
   *
   * @return �nderungszustand
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   */
  public boolean moveToRecord(int dir)
    throws SQLException
  {
    return moveToRecord( dir, false );
  }

  public boolean moveToRecord(int dir,boolean keepRecord)
    throws SQLException
  {
    reload();

    int newRecord = currentRecord;
    switch ( dir ) {
    case FIRST:
      newRecord = 0;
      break;
    case PREV:
      if ( currentRecord > 0 )
	newRecord = currentRecord - 1;
      break;
    case NEXT:
      if ( currentRecord < records - 1 )
	newRecord = currentRecord + 1;
      break;
    case LAST:
      if ( records > 0 )
	newRecord = records - 1;
      break;
    }

    boolean load = currentRecord != newRecord;
    currentRecord = newRecord;

    if ( load || dir == CURRENT ) {
      forceReferenceReload( keepRecord );
      return true;
    }

    return load;
  }

  /**
   * Erzwingen des Neuladens referenzierter Tabellen.
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   */
  protected void forceReferenceReload()
    throws SQLException
  {
    forceReferenceReload( false );
  }

  protected void forceReferenceReload(boolean keepRecord)
    throws SQLException
  {
    for ( int i=0; i<reference.size(); i++ ) {
      TableCache tc = reference.elementAt( i );
      tc.useReload = true;
      tc.reload( true, keepRecord );
      tc.forceReferenceReload( keepRecord );
    }
  }

  /**
   * Holt einen eindeutigen Wert aus der Datenbank.
   *
   * @param idName Name der sog. Sequence-ID
   *
   * @return n�chster Wert der gegebenen Sequence-ID
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   *
   * @see #createKey
   */
  public Object getSequenceID(String idName)
    throws SQLException
  {
    Object x = null;
    String query = "select "+idName+".nextval from dual";
    Statement sm = connection.createStatement();
    ResultSet rset = sm.executeQuery( query );

    if ( rset.next() )
      x = rset.getObject( 1 );

    rset.close();
    sm.close();

    return x;
  }

  /**
   * Erzeugt einen Prim�rschl�ssel.
   *
   * Implementierung:
   * <pre>
   * public Object createKey()
   *   throws SQLException
   * {
   *   return getSequenceID( "id" );
   * }
   * </pre>
   *
   * @return Prim�rschl�ssel
   *
   * @exception SQLException wenn im DBMS ein Fehler auftritt.
   *
   * @see #getSequenceID
   */
  public Object createKey()
    throws SQLException
  {
    return getSequenceID( "id" );
  }

  /**
   * �ndert die Daten der aktuellen Zeile.
   *
   * Der Parameter <code>rowData</code> darf <b>kein</b>
   * Schl�sselfeld enthalten, d.h. dieses Array besitzt ein Feld weniger, als
   * durch <code>columns</code> angegeben.
   *
   * Da durch die �nderung die Sortierung verloren gehen kann, wird die
   * (Teil-)Tabelle neu eingelesen. Dabei wird <code>currentRecord</code>
   * so angepasst, dass er die evtl. ge�nderte Zeilenposition annimmt.
   *
   * @param rowData neue Zeile
   */
  public void update(Object[] rowData)
    throws SQLException
  {
    // Nichts passiert, wenn Tabelle leer ist, oder wenn vorher kein reload()
    // ausgef�hrt wurde.
    if ( isEOD() ) return;

    // Update-Kommando berechnen ...
    StringBuffer sb = new StringBuffer( 200 );
    int cols = attribute.length;

    sb.append( "update " );
    sb.append( tableName );
    sb.append( " set " );
    for ( int i=0; i<cols; i++ ) {
      if ( i > 0 ) sb.append( ',' );
      sb.append( attribute[i].name );
      sb.append( '=' );
      sb.append( SQLUtilities.quote(rowData[i].toString()) );
    }
    sb.append( getKeyClause() );

    debugLine( "executing statement" );
    debugLine( ">> "+sb.toString() );

    // Update ausf�hren ...
    Statement sm = connection.createStatement();
    sm.executeUpdate( sb.toString() );
    sm.close();

    // Neue Datensatz-Position berechnen ...
    Object key = data.elementAt(currentRecord)[0];
    useReload = true;
    reload();
    currentRecord = findIndexByKey( key );
  }

  /**
   * Einf�gen von Daten.
   *
   * Der Parameter <code>rowData</code> darf <b>kein</b>
   * Schl�sselfeld enthalten, d.h. dieses Array besitzt ein Feld weniger, als
   * durch <code>columns</code> angegeben.
   *
   *
   * @param rowData neue Zeile
   */
  public void insert(Object[] rowData)
    throws SQLException
  {
    // Im Gegensatz zu update() f�hren wir hier ein reload() aus ...
    reload();

    // Erzeuge neuen PriKey ...
    Object newKey = createKey();
    // ... und eine vollst�ndige neue Zeile.
    Object[] newRow = new Object[columns];

    // Daten �bernehmen ...
    for ( int i=0; i<rowData.length; i++ )
      newRow[i+1] = rowData[i];

    // ... und den Schl�ssel
    newRow[0] = newKey;

    // Insert-Kommando formulieren ...
    StringBuffer sb = new StringBuffer( 200 );
    sb.append( "insert into " );
    sb.append( tableName );
    sb.append( " (" );
    sb.append( getKeyName() );

    for ( int i=0; i<parent.size(); i++ ) {
      TableCache p = parent.elementAt( i );
      sb.append( ","+p.getKeyName() );
    }

    for ( int i=0; i<attribute.length; i++ )
      sb.append( ","+attribute[i].name );
    sb.append( ") values (" );
    sb.append( newKey.toString() );

    for ( int i=0; i<parent.size(); i++ ) {
      TableCache p = parent.elementAt( i );
      sb.append( ","+p.getKeyValue() );
    }

    for ( int i=0; i<rowData.length; i++ )
      sb.append( ","+SQLUtilities.quote(rowData[i].toString()) );
    sb.append( ")" );

    debugLine( "executing statement" );
    debugLine( ">> "+sb.toString() );

    // Insert ausf�hren ...
    Statement sm = connection.createStatement();
    sm.executeUpdate( sb.toString() );
    sm.close();

    // Neue Datensatz-Position berechnen und vollst�ndiges Neuladen
    useReload = true;
    reload();
    forceReferenceReload();
    currentRecord = findIndexByKey( newKey );
  }

  public void delete()
    throws SQLException
  {
    delete( false );
  }

  public void delete(boolean all)
    throws SQLException
  {
    if ( currentRecord == records ) return;

    for ( int i=0; i<reference.size(); i++ )
      reference.elementAt(i).delete( true );

    StringBuffer sb = new StringBuffer();
    sb.append( "delete from " );
    sb.append( tableName );

    // "where appended" ?
    boolean wa = false;

    if ( all ) {
      // Selektion bzgl. Parents oder
      for ( int i=0; i<parent.size(); i++ ) {
	TableCache p = parent.elementAt( i );
	String keyName = p.getKeyName();
	Object keyValue = p.getKeyValue();
	if ( keyValue != null ) {
	  if ( wa )
	    sb.append( " and " );
	  else
	    sb.append( " where " );
	  sb.append( keyName );
	  sb.append( '=' );
	  sb.append( keyValue.toString() );
	  wa = true;
	}
      }
    }
    else {
      // bzgl. der eigenen Kennung
      sb.append( " where " );
      sb.append( getKeyName() );
      sb.append( "=" );
      sb.append( getKeyValue().toString() );
    }

    debugLine( "executing statement" );
    debugLine( ">> "+sb.toString() );

    Statement sm = connection.createStatement();
    sm.executeUpdate( sb.toString() );
    sm.close();

    if ( currentRecord == records - 1 && currentRecord > 0 )
      currentRecord--;

    int ocr = currentRecord;
    useReload = true;
    reload();
    currentRecord = ocr;
  }

  public int findIndexByKey(Object key)
  {
    for ( int i=0; i<records; i++ ) {
      Object key2 = data.elementAt(i)[0];
      if ( key.toString().equals(key2.toString()) )
	return i;
    }

    return records;
  }
  protected String getKeyClause()
    throws SQLException
  {
    return " where "+getKeyName()+"="+getKeyValue().toString();
  }

  protected abstract void setAttributes();
}

class RollbackObservable extends Observable
{
  public void notifyTables()
  {
    setChanged();
    notifyObservers();
  }
}
