package de.bo.base.store2;

public class StoreException extends Exception
{
  private static final long serialVersionUID = 1L;
  public StoreException() {
    super();
  }

  public StoreException(String msg) {
    super(msg);
  }

  public StoreException(Throwable x) {
    super(x.getMessage());
  }
}
