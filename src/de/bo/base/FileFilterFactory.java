package de.bo.base;

import java.io.*;
import javax.swing.filechooser.*;

public class FileFilterFactory
{
  public static javax.swing.filechooser.FileFilter
    createAllFileFilter(String description) {
    return new AllFileFilter( description );
  }
  public static javax.swing.filechooser.FileFilter
    createAllFileFilter() {
    return new AllFileFilter();
  }

  public static javax.swing.filechooser.FileFilter
    createHTMLFileFilter(String description) {
    return new HTMLFileFilter( description );
  }
  public static javax.swing.filechooser.FileFilter
    createHTMLFileFilter() {
    return new HTMLFileFilter();
  }

  protected abstract static class BaseFileFilter
    extends javax.swing.filechooser.FileFilter
  {
    protected String description;

    BaseFileFilter(String description) {
      super();

      this.description = description;
    }
    public String getDescription() {
      return description;
    }
  }

  protected static class AllFileFilter extends BaseFileFilter
  {
    public AllFileFilter(String description) {
      super( description );
    }
    public AllFileFilter() {
      super( "All Files" );
    }

    public boolean accept(File f) {
      return true;
    }
  }

  protected static class HTMLFileFilter extends BaseFileFilter
  {
    public HTMLFileFilter(String description) {
      super( description );
    }
    public HTMLFileFilter() {
      super( "HTML Files" );
    }

    public boolean accept(File f) {
      String s = f.getName().toLowerCase();
      return f.isDirectory() ||
	s.endsWith( ".htm" ) ||
	s.endsWith( ".html" );
    }
  }
}
