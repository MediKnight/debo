package de.bo.base.application;

import java.io.*;

public class UserProperties extends FiledProperties {
    private static final long serialVersionUID = 1L;
	public UserProperties(String path) {
		super(path);
	}

	public UserProperties(String path, String header) {
		super(path, header);
	}

	protected String extendPath(String path) {
		String home = System.getProperty("user.home");
		return home + File.separator + path;
	}
}
