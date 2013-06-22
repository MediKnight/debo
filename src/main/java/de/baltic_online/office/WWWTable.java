package de.baltic_online.office;

import java.sql.*;

import de.baltic_online.base.dbs.*;

public class WWWTable extends AbstractTable {
    public WWWTable() {
        super();
    }
    public WWWTable(Connection conn) {
        super(conn);
    }

    public String getIdentifier() {
        return "www";
    }
    protected String getOrderClause() {
        return "url";
    }
}
