package de.baltic_online.office;

import java.sql.*;

import de.baltic_online.base.dbs.*;

public class EMailRecord extends ExternalRecord {
    protected String username;
    protected String domain;
    protected String remark;

    public EMailRecord(AbstractTable table) {
        super(table);
    }

    public boolean create() throws SQLException {

        username = domain = remark = "";
        return super.create();
    }

    protected void makeDataAbstract() {
        record[4] = username;
        record[5] = domain;
        record[6] = remark;
    }
    protected void makeDataConcrete() {
        username = DBUtilities.objectToString(record[4]);
        domain = DBUtilities.objectToString(record[5]);
        remark = DBUtilities.objectToString(record[6]);
    }

    protected Object createKey() throws SQLException {
        return DBUtilities.createKey(table.getConnection(), "id");
    }

    public String getName() {
        return username;
    }

    public String getDomain() {
        return domain;
    }

    public String getRemark() {
        return remark;
    }

    public String toString() {
        return username + "@" + domain;
    }

    public void set(String username, String domain, String remark) {
        this.username = username;
        this.domain = domain;
        this.remark = remark;
    }

    public void setName(String sname) {
        this.username = sname;
    }
    public void setDomain(String sname) {
        this.domain = sname;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
