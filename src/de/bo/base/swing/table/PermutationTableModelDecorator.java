package de.bo.base.swing.table;

import javax.swing.event.*;
import javax.swing.table.*;

import de.bo.base.util.Permutation;

public class PermutationTableModelDecorator implements TableModel
{
  protected TableModel model;
  protected Permutation permutation;

  public PermutationTableModelDecorator(TableModel model) {
    this( model, null );
  }
  public PermutationTableModelDecorator(TableModel model,
					Permutation permutation) {
    this.model = model;
    this.permutation = permutation;

    if ( permutation == null )
      this.permutation = new Permutation( getRowCount() );

    if ( this.permutation.getArray().length != getRowCount() )
      throw new IllegalArgumentException();
  }

  public int getRowCount() {
    return model.getRowCount();
  }

  public int getColumnCount() {
    return model.getColumnCount();
  }

  public String getColumnName(int columnIndex) {
    return model.getColumnName( columnIndex );
  }

  public Class<?> getColumnClass(int columnIndex) {
    return model.getColumnClass( columnIndex );
  }

  public boolean isCellEditable(int rowIndex,int columnIndex) {
    return model.isCellEditable( permutation.getArray()[rowIndex],
				 columnIndex );
  }

  public Object getValueAt(int rowIndex,int columnIndex) {
    return model.getValueAt( permutation.getArray()[rowIndex],
			     columnIndex );
  }

  public void setValueAt(Object aValue,
			 int rowIndex,
			 int columnIndex) {
    model.setValueAt( aValue,
		      permutation.getArray()[rowIndex],
		      columnIndex );
  }

  public void addTableModelListener(TableModelListener l) {
    model.addTableModelListener( l );
  }

  public void removeTableModelListener(TableModelListener l) {
    model.removeTableModelListener( l );
  }
}
