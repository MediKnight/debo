package de.bo.bobo;

import de.bo.base.store2.*;

/**
 * Baltic-Online Geschäftsobjekt <b>Standort</b>.
 * <p>
 * Da ein Standort ein konkretes Objekt ist, dass Mitarbeiter enthält,
 * werden fast alle Funktionen von <code>EmployeeContainer</code> geerbt.
 *
 * @see Company
 * @see Employee
 * @see EmployeeContainer
 */

public class Location extends EmployeeContainer {
    /**
     * Erzeugt Standort mit Default-Storekeeper.
     */
    public Location() {
        super("sid");
    }

    /**
     * Erzeugt Standort mit gegebenen Storekeeper.
     */
    public Location(StoreKeeper storeKeeper) {
        super(storeKeeper, "sid");
    }

    /**
     * Liefert zugehörige Firma (lädt sie bei Bedarf).
     */
    public Company getCompany() throws StoreException {

        return (Company) getParent(0);
    }

    /**
     * Erzeugt Eltern-Objekt zum gegebenen Index (im Bereich von
     * <code>getParentCount()</code>).
     *
     * Hier ist nur 0 als Index gültig, d.h. die Funktion erzeugt eine Firma.
     */
    protected Bobo createParent(int index) {
        Bobo record = null;
        if (index == 0) {
            record = new Company();
            record.setStoreKeeper(getStoreKeeper());
        }
        return record;
    }

    /**
     * Liefert "Eltern"-Schlüssel zum gegebenen Index (im Bereich von
     * <code>getParentCount()</code>).
     *
     * Hier ist nur 0 als Index gültig, d.h. die Funktion liefert den Schlüssel
     * der Firma.
     */
    protected Object getParentKey(int index) {
        return data[1];
    }

    protected void setParentKey(int index, Object key) {
        data[1] = key;
    }

    /**
     * Liefert Anzahl der "Eltern".
     *
     * @return <code>1</code>
     */
    public int getParentCount() {
        return 1;
    }

    /**
     * Liefert Name des Standorts.
     */
    public String getName() {
        return getString(2);
    }

    /**
     * Liefert zusätzliche Info zum Standort.
     */
    public String getRemark() {
        return getString(3);
    }

    /**
     * Setzen der Standort-Daten.
     */
    public void set(String name, String remark) {
        setName(name);
        setRemark(remark);
    }

    /**
     * Setzen des Namen.
     */
    public void setName(String name) {
        setString(2, name);
    }

    /**
     * Setzen der Zusatzinfo.
     */
    public void setRemark(String remark) {
        setString(3, remark);
    }

    /**
     * Liefert Name des Standorts.
     */
    public String toString() {
        return getName();
    }

    /**
     * Liefert Namen des Objekttyps "Standort".
     *
     * (i.A. Name der Tabelle "standort")
     *
     * @return <code>"standort"</code>
     */
    public String getIdentifier() {
        return "standort";
    }

    /**
     * Liefert Namen des Primärschlüssels "Standort".
     *
     * (i.A. Spaltenattribute des Primärschlüssels)
     *
     * @return <code>"sid"</code>
     */
    public String getKeyIdentifier() {
        return "sid";
    }

    /**
     * Liefert Attribute des Objekttyps "Standort".
     *
     * (i.A. Spaltenattribute der Tabelle "standort")
     */
    public String[] getAttributes() {
        return new String[] { "sid", "fid", "sname", "bemerkung" };
    }

    /**
     * Liefert Anzahl der Attribute.
     */
    public int getColumnCount() {
        return 4;
    }
}
