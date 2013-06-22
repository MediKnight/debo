package de.baltic_online.office;

import java.sql.*;
import de.baltic_online.base.dbs.*;
import de.baltic_online.base.util.*;

public class BillRecord extends AbstractRecord
{
    protected String state;          // Status
    protected String number;         // Rechnungsnr.
    protected String type;           // Art
    protected String remark;         // Bemerkung
    protected String date;           // Datum
    protected String due;            // Fällig
    protected String paid;           // Bezahlt
    protected Long amount;           // Betrag
    protected Long total;            // Gesamt
    protected Integer tax;           // MwSt.
    protected String bookDate;       // Buchungs-Datum
    protected Object department;     // Abteilung

    protected static Integer defaultTax = new Integer(1600);

    public BillRecord(AbstractTable table) {
	super(table);
    }

    public boolean create()
	throws SQLException {

	state = number = type = remark = date = due = paid = bookDate = "" ;
	amount = total = new Long(0L);
	tax = defaultTax;
	department = null;

	return super.create();
    }

    protected void makeDataAbstract() {
	record[1] = number;
	record[2] = date;
	record[3] = amount;
	record[4] = tax;
	record[5] = total;
	record[6] = type;
	record[7] = due;
	record[8] = paid;
	record[9] = remark;  
    }

    protected void makeDataConcrete() {
	number   = DBUtilities.objectToString(record[1]);
	date     = DBUtilities.objectToString(record[2]);  
	amount   = DBUtilities.objectToLong(record[3]);
	tax      = DBUtilities.objectToInteger(record[4]) ;
	total    = DBUtilities.objectToLong(record[5]);
	type     = DBUtilities.objectToString(record[6]);
	due      = DBUtilities.objectToString(record[7]);
	paid     = DBUtilities.objectToString(record[8]);
	remark   = DBUtilities.objectToString(record[9]);
    }

    protected Object createKey()
	throws SQLException {
	return DBUtilities.createKey(table.getConnection(),"id2");
    }

    public CurrencyNumber getCurrencyNumber(String s) {
	long l;
	try {
	    l = Long.parseLong(s);
	}
	catch (NumberFormatException e) {
	    l = 0;
	}
	return new CurrencyNumber(l);
    }

    public Integer getInteger(String s) {
	Integer i;
	try {
	    i = new Integer(s);
	}
	catch (NumberFormatException e) {
	    i = new Integer(0);
	}
	return i;
    }

    public char getState() {
	return (state==null | state.length()==0) ? ' ' : state.charAt(0);
    }

    public String getNumber() {
	return number;
    }

    public String getType() {
	return type;
    }

    public String getRemark() {
	return remark;
    }

    public CurrencyNumber getAmount() {
	return new CurrencyNumber(amount.longValue());
    }

    public CurrencyNumber getTotal() {
	return new CurrencyNumber(total.longValue());
    }

    public String getDate() {
	return date;
    }

    public String getDue() {
	return due;
    }

    public String getPaid() {
	return paid;
    }

    public Integer getTax() {
	return tax;
    }

    public String getBookDate() {
	return bookDate;
    }

    public Object getDepartment() {
	return department;
    }

    public static void setDefaultTax(int t) {
	defaultTax = new Integer(t);
    }

    public static void setDefaultTax(double t) {
	defaultTax = new Integer((int)(t*100.0+0.5));
    }

    public void setState(char c) {
	state = (c==' ') ? "" : new Character(c).toString();
    }

    public void setNumber(String number) {
	this.number = number;
    }

    public void setDate(String s) {
	date = s;
    }

    public void setDue(String s) {
	due = s;
    }

    public void setPaid(String s) {
	paid = s;
    }

    public void setBookDate(String s) {
	bookDate = s;
    }

    public void setType(String s) {
	type = s;
    }

    public void setRemark(String s) {
	remark = s;
    }

    public void setDepartment(Object key) {
	department = key;
    }

    public void setAmount(CurrencyNumber am) {
	amount = new Long(am.longValue());
    }

    public void setTotal(CurrencyNumber t) {
	total = new Long(t.longValue());
    }

    public void setTax(int t) {
	tax = new Integer(t);
    }

    public void setTax(double t) {
	tax = new Integer((int)(t*100.0+0.5));
    }

    public CurrencyNumber calcTotal() {
	if ( amount == null || tax == null )
	    return null;

	CurrencyNumber n = getAmount();
	return n.mul(1.0+tax.doubleValue()/10000.0);
    }

    public CurrencyNumber calcTaxAmount() {
	if ( amount == null || tax == null )
	    return null;

	// CurrencyNumber n = getAmount();
	// return n.mul( tax.doubleValue()/10000.0 );

	return getTotal().sub(getAmount());
    }
}
