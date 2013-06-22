package de.baltic_online.office;

import java.sql.*;

import de.baltic_online.base.dbs.*;

public class TelefonTable extends AbstractTable {
    public TelefonTable() {
        super();
    }
    public TelefonTable(Connection conn) {
        super(conn);
    }

    public String getIdentifier() {
        return "telefon";
    }
    protected String getOrderClause() {
        return "land,vorwahl,anschluss,durchwahl";
    }
}
