package de.bo.base.store2.sql;

import java.sql.*;

import de.bo.base.store2.*;

public class OracleToolkit extends SQLToolkit
{
  public OracleToolkit() {
    super();
  }

  public Object createKey(Connection connection,Object param)
    throws StoreException {

    Object key = null;
    // Annahme: param repräsentiert Sequence-ID
    String seqid = param.toString();

    try {
      String query = "select "+seqid+".nextval from dual";
      Statement sm = connection.createStatement();
      ResultSet rset = sm.executeQuery(query);

      if ( rset.next() )
	key = rset.getObject( 1 );

      rset.close();
      sm.close();

      return key;
    }
    catch ( SQLException x ) {
      // Exception-Delegation:
      throw new StoreException(x);
    }
  }
}
