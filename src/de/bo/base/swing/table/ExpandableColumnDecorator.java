package de.bo.base.swing.table;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ExpandableColumnDecorator implements TableCellRenderer
{
  protected JTable table;
  protected TableColumn column;
  protected TableCellRenderer renderer;
  protected JCheckBox box;
  //  protected JPanel panel;
  protected boolean expanded;
  protected int sizeExpanded,sizeShrinked;

  public ExpandableColumnDecorator(JTable table,
				   TableColumn column,
				   TableCellRenderer renderer) {
    this.table = table;
    this.column = column;
    this.renderer = renderer;

    expanded = true;

    sizeShrinked = 1;
    sizeExpanded = 0;

    //    panel = new JPanel( false );
    box = new JCheckBox();
    box.setSelected( expanded );

    initListener();
  }

  private void initListener() {
    final JTableHeader header = table.getTableHeader();
    header.addMouseListener( new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
	TableColumnModel columnModel = table.getColumnModel();
	int viewColumn = columnModel.getColumnIndexAtX(e.getX()); 
	int columnIndex = table.convertColumnIndexToModel(viewColumn); 
	if ( e.getClickCount() == 1 && columnIndex != -1 &&
	     columnModel.getColumn(columnIndex) == column )
	  toggleExpanded();
      }
    } );
  }

  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
    box.setSelected( expanded );

    if ( sizeExpanded == 0 || !expanded )
      sizeExpanded = column.getWidth();

    int size = (expanded) ? sizeExpanded : sizeShrinked;
    if ( size > 0 )
      column.setPreferredWidth( size );

    table.sizeColumnsToFit( -1 );
  }

  public void toggleExpanded() {
    setExpanded( !expanded );
  }

  public boolean isExpanded() {
    return expanded;
  }

  public Component getTableCellRendererComponent(JTable table,
						 Object value,
						 boolean isSelected,
						 boolean hasFocus,
						 int row,
						 int column) {
    Component c = renderer.getTableCellRendererComponent( table,
							  value,
							  isSelected,
							  hasFocus,
							  row,
							  column );
    if ( row == -1 ) {
      if ( isExpanded() ) {
	box.setText( value.toString() );
      }
      else {
	box.setText( "" );
      }

      return box;
    }

    return c;
  }
}
