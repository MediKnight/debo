
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package de.bo.office;

import java.sql.Connection;

import de.bo.base.dbs.AbstractTable;

/**
 *
 */
public abstract class AbstractBillTable extends AbstractTable {

  public static final int BILL_DATE = 1;
  public static final int BOOK_DATE = 2;

  int dateOrder = BILL_DATE;


  /**
   *
   */
  public AbstractBillTable() {
    super();
  }

  /**
   *
   */
  public AbstractBillTable( Connection connection ) {
    super( connection );
  }


  /**
   *
   */
  public void setDateOrder( int order ) {
    dateOrder = order;
  }

  /**
   *
   */
  public int getDateOrder() {
    return dateOrder;
  }

  /**
   *
   */
  public String getOrderColumn() {
//     if( dateOrder == BILL_DATE ) {
//       //
//       // Sortieren der Tabelle nach dem Rechnungsdatum.
//       //

//       return "datum";
//     } else if( dateOrder == BOOK_DATE ) {
//       //
//       // Die Tabelle soll nach dem Buchungsdatum sortiert werden.
//       //

//       return "buchung";
//     } else {
//       //
//       // Standard ist, die Tabelle nach dem Rechnungsdatum zu sortieren.
//       //

      return "buchung";
  }

  public String getOrderColumn(int which) {
    return (which==1) ? "buchung" : "datum";
  }

  /**
   *
   */
  protected String getOrderClause() {
    //    return getOrderColumn() + ",nr";
    return "datum,nr,buchung";
  }
}
