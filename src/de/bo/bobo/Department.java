package de.bo.bobo;

import java.util.*;
import java.text.DateFormat;

import de.bo.base.store2.*;
import de.bo.base.store2.sql.*;
import de.bo.base.util.*;

/**
 * Baltic-Online Geschäftsobjekt <b>Abteilung</b>.
 * <p>
 * Diese Klasse repräsentiert ausschließlich readonly Objekte,
 * weshalb sie keine "setter"-methoden besitzt.
 */

public class Department extends Bobo
{
    /**
     * Erzeugt Abteilung mit Default-Storekeeper
     */
    public Department() {
	super();
    }

    /**
     * Erzeugt Abteilung mit gegebenen Storekeeper.
     *
     * @param storeKeeper verwendetes Datensystem
     */
    public Department(StoreKeeper storeKeeper) {
	super(storeKeeper);
    }

    /**
     * Liefert Anzahl der "Eltern".
     *
     * Eine Abteilung hat keine "Eltern" im Sinne der BO-Hierarchie und
     * deshalb liefert diese Funktion <code>0</code>.
     *
     * @return <code>0</code>
     */
    public int getParentCount() {
	return 0;
    }

    /**
     * Erzeugt "Eltern"-Objekt zum gegebenen Index (im Bereich von
     * <code>getParentCount()</code>).
     *
     * Eine Abteilung hat keine "Eltern" im Sinne der BO-Hierarchie und
     * deshalb liefert diese Funktion <code>null</code>.
     *
     * @return <code>null</code>
     */
    protected Bobo createParent(int index) {
	return null;
    }

    /**
     * Liefert "Eltern"-Schlüssel zum gegebenen Index (im Bereich von
     * <code>getParentCount()</code>).
     *
     * Eine Abteilung hat keine "Eltern" im Sinne der BO-Hierarchie und
     * deshalb liefert diese Funktion <code>null</code>.
     *
     * @return <code>null</code>
     */
    protected Object getParentKey(int index) {
	return null;
    }

    protected void setParentKey(int index,Object key) {
    }

    /**
     * Liefert Name der Abteilung.
     */
    public String getName() {
	return getString(1);
    }

    /**
     * Liefert Abkürzung der Abteilung.
     */
    public String getAbbreviation() {
	return getString(2);
    }

    /**
     * Liefert zusätzliche Info der Abteilung.
     */
    public String getRemark() {
	return getString(3);
    }

    /**
     * Liefert Namen des Objekttyps "Abteilung".
     *
     * (i.A. Name der Tabelle "abteilung")
     *
     * @return <code>"abteilung"</code>
     */
    public String getIdentifier() {
	return "abteilung";
    }

    /**
     * Liefert Namen des Primärschlüssels "Abteilung".
     *
     * (i.A. Spaltenattribute des Primärschlüssels)
     *
     * @return <code>"id"</code>
     */
    public String getKeyIdentifier() {
	return "id";
    }

    /**
     * Liefert Attribute des Objekttyps "Abteilung".
     *
     * (i.A. Spaltenattribute der Tabelle "abteilung")
     */
    public String[] getAttributes() {
	return new String[] { "id", "name", "abk", "bemerkung" };
    }

    /**
     * Liefert Anzahl der Attribute.
     */
    public int getColumnCount() {
	return 4;
    }

    /**
     * Liefert Abkürzung
     */
    public String toString() {
	return getAbbreviation();
    }
}
