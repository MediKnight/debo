package de.bo.base.dbs;

import java.util.*;

/**
 * Eine Instanz dieser Klasse ermöglicht eine Selektion von Tabellen
 * gemäß der <code>or</code> oder <code>and</code>-Verknüpfung.
 * <p>
 * Eine Instanz dieser Klasse beinhaltet mehrere
 * <code>TableSelection</code>-Objekte, die dann in der String-Darstellung
 * mit <code>or</code> oder <code>and</code> verknüpft werden.
 *
 * @see TableOrSelection
 * @see TableAndSelection
 */

public abstract class BooleanTableSelection extends TableSelection
  implements SelectionStack
{
  /**
   * Vektor von <code>TableSelection</code>-Objekten.
   */
  protected Stack selection;

  /**
   * Erzeugt leeres Objekt.
   *
   * Leere Objekte sind semantisch identisch mit
   * <code>TableSelection.FALSE</code>.
   */
  public BooleanTableSelection() {
    selection = new Stack();
  }

  /**
   * Erzeugt Objekt mit initialer Gleichheits-Selektion.
   *
   * Führt <code>this( attribute, value, DBUtilities.EQUALS );</code>
   * aus.
   *
   * @param attribute Attribut der initialen Bedingung
   * @param value Wert der initialen Bedingung
   */
  public BooleanTableSelection(String attribute,Object value) {
    this( attribute, value, DBUtilities.EQUALS );
  }

  /**
   * Erzeugt Objekt mit initialer Selektion.
   *
   * @param attribute Attribut der initialen Bedingung
   * @param value Wert der initialen Bedingung
   * @param operator Operator-Konstante gemäß <code>DBUtilities</code>
   * der initialen Bedingung
   */
  public BooleanTableSelection(String attribute,Object value,int operator) {
    selection = new Stack();
    addSelection( new TableSelection( attribute, value, operator ) );
  }

  /**
   * Hinzufügen einer Selektion.
   *
   * @param sel Selektion, die hinzugefügt werden soll
   */
  public void addSelection(TableSelection sel) {
    selection.push( sel );
  }

  /**
   * Hinzufügen einer Selektion.
   *
   * @param attribute Attribut der Bedingung
   * @param value Wert der Bedingung
   * @param operator Operator-Konstante gemäß <code>DBUtilities</code>
   */
  public void addSelection(String attribute,Object value,int operator) {
    addSelection( new TableSelection(attribute,value,operator) );
  }

  /**
   * Entfernen der letzten Selektion.
   *
   * @return letzte Selektion, falls Stapel nicht leer, sonst
   * <code>null</code>
   */
  public TableSelection removeSelection() {
    if ( !selection.isEmpty() )
      return (TableSelection)selection.pop();
    return null;
  }

  /**
   * Entfernen aller Selektionen.
   */
  public void removeSelections() {
    while ( !selection.isEmpty() )
      selection.pop();
  }

  public abstract String getOperatorString();

  /**
   * String-Darstellung.
   *
   * Der zurückgelieferte String ist eine geklammerte Hintereinanderausführung
   * von <code>and/or</code>-Operatoren bzgl. der enthaltenen
   * <code>TableSelection</code>-Objekte.
   *
   * Diese Darstellung wird in der Where-Klausel der Select-Kommandos
   * der betreffenden Tabelle verwendet.
   *
   * @return String-Darstellung
   * @see TableSelection#toString
   * @see AbstractTable#getWhereClause
   */
  public String toString() {
    int n = selection.size();

    if ( n == 0 ) return FALSE.toString();
    if ( n == 1 ) return selection.elementAt( 0 ).toString();

    StringBuffer sb = new StringBuffer();
    String or = DBUtilities.getOperatorString(DBUtilities.OR);
    String op = getOperatorString();
    sb.append( "(" );
    for ( int i=0; i<n; i++ ) {
      if ( i > 0 ) sb.append( " "+op+" " );
      sb.append( selection.elementAt( i ).toString() );
    }
    sb.append( ")" );

    return sb.toString();
  }
}
