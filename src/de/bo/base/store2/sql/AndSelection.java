package de.bo.base.store2.sql;

import de.bo.base.store2.*;
import java.util.*;

public class AndSelection extends SelectionContainer
{
  public AndSelection(SQLToolkit toolkit) {
    super(toolkit);
  }

  protected String getOperatorString() {
    return toolkit.getOperatorString(SQLToolkit.AND);
  }
}
