package de.bo.office2;

/**
 * Baltic-Online Geschäftsobjekte, die dieses Interface implementieren,
 * können Kunde, Lieferant oder Partner sein.
 */

public interface BusinessRelation
{
  /**
   * Liefert <code>true</code>, wenn Objekt ein Kunde ist.
   */
  public boolean isCustomer();

  /**
   * Liefert <code>true</code>, wenn Objekt ein Lieferant ist.
   */
  public boolean isSupplier();

  /**
   * Liefert <code>true</code>, wenn Objekt ein Partner ist.
   */
  public boolean isPartner();

  /**
   * Setzen des <code>isCustomer</code>-Zustands.
   */
  public void setCustomer(boolean customer);

  /**
   * Setzen des <code>isSupplier</code>-Zustands.
   */
  public void setSupplier(boolean supplier);

  /**
   * Setzen des <code>isPartner</code>-Zustands.
   */
  public void setPartner(boolean partner);
}
