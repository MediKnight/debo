/*
 *
 */

package de.bo.base.util;

import java.util.*;

/**
 * Ein universelles Datum. Ein universelles Datum liefert bei Bedarf ein
 * absolutes Datum (<code>getDate</code>) und hat eine textuelle
 * Beschreibung.
 *
 * @author Jan Bernhardt
 * @version $Id$
 */
public interface UniversalDate {

    /**
     * Liefert ein absolutes Datum.
     */
    public Date getDate();

    /**
     * Legt die Beschreibung des Datums fest.
     *
     * @param description	Eine textuelle Beschreibung des Datums
     */
    public void setDescription(String description);

    /**
     * Liefert die textuelle Beschreibung des Datums.
     */
    public String getDescription();
}
