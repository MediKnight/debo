package de.bo.office2;

import de.bo.base.store.StoreKeeper;

/**
 * Baltic-Online Gesch�ftsobjekt <b>Standort</b>.
 * <p>
 * Da ein Standort ein konkretes Objekt ist, dass Mitarbeiter enth�lt,
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
        super();
    }

    /**
     * Erzeugt Standort mit gegebenen Storekeeper.
     */
    public Location(StoreKeeper<Bobo> storeKeeper) {
        super(storeKeeper);
    }

    /**
     * Liefert zugeh�rige Firma (l�dt sie bei Bedarf).
     */
    public Company getCompany() {
        return (Company) getParent(0);
    }

    /**
     * Erzeugt Eltern-Objekt zum gegebenen Index (im Bereich von
     * <code>getParentCount()</code>).
     *
     * Hier ist nur 0 als Index g�ltig, d.h. die Funktion erzeugt eine Firma.
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
     * Liefert "Eltern"-Schl�ssel zum gegebenen Index (im Bereich von
     * <code>getParentCount()</code>).
     *
     * Hier ist nur 0 als Index g�ltig, d.h. die Funktion liefert den Schl�ssel
     * der Firma.
     */
    protected Object getParentKey(int index) {
        return data[1];
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
        return getStoreToolkit().objectToString(data[2]);
    }

    /**
     * Liefert zus�tzliche Info zum Standort.
     */
    public String getRemark() {
        return getStoreToolkit().objectToString(data[3]);
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
        data[2] = getStoreToolkit().stringToObject(name);
    }

    /**
     * Setzen der Zusatzinfo.
     */
    public void setRemark(String remark) {
        data[3] = getStoreToolkit().stringToObject(remark);
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
     * Liefert Namen des Prim�rschl�ssels "Standort".
     *
     * (i.A. Spaltenattribute des Prim�rschl�ssels)
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
