package de.bo.bobo;

import java.util.*;

import de.bo.base.store2.*;
import de.bo.base.store2.sql.*;

public class CustomerSearch {
	public final static int COMPANY = 1;
	public final static int PERSON = 2;
	public final static int LOCATION = 4;
	public final static int ALL = COMPANY | PERSON | LOCATION;

	protected String searchString;
	protected boolean ignoreCase;
	protected int tableFlag;

	protected BOBase boBase;

	public CustomerSearch(String searchString) {
		this(searchString, true, ALL);
	}

	public CustomerSearch(String searchString, boolean ignoreCase) {
		this(searchString, ignoreCase, ALL);
	}

	public CustomerSearch(
		String searchString,
		boolean ignoreCase,
		int tableFlag) {

		if (tableFlag == 0)
			throw new IllegalArgumentException();

		this.searchString = searchString;
		this.ignoreCase = ignoreCase;
		this.tableFlag = tableFlag;

		boBase = null;
	}

	public void setBOBase(BOBase boBase) {
		this.boBase = boBase;
	}

	public List search() throws StoreException {

		LinkedList l = new LinkedList();
		String[] attribute;

		if ((tableFlag & COMPANY) != 0) {
			attribute = new String[] { "name" };
			search(attribute, l, new Company(boBase));
		}
		if ((tableFlag & LOCATION) != 0) {
			attribute = new String[] { "sname" };
			search(attribute, l, new Location(boBase));
		}
		if ((tableFlag & PERSON) != 0) {
			attribute = new String[] { "name", "vorname" };
			search(attribute, l, new Person(boBase));
		}

		return l;
	}

	protected void search(String[] attribute, LinkedList list, Bobo bobo)
		throws StoreException {

		SQLToolkit tk = boBase.getSQLToolkit();
		char mmc = tk.getMultiMatchingChar();
		String ss = mmc + searchString + mmc;
		OrSelection os = new OrSelection(tk);

		for (int i = 0; i < attribute.length; i++)
			os.addSelection(
				new SQLSelection(
					tk,
					attribute[i],
					ss,
					SQLToolkit.LIKE,
					ignoreCase));

		Collection c = boBase.retrieve(bobo, os);
		for (Iterator i = c.iterator(); i.hasNext();)
			list.add(i.next());
	}
}
