package de.baltic_online.base.util;

import java.util.*;

public class TimeSpace
{
  protected Locale locale;
  protected GregorianCalendar startDate;
  protected GregorianCalendar endDate;

  public TimeSpace() {
    this( null, null, null );
  }
  public TimeSpace(Locale locale) {
    this( locale, null, null );
  }
  public TimeSpace(GregorianCalendar endDate) {
    this( null, null, endDate );
  }
  public TimeSpace(Locale locale,GregorianCalendar endDate) {
    this( locale, null, endDate );
  }
  public TimeSpace(Locale l,GregorianCalendar sd,GregorianCalendar ed) {
    locale = (l==null) ? Locale.getDefault() : l;
    startDate = (sd==null) ? new GregorianCalendar( locale ) : sd;
    endDate = ed;
    if ( ed == null ) {
      endDate = new GregorianCalendar( locale );
      endDate.set( startDate.get( Calendar.YEAR ),
		   startDate.get( Calendar.MONTH ),
		   startDate.getActualMaximum( Calendar.DAY_OF_MONTH )
		   );
    }
  }

  public TimeSpace(int month) {
    this( null, month, 0 );
  }
  public TimeSpace(int month,int year) {
    this( null, month, year );
  }
  public TimeSpace(Locale locale,int month) {
    this( locale, month, 0 );
  }
  public TimeSpace(Locale locale,int month,int year) {
    this.locale = (locale==null) ? Locale.getDefault() : locale;
    GregorianCalendar current = new GregorianCalendar( this.locale );
    if ( year <= 0 ) year = current.get( Calendar.YEAR );
    if ( month < 0 ) month = current.get( Calendar.MONTH );

    startDate = new GregorianCalendar( this.locale );
    endDate = new GregorianCalendar( this.locale );

    startDate.set( year, month, 1 );
    endDate.set( year, month,
		 startDate.getActualMaximum(Calendar.DAY_OF_MONTH) );
  }

  public TimeSpace getCurrentYear() {
    TimeSpace ts = new TimeSpace( locale );
    GregorianCalendar cal = new GregorianCalendar( locale );
    cal.set( startDate.get(Calendar.YEAR), 0, 1 );
    ts.setStartDate( cal );

    cal = new GregorianCalendar( locale );
    cal.set( Calendar.YEAR, startDate.get(Calendar.YEAR) );
    cal.set( Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH) );
    cal.set( Calendar.DAY_OF_MONTH,
	     cal.getActualMaximum(Calendar.DAY_OF_MONTH) );
    ts.setEndDate( cal );

    return ts;
  }

  public void setStartDate(GregorianCalendar startDate) {
    this.startDate = startDate;
  }
  public void setEndDate(GregorianCalendar endDate) {
    this.endDate = endDate;
  }
  public GregorianCalendar getStartDate() {
    return startDate;
  }
  public GregorianCalendar getEndDate() {
    return endDate;
  }

  public boolean isBetween(GregorianCalendar date) {
    return
      startDate.equals( date ) ||
      endDate.equals( date ) ||
      (startDate.before(date) && endDate.after(date));
  }
  public long getTime() {
    return endDate.getTime().getTime() - startDate.getTime().getTime();
  }
  public int getSeconds() {
    return (int)(getTime() / 1000L);
  }
  public int getMinutes() {
    return (int)(getTime() / 60000L);
  }
  public int getHours() {
    return (int)(getTime() / 3600000L);
  }
  public int getDays() {
    return (int)(getTime() / 86400000L);
  }
  public int getWeeks() {
    return (int)(getTime() / 604800000L);
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append( "** TimeSpace **\n" );
    sb.append( "   StartDate: " );
    sb.append( startDate.get(Calendar.YEAR) );
    sb.append( "-" );
    sb.append( startDate.get(Calendar.MONTH)+1 );
    sb.append( "-" );
    sb.append( startDate.get(Calendar.DATE) );
    sb.append( "\n" );
    sb.append( "   EndDate  : " );
    sb.append( endDate.get(Calendar.YEAR) );
    sb.append( "-" );
    sb.append( endDate.get(Calendar.MONTH)+1 );
    sb.append( "-" );
    sb.append( endDate.get(Calendar.DATE) );
    return sb.toString();
  }
}
