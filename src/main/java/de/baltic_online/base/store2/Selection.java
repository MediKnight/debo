package de.baltic_online.base.store2;

public interface Selection
{
  public void setIdentifier(String identifier);

  public String getIdentifier();

  public void setValue(Object value);

  public Object getValue();

  public void setOperation(int operation);

  public int getOperation();
}
