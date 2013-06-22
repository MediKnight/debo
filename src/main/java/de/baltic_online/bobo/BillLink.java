package de.baltic_online.bobo;

import de.baltic_online.base.store2.StoreKeeper;

/**
 * Baltic-Online Geschäftsobjekt <b>Verbindung</b>.
 * <p>
 */

public class BillLink extends Bobo
{
    /**
     * Create a bill link object
     */
    public BillLink() {
	super();
    }

    /**
     * Create a bill link object
     *
     * @param storeKeeper used keeper
     */
    public BillLink(StoreKeeper storeKeeper) {
	super(storeKeeper);
    }

    private String getClaimString() {
	return getString(1);
    }

    private String getLiabilityString() {
	return getString(2);
    }

    public int getParentCount() {
	return 0;
    }

    protected Bobo createParent(int index) {
	return null;
    }

    protected Object getParentKey(int index) {
	return null;
    }

    protected void setParentKey(int index,Object key) {
    }

    public String getKeyIdentifier() {
	return "id";
    }

    public String toString() {
	return "("+getClaimString()+")x("+getLiabilityString()+")";
    }

    public String getIdentifier() {
	return "fordverb";
    }

    public String[] getAttributes() {
	return new String[] { "id", "fidlist", "vidlist" };
    }

    public int getColumnCount() {
	return 3;
    }
}
