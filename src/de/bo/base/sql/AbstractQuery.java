package de.bo.base.sql;

import java.sql.SQLException;
import java.util.Vector;

public abstract class AbstractQuery
{
  protected Vector<QueryInputData> inputData;

  public AbstractQuery(TableCache cache)
  {
    this( cache, null );
  }
  public AbstractQuery(TableCache cache,TableAttribute[] ta)
  {
    inputData = new Vector<QueryInputData>( 20, 10 );
    addInputData( cache, ta );
  }

  public void addInputData(TableCache cache,TableAttribute[] ta)
  {
    QueryInputData qi = new QueryInputData( cache, ta );
    inputData.addElement( qi );
  }

  public QueryOutputData[] runQuery()
    throws SQLException
  {
    Vector<QueryOutputData> qo = new Vector<QueryOutputData>( 100, 20 );
    QueryInputData top = inputData.elementAt( 0 );

    runQuery( qo, top );

    if ( qo.size() > 0 ) {
      Object[] tmp = qo.toArray();
      return (QueryOutputData[])tmp;
    }
    return null;
  }
  protected void runQuery(Vector<QueryOutputData> qo,QueryInputData qi)
    throws SQLException
  {
    TableCache tc = qi.getTableCache();
    int n = tc.getRecords();
    int onr = tc.getRecordIndex();

    // Iteriere �ber alle Datens�tze ...
    for ( int i=0; i<n; i++ ) {

      // Lade Zeile an Position i ...
      tc.goToRecord( i );
      Object[] oa = tc.getCurrentData();

      // Iteriere nun �ber alle Attribute in der Zeile i.
      // Die Position 0 wird ausgelassen, da das die
      // Schl�sselposition ist.
      for ( int j=1; j<oa.length; j++ ) {

	// Von der Tabelle gesetzten Attribute:
	TableAttribute ta = tc.getAttribute( j-1 );

	// Existiert ein Attribut-Array in den Eingabe-Daten?
	if ( qi.getAttribute(0) == null ) {
	  // Wenn nicht, dann ber�cksichtigen wir alle Felder in der Suche.
	  if ( find( oa[j] ) )
	    qo.addElement( new QueryOutputData( tc, oa[0], ta ) );
	}
	else {
	  // Andernfalls pr�fen wir, ob das vorhende Attribut zu den
	  // geforderten Attributen geh�rt.
	  int ac = qi.getAttributeCount();
	  for ( int k=0; k<ac; k++ )
	    if ( ta.equals( qi.getAttribute(k) ) ) {
	      if ( find( oa[j] ) )
		qo.addElement( new QueryOutputData( tc, oa[0], ta ) );
	      break;
	    }
	}
      }  // endfor j
    } // endfor i

    // Urspr�ngliche Datensatz-Position wiederherstellen.
    tc.goToRecord( onr );
  }

  public abstract boolean find(Object toFind);
}
