package de.bo.office;

import java.sql.SQLException;

import de.bo.base.dbs.AbstractTable;
import de.bo.base.dbs.DBUtilities;
import de.bo.base.util.CurrencyNumber;

// Das ist die Forderung.

public class ClaimRecord extends BillRecord
{
    protected Object customer;
    protected Object task;
    protected Long profit;

    public ClaimRecord(AbstractTable table) {
	super(table);
    }

    public boolean create()
	throws SQLException {

	profit = new Long(0L);
	customer = task = null;

	return super.create();
    }

    protected void makeDataAbstract() {
	super.makeDataAbstract();

	record[10] = profit;
	record[11] = customer;
	record[12] = task;
	record[13] = state;
	record[14] = bookDate;
	record[15] = department;
    }

    protected void makeDataConcrete() {
	super.makeDataConcrete();

	profit     = DBUtilities.objectToLong(record[10]);
	customer   = record[11];
	task       = record[12];
	state      = DBUtilities.objectToString(record[13]);
	bookDate   = DBUtilities.objectToString(record[14]);
	department = record[15];
    }

    public CurrencyNumber getProfit() {
	return new CurrencyNumber(profit.longValue());
    }

    public Object getCustomer() {
	return customer;
    }

    public Object getTask() {
	return task;
    }

    public void setProfit(CurrencyNumber p) {
	profit = new Long( p.longValue() );
    }

    public void setCustomer(Object c) {
	this.customer = c;
    }

    public void setTask(Object t) {
	this.task = t;
    }
}
