package de.baltic_online.office2;

import java.util.*;

import de.baltic_online.base.store.*;
import de.baltic_online.base.store.sql.*;

public class CustomerSearch
{
  public final static int COMPANY = 1;
  public final static int PERSON = 2;
  public final static int LOCATION = 4;
  public final static int ALL = COMPANY|PERSON|LOCATION;

  protected String searchString;
  protected boolean ignoreCase;
  protected int tableFlag;

  protected StoreKeeper<Bobo> storeKeeper;

  public CustomerSearch(String searchString) {
    this( searchString, true, ALL );
  }

  public CustomerSearch(String searchString,boolean ignoreCase) {
    this( searchString, ignoreCase, ALL );
  }

  public CustomerSearch(String searchString,
			boolean ignoreCase,
			int tableFlag) {

    this.searchString = searchString;
    this.ignoreCase   = ignoreCase;
    this.tableFlag    = tableFlag;

    storeKeeper = null;

    if ( tableFlag == 0 )
      throw new IllegalArgumentException();
  }

  public void setStoreKeeper(StoreKeeper<Bobo> storeKeeper) {
    this.storeKeeper = storeKeeper;
  }

  public List<Bobo> search() {
    LinkedList<Bobo> l = new LinkedList<Bobo>();
    String[] attribute;

    if ( (tableFlag&COMPANY) != 0 ) {
      attribute = new String[] { "name" };
      if ( storeKeeper != null )
	search( attribute, l, new Company(storeKeeper) );
      else
	search( attribute, l, new Company() );
    }
    if ( (tableFlag&LOCATION) != 0 ) {
      attribute = new String[] { "sname" };
      if ( storeKeeper != null )
	search( attribute, l, new Location(storeKeeper) );
      else
	search( attribute, l, new Location() );
    }
    if ( (tableFlag&PERSON) != 0 ) {
      attribute = new String[] { "name", "vorname" };
      if ( storeKeeper != null )
	search( attribute, l, new Person(storeKeeper) );
      else
	search( attribute, l, new Person() );
    }

    return l;
  }

  protected void search(String[] attribute,LinkedList<Bobo> list,Bobo bobo) {
    StoreToolkit tk = bobo.getStoreKeeper().getStoreToolkit();
    char mmc = ((SQLToolkit)tk).getMultiMatchingChar();
    String ss = mmc + searchString + mmc;
    OrSelection os = new OrSelection( tk );

    for ( int i=0; i<attribute.length; i++ )
      os.addSelection( new DefaultSelection( tk,
					     attribute[i], ss,
					     SQLToolkit.LIKE, ignoreCase )
		       );
    Enumeration<Bobo> e = bobo.getEnumeration( os );
    while ( e.hasMoreElements() )
      list.add( e.nextElement() );
  }
}
