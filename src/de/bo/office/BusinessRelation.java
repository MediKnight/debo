package de.bo.office;

public interface BusinessRelation
{
  public boolean isCustomer();
  public boolean isSupplier();
  public boolean isPartner();

  public void setCustomer(boolean customer);
  public void setSupplier(boolean supplier);
  public void setPartner(boolean partner);
}
