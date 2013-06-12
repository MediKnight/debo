package de.bo.base.store.sql;

import java.sql.*;

public class SQLRuntimeException extends RuntimeException
{
  private static final long serialVersionUID = 1L;
  protected String sqlState;
  protected int errorCode;

  public SQLRuntimeException(SQLException x) {
    super( x.getMessage() );
    sqlState = x.getSQLState();
    errorCode = x.getErrorCode();
  }

  public int getErrorCode() {
    return errorCode;
  }
  public String getSQLState() {
    return sqlState;
  }
}
