package de.baltic_online.base.store.sql;

import de.baltic_online.base.store.*;

public class OrSelection extends SelectionContainer {
	public OrSelection(StoreToolkit toolkit) {
		super(toolkit);
	}

	protected String getOperatorString() {
		return ((SQLToolkit) toolkit).getOperatorString(SQLToolkit.OR);
	}
}
