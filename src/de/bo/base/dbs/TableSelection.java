package de.bo.base.dbs;

/**
 * Instanzen dieser Klasse erm�glichen Selektierungen von Tabellen.
 * <p>
 * Jede Instanz repr�sentiert eine Bedingung der Form
 * <code>attribute <b>op</b> wert</code>.
 * <p>
 * Mehrere Instanzen k�nnen auf eine Tabelle angewendet werden.
 * In diesem Fall werden sie mit <code>and</code> verkn�pft.
 * F�r die Behandlung mit <code>or</code> kann die Klasse
 * <code>TableOrSelection</code> benutzt werden.
 * <p>
 * Operator-Konstanten und String-Darstellungen werden von der Klasse
 * <code>DBUtilities</code> geliefert.
 *
 * @see AbstractTable
 * @see DBUtilities
 * @see TableOrSelection
 */

public class TableSelection
{
  /**
   * Diese Instanz repr�sentiert die niemals wahre Selektion.
   *
   * Wird diese Instanz auf eine Tabelle angewandt, so ist die
   * Tabelle leer.
   */
  public final static TableSelection FALSE = new FalseSelection();

  /**
   * Diese Instanz repr�sentiert die stets wahre Selektion.
   *
   * Wird diese Instanz auf eine Tabelle angewandt, so bleibt die
   * Tabelle unver�ndert.
   */
  public final static TableSelection TRUE = new TrueSelection();

  /**
   * Betreffendes Attribut im Ausdruck.
   */
  protected String attribute;

  /**
   * Betreffender Wert im Ausdruck.
   */
  protected Object value;

  /**
   * Betreffender Operator im Ausdruck.
   */
  protected int operator;

  protected boolean ignoreCase;

  /**
   * Neutraler Konstruktor.
   *
   * Setzt Elemente auf <code>null</code>-Werte.
   */
  protected TableSelection() {
    this( null, null, DBUtilities.EQUALS );
  }

  /**
   * Erzeugt Gleichheits-Selektion.
   *
   * F�hrt <code>this( attribute, value, DBUtilities.EQUALS );</code>
   * aus.
   *
   * @param attribute Attribut der Bedingung
   * @param value Wert der Bedingung
   */
  public TableSelection(String attribute,Object value) {
    this( attribute, value, DBUtilities.EQUALS );
  }

  /**
   * Erzeugt Selektion.
   *
   * @param attribute Attribut der Bedingung
   * @param value Wert der Bedingung
   * @param operator Operator-Konstante gem�� <code>DBUtilities</code>
   */
  public TableSelection(String attribute,Object value,int operator) {
    this.attribute = attribute;
    this.value = value;
    this.operator = operator;

    ignoreCase = false;
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
   * @see AbstractTable#getWhereClause
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();
    
    if ( ignoreCase )
      sb.append( DBUtilities.getLowerFunctionName()+"("+attribute+")" );
    else
      sb.append( attribute );

    if ( value == null || value.toString().length() == 0 ) {
      sb.append( " "+DBUtilities.getNullOperatorString( operator ) );
    }
    else {
      sb.append( " "+DBUtilities.getOperatorString( operator )+" " );

      if ( ignoreCase )
	sb.append( DBUtilities.quote( value.toString().toLowerCase() ) );
      else
	sb.append( DBUtilities.quote( value.toString() ) );
    }

    return sb.toString();
  }

  /**
   * Vergleichsfunktion.
   *
   * Selektionen sind gleich, wenn Operatoren, Werte und Attribute
   * (ohne Ber�cksichtigung von Gro�/Kleinschreibung) gleich sind.
   *
   * @param o zu vergleichende Selektion
   * @return boolescher Ausdruck des Vergleichs
   */
  public boolean equals(Object o) {
    TableSelection sel = (TableSelection)o;
    return attribute.equalsIgnoreCase( sel.attribute ) &&
      value.equals( sel.value ) &&
      operator == sel.operator;
  }
}

class FalseSelection extends TableSelection
{
  public String toString() {
    return "0=1";
  }
}

class TrueSelection extends TableSelection
{
  public String toString() {
    return "1=1";
  }
}
