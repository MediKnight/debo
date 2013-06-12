package de.bo.base.store.sql;

import de.bo.base.store.*;

import java.util.*;

public abstract class SelectionContainer extends DefaultSelection
{
  protected Stack<Selection> stack;

  public SelectionContainer(StoreToolkit toolkit) {
    super( toolkit );

    stack = new Stack<Selection>();
  }

  /**
   * Hinzuf�gen einer Selektion.
   *
   * @param selection Selektion, die hinzugef�gt werden soll
   */
  public void addSelection(Selection selection) {
    stack.push( selection );
  }

  /**
   * Entfernen der letzten Selektion.
   *
   * @return letzte Selektion, falls Stapel nicht leer, sonst
   * <code>null</code>
   */
  public Selection removeSelection() {
    if ( !stack.isEmpty() )
      return stack.pop();
    return null;
  }

  /**
   * Entfernen aller Selektionen.
   */
  public void removeSelections() {
    while ( !stack.isEmpty() )
      stack.pop();
  }

  /**
   * String-Darstellung.
   *
   * @return String-Darstellung
   * @see DefaultSelection#toString
   */
  public String toString() {
    int n = stack.size();

    if ( n == 0 ) return FALSE.toString();
    if ( n == 1 ) return stack.elementAt( 0 ).toString();

    StringBuffer sb = new StringBuffer();
    String ops = getOperatorString();

    sb.append( "(" );
    for ( int i=0; i<n; i++ ) {
      if ( i > 0 ) sb.append( " "+ops+" " );
      sb.append( stack.elementAt( i ).toString() );
    }
    sb.append( ")" );

    return sb.toString();
  }

  protected abstract String getOperatorString();
}
