package de.baltic_online.bobo;

import java.util.*;

import de.baltic_online.base.store2.*;
import de.baltic_online.base.store2.sql.*;

/**
 * Baltic-Online Business Object <b>Verbindung</b>.
 * <p>
 * Disjunct Cross-Product implementation.
 */

public class XLink extends Bobo {
	/**
	 * Create a bill link object
	 */
	public XLink() {
		super();
	}

	/**
	 * Create a X link object
	 *
	 * @param storeKeeper used keeper
	 */
	public XLink(StoreKeeper storeKeeper) {
		super(storeKeeper);
	}

	public Object getGroupId() {
		return getObject(1);
	}

	public void setGroupId(Object id) {
		setObject(1, id);
	}

	public Object getObjectId() {
		return getObject(2);
	}

	public void setObjectId(Object id) {
		setObject(2, id);
	}

	public int getSideIndex() {
		return getInteger(3);
	}

	public void setSideIndex(int side) {
		setInteger(3, side);
	}

	public Object createGroupId() throws StoreException {

		BOBase boBase = (BOBase) getStoreKeeper();
		Object gid = boBase.createKey(this);
		setGroupId(gid);

		return gid;
	}

	public static void deleteGroup(BOBase boBase, Object gid)
		throws StoreException {

		SQLSelection sel = new SQLSelection(boBase.getSQLToolkit(), "gid", gid);
		XLink xlink = new XLink(boBase);
		Collection<XLink> coll = boBase.retrieve(xlink, sel);
		for (Iterator<XLink> i = coll.iterator(); i.hasNext();)
			 ((Bobo) i.next()).delete();
	}

	public String toString() {
		return "("
			+ getKey()
			+ ","
			+ getGroupId()
			+ ","
			+ getObjectId()
			+ ","
			+ getSideIndex()
			+ ")";
	}

	public int getParentCount() {
		return 0;
	}

	protected Bobo createParent(int index) {
		return null;
	}

	protected Object getParentKey(int index) {
		return null;
	}

	protected void setParentKey(int index, Object key) {
	}

	public String getKeyIdentifier() {
		return "id";
	}

	public String getIdentifier() {
		return "xprodukt";
	}

	public String getSequenceIdentifier() {
		return "id2";
	}

	public String[] getAttributes() {
		return new String[] { "id", "gid", "oid", "seite" };
	}

	public int getColumnCount() {
		return 4;
	}
}
