package de.bo.bobo;

import java.sql.SQLException;
import java.sql.Connection;

import java.util.*;
import java.text.DateFormat;

import de.bo.base.store2.*;
import de.bo.base.store2.sql.*;
import de.bo.base.util.*;

/**
 * Baltic-Online Gesch�ftsobjekt <b>Rechnung</b>.
 * <p>
 */

public abstract class Bill extends Bobo
{
  protected static int defaultTax = 1600;

  /**
   * Erzeugt Rechnung mit Default-Storekeeper
   */
  public Bill() {
    super();
  }

  /**
   * Erzeugt Rechnung mit gegebenen Storekeeper.
   *
   * @param storeKeeper verwendetes Datensystem
   */
  public Bill(StoreKeeper storeKeeper) {
    super(storeKeeper);
  }

  /**
   * Liefert Anzahl der "Eltern".
   *
   * Eine Rechnung hat keine "Eltern" im Sinne der BO-Hierarchie und
   * deshalb liefert diese Funktion <code>0</code>.
   *
   * @return <code>0</code>
   */
  public int getParentCount() {
    return 0;
  }

  /**
   * Erzeugt "Eltern"-Objekt zum gegebenen Index (im Bereich von
   * <code>getParentCount()</code>).
   *
   * Eine Rechnung hat keine "Eltern" im Sinne der BO-Hierarchie und
   * deshalb liefert diese Funktion <code>null</code>.
   *
   * @return <code>null</code>
   */
  protected Bobo createParent(int index) {
    return null;
  }

  /**
   * Liefert "Eltern"-Schl�ssel zum gegebenen Index (im Bereich von
   * <code>getParentCount()</code>).
   *
   * Eine Rechnung hat keine "Eltern" im Sinne der BO-Hierarchie und
   * deshalb liefert diese Funktion <code>null</code>.
   *
   * @return <code>null</code>
   */
  protected Object getParentKey(int index) {
    return null;
  }

  protected void setParentKey(int index,Object key) {
  }

  public boolean getState() {
    return getPaid() != null;
  }

  public String getNumber() {
    return getString(1);
  }

  public Date getDate() {
    return getDateFromString(2);
  }

  public Date getBookDate() {
    return getDateFromString(3);
  }

  public CurrencyNumber getAmount() {
    return getCurrency(4);
  }

  public int getTax() {
    return getInteger(5);
  }

  public CurrencyNumber getTotal() {
    return getCurrency(6);
  }

  public String getType() {
    return getString(7);
  }

  public Object getDepartmentKey() {
    return getObject(8);
  }

  public Date getDue() {
    return getDateFromString(9);
  }

  public Date getPaid() {
    return getDateFromString(10);
  }

  public String getRemark() {
    return getString(11);
  }

  public static void setDefaultTax(int t) {
    defaultTax = t;
  }

  public static void setDefaultTax(double t) {
    defaultTax = (int)(t*100.0+0.5);
  }

  public void setNumber(String number) {
    if ( number == null || number.trim().length() == 0 )
      throw new IllegalArgumentException("empty bill number");

    setString(1,number);
  }

  public void setDate(Date d) {
    if ( d == null )
      d = new Date();

    setDateAsString(2,d);
  }

  public void setBookDate(Date d) {
    setDateAsString(3,d);
  }

  public void setAmount(CurrencyNumber cn) {
    setCurrency(4,cn);
  }

  public void setTax(int t) {
    setInteger(5,t);
  }

  public void setTax(double t) {
    setInteger(5,(int)(t*100.0+0.5));
  }

  public void setTotal(CurrencyNumber cn) {
    setCurrency(6,cn);
  }

  public void setType(String s) {
    setString(7,s);
  }

  public void setDepartmentKey(Object key) {
    setObject(8,key);
  }

  public void setDue(Date d) {
    setDateAsString(9,d);
  }

  public void setPaid(Date d) {
    setDateAsString(10,d);
  }

  public void setRemark(String s) {
    setString(11,s);
  }

  public CurrencyNumber calcTotal() {
    CurrencyNumber amount = getAmount();
    if ( amount == null )
      return new CurrencyNumber(0L);
    else
      return amount.mul(1.0+(double)getTax()/10000.0);
  }

  public CurrencyNumber calcTaxAmount() {
    CurrencyNumber amount = getAmount();
    CurrencyNumber total = getTotal();
    if ( amount == null || total == null )
      return null;

    // CurrencyNumber n = getAmount();
    // return n.mul( tax.doubleValue()/10000.0 );

    return total.sub(amount);
  }

  /**
   * Liefert Namen des Prim�rschl�ssels.
   *
   * (i.A. Spaltenattribute des Prim�rschl�ssels)
   *
   * @return <code>"id"</code>
   */
  public String getKeyIdentifier() {
    return "id";
  }

  public String toString() {
    return getNumber()+": "+
        DateFormat.getDateInstance().format(getDate());
  }

  //
  // Linkage
  //

  /**
   * Deletes bill.
   * <p>
   * First this method deletes all possible links to other bills.
   */
  public void delete()
      throws StoreException {

    removeLink();
    super.delete();
  }

  /**
   * Retrieve the group of links associated with this bill.
   * <p>
   * Each Object of the returned <tt>Collection</tt> is an instance of
   * <tt>XLink</tt>.
   * This method returns <tt>null</tt> if there exists no link for
   * this bill.
   */
  public Collection<XLink> retrieveGroup()
      throws StoreException {

    BOBase boBase = (BOBase)getStoreKeeper();
    XLink xlink = new XLink(boBase);
    Object key = getKey();
    SQLSelection sel = new SQLSelection(boBase.getSQLToolkit(),"oid",key);
    Collection<XLink> coll = boBase.retrieve(xlink,sel);

    if ( coll.size() > 0 ) {
      xlink = (XLink)coll.iterator().next();
      Object gid = xlink.getGroupId();
      sel = new SQLSelection(boBase.getSQLToolkit(),"gid",gid);
      coll = boBase.retrieve(xlink,sel);

      return coll;
    }
    else
      return null;
  }

  /**
   * Remove existing link of this bill.
   */
  public void removeLink()
      throws StoreException {

    BOBase boBase = (BOBase)getStoreKeeper();
    XLink xlink = new XLink(boBase);
    Object key = getKey();
    SQLSelection sel = new SQLSelection(boBase.getSQLToolkit(),"oid",key);
    Collection coll = boBase.retrieve(xlink,sel);

    if ( coll.size() > 0 ) {
      ((XLink)coll.iterator().next()).delete();
    }
  }

  /**
   * Returns <tt>true</tt> if there exist a link.
   */
  public boolean hasLink()
      throws StoreException {

    BOBase boBase = (BOBase)getStoreKeeper();
    XLink xlink = new XLink(boBase);
    Object key = getKey();
    SQLSelection sel = new SQLSelection(boBase.getSQLToolkit(),"oid",key);
    Collection coll = boBase.retrieve(xlink,sel);

    return coll.size() > 0;
  }

  public static Bill createBill(XLink xlink)
      throws StoreException {

    Bill bill;
    BOBase boBase = (BOBase)xlink.getStoreKeeper();
    int side = xlink.getSideIndex();

    if ( side == 0 )
      bill = new Claim(boBase);
    else
      bill = new Liability(boBase);

    bill.retrieve(xlink.getObjectId());

    return bill;
  }

  public static Object createGroup(BOBase boBase,
      Collection coll1,Collection coll2)
          throws StoreException {

    return createGroup(boBase,null,coll1,coll2);
  }

  public static Object createGroup(BOBase boBase,Object gid,
      Collection coll1,Collection coll2)
          throws StoreException {

    Connection c = boBase.getConnection();
    if ( gid == null )
      gid = boBase.createKey(new XLink(boBase));
    else {
      // raise performance
      try {
        synchronized (c) {
          c.setAutoCommit(false);
          XLink.deleteGroup(boBase,gid);
          c.commit();
          c.setAutoCommit(true);
        }
      }
      catch ( SQLException x ) {
        throw new StoreException(x);
      }
    }

    try {
      // raise performance
      synchronized (c) {
        c.setAutoCommit(false);
        addLinks(boBase,gid,coll1,0);
        addLinks(boBase,gid,coll2,1);
        c.commit();
        c.setAutoCommit(true);
      }
    }
    catch ( SQLException x ) {
      throw new StoreException(x);
    }

    return gid;
  }

  public static void addLinks(BOBase boBase,Object gid,
      Collection coll,int sideIndex)
          throws StoreException {

    for ( Iterator i=coll.iterator(); i.hasNext(); ) {
      XLink xlink = new XLink(boBase);
      xlink.setGroupId(gid);
      xlink.setObjectId(((Bobo)i.next()).getKey());
      xlink.setSideIndex(sideIndex);
      xlink.store();
    }
  }
}
