package de.bo.base.store2.sql;

import de.bo.base.store2.*;

public abstract class SQLRecord extends AbstractStorable {
	protected SQLRecord() {
		super();
		data = new Object[getAttributes().length];
	}

	protected SQLRecord(StoreKeeper storeKeeper) {
		super(storeKeeper);
		data = new Object[getAttributes().length];
	}

	public String debugInfo() {
		if (data == null)
			return "<< not set >>";

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			if (i > 0)
				sb.append(" | ");
			if (data[i] == null)
				sb.append("<null>");
			else
				sb.append(data[i].toString());
		}

		return sb.toString();
	}

	// Paket-interne Funktion: Empf�ngt Daten vom Objekt
	void getInternal(Object[] data) {
		get(data);
	}

	protected void get(Object[] data) {
		for (int i = 0; i < this.data.length; i++)
			data[i] = this.data[i];
	}

	// Paket-interne Funktion: �bertr�gt Daten zum Objekt
	void putInternal(Object[] data) {
		put(data);
	}

	protected void put(Object[] data) {
		for (int i = 0; i < data.length; i++)
			this.data[i] = data[i];
	}

	// Paket-interne Funktion: Setzt Prim�rschl�ssel
	void setKeyInternal(Object key) {
		setKey(key);
	}

	/**
	 * Liefert globale Objekt-Bezeichnung (Tabellen-Name).
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
}
