package de.baltic_online.office;

import de.baltic_online.base.dbs.*;

public abstract class ExternalRecord extends AbstractRecord
{
  public ExternalRecord(AbstractTable table) {
    super( table );
  }
  public void setEmployeeKey(Object key) {
    record[1] = key;
  }
  public void setPersonKey(Object key) {
    record[2] = key;
  }
  public void setLocationKey(Object key) {
    record[3] = key;
  }
}
