package de.baltic_online.base.dbs;

public interface TableConsumer
{
  public void startConsume();
  public void endConsume();
  public void consume(Object[] data,int recordnr);
}
