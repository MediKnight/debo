package de.bo.base.store;

public interface StoreToolkit
{
  public String objectToString(Object o);
  public Object stringToObject(String s);
  public boolean objectToBoolean(Object o);
  public Object booleanToObject(boolean b);
}
