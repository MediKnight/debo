package de.bo.base.dbs;

import java.util.*;

public interface SelectionStack
{
  public void addSelection(TableSelection sel);
  public TableSelection removeSelection();
  public void removeSelections();
}
