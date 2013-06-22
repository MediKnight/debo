package de.baltic_online.office;

import java.sql.*;

import de.baltic_online.base.dbs.*;

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
