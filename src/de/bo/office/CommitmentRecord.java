package de.bo.office;

import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import de.bo.base.dbs.*;

// Das ist die Verbindlichkeit.

public class CommitmentRecord extends BillRecord
{
    protected Object supplier; 
  
    public CommitmentRecord(AbstractTable table) {
	super(table);
    }

    public boolean create()
	throws SQLException {

	supplier = null;

	return super.create();
    }

    protected void makeDataAbstract() {
	super.makeDataAbstract();

	record[10] = supplier;     
	record[11] = state;
	record[12] = bookDate;
	record[13] = department;
  }

    protected void makeDataConcrete() {
	super.makeDataConcrete();

	supplier      = record[10];
	state         = DBUtilities.objectToString(record[11]);
	bookDate      = DBUtilities.objectToString(record[12]);
	department    = record[13];
    }

    public Object getSupplier() {
	return supplier;
    }

    public void setSupplier(Object s) {
	this.supplier = s;
    }
}
