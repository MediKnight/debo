package de.bo.base.store2;

import java.util.Date;
import java.text.*;

import de.bo.base.util.CurrencyNumber;

public abstract class AbstractStorable
    implements Storable
{
    protected static StoreKeeper defaultStoreKeeper;

    protected Object[] data;
    protected StoreKeeper storeKeeper;

    private final static DateFormat dateFormat =
	new SimpleDateFormat("yyyyMMdd");

    /**
     * Setzen des Default-Storekeepers.
     */
    public static void setDefaultStoreKeeper(StoreKeeper storeKeeper) {
	defaultStoreKeeper = storeKeeper;
    }

    /**
     * Liefert Default-Storekeeper.
     */
    public static StoreKeeper getDefaultStoreKeeper() {
	return defaultStoreKeeper;
    }

    /**
     * Erzeugt Storable mit dem durch <code>setDefaultStoreKeeper</code>
     * gesetzten Objekt.
     */
    public AbstractStorable() {
	this(null);
    }

    /**
     * Erzeugt Storable mit gegebenen Storekeeper.
     */
    public AbstractStorable(StoreKeeper storeKeeper) {
	this.storeKeeper = (storeKeeper==null) ?
	    defaultStoreKeeper : storeKeeper;

	data = null;
    }

    /**
     * Setzen des aktuellen Storekeepers.
     */
    public void setStoreKeeper(StoreKeeper storeKeeper) {
	this.storeKeeper = storeKeeper;
    }

    /**
     * Liefert aktuellen Storekeeper.
     */
    public StoreKeeper getStoreKeeper() {
	return storeKeeper;
    }

    /**
     * Erzeugt neuen Schlüssel für das Objekt.
     * Diese Methode kann <tt>null</tt> zurückliefern. In diesem
     * Fall darf das Schlüsselattribut einer Datenbank-Tabelle
     * nicht gesetzt werden.
     */
    protected Object createKey() throws StoreException {
	return storeKeeper.createKey(this);
    }

    /**
     * Laden des Objekts mit den Daten, die zum gegebenen Schlüssel passen.
     */
    public void retrieve(Object key)
	throws StoreException {

	data = storeKeeper.retrieve(this,key);

	// eigentlich nicht notwendig, da i.A. das Array data nach
	// dem Retrieval den Schlüssel bereits enthält.
	if ( data != null )
	    setKey(key);
    }

    /**
     * Returns <tt>true</tt> if this record exists.
     */
    public boolean exists() {
	return data != null;
    }

    /**
     * Speichert aktuelles Objekt.
     */
    public void store()
	throws StoreException {

	Object key = getKey();

	if ( key == null ) 
	    storeKeeper.insert(this,data,createKey());
	else
	    storeKeeper.store(this,data,key);
    }

    /**
     * Entfernt Objekt aus dem Storekeeper.
     */
    public void delete()
	throws StoreException {

	Object key = getKey();

	if ( key != null ) {
	    storeKeeper.delete(this,key);
	}
    }

    /**
     * Returns <tt>true</tt> if this object has a key.
     * E.g. this object was previously inserted and not deleted.
     */
    public boolean hasKey() {
	return getKey() != null;
    }

    protected Object getObject(int index) {
	return data[index];
    }

    protected void setObject(int index,Object o) {
	data[index] = o;
    }

    protected String getString(int index) {
	return (data[index]!=null) ? data[index].toString() : "";
    }

    protected void setString(int index,String s) {
	data[index] = s;
    }

    protected boolean getBoolean(int index) {
	Object o = data[index];
	if ( o != null ) {
	    if ( o instanceof Boolean )
		return ((Boolean)o).booleanValue();

	    String s = data[index].toString();
	    if ( s.length() == 0 )
		return false;

	    return "yYtT1".indexOf(s.charAt(0)) >= 0;
	}
	return false;
    }

    protected void setBoolean(int index,boolean b) {
	data[index] = new Boolean(b);
    }

    protected int getInteger(int index) {
	Object n = data[index];
	if ( n == null )
	    return 0;

	if ( n instanceof Number )
	    return ((Number)n).intValue();

	try {
	    return Integer.parseInt(n.toString());
	}
	catch ( RuntimeException x ) {
	    return 0;
	}
    }

    protected void setInteger(int index,int n) {
	data[index] = new Integer(n);
    }

    protected Date getDateFromString(int index) {
	try {
	    return dateFormat.parse(getString(index));
	}
	catch ( ParseException x ) {
	    return null;
	}
    }

    protected void setDateAsString(int index,Date date) {
	data[index] = (date!=null) ? dateFormat.format(date) : "";
    }

    protected CurrencyNumber getCurrency(int index) {
	Object n = data[index];
	if ( n == null )
	    return null;

	if ( n instanceof Number )
	    return new CurrencyNumber(((Number)n).longValue());

	try {
	    return CurrencyNumber.parse(n.toString());
	}
	catch ( RuntimeException x ) {
	    return null;
	}
    }

    protected void setCurrency(int index,CurrencyNumber n) {
	// Casts auf Number dienen nur zur Compiler-Beruhigung.
	data[index] = (n!=null) ?
	    (Number)new Long(n.toEuro().longValue()) :
	    (Number)n;
    }

    /**
     * Setzen des Schlüssels.
     */
    protected abstract void setKey(Object key);

    /**
     * Liefert den Schlüssel des Objekts.
     */
    public abstract Object getKey();
}
