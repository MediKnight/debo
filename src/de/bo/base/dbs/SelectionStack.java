package de.bo.base.dbs;

public interface SelectionStack {
	public void addSelection(TableSelection sel);
	public TableSelection removeSelection();
	public void removeSelections();
}
