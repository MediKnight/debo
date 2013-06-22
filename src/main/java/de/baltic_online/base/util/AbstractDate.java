/*
 *
 */

package de.baltic_online.base.util;

/**
 * Ein abstraktes Datum. Ein abstraktes Datum erweitert ein einfaches
 * Datum um die M&ouml;glichkeit, eine textuelle Beschreibung des
 * dargestellten Datums anzugeben (z.B. "Jetzt", "Morgen", ...).
 */
public abstract class AbstractDate implements UniversalDate {

    /**
     *
     */
    protected String description;


    /**
     * Legt die textuelle Beschreibung des abstrakten Datums fest.
     * Ist diese <code>null</code> so wird die <code>toString</code>-Methode
     * verwendet.
     *
     * @param description	Beschreibung des Datums
     */
    public void setDescription( String description ) {
	this.description = description;
    }

    /**
     * Liefert die textuelle Beschreibung des Datums.
     *
     * @return Die textuelle Beschreibung des Datums
     */
    public String getDescription() {
	return description;
    }


    //
    //
    //
    public String toString() {
	if( description != null ) {
	    return description;
	} else {
	    return super.toString();
	}
    }
}
