package de.bo.base.store.sql;

public class OracleToolkit extends SQLToolkit
{
  public OracleToolkit() {
    super();
  }

  public char getQuotationChar() {
    return '\'';
  }
  public String getQuotationReplacement() {
    return "''";
  }
  public char getSingleMatchingChar() {
    return '_';
  }
  public char getMultiMatchingChar() {
    return '%';
  }
}
