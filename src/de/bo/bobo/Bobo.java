package de.bo.bobo;

import java.util.*;

import de.bo.base.store2.*;
import de.bo.base.store2.sql.*;

/**
 * Basis-Klasse f�r Baltic-Online Business-Objects (BOBO).
 * <p>
 * Diese Klasse hat keine Kenntnisse �ber die verwendete Datenverwaltung.
 */

public abstract class Bobo extends SQLRecord
{
    protected static final String ALLREADY_EXISTS = " allready exists";
    protected static final String NOT_CREATED = " not created";
    protected static final String NO_MEMBER_OF = " no member of ";

    protected static final String MAYBE_INUSE_VIOLATION =
	"Record may be in use by a billing entry";

    protected static final String INUSE_VIOLATION =
	"Record is in use by a billing entry";

    protected Bobo[] parent;

    protected Bobo() {
	super();

	parent = null;
    }

    protected Bobo(StoreKeeper storeKeeper) {
	super(storeKeeper);

	parent = null;
    }

    protected Bobo getParent()
	throws StoreException {

	return getParent(0);
    }

    protected Bobo getParent(int index)
	throws StoreException {

	prepare();

	// retrieveParent versagt sonst.
	if ( getParentKey(index) == null )
	    return null;

	if ( parent[index] == null )
	    retrieveParent(index);

	return parent[index];
    }

    protected void setParent(int index,Bobo parentObject) {
	prepare();
	setParentKey(index,parentObject.getKey());
	parent[index] = parentObject;
    }

    protected void retrieveParent(int index)
	throws StoreException {

	Bobo p = createParent(index);
	parent[index] = p;
	p.retrieve(getParentKey(index));
    }

    /**
     * Objekt-Vorbereitung (Post-Konstruktor)
     *
     * Erzeugt bei Bedarf Object-Array <code>data</code>.
     */
    protected void prepare() {
	if ( parent == null && getParentCount() > 0 )
	    parent = new Bobo[getParentCount()];
    }

    protected void put(Object[] data) {
	prepare();

	super.put(data);
    }

    public void retrieve(Object key)
	throws StoreException {

	super.retrieve(key);
	parent = null;
    }

    /**
     * This method tests equality by comparing the key.
     * <p>
     * This method first compares the classes of the keys.
     * If the keys are of the same classes this method returns
     * the result of the natural equals method, otherwise the
     * keys are converted to strings.
     */
    public boolean equals(Object o) {

	Object key1 = data[0];
	Object key2 = ((Bobo)o).data[0];

	if ( key1.getClass().equals(key2.getClass()) )
	    return key1.equals(key2);

	return key1.toString().equals(key2.toString());
    }

    /**
     * Setzen des Primary-Keys.
     */
    protected void setKey(Object key) {
	prepare();
	data[0] = key;
    }

    /**
     * Liefert Primary-Key.
     */
    public Object getKey() {
	prepare();
	return data[0];
    }

    protected void setBoolean(int index,boolean b) {
	data[index] = b ? "y" : "n";
    }

    public String getSequenceIdentifier() {
	return "id";
    }

    /**
     * This method throws a <tt>StoreException</tt> if this
     * container may be used for billing entries.
     */
    protected void validateDelete()
	throws StoreException {

	Object key = getKey();
	if ( key != null ) {
	    BOBase boBase = (BOBase)getStoreKeeper();
	    SQLToolkit tk = boBase.getSQLToolkit();
	    OrSelection osel = new OrSelection(tk);
	    osel.addSelection(new SQLSelection(tk,"kunde",key));
	    osel.addSelection(new SQLSelection(tk,"einsatz",key));

	    Collection<? extends Bill> coll = null;
	    try {
		coll = boBase.retrieve(new Claim(boBase),osel);
	    }
	    catch ( StoreException sx ) {
		// "delegate" privilege violation
		throw new StoreException(MAYBE_INUSE_VIOLATION);
	    }
	    if ( coll.size() > 0 )
		throw new StoreException(INUSE_VIOLATION);

	    // once again
	    SQLSelection sel = new SQLSelection(tk,"lieferant",key);
	    try {
		coll = boBase.retrieve(new Liability(boBase),sel);
	    }
	    catch ( StoreException sx ) {
		// "delegate" privilege violation
		throw new StoreException(MAYBE_INUSE_VIOLATION);
	    }
	    if ( coll.size() > 0 )
		throw new StoreException(INUSE_VIOLATION);
	}
    }

    /**
     * Liefert Anzahl aller Eltern.
     */
    public abstract int getParentCount();

    /**
     * Erzeugt Parent-Objekt zum gegebenen Index.
     */
    protected abstract Bobo createParent(int index)
	throws StoreException;
    
    /**
     * Liefert Parent-Key zum gegebenen Index.
     */
    protected abstract Object getParentKey(int index);

    /**
     * Setzt Parent-Key zum gegebenen Index.
     */
    protected abstract void setParentKey(int index,Object key);
}
