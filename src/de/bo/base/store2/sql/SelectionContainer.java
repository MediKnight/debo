package de.bo.base.store2.sql;

import java.util.*;

public abstract class SelectionContainer extends SQLSelection {
	protected Stack stack;

	public SelectionContainer(SQLToolkit toolkit) {
		super(toolkit);

		stack = new Stack();
	}

	/**
	 * Hinzufügen einer Selektion.
	 *
	 * @param selection Selektion, die hinzugefügt werden soll
	 */
	public void addSelection(SQLSelection selection) {
		stack.push(selection);
	}

	/**
	 * Entfernen der letzten Selektion.
	 *
	 * @return letzte Selektion, falls Stapel nicht leer, sonst
	 * <code>null</code>
	 */
	public SQLSelection removeSelection() {
		if (!stack.isEmpty())
			return (SQLSelection) stack.pop();
		return null;
	}

	/**
	 * Entfernen aller Selektionen.
	 */
	public void removeSelections() {
		while (!stack.isEmpty())
			stack.pop();
	}

	/**
	 * String-Darstellung.
	 *
	 * Die passenden Vergleichswerte werden mit <tt>getValues()</tt>
	 * ermittelt.
	 *
	 * @return String-Darstellung
	 * @see #getValues()
	 * @see SQLSelection#toString()
	 */
	public String toString() {
		int n = stack.size();

		if (n == 0)
			return TRUE.toString();
		if (n == 1)
			return stack.elementAt(0).toString();

		StringBuffer sb = new StringBuffer();
		String ops = getOperatorString();

		sb.append("(");
		for (int i = 0; i < n; i++) {
			if (i > 0)
				sb.append(" " + ops + " ");
			sb.append(stack.elementAt(i).toString());
		}
		sb.append(")");

		return sb.toString();
	}

	/**
	 * Liefert die Werte des Containers in der Reihenfolge der Bezeichner
	 * aus <tt>toString()</tt> zurück.
	 *
	 * @see #toString()
	 */
	public Collection getValues() {
		Collection c = new LinkedList();
		int n = stack.size();

		for (int i = 0; i < n; i++) {
			SQLSelection sel = (SQLSelection) stack.elementAt(i);
			c.addAll(sel.getValues());
		}

		return c;
	}

	protected abstract String getOperatorString();
}
