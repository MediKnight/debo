package de.baltic_online.office2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import de.baltic_online.base.store.Selection;
import de.baltic_online.base.store.Storable;
import de.baltic_online.base.store.sql.Database;
import de.baltic_online.base.store.sql.DefaultSelection;
import de.baltic_online.base.store.sql.SQLRuntimeException;
import de.baltic_online.base.store.sql.SQLToolkit;

/**
 * Implementierung einer Datenbank-Klasse, optimiert f�r die
 * Baltic-Online Datenbank.
 */

public class BOBase extends Database<Bobo>
  implements Debugable
{

  /**
   * Debug-Flag, default ist <code>false</code> (keine Ausgaben).
   *
   * @see #debugMsg
   */
  public static boolean debug = false;

  public BOBase() {
    super();
    init();
  }
  public BOBase(Connection connection) {
    super( connection );
    init();
  }
  public BOBase(SQLToolkit toolkit) {
    super( toolkit );
    init();
  }
  public BOBase(Connection connection,SQLToolkit tk) {
    super( connection, tk );
    init();
  }
  private void init() {
  }

  public Object createKey(Storable object) {
    String seqid = (object instanceof AddressRecord) ?
      "id2" : "id";
    Object key = null;

    try {
      String query = "select "+seqid+".nextval from dual";
      debugMsg( "Query: "+query );
      Statement sm = connection.createStatement();
      ResultSet rset = sm.executeQuery( query );

      if ( rset.next() )
	key = rset.getObject( 1 );

      rset.close();
      sm.close();
    }
    catch ( SQLException x ) {
      // Exception-Delegation:
      throw new SQLRuntimeException( x );
    }

    return key;
  }

  /**
   * Erzeugen eines SQL-Query-Strings ohne Sortierung.
   *
   * @param bobo Datenbank-Objekt (Datensatz)
   * @param keyName Name des Schl�ssels, nach dem selektiert werden soll
   * (kann <code>null</code> sein, dann keine Selektierung).
   * @param key Selektierschl�ssel (darf nur <code>null</code> sein,
   * wenn <code>keyName null</code> ist.
   *
   * @return SQL-Query-String
   */
  protected String createQueryString(Bobo bobo,Selection selection) {
    return createQueryString( bobo, selection, false );
  }

  /**
   * Erzeugen eines SQL-Query-Strings.
   *
   * @param bobo Datenbank-Objekt (Datensatz)
   * @param keyName Name des Schl�ssels, nach dem selektiert werden soll
   * (kann <code>null</code> sein, dann keine Selektierung).
   * @param key Selektierschl�ssel (darf nur <code>null</code> sein,
   * wenn <code>keyName null</code> ist.
   * @param useOrder wenn <code>true</code>, dann wird Order-Klausel
   * (Sortierung) hinzugef�gt.
   *
   * @return SQL-Query-String
   */
  protected String createQueryString(Bobo bobo,
				     Selection selection,
				     boolean useOrder) {

    StringBuffer sbQuery = new StringBuffer();
    String[] attribute = bobo.getAttributes();

    sbQuery.append( "select " );
    for ( int i=0; i<attribute.length; i++ ) {
      if ( i > 0 )
	sbQuery.append( "," );
      sbQuery.append( attribute[i] );
    }
    sbQuery.append( " from "+bobo.getIdentifier() );
    sbQuery.append( " where "+selection );

    // Order-Klausel bilden ...
    if ( useOrder ) {

      // default ist aufsteigende Ordnung
      // Wenn im Array order ein negativer Index vorkommt, wird
      // absteigende Sortierung angenommen.
      boolean descend = false;

      // Ist Ordnung f�r diese Objekte �berhaupt vorgesehen?
      int[] order = bobo.getOrderIndexes();
      if ( order != null ) {

	// Klausel als Liste bilden
	int m = order.length;
	StringBuffer sb = new StringBuffer();
	boolean first = true;
	for ( int i=0; i<m; i++ ) {
	  if ( order[i] < 0 ) descend = true;
	  else {
	    if ( !first ) sb.append( "," );
	    sb.append( attribute[order[i]] );
	    first = false;
	  }
	}
	sbQuery.append( " order by " );
	sbQuery.append( sb.toString() );
	if ( descend )
	  sbQuery.append( " desc" );
      }
    }

    return sbQuery.toString();
  }

  /**
   * Objekt aus Datenbank zum passenden Schl�ssel einlesen.
   *
   * @param object Quell-Objekt (dient nur zur Identifizierung)
   * @param data Ziel-Array
   * @param key Passender Schl�ssel
   * @return <code>true</code> bei Erfolg
   */
  public boolean retrieve(Storable object,Object[] data,Object key) {

    // Gefunden?
    boolean found = false;

    // Objekte dieser Implementierung sind
    // Baltic-Online Business Objekte.
    Bobo bobo = (Bobo)object;

    Selection sel = new DefaultSelection( toolkit,
					  bobo.getKeyIdentifier(),
					  key );
    String query = createQueryString( bobo, sel );

    debugMsg( "Query: "+query );

    // Abfrage anwenden ...
    try {
      Statement sm = connection.createStatement();
      ResultSet rset = sm.executeQuery( query );

      if ( rset.next() ) { // Erfolg!
	found = true;

	// Daten ziehen ...
	for ( int i=0; i<data.length; i++ )
	  data[i] = rset.getObject( i+1 );
      }

      rset.close();
      sm.close();
    }
    catch ( SQLException x ) {
      // Exception-Delegation:
      throw new SQLRuntimeException( x );
    }

    return found;
  }

  /**
   * Liefert SQL-"update"-String zum passenden Datensatz.
   *
   * @param record Datensatz
   *
   * @return SQL-"update"-String
   *
   */
  protected String createUpdateString(Bobo bobo,Object[] data,
				      Selection sel) {
    StringBuffer sb = new StringBuffer();

    sb.append( "update " );
    sb.append( bobo.getIdentifier() );
    sb.append( " set " );

    int n = bobo.getColumnCount();
    String[] attribute = bobo.getAttributes();
    for ( int i=0; i<n; i++ ) {
      if ( i > 0 ) sb.append( "," );

      sb.append( attribute[i] );
      sb.append( '=' );
      if ( data[i] != null )
	sb.append( toolkit.quote(data[i].toString()) );
      else
	sb.append( toolkit.getNullString() );	
    }

    sb.append( " where "+sel );

    return sb.toString();
  }

  public boolean store(Storable object,Object[] data,Object key) {

    Bobo bobo = (Bobo)object;
    Selection sel = new DefaultSelection( toolkit,
					  bobo.getKeyIdentifier(),
					  key );
    String update = createUpdateString( bobo, data, sel );
    boolean stored = false;
    debugMsg( "Update: "+update );

    try {
      Statement sm = connection.createStatement();
      sm.executeUpdate( update );
      sm.close();
      stored = true;
    }
    catch ( SQLException x ) {
      throw new SQLRuntimeException( x );
    }
    return stored;
  }

  /**
   * Liefert SQL-"insert"-String zum passenden Datensatz.
   *
   * @param record Datensatz
   *
   * @return SQL-"insert"-String
   *
   */
  protected String createInsertString(Bobo bobo,Object[] data,Object key) {

    StringBuffer sb = new StringBuffer();

    sb.append( "insert into " );
    sb.append( bobo.getIdentifier() );
    sb.append( " (" );

    int n = bobo.getColumnCount();
    int keyColumn = -1;
    String[] attribute = bobo.getAttributes();
    for ( int i=0; i<n; i++ ) {
      if ( i > 0 ) sb.append( "," );
      sb.append( attribute[i] );
      if ( attribute[i].equals( bobo.getKeyIdentifier() ) )
	keyColumn = i;
    }
    sb.append( ") values (" );
    for ( int i=0; i<n; i++ ) {
      if ( i > 0 ) sb.append( "," );
      if ( i == keyColumn )
	sb.append( toolkit.quote(key.toString()) );
      else {
	if ( data[i] != null )
	  sb.append( toolkit.quote(data[i].toString()) );
	else
	  sb.append( toolkit.getNullString() );	
      }
    }
    sb.append( ")" );

    return sb.toString();
  }
  public boolean insert(Storable object,Object[] data,Object key) {

    Bobo bobo = (Bobo)object;
    String insert = createInsertString( bobo, data, key );
    boolean stored = false;
    debugMsg( "Insert: "+insert );

    try {
      Statement sm = connection.createStatement();
      sm.executeUpdate( insert );
      sm.close();
      stored = true;
    }
    catch ( SQLException x ) {
      throw new SQLRuntimeException( x );
    }
    return stored;
  }

  public boolean delete(Storable object,Object key) {
    return true;
  }

  public Enumeration<Bobo> getEnumeration(Bobo object,
				    String identifier,
				    Object key,
				    boolean order) {
    return new BOEnumeration( object,
			      new DefaultSelection(toolkit,identifier,key),
			      order );
  }
  public Enumeration<Bobo> getEnumeration(Bobo object,
				    Selection selection,
				    boolean order) {
    return new BOEnumeration( object, selection, order );
  }

  public void debugMsg(String msg) {
    if ( debug )
      System.err.println( getClass().getName()+" >> "+msg );
  }

  protected class BOEnumeration implements Enumeration<Bobo>
  {
    boolean state;
    Statement sm;
    ResultSet rset;
    Bobo bobo;

    BOEnumeration(Bobo bobo,Selection selection,boolean order) {
      state = false;
      this.bobo = bobo;

      String query = createQueryString( bobo, selection, order );
      debugMsg( "Query: "+query );

      try {
	sm = getConnection().createStatement();
	rset = sm.executeQuery( query );
      }
      catch ( SQLException x ) {
	sm = null;
	rset = null;
	throw new SQLRuntimeException( x );
      }
    }
    public boolean hasMoreElements() {
      try {
	if ( !(state = rset.next()) ) {
	  rset.close();
	  sm.close();
	}
      }
      catch ( SQLException x ) {
	throw new SQLRuntimeException( x );
      }

      return state;
    }
    public Bobo nextElement() {
      if ( !state ) return null;

      Bobo bobo2;

      try {
	bobo2 = (Bobo)bobo.getClass().newInstance();
	int n = bobo2.getColumnCount();
	Object[] data = new Object[n];
	for ( int i=0; i<n; i++ )
	  data[i] = rset.getObject( i+1 );

	bobo2.put( data );
	bobo2.setStoreKeeper( bobo.getStoreKeeper() );
      }
      catch ( SQLException sqlx ) {
	throw new SQLRuntimeException( sqlx );
      }
      catch ( Exception x ) {
	x.printStackTrace();
	return null;
      }

      return bobo2;
    }
  }
}
