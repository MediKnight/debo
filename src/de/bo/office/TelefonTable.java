package de.bo.office;

import java.sql.*;

import de.bo.base.dbs.*;

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
