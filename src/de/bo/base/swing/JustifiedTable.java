
package de.bo.base.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class JustifiedTable extends JTable
{
  private static final long serialVersionUID = 1L;
  
  protected TableJustifier justifier;

  public JustifiedTable(TableModel tm,TableJustifier justifier) {
    super( tm );
    this.justifier = justifier;
  }

  public Component prepareRenderer(TableCellRenderer renderer,
                                   int row,int column) {

    Component comp = super.prepareRenderer( renderer, row, column );
    if ( comp instanceof DefaultTableCellRenderer ) {
      DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer)comp;
      int align = justifier.getColumnAlign( column );
      switch ( align ) {
      case SwingConstants.LEFT:
	dtcr.setHorizontalAlignment( JLabel.LEFT );
	break;
      case SwingConstants.CENTER:
	dtcr.setHorizontalAlignment( JLabel.CENTER );
	break;
      case SwingConstants.RIGHT:
	dtcr.setHorizontalAlignment( JLabel.RIGHT );
	break;
      }
    }
    return comp;
  }

  public void setColumnWidths(int[] relw) {
    int n = relw.length;
    TableColumnModel tcm = getColumnModel();
    if ( n != tcm.getColumnCount() )
      throw new IllegalArgumentException();

    int totw = 0;
    int sum = 0;

    for ( int i=0; i<n; i++ ) {
      TableColumn tc = tcm.getColumn( i );
      totw += tc.getWidth();
      sum += relw[i];
    }

    double scale = (double)totw / (double)sum;

    for ( int i=0; i<n; i++ ) {
      TableColumn tc = tcm.getColumn( i );
      int nw = (int)(scale*relw[i]);
      tc.setPreferredWidth( nw );
    }
  }
}
