package de.bo.bobo;

import java.util.*;

import de.bo.base.store2.*;
import de.bo.base.store2.sql.*;
import de.bo.base.util.*;

/**
 * Baltic-Online Geschäftsobjekt <b>Verbindlichkeit</b>.
 * <p>
 */

public class Liability extends Bill
{
    /**
     * Erzeugt Verbindlichkeit mit Default-Storekeeper
     */
    public Liability() {
	super();
    }

    /**
     * Erzeugt Verbindlichkeit mit gegebenen Storekeeper.
     *
     * @param storeKeeper verwendetes Datensystem
     */
    public Liability(StoreKeeper storeKeeper) {
	super(storeKeeper);
    }

    public Object getSupplier() {
	return getObject(12);
    }

    public void setSupplier(Object key) {
	setObject(12,key);
    }

    /**
     * Liefert Namen des Objekttyps "Verbindlichkeit".
     *
     * (i.A. Name der Tabelle "verbind")
     *
     * @return <code>"verbind"</code>
     */
    public String getIdentifier() {
	return "verbind";
    }

    /**
     * Liefert Attribute des Objekttyps "Verbindlichkeit".
     *
     * (i.A. Spaltenattribute der Tabelle "verbind")
     */
    public String[] getAttributes() {
	return new String[] {
	    "id", "nr", "datum", "buchung", "betrag", "mwst",
		"gesamt", "art", "aid", "faellig", "bezahlt",
		"bemerkung",
		"lieferant" };
    }

    /**
     * Liefert Anzahl der Attribute.
     */
    public int getColumnCount() {
	return 13;
    }

    protected String getLinkKeyIdentifier() {
	return "vid";
    }

    /**
     * Creates the "other" bill.
     */
    protected Bill createLinkBill() {
	return new Claim();
    }
}
