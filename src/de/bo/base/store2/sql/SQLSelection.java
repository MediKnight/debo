package de.bo.base.store2.sql;

import java.util.*;

import de.bo.base.store2.*;

public class SQLSelection
  implements Selection
{
  /**
   * Diese Instanz repräsentiert die niemals wahre Selektion.
   *
   * Wird diese Instanz auf eine Tabelle angewandt, so ist die
   * Tabelle leer.
   */
  public final static SQLSelection FALSE = new FalseSelection();

  /**
   * Diese Instanz repräsentiert die stets wahre Selektion.
   *
   * Wird diese Instanz auf eine Tabelle angewandt, so bleibt die
   * Tabelle unverändert.
   */
  public final static SQLSelection TRUE = new TrueSelection();

  protected SQLToolkit toolkit;
  protected String identifier;
  protected Object value;
  protected int operation;
  protected boolean ignoreCase;

  protected SQLSelection() {
  }

  protected SQLSelection(SQLToolkit toolkit) {
    this.toolkit = toolkit;
  }

  public SQLSelection(SQLToolkit toolkit,String identifier,
		      Object value) {
    this(toolkit,identifier,value,SQLToolkit.EQUALS,false);
  }

  public SQLSelection(SQLToolkit toolkit,String identifier,
		      Object value,int operation) {
    this(toolkit,identifier,value,operation,false);
  }

  public SQLSelection(SQLToolkit toolkit,String identifier,
		      Object value,int operation,
		      boolean ignoreCase) {

    // Die Reihenfolge ist hier wichtig!
    setToolkit(toolkit);
    setIdentifier(identifier);
    setValue(value);
    setOperation(operation);
    setIgnoreCase(ignoreCase);
  }

  public void setToolkit(SQLToolkit toolkit) {
    this.toolkit = (toolkit!=null) ? toolkit : new SQLToolkit();
  }

  public SQLToolkit getToolkit() {
    return toolkit;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setValue(Object value) {
    if ( value != null && value instanceof String &&
	 value.toString().equalsIgnoreCase(toolkit.getNullString()) )
      value = null;

    this.value = value;
  }

  public Object getValue() {
    return value;
  }

  public void setOperation(int operation) {
    if ( value == null ) {
      if ( operation == SQLToolkit.EQUALS )
	operation = SQLToolkit.IS;

      if ( operation == SQLToolkit.NEQUALS )
	operation = SQLToolkit.ISNOT;

      if ( operation != SQLToolkit.IS && operation != SQLToolkit.ISNOT )
	throw new IllegalArgumentException();
    }

    this.operation = operation;
  }

  public int getOperation() {
    return operation;
  }

  public void setIgnoreCase(boolean ignoreCase) {
    this.ignoreCase = ignoreCase && value != null &&
      value instanceof String;

    // das müssen wir machen, denn sonst nützt ignoreCase gar nichts.
    if ( this.ignoreCase )
      value = ((String)value).toLowerCase();
  }

  public boolean isIgnoreCase() {
    return ignoreCase;
  }

  /**
   * String-Darstellung.
   *
   * Diese Darstellung wird in der Where-Klausel der Select-Kommandos
   * der betreffenden Tabelle verwendet. Der Vergleichswert wird
   * durch ein '?' dargestellt, damit der String im sog.
   * <tt>PreparedStatement</tt> verwendet werden kann.
   * Der oder die Vergleichswerte selbst werden mit
   * <tt>getValues()</tt> abgefragt.
   *
   * @return String-Darstellung
   * @see #getValues()
   */
  public String toString() {

    if ( identifier == null )
      return TRUE.toString();

    if ( value == null )
      return identifier+" "+toolkit.getOperatorString(operation)+
	" "+toolkit.getNullString();

    StringBuffer sb = new StringBuffer();

    if ( ignoreCase )
      sb.append(toolkit.getLowerFunctionName()+"("+identifier+")");
    else
      sb.append(identifier);

    sb.append(" "+toolkit.getOperatorString(operation)+" ?");

    return sb.toString();
  }

  public Collection getValues() {
    Collection c = new LinkedList();
    c.add(value);
    return c;
  }

  static class FalseSelection extends SQLSelection
  {
    public String toString() {
      return "0=1";
    }
    public Collection getValues() {
      return new LinkedList();
    }
  }

  static class TrueSelection extends SQLSelection
  {
    public String toString() {
      return "1=1";
    }
    public Collection getValues() {
      return new LinkedList();
    }
  }
}
