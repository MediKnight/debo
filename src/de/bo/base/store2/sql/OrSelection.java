package de.bo.base.store2.sql;

public class OrSelection extends SelectionContainer {
	public OrSelection(SQLToolkit toolkit) {
		super(toolkit);
	}

	protected String getOperatorString() {
		return toolkit.getOperatorString(SQLToolkit.OR);
	}
}
