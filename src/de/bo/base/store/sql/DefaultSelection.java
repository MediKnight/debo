package de.bo.base.store.sql;

import de.bo.base.store.*;

public class DefaultSelection
  implements Selection
{
  /**
   * Diese Instanz repräsentiert die niemals wahre Selektion.
   *
   * Wird diese Instanz auf eine Tabelle angewandt, so ist die
   * Tabelle leer.
   */
  public final static DefaultSelection FALSE = new FalseSelection();

  /**
   * Diese Instanz repräsentiert die stets wahre Selektion.
   *
   * Wird diese Instanz auf eine Tabelle angewandt, so bleibt die
   * Tabelle unverändert.
   */
  public final static DefaultSelection TRUE = new TrueSelection();

  protected StoreToolkit toolkit;
  protected String identifier;
  protected Object value;
  protected int operation;
  protected boolean ignoreCase;

  protected DefaultSelection() {
    toolkit = null;
  }
  protected DefaultSelection(StoreToolkit toolkit) {
    this.toolkit = toolkit;
  }
  public DefaultSelection(StoreToolkit toolkit,String identifier,
			  Object value) {
    this( toolkit, identifier, value, SQLToolkit.EQUALS, false );
  }
  public DefaultSelection(StoreToolkit toolkit,String identifier,
			  Object value,int operation) {
    this( toolkit, identifier, value, operation, false );
  }
  public DefaultSelection(StoreToolkit toolkit,String identifier,
			  Object value,int operation,boolean ignoreCase) {
    this.toolkit = toolkit;
    this.identifier = identifier;
    this.value = value;
    this.operation = operation;
    this.ignoreCase = ignoreCase;
  }

  public void setIgnoreCase(boolean ignoreCase) {
    this.ignoreCase = ignoreCase;
  }

  /**
   * String-Darstellung.
   *
   * Diese Darstellung wird in der Where-Klausel der Select-Kommandos
   * der betreffenden Tabelle verwendet.
   *
   * @return String-Darstellung
   */
  public String toString() {

    if ( identifier == null || value == null )
      return TRUE.toString();

    StringBuffer sb = new StringBuffer();
    SQLToolkit tk = (SQLToolkit)toolkit;

    if ( ignoreCase )
      sb.append( tk.getLowerFunctionName()+"("+identifier+")" );
    else
      sb.append( identifier );

    sb.append( " "+tk.getOperatorString( operation )+" " );

    if ( ignoreCase )
      sb.append( tk.quote( value.toString().toLowerCase() ) );
    else
      sb.append( tk.quote( value.toString() ) );

    return sb.toString();
  }

  protected static class FalseSelection extends DefaultSelection
  {
    public String toString() {
      return "0=1";
    }
  }

  protected static class TrueSelection extends DefaultSelection
  {
    public String toString() {
      return "1=1";
    }
  }
}
