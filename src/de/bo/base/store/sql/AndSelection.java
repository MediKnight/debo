package de.bo.base.store.sql;

import de.bo.base.store.StoreToolkit;

public class AndSelection extends SelectionContainer
{
  public AndSelection(StoreToolkit toolkit) {
    super( toolkit );
  }

  protected String getOperatorString() {
    return ((SQLToolkit)toolkit).getOperatorString( SQLToolkit.AND );
  }
}
