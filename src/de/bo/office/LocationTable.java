package de.bo.office;

import java.sql.*;

import de.bo.base.dbs.*;

public class LocationTable extends AbstractTable {
    public LocationTable() {
        super();
    }
    public LocationTable(Connection conn) {
        super(conn);
    }

    public String getIdentifier() {
        return "standort";
    }
    public String getKeyIdentifier() {
        return "sid";
    }
    protected String getOrderClause() {
        return "sname";
    }
}
