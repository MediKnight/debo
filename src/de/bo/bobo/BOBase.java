package de.bo.bobo;

import de.bo.base.store2.*;
import de.bo.base.store2.sql.*;

import java.sql.*;

/**
 * Implementierung einer Datenbank-Klasse, optimiert für die
 * Baltic-Online Datenbank.
 */

public class BOBase extends SQLBase
{
    public BOBase() {
	super(new OracleToolkit());
    }

    public BOBase(Connection connection) {
	super(connection,new OracleToolkit());
    }

    public BOBase(SQLToolkit toolkit) {
	super(toolkit);
    }

    public BOBase(Connection connection,SQLToolkit toolkit) {
	super(connection,toolkit);
    }

    public Object createKey(Storable object)
	throws StoreException {

	return toolkit.createKey(connection,
				 ((Bobo)object).getSequenceIdentifier());
    }
}
