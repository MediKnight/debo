
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package de.bo.base.dbs;


/**
 *
 */
public class SimpleSelection extends TableSelection {

    /**
     *
     */
    String whereClause;


    /**
     *
     */
    public SimpleSelection( String whereClause ) {
        this.whereClause = whereClause;
    }


    /**
     *
     */
    public String toString() {
        return whereClause;
    }
}