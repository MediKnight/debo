package de.bo.base.application;

import java.util.*;
import java.io.*;

public class ApplicationProperties extends FiledProperties
{
    public ApplicationProperties(String path) {
	super(path);
    }

    public ApplicationProperties(String path,String header) {
	super(path,header);
    }

    protected String extendPath(String path) {
	String dir = System.getProperty("user.dir");
	return dir+File.separator+path;
    }

    public InputStream getInputStream()
	throws IOException {

	try {
	    return super.getInputStream();
	}
	catch ( FileNotFoundException fnfe ) {
	    String path = getPath();
	    InputStream is = ClassLoader.getSystemResourceAsStream(path);
	    if ( is == null ) {
		path += ".properties";
		is = ClassLoader.getSystemResourceAsStream(path);
		if ( is == null )
		    throw fnfe;
	    }
	    return is;
	}
    }
}
