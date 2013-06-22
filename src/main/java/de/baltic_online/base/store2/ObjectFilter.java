package de.baltic_online.base.store2;

public interface ObjectFilter
{
  public boolean accept(Storable object)
    throws StoreException;
}
