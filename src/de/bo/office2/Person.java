package de.bo.office2;

import de.bo.base.store.StoreKeeper;

/**
 * Baltic-Online Geschäftsobjekt <b>Person</b>.
 * <p>
 * Eine Person enthält mehrere Adressen und mehrere Mitarbeiter-Einträge und
 * ist daher ein <code>EmployeeContainer</code>.
 * <p>
 * Eine Person kann wie eine Firma Kunde, Lieferant oder Partner sein
 * und ist deshalb eine <code>BusinessRelation</code>.
 *
 * @see Company
 * @see BusinessRelation
 */

public class Person extends EmployeeContainer implements BusinessRelation {
    /**
     * Erzeugt Person mit Default-Storekeeper.
     */
    public Person() {
        super();
    }

    /**
     * Erzeugt Person mit gegebenen Storekeeper.
     *
     * @param storeKeeper verwendetes Datensystem
     */
    public Person(StoreKeeper storeKeeper) {
        super(storeKeeper);
    }

    /**
     * Liefert Anzahl der "Eltern".
     *
     * Eine Person hat keine "Eltern" im Sinne der BO-Hierarchie und
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
     * Eine Person hat keine "Eltern" im Sinne der BO-Hierarchie und
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
     * Eine Person hat keine "Eltern" im Sinne der BO-Hierarchie und
     * deshalb liefert diese Funktion <code>null</code>.
     *
     * @return <code>null</code>
     */
    protected Object getParentKey(int index) {
        return null;
    }

    /**
     * Liefert Name der Person.
     */
    public String getName() {
        return getStoreToolkit().objectToString(data[1]);
    }

    /**
     * Liefert Vorname der Person.
     */
    public String getFirstName() {
        return getStoreToolkit().objectToString(data[2]);
    }

    /**
     * Liefert vollständigen Namen der Person.
     */
    public String getFullName() {
        String firstName = getFirstName();
        String name = getName();
        if (firstName.length() > 0)
            return name + ", " + firstName;

        return name;
    }

    /**
     * Liefert Initialen der Person.
     */
    public String getInit() {
        return getStoreToolkit().objectToString(data[3]);
    }

    /**
     * Liefert Titel der Person.
     */
    public String getTitle() {
        return getStoreToolkit().objectToString(data[4]);
    }

    /**
     * Liefert Anrede der Person.
     */
    public String getSalutation() {
        return getStoreToolkit().objectToString(data[5]);
    }

    public boolean isCustomer() {
        return getStoreToolkit().objectToBoolean(data[6]);
    }
    public boolean isSupplier() {
        return getStoreToolkit().objectToBoolean(data[7]);
    }
    public boolean isPartner() {
        return getStoreToolkit().objectToBoolean(data[8]);
    }

    /**
     * Liefert zusätzliche Info zur Person.
     */
    public String getRemark() {
        return getStoreToolkit().objectToString(data[9]);
    }

    /**
     * Setzen von Name und Vorname der Person.
     *
     * Alle anderen Daten dieser Person werden mit neutralen Werten
     * überschrieben.
     */
    public void set(String name, String firstName) {
        set(name, firstName, "", "", "", false, false, false, "");
    }

    /**
     * Setzen aller Personen-Daten.
     */
    public void set(
        String name,
        String firstName,
        String init,
        String title,
        String salutation,
        boolean customer,
        boolean supplier,
        boolean partner,
        String remark) {
        setName(name);
        setFirstName(firstName);
        setInit(init);
        setTitle(title);
        setSalutation(salutation);
        setCustomer(customer);
        setSupplier(supplier);
        setPartner(partner);
        setRemark(remark);
    }

    /**
     * Setzen des Namen.
     */
    public void setName(String name) {
        data[1] = getStoreToolkit().stringToObject(name);
    }

    /**
     * Setzen des Vornamen.
     */
    public void setFirstName(String firstName) {
        data[2] = getStoreToolkit().stringToObject(firstName);
    }

    /**
     * Setzen der Initialen.
     */
    public void setInit(String init) {
        data[3] = getStoreToolkit().stringToObject(init);
    }

    /**
     * Setzen des Titels der Personen.
     */
    public void setTitle(String title) {
        data[4] = getStoreToolkit().stringToObject(title);
    }

    /**
     * Setzen der Anrede der Person.
     */
    public void setSalutation(String salutation) {
        data[5] = getStoreToolkit().stringToObject(salutation);
    }

    public void setCustomer(boolean customer) {
        data[6] = getStoreToolkit().booleanToObject(customer);
    }
    public void setSupplier(boolean supplier) {
        data[7] = getStoreToolkit().booleanToObject(supplier);
    }
    public void setPartner(boolean partner) {
        data[8] = getStoreToolkit().booleanToObject(partner);
    }

    /**
     * Setzen der Zusatzinfo der Person.
     */
    public void setRemark(String remark) {
        data[9] = getStoreToolkit().stringToObject(remark);
    }

    /**
     * Liefert Namen des Objekttyps "Person".
     *
     * (i.A. Name der Tabelle "person")
     *
     * @return <code>"person"</code>
     */
    public String getIdentifier() {
        return "person";
    }

    /**
     * Liefert Namen des Primärschlüssels "Person".
     *
     * (i.A. Spaltenattribute des Primärschlüssels)
     *
     * @return <code>"pid"</code>
     */
    public String getKeyIdentifier() {
        return "pid";
    }

    /**
     * Liefert Attribute des Objekttyps "Person".
     *
     * (i.A. Spaltenattribute der Tabelle "person")
     */
    public String[] getAttributes() {
        return new String[] {
            "pid",
            "name",
            "vorname",
            "initialen",
            "titel",
            "anrede",
            "kunde",
            "lieferant",
            "partner",
            "bemerkung" };
    }

    /**
     * Liefert Anzahl der Attribute.
     */
    public int getColumnCount() {
        return 10;
    }

    /**
     * Liefert alle primären Daten zur Person (ohne Zusatzinfo).
     */
    public String toString() {
        String s;
        StringBuffer sb = new StringBuffer();

        s = getSalutation();
        if (s.length() > 0)
            sb.append(s + " ");

        sb.append(getFullName());

        s = getTitle();
        if (s.length() > 0)
            sb.append(" (" + s + ")");

        return sb.toString();
    }
}
