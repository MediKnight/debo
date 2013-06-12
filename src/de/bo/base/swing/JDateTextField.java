package de.bo.base.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.util.Date;
import de.bo.base.swing.text.*;

public class JDateTextField extends JTextField
                            implements ActionListener
{
  private static final long serialVersionUID = 1L;
  
  private Date date;

  public JDateTextField() { this(new Date()); }

  public JDateTextField(Date d)
  {
    super();
    date = d;
    DocumentDecorator.updateDocumentDecorator(
      this,
      new DateDocumentDecorator(this.getDocument(), null)
    );

    DocumentDecorator.updateDocumentDecorator(
      this,
      new LengthLimitedDocumentDecorator(this.getDocument(), 10)
    );

    addActionListener(this);
  }

  public void setDate(Date d) { date = d; }
  public Date getDate() { return date; }

  public void actionPerformed(ActionEvent e)
  {
		String err = checkValue();
		if( err != null )
    {
			Toolkit.getDefaultToolkit().beep();
      JOptionPane.showMessageDialog( null, err, "Achtung   ",
			JOptionPane.WARNING_MESSAGE );
		}
    else
    {
//      super.postActionEvent();
    }
  }

  private String checkValue()
  {
    DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);
    try
    {
      date = formatter.parse(this.getText());
    }
    catch (Exception ex)
    {
      return "Es wurde ein ung�ltiges Datum eingegeben. Richtiges Format: dd.mm.yy";
    }
    return null;
  }

  public static void main( String args[] )
  {
    JFrame f = new JFrame();
    JDateTextField tf = new JDateTextField();
    f.getContentPane().add(tf);
    f.addWindowListener(new WindowAdapter()
    {
      public void windowClosed(WindowEvent e)
      {
        System.exit(0);
      }
    });
    f.pack();
    f.setVisible(true);
  }
}
