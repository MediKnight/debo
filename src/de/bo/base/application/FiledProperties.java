package de.bo.base.application;

import java.util.*;
import java.io.*;

public abstract class FiledProperties extends Properties
{
    private String path;
    private String header;

    public FiledProperties(String path) {
	this(path,"");
    }

    public FiledProperties(String path,String header) {
	setPath(path);
	setHeader(header);
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = getIndependentPath(path);
    }

    public String getHeader() {
	return header;
    }

    public void setHeader(String header) {
	this.header = header;
    }

    public void save()
	throws IOException {

	OutputStream os = getOutputStream();
	store(os,this.getClass().getName());
	os.close();
    }

    public void load()
	throws IOException {

	InputStream is = getInputStream();
	load(is);
	is.close();
    }

    public InputStream getInputStream()
	throws IOException {

	return new FileInputStream(getFile());
    }

    public OutputStream getOutputStream()
	throws IOException {

	return new FileOutputStream(getFile());
    }

    public File getFile() {
	String extPath = extendPath(path);
	File f = new File(extPath);
	if ( !f.exists() && !extPath.endsWith(".properties") ) {
	    extPath += ".properties";
	    f = new File(extPath);
	}

	return f;
    }

    protected void createPath()
	throws IOException {
    }

    public static String getIndependentPath(String path) {
	StringBuffer sb = new StringBuffer(path);
	for ( int i=0; i<sb.length(); i++ )
	    if ( sb.charAt(i) == '|' )
		sb.setCharAt(i,File.separatorChar);
	return sb.toString();
    }

    protected abstract String extendPath(String path);
}
