package de.bo.base.application;

import java.util.*;
import java.io.*;
import java.text.*;

public class ApplicationVersion
{
  protected Calendar date;

  public ApplicationVersion()
  {
    date = (Calendar)Calendar.getInstance().clone();
  }
  public String getAuthor()
  {
    return "";
  }
  public Calendar getDate()
  {
    return date;
  }
  protected void setDate()
  {
  }
  public String getApplicationName()
  {
    return "StdApplication";
  }
  public int getMajorVersion()
  {
    return 1;
  }
  public int getMinorVersion()
  {
    return 0;
  }
  public String toString()
  {
    String ds = DateFormat.getDateInstance().format( date.getTime() );
    String s = getApplicationName();
    s += " "+getMajorVersion()+"."+getMinorVersion()+" ";
    s += ds+" "+getAuthor();
    return s;
  }
}
