package de.bo.bobo;

import de.bo.base.store2.StoreKeeper;
import de.bo.base.util.CurrencyNumber;

/**
 * Baltic-Online Geschäftsobjekt <b>Forderung</b>.
 * <p>
 */

public class Claim extends Bill
{
    /**
     * Erzeugt Forderung mit Default-Storekeeper
     */
    public Claim() {
	super();
    }

    /**
     * Erzeugt Forderung mit gegebenen Storekeeper.
     *
     * @param storeKeeper verwendetes Datensystem
     */
    public Claim(StoreKeeper storeKeeper) {
	super(storeKeeper);
    }

    public CurrencyNumber getProfit() {
	return getCurrency(12);
    }

    public Object getCustomer() {
	return getObject(13);
    }

    public Object getTask() {
	return getObject(14);
    }

    public void setProfit(CurrencyNumber n) {
	setCurrency(12,n);
    }

    public void setCustomer(Object key) {
	setObject(13,key);
    }

    public void setTask(Object key) {
	setObject(14,key);
    }

    /**
     * Liefert Namen des Objekttyps "Forderung".
     *
     * (i.A. Name der Tabelle "forderung")
     *
     * @return <code>"forderung"</code>
     */
    public String getIdentifier() {
	return "forderung";
    }

    /**
     * Liefert Attribute des Objekttyps "Forderung".
     *
     * (i.A. Spaltenattribute der Tabelle "forderung")
     */
    public String[] getAttributes() {
	return new String[] {
	    "id", "nr", "datum", "buchung", "betrag", "mwst",
		"gesamt", "art", "aid", "faellig", "bezahlt",
		"bemerkung",
		"gewinn", "kunde", "einsatz" };
    }

    /**
     * Liefert Anzahl der Attribute.
     */
    public int getColumnCount() {
	return 15;
    }

    protected String getLinkKeyIdentifier() {
	return "fid";
    }

    /**
     * Creates the "other" bill.
     */
    protected Bill createLinkBill() {
	return new Liability();
    }
}
