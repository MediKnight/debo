package de.bo.base.store2;

public interface ObjectFilter
{
  public boolean accept(Storable object)
    throws StoreException;
}
