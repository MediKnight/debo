package de.bo.base.store2.sql;

import java.sql.*;
import java.util.*;

import de.bo.base.store2.*;
import de.bo.base.util.log.*;

public abstract class SQLBase implements StoreKeeper, TraceConstants {
	private final static String NOKEY = "No key on object ";
	private final static String TRACECLASS = JDBC;

	private final static int RETRIEVAL_SIZE = 1000;
	private final static int RETRIEVAL_INC = 500;

	private static Tracer tracer = null;

	protected static Connection defaultConnection;
	protected static SQLToolkit defaultToolkit;

	protected Connection connection;
	protected SQLToolkit toolkit;

	static {
		defaultConnection = null;
		defaultToolkit = new SQLToolkit();
	}

	public static void setDefaultConnection(Connection conn) {
		defaultConnection = conn;
	}

	public static void setTracer(Tracer tracer) {
		SQLBase.tracer = tracer;
	}

	public static Tracer getTracer() {
		return tracer;
	}

	public SQLBase() {
		this(null, null);
	}

	public SQLBase(Connection connection) {
		this(connection, null);
	}

	public SQLBase(SQLToolkit toolkit) {
		this(null, toolkit);
	}

	public SQLBase(Connection conn, SQLToolkit tk) {
		connection = (conn == null) ? defaultConnection : conn;
		toolkit = (tk == null) ? defaultToolkit : tk;
	}

	public void setSQLToolkit(SQLToolkit sqlToolkit) {
		toolkit = sqlToolkit;
	}

	public SQLToolkit getSQLToolkit() {
		return toolkit;
	}

	public Connection getConnection() {
		return connection;
	}

	//    public abstract Object createKey(Storable object)
	//      throws StoreException;

	protected String createSelectString(SQLRecord record) {

		StringBuffer sbQuery = new StringBuffer();
		String[] attribute = record.getAttributes();

		sbQuery.append("select ");
		for (int i = 0; i < attribute.length; i++) {
			if (i > 0)
				sbQuery.append(",");
			sbQuery.append(attribute[i]);
		}
		sbQuery.append(" from " + record.getIdentifier());

		if (tracer != null)
			tracer.trace(TRACECLASS, sbQuery.toString());

		return sbQuery.toString();
	}

	protected void prepareWhereClause(
		PreparedStatement stmt,
		SQLSelection selection,
		int startIndex)
		throws StoreException {

		try {
			if (selection != null)
				for (Iterator it = selection.getValues().iterator();
					it.hasNext();
					) {
					Object value = it.next();
					if (value != null)
						stmt.setObject(startIndex++, value);
				}
		} catch (SQLException x) {
			// Exception-Delegation:
			throw new StoreException(x);
		}
	}

	/**
	 * Erzeugen eines SQL-Query-Statements ohne Sortierung.
	 *
	 * @param record Datenbank-Objekt (Datensatz)
	 * @param selection Auswahl (bei <tt>null</tt> unbeschränkte
	 * Auswahl)
	 *
	 * @return SQL-Query-String
	 */
	protected PreparedStatement createQueryStatement(
		SQLRecord record,
		SQLSelection selection)
		throws StoreException {

		return createQueryStatement(record, selection, null);
	}

	/**
	 * Erzeugen eines SQL-Query-Statements.
	 *
	 * @param record Datenbank-Objekt (Datensatz)
	 * @param selection Auswahl (bei <tt>null</tt> unbeschränkte
	 * Auswahl)
	 * @param order SQL Order-Klausel (Kommata-getrennte Attribut-Liste
	 * mit evtl. "desc"-Zusatz).
	 *
	 * @return SQL-Query-String
	 */
	protected PreparedStatement createQueryStatement(
		SQLRecord record,
		SQLSelection selection,
		String order)
		throws StoreException {

		try {
			StringBuffer sbQuery = new StringBuffer();
			String[] attribute = record.getAttributes();

			sbQuery.append(createSelectString(record));

			if (selection != null)
				sbQuery.append(" where " + selection);

			// Order-Klausel bilden ...
			if (order != null && order.length() > 0) {
				sbQuery.append(" order by ");
				sbQuery.append(order);
			}

			// System.err.println(sbQuery.toString());

			if (tracer != null)
				tracer.trace(TRACECLASS, sbQuery.toString());

			PreparedStatement stmt =
				connection.prepareStatement(sbQuery.toString());

			prepareWhereClause(stmt, selection, 1);

			return stmt;
		} catch (SQLException x) {
			// Exception-Delegation:
			throw new StoreException(x);
		}
	}

	/**
	 * Objekt aus Datenbank zum passenden Schlüssel einlesen.
	 *
	 * @param object Quell-Objekt (dient nur zur Identifizierung)
	 * @param key Passender Schlüssel
	 */
	public Object[] retrieve(Storable object, Object key)
		throws StoreException {

		if (key == null)
			throw new StoreException(NOKEY + object);

		// Objekte dieser Implementierung sind
		// Baltic-Online Business Objekte.
		SQLRecord record = (SQLRecord) object;
		Object[] data = null;
		SQLSelection sel =
			new SQLSelection(toolkit, record.getKeyIdentifier(), key);
		// Abfrage anwenden ...
		try {
			PreparedStatement sm = createQueryStatement(record, sel);
			if (tracer != null)
				tracer.trace(TRACECLASS, sm);
			ResultSet rset = sm.executeQuery();

			if (rset.next()) { // Erfolg!

				// Daten ziehen ...
				data = new Object[record.getAttributes().length];
				for (int i = 0; i < data.length; i++)
					data[i] = rset.getObject(i + 1);
			}

			rset.close();
			sm.close();

			return data;
		} catch (SQLException x) {
			// Exception-Delegation:
			throw new StoreException(x);
		}
	}

	/**
	 * Liefert SQL-"update"-Statement zum passenden Datensatz.
	 *
	 * @param record Datensatz
	 *
	 * @return SQL-"update"-Statement
	 *
	 */
	protected PreparedStatement createUpdateStatement(
		SQLRecord record,
		Object[] data,
		SQLSelection selection)
		throws StoreException {

		try {
			StringBuffer sb = new StringBuffer();

			sb.append("update ");
			sb.append(record.getIdentifier());
			sb.append(" set ");

			String[] attribute = record.getAttributes();
			int n = attribute.length;

			for (int i = 0; i < n; i++) {
				if (i > 0)
					sb.append(",");

				sb.append(attribute[i] + "=");
				if (data[i] == null)
					sb.append(toolkit.getNullString());
				else
					sb.append("?");
			}

			if (selection != null)
				sb.append(" where " + selection);

			if (tracer != null)
				tracer.trace(TRACECLASS, sb.toString());

			PreparedStatement stmt = connection.prepareStatement(sb.toString());

			int q = 0; // zählt '?'
			for (int i = 0; i < n; i++)
				if (data[i] != null)
					stmt.setObject(++q, data[i]);

			prepareWhereClause(stmt, selection, q + 1);

			return stmt;
		} catch (SQLException x) {
			// Exception-Delegation:
			throw new StoreException(x);
		}
	}

	public void store(Storable object, Object[] data, Object key)
		throws StoreException {

		if (key == null)
			throw new StoreException(NOKEY + object);

		SQLRecord record = (SQLRecord) object;
		SQLSelection sel =
			new SQLSelection(toolkit, record.getKeyIdentifier(), key);

		try {
			PreparedStatement sm = createUpdateStatement(record, data, sel);

			if (tracer != null)
				tracer.trace(TRACECLASS, sm);

			sm.executeUpdate();
			sm.close();
		} catch (SQLException x) {
			throw new StoreException(x);
		}
	}

	/**
	 * Liefert SQL-"insert"-Statement zum passenden Datensatz.
	 *
	 * @param record Datensatz
	 *
	 * @return SQL-"insert"-Statement
	 *
	 */
	protected PreparedStatement createInsertStatement(
		SQLRecord record,
		Object[] data,
		Object key)
		throws StoreException {

		try {
			StringBuffer sb = new StringBuffer();

			sb.append("insert into ");
			sb.append(record.getIdentifier());
			sb.append(" (");

			String[] attribute = record.getAttributes();
			int n = attribute.length;
			int keyColumn = -1;

			for (int i = 0; i < n; i++)
				if (attribute[i].equals(record.getKeyIdentifier())) {
					keyColumn = i;
					break;
				}

			boolean flag = false;
			for (int i = 0; i < n; i++) {
				if (i == keyColumn && key == null)
					continue;

				if (flag)
					sb.append(",");

				sb.append(attribute[i]);
				flag = true;
			}

			sb.append(") values (");
			flag = false;
			for (int i = 0; i < n; i++) {
				if (i == keyColumn && key == null)
					continue;

				if (flag)
					sb.append(",");

				if (data[i] != null || i == keyColumn)
					sb.append("?");
				else
					sb.append(toolkit.getNullString());

				flag = true;
			}
			sb.append(")");

			if (tracer != null)
				tracer.trace(TRACECLASS, sb.toString());

			PreparedStatement stmt = connection.prepareStatement(sb.toString());

			int q = 0; // zählt '?'
			for (int i = 0; i < n; i++) {
				if (i == keyColumn) {
					if (key == null)
						continue;
					stmt.setObject(++q, key);
				} else if (data[i] != null)
					stmt.setObject(++q, data[i]);
			}

			return stmt;
		} catch (SQLException x) {
			throw new StoreException(x);
		}
	}

	public void insert(Storable object, Object[] data, Object key)
		throws StoreException {

		SQLRecord record = (SQLRecord) object;

		try {
			PreparedStatement sm = createInsertStatement(record, data, key);

			if (tracer != null)
				tracer.trace(TRACECLASS, sm);

			sm.executeUpdate();
			sm.close();

			record.setKeyInternal(key);
		} catch (SQLException x) {
			throw new StoreException(x);
		}
	}

	protected PreparedStatement createDeleteStatement(
		SQLRecord record,
		SQLSelection selection)
		throws StoreException {

		try {
			StringBuffer sb = new StringBuffer();

			sb.append("delete from ");
			sb.append(record.getIdentifier());
			if (selection != null)
				sb.append(" where " + selection);

			if (tracer != null)
				tracer.trace(TRACECLASS, sb.toString());

			PreparedStatement stmt = connection.prepareStatement(sb.toString());

			prepareWhereClause(stmt, selection, 1);

			return stmt;
		} catch (SQLException x) {
			throw new StoreException(x);
		}
	}

	public void delete(Storable object, Object key) throws StoreException {

		if (key == null)
			throw new StoreException(NOKEY + object);

		SQLRecord record = (SQLRecord) object;
		SQLSelection sel =
			new SQLSelection(toolkit, record.getKeyIdentifier(), key);

		try {
			PreparedStatement sm = createDeleteStatement(record, sel);

			if (tracer != null)
				tracer.trace(TRACECLASS, sm);

			sm.executeUpdate();
			sm.close();

			record.setKeyInternal(null);
		} catch (SQLException x) {
			throw new StoreException(x);
		}
	}

	public Collection retrieve(Storable object) throws StoreException {

		return retrieve(object, null, null, null);
	}

	public Collection retrieve(Storable object, ObjectFilter filter)
		throws StoreException {

		return retrieve(object, null, filter, null);
	}

	public Collection retrieve(Storable object, Selection selection)
		throws StoreException {

		return retrieve(object, selection, null, null);
	}

	public Collection retrieve(
		Storable object,
		Selection selection,
		String order)
		throws StoreException {

		return retrieve(object, selection, null, order);
	}

	public Collection retrieve(
		Storable object,
		Selection selection,
		ObjectFilter filter)
		throws StoreException {

		return retrieve(object, selection, filter, null);
	}

	public Collection retrieve(
		Storable object,
		Selection selection,
		ObjectFilter filter,
		String order)
		throws StoreException {

		SQLRecord record = (SQLRecord) object;
		Vector v = new Vector(RETRIEVAL_SIZE, RETRIEVAL_INC);
		int n = record.getAttributes().length;

		try {
			PreparedStatement sm =
				createQueryStatement(record, (SQLSelection) selection, order);

			if (tracer != null)
				tracer.trace(TRACECLASS, sm);

			ResultSet rset = sm.executeQuery();
			while (rset.next()) { // Erfolg!

				// Daten ziehen ...
				Object[] data = new Object[n];
				for (int i = 0; i < n; i++)
					data[i] = rset.getObject(i + 1);

				SQLRecord r2 = (SQLRecord) record.getClass().newInstance();
				r2.putInternal(data);
				r2.setStoreKeeper(record.getStoreKeeper());

				if (filter == null || filter.accept(r2))
					v.add(r2);
			}

			rset.close();
			sm.close();

			// Baue evtl. kleineren Vector
			if (v.size() <= RETRIEVAL_SIZE / 2) {
				int m = v.size();
				Vector v1 = new Vector(m);
				for (int i = 0; i < m; i++)
					v1.add(v.get(i));

				return v1;
			}

			return v;
		} catch (SQLException x) {
			// Exception-Delegation:
			throw new StoreException(x);
		} catch (Exception x) {
			x.printStackTrace();
			return null;
		}
	}
}