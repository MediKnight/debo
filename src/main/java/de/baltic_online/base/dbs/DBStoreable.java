package de.baltic_online.base.dbs;

import java.sql.*;

public interface DBStoreable
{
  public boolean store() throws SQLException;
  public boolean delete() throws SQLException;
  public boolean retrieve() throws SQLException;
}
