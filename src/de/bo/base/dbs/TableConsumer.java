package de.bo.base.dbs;

public interface TableConsumer
{
  public void startConsume();
  public void endConsume();
  public void consume(Object[] data,int recordnr);
}
