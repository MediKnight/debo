package de.bo.base.store.sql;

import de.bo.base.store.*;
import java.util.*;

public class OrSelection extends SelectionContainer
{
  public OrSelection(StoreToolkit toolkit) {
    super( toolkit );
  }

  protected String getOperatorString() {
    return ((SQLToolkit)toolkit).getOperatorString( SQLToolkit.OR );
  }
}
