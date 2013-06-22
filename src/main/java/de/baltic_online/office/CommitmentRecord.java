package de.baltic_online.office;

import java.sql.SQLException;

import de.baltic_online.base.dbs.AbstractTable;
import de.baltic_online.base.dbs.DBUtilities;

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
