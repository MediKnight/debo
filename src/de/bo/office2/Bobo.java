package de.bo.office2;

import de.bo.base.store.*;
import java.util.*;

/**
 * Basis-Klasse f�r Baltic-Online Business-Objects (BOBO).
 * <p>
 * Diese Klasse hat keine Kenntnisse �ber die verwendete Datenverwaltung.
 */

public abstract class Bobo extends AbstractStorable
{
    /**
     * Array gibt Sortierreihenfolge bei Enumerations dieser Objekte an.
     * Bei <code>null</code> keine Sortierung.
     *
     * Wird eine SQL-Datenbank benutzt, so beeinflusst dieses Array die
     * Order-Klausel in Select-Statements.
     */
    protected int[] orderIndexes;

    protected Bobo[] parent;

    protected Bobo() {
	super();

	orderIndexes = null;
	parent = null;
    }

    protected Bobo(StoreKeeper<? extends Bobo> storeKeeper) {
	super(storeKeeper);

	orderIndexes = null;
	parent = null;
    }

    protected Bobo getParent() {
	return getParent(0);
    }

    protected Bobo getParent(int index) {
	prepare();
	if ( parent[index] == null ) {
	    if ( retrieveParent(index) )
		return parent[index];
	    else
		return null;
	}
	return parent[index];
    }

    protected void setParent(int index,Bobo bobo) {
	prepare();
	parent[index] = bobo;
    }

    protected synchronized boolean retrieveParent(int index) {
	Bobo p = createParent( index );
	parent[index] = p;
	return p.retrieve(getParentKey(index));
    }

    // Paket-interne Funktion: Empf�ngt Daten vom Objekt
    void get(Object[] data) {
	for ( int i=0; i<this.data.length; i++ )
	    data[i] = this.data[i];
    }

    // Paket-interne Funktion: �bertr�gt Daten zum Objekt
    void put(Object[] data) {
	prepare();

	for ( int i=0; i<data.length; i++ )
	    this.data[i] = data[i];

	created = deleted = false;
    }

    /**
     * Objekt-Vorbereitung (Post-Konstruktor)
     *
     * Erzeugt bei Bedarf Object-Array <code>data</code>.
     */
    protected void prepare() {
	if ( data == null )
	    data = new Object[getColumnCount()];
	if ( parent == null && getParentCount() > 0 )
	    parent = new Bobo[getParentCount()];
    }

    public boolean retrieve(Object key) {
	boolean b = super.retrieve(key);
	if ( b ) parent = null;
	return b;
    }

    /**
     * Setzen des Primary-Keys.
     */
    protected void setKey(Object priKey) {
	prepare();
	data[0] = priKey;
    }

    /**
     * Liefert Primary-Key.
     */
    public Object getKey() {
	return data[0];
    }

    /**
     * Liefert Default-Enumeration von Objekten dieser Klasse.
     *
     * Diese Enumeration liefert alle Objekte und ohne Sortierung.
     */
    public Enumeration<Bobo> getEnumeration() {
	return getEnumeration(null,null,false);
    }

    /**
     * Liefert Enumeration von Objekten dieser Klasse.
     *
     * Diese Enumeration liefert alle Objekte.
     *
     * @param order <code>true</code> bei Sortierung
     */
    public Enumeration<Bobo> getEnumeration(boolean order) {
	return getEnumeration(null,null,order);
    }

    /**
     * Liefert unsortierte Enumeration von ausgew�hlten Objekten
     * dieser Klasse.
     *
     * @param keyIdentifier Key-Attribut
     * @param key passender Key
     */
    public Enumeration<Bobo> getEnumeration(String keyIdentifier,
				      Object key) {
	return getEnumeration(keyIdentifier,key,false);
    }

    /**
     * Liefert unsortierte Enumeration von ausgew�hlten Objekten
     * dieser Klasse.
     *
     * @param sel Auswahl
     */
    public Enumeration<Bobo> getEnumeration(Selection sel) {
	return getEnumeration(sel,false);
    }

    /**
     * Liefert Enumeration von ausgew�hlten Objekten dieser Klasse.
     *
     * @param sel Auswahl
     * @param order <code>true</code> bei Sortierung
     */
    @SuppressWarnings("unchecked")
    public Enumeration<Bobo> getEnumeration(Selection sel,boolean order) {
	return ((StoreKeeper<Bobo>) storeKeeper).getEnumeration(this,sel,order);
    }

    /**
     * Liefert Enumeration von ausgew�hlten Objekten dieser Klasse.
     *
     * @param keyIdentifier Key-Attribut
     * @param key passender Key
     * @param order <code>true</code> bei Sortierung
     */
    @SuppressWarnings("unchecked")
    public Enumeration<Bobo> getEnumeration(String keyIdentifier,
				      Object key,
				      boolean order) {
	return ((StoreKeeper<Bobo>) storeKeeper).getEnumeration(this,keyIdentifier,key,order);
    }

    /**
     * Liefert <code>orderIndexes</code>.
     *
     * @see #orderIndexes
     */
    public int[] getOrderIndexes() {
	return orderIndexes;
    }

    /**
     * Setzen von <code>orderIndexes</code>.
     *
     * @see #orderIndexes
     */
    public void setOrderIndexes(int[] indexes) {
	orderIndexes = indexes;
    }

    /**
     * Liefert globale Objekt-Bezeichnung
     * (Name der Klasse oder Tabellen-Name).
     */
    public abstract String getIdentifier();

    /**
     * Liefert Schl�ssel-Attribut.
     */
    public abstract String getKeyIdentifier();

    /**
     * Liefert alle Attribute.
     */
    public abstract String[] getAttributes();

    /**
     * Liefert Anzahl aller Eltern.
     */
    public abstract int getParentCount();

    /**
     * Erzeugt Parent-Objekt zum gegebenen Index.
     */
    protected abstract Bobo createParent(int index);

    /**
     * Liefert Parent-Key zum gegebenen Index.
     */
    protected abstract Object getParentKey(int index);
}
