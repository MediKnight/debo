
package de.bo.base.util;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class ObjectFinder
{
  /*
  private String pathEntries;

  public ObjectFinder() {
    this("");
  }

  public ObjectFinder(String pathEntries) {
    this.pathEntries = pathEntries;
  }

  public InputStream getInputStream(String name)
    throws IOException {

    Object o = findObject(name);

    if ( o instanceof ZipFile ) {
      ZipFile zf = (ZipFile)o;
      ZipEntry ze = zf.getEntry(name);
      return zf.getInputStream(ze);
    }

    if ( o instanceof File ) {
      return new FileInputStream((File)o);
    }

    return null;
  }
  */
}
