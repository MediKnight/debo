package de.bo.base.store.sql;

import de.bo.base.store.*;
import java.sql.*;

public abstract class Database implements StoreKeeper
{
  protected static Connection defaultConnection;
  protected static SQLToolkit defaultToolkit;

  protected Connection connection;
  protected SQLToolkit toolkit;

  static {
    defaultConnection = null;
    defaultToolkit = new SQLToolkit();
  }

  public static void setDefaultConnection(Connection conn) {
    defaultConnection = conn;
  }

  public Database() {
    this( null, null );
  }
  public Database(Connection connection) {
    this( connection, null );
  }
  public Database(SQLToolkit toolkit) {
    this( null, toolkit );
  }
  public Database(Connection conn,SQLToolkit tk) {
    connection = (conn == null) ? defaultConnection : conn;
    toolkit = (tk == null) ? defaultToolkit : toolkit;
  }

  public void setStoreToolkit(StoreToolkit storeToolkit) {
    toolkit = (SQLToolkit)storeToolkit;
  }
  public StoreToolkit getStoreToolkit() {
    return toolkit;
  }

  public Connection getConnection() {
    return connection;
  }

  public abstract Object createKey(Storable object);
  public abstract boolean retrieve(Storable object,Object[] data,Object key);
  public abstract boolean insert(Storable object,Object[] data,Object key);
  public abstract boolean store(Storable object,Object[] data,Object key);
  public abstract boolean delete(Storable object,Object key);
}
