package de.bo.base.store2.sql;

import de.bo.base.store2.*;
import java.util.*;

public class OrSelection extends SelectionContainer
{
  public OrSelection(SQLToolkit toolkit) {
    super(toolkit);
  }

  protected String getOperatorString() {
    return toolkit.getOperatorString(SQLToolkit.OR);
  }
}
