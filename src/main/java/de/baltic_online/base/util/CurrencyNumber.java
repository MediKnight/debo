package de.baltic_online.base.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Euro-W�hrungsklasse.
 *
 * Instanzen dieser Klasse sind W�hrungsbetr�ge in Euro oder in anderen
 * noch g�ltigen W�hrungen mit konstantem Umrechnungskurs zum Euro.
 * <p>
 * Der Betrag wird durch den Datentyp <code>long</code> bis auf ein
 * hundertstel Cent dargestellt. Dies erm�glicht immerhin die Darstellung
 * von �ber einer Billiarde (10^15) Euro.
 * <p>
 * F�r Objekte dieser Klasse stehen die vier Grundrechnungsarten sowie
 * diverse Umrechnungsfunktionen bereit.
 */

public class CurrencyNumber extends Number implements Cloneable, Comparable<CurrencyNumber> {
  private static final long serialVersionUID = 1L;

  /**
   * W�hrung in Euro (default).
   */
  public final static int EUR = 0;

  /**
   * W�hrung in DM.
   */
  public final static int DM = 1;

  /**
   * W�hrung in DEM.
   */
  public final static int DEM = 1;

  /**
   * W�hrung in Belgische Franken.
   */
  public final static int BEF = 2;

  /**
   * W�hrung in Finnmark.
   */
  public final static int FIM = 3;

  /**
   * W�hrung in Franz�siche Franken.
   */
  public final static int FRF = 4;

  /**
   * W�hrung in Irische Pfund.
   */
  public final static int IEP = 5;

  /**
   * W�hrung in Italienische Lira.
   */
  public final static int ITL = 6;

  /**
   * W�hrung in Luxemburgische Franc.
   */
  public final static int LUF = 7;

  /**
   * W�hrung in Niederl�ndische Gulden.
   */
  public final static int NLG = 8;

  /**
   * W�hrung in �stereichische Schilling.
   */
  public final static int ATS = 9;

  /**
   * W�hrung in Portugisiche Escudo.
   */
  public final static int PTE = 10;

  /**
   * W�hrung in Spanische Peseta.
   */
  public final static int ESP = 11;

  private static String[] csymbols =
    {
    "\u20AC EUR EURO EU E",
    "DM DEM",
    "BEF BFRS BF BFR",
    "FIM FM FMK",
    "FRF F FF",
    "IEP IR� IR IRP",
    "ITL L LIT",
    "LUF LFRS LFR LF",
    "NLG HFL FL",
    "ATS �S S",
    "PTE ESC",
    "ESP PTS" };

  private static double[] cfactor =
    {
    1.0,
    1.95583,
    40.339,
    5.94573,
    6.55957,
    0.787564,
    1936.27,
    40.339,
    2.20371,
    13.7603,
    200.487,
    166.386 };

  private static Locale[] locale = {
    //    Locale.US,
    Locale.GERMANY,
    Locale.GERMANY,
    new Locale("fr", "BE"),
    new Locale("fi", "FI"),
    Locale.FRANCE,
    new Locale("en", "IE"),
    Locale.ITALY,
    new Locale("fr", "LU"),
    new Locale("nl", "NL"),
    new Locale("de", "AT"),
    new Locale("pt", "PT"),
    new Locale("es", "ES")};

  private long value;
  private int currency;

  /**
   * Erzeugt W�hrungsbetrag von 0 in Euro.
   */
  public CurrencyNumber() {
    value = 0L;
    currency = EUR;
  }

  /**
   * Erzeugt W�hrungsbetrag von 0 in gegebener W�hrung.
   *
   * @param currency W�hrung
   */
  public CurrencyNumber(int currency) {
    value = 0L;
    this.currency = currency;
  }

  /**
   * Erzeugt W�hrungsbetrag interner Darstellung in Euro.
   *
   * @param value neuer Betrag (interne Darstellung)
   */
  public CurrencyNumber(long value) {
    this.value = value;
    currency = EUR;
  }

  /**
   * Erzeugt W�hrungsbetrag interner Darstellung in gegebener W�hrung.
   *
   * @param value neuer Betrag (interne Darstellung)
   * @param currency W�hrung
   */
  public CurrencyNumber(long value, int currency) {
    this.value = value;
    this.currency = currency;
  }

  /**
   * Erzeugt W�hrungsbetrag in Euro.
   *
   * @param v neuer Betrag
   */
  public CurrencyNumber(double v) {
    this(v, EUR);
  }

  /**
   * Erzeugt W�hrungsbetrag in gegebener W�hrung.
   *
   * @param v neuer Betrag
   * @param currency W�hrung
   */
  public CurrencyNumber(double v, int currency) {
    value = (long) (v * 10000.0 + (v > 0 ? 0.5 : -0.5));
    this.currency = currency;
  }

  /**
   * Erzeugt W�hrungsbetrag in Euro.
   *
   * @param f neuer Betrag
   */
  public CurrencyNumber(float f) {
    this(f, EUR);
  }

  /**
   * Erzeugt W�hrungsbetrag in gegebener W�hrung.
   *
   * @param f neuer Betrag
   * @param currency W�hrung
   */
  public CurrencyNumber(float f, int currency) {
    this((double) f, currency);
  }

  /**
   *
   */
  public int getCurrency() {
    return currency;
  }

  /**
   * W�hrungsbetr�ge k�nnen kopiert werden.
   *
   * @return identische Kopie des Objekts
   */
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException x) {
      throw new Error("Bad clone()-Method");
    }
  }

  public CurrencyNumber createClone() {
    return (CurrencyNumber) clone();
  }

  /**
   * 
   */
  public CurrencyNumber round(int precision) {
    double dv = doubleValue() * (long) Math.pow(10, precision) + (doubleValue() > 0 ? 0.5 : -0.5);
    double rv = (long) dv / Math.pow(10, precision);
    value = (long) (rv * 10000);
    return this;
  }

  /**
   * Konvertiert nach Euro und liefert neues Objekt zur�ck.
   *
   * @return �quivalenter W�hrungsbetrag in Euro
   */
  public CurrencyNumber toEuro() {
    return new CurrencyNumber((long) ((double) value / cfactor[currency] + (value > 0 ? 0.5 : -0.5)));
  }

  /**
   * Konvertiert Objekt nach Euro und �ndert das Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber norm() {
    CurrencyNumber cn = toEuro();
    value = cn.value;
    currency = EUR;
    return this;
  }

  /**
   * Konvertiert in gegebene W�hrung und liefert neues Objekt zur�ck.
   *
   * @param gegebene W�hrung
   * @return �quivalenter W�hrungsbetrag in gegebener W�hrung
   */
  public CurrencyNumber toCurrency(int newCurrency) {
    double f =
        (currency == EUR)
        ? cfactor[newCurrency]
            : cfactor[newCurrency] / cfactor[currency];

        return new CurrencyNumber((long) ((double) value * f + (value > 0 ? 0.5 : -0.5)), newCurrency);
  }

  /**
   * Liefert negiertes Objekt zur�ck (l��t Objekt unver�ndert).
   *
   * @return negativer W�hrungsbetrag
   */
  public CurrencyNumber neg() {
    return new CurrencyNumber(-value, currency);
  }

  /**
   * Negiert Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber negate() {
    value = -value;
    return this;
  }

  /**
   * Addiert gegebenen W�hrungsbetrag zum Objekt.
   *
   * @param cn W�hrungsbetrag
   * @return <code>this</code>
   */
  public CurrencyNumber add(CurrencyNumber cn) {
    int oc = currency;
    CurrencyNumber ce = cn.toEuro();
    norm();
    value += ce.value;

    CurrencyNumber c = toCurrency(oc);
    value = c.value;
    currency = oc;

    return this;
  }

  /**
   * Subtrahiert gegebenen W�hrungsbetrag vom Objekt.
   *
   * @param cn W�hrungsbetrag
   * @return <code>this</code>
   */
  public CurrencyNumber sub(CurrencyNumber cn) {
    int oc = currency;
    CurrencyNumber ce = cn.toEuro();
    norm();
    value -= ce.value;

    CurrencyNumber c = toCurrency(oc);
    value = c.value;
    currency = oc;

    return this;
  }

  /**
   * Multipliziert <code>int</code> zum Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber mul(int x) {
    value *= x;

    return this;
  }

  /**
   * Multipliziert <code>long</code> zum Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber mul(long x) {
    value *= x;

    return this;
  }

  /**
   * Multipliziert <code>double</code> zum Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber mul(double x) {
    double v = (double) value * x;
    value = (long) (v + (v > 0 ? 0.5 : -0.5));

    return this;
  }

  /**
   * Multipliziert <code>float</code> zum Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber mul(float x) {
    return mul((double) x);
  }

  /**
   * Dividiert <code>int</code> vom Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber div(int x) {
    value /= (long) x;

    return this;
  }

  /**
   * Dividiert <code>long</code> vom Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber div(long x) {
    value /= x;

    return this;
  }

  /**
   * Dividiert <code>double</code> vom Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber div(double x) {
    double v = (double) value / x;
    value = (long) (v + (v > 0 ? 0.5 : -0.5));

    return this;
  }

  /**
   * Dividiert <code>float</code> vom Objekt.
   *
   * @return <code>this</code>
   */
  public CurrencyNumber div(float x) {
    return div((double) x);
  }

  /**
   * Dividiert gegebenes vom aktuellen Objekt.
   *
   * @return <code>(double)toEuro().value / (double)cn.toEuro().value *
   * 10000.0</code>
   */
  public double div(CurrencyNumber cn) {
    return (double) toEuro().value / (double) cn.toEuro().value;
  }

  /**
   * Objektvergleich im Sinne von <code>Comparable</code>
   *
   * @param o zu vergleichender Betrag
   *
   * @see Comparable
   */
  public int compareTo(CurrencyNumber o) {
    long v1 = toEuro().value;
    long v2 = o.toEuro().value;

    if (v1 == v2)
      return 0;
    if (v1 < v2)
      return -1;
    return 1;
  }

  /**
   * Objektvergleich.
   *
   * @param o zu vergleichender Betrag
   *
   * @return <code>toEuro().value == ((CurrencyNumber)o).toEuro().value</code>
   */
  public boolean equals(Object o) {
    return toEuro().value == ((CurrencyNumber) o).toEuro().value;
  }

  /**
   * String-Darstellung des W�hrungsbetrags.
   *
   * Der W�hrungsbetrag wird bzgl. des Landes der zugrundeliegenden W�hrung
   * dargestellt.
   *
   * @return String-Darstellung des W�hrungsbetrags inklusive W�hrungssymbol
   *
   * @see #toString(Locale,boolean)
   */
  public String toString() {
    return toString(locale[currency], true);
  }

  /**
   * String-Darstellung des W�hrungsbetrags.
   *
   * Der W�hrungsbetrag wird bzgl. des gegebenen <code>locale</code> Objekts
   * dargestellt.
   *
   * @param locale angewendetes <code>locale</code> Objekt
   * @return String-Darstellung des W�hrungsbetrags inklusive W�hrungssymbol
   *
   * @see #toString(Locale,boolean)
   */
  public String toString(Locale locale) {
    return toString(locale, true);
  }

  /**
   * String-Darstellung des W�hrungsbetrags.
   *
   * Der W�hrungsbetrag wird bzgl. des gegebenen <code>locale</code> Objekts
   * dargestellt.
   *
   * @param locale angewendetes <code>locale</code> Objekt
   * @param showCurrencySymbol bestimmt, ob W�hrungssymbol angef�gt
   * werden soll
   * @return String-Darstellung des W�hrungsbetrags
   */
  public String toString(Locale locale, boolean showCurrencySymbol) {
    DecimalFormat form =
        new DecimalFormat("#,###,###,###,##0.00", new DecimalFormatSymbols(locale));
    StringBuffer sb = new StringBuffer();
    sb.append(form.format(doubleValue()));
    if (showCurrencySymbol) {
      sb.append(" ");
      sb.append(getCurrencySymbol());
    }
    return sb.toString();
  }

  /**
   * Liefert W�hrungssymbol zur W�hrung des Objekts.
   *
   * @return W�hrungssymbol
   */
  public String getCurrencySymbol() {
    return getCurrencySymbol(currency);
  }

  /**
   * Liefert W�hrungssymbol zur angegebenen W�hrung.
   *
   * @param currency angegebene W�hrung
   * @return W�hrungssymbol
   */
  public static String getCurrencySymbol(int currency) {
    StringTokenizer st = new StringTokenizer(csymbols[currency]);
    return st.nextToken();
  }

  /**
   * Parsen des gegebenen Strings auf einen W�hrungsbetrag.
   *
   * Falls kein W�hrungssymbol dem Betrag angeh�ngt ist, wird Euro
   * als Default genommen.
   *
   * @param s Betrag in String-Darstellung
   * @return W�hrungsbetrag
   * @exception IllegalArgumentException, wenn Eingabe nicht geparst
   * werden kann.
   */
  public static CurrencyNumber parse(String s) {
    return parse(s, EUR);
  }

  /**
   * Parsen des gegebenen Strings auf einen W�hrungsbetrag.
   *
   * @param s Betrag in String-Darstellung
   * @param defaultCurreny Default-W�hrung, falls kein W�hrungssymbol
   * dem Betrag angeh�ngt ist
   * @return W�hrungsbetrag
   * @exception IllegalArgumentException, wenn Eingabe nicht geparst
   * werden kann.
   */
  public static CurrencyNumber parse(String s, int defaultCurrency) {
    // finde Position des W�hrungssymbols:
    int n = s.length();
    int cp = -1;
    for (int i = 0; i < n; i++) {
      char c = s.charAt(i);
      if (Character.isLetter(c) || c == '\u20AC') {
        cp = i;
        break;
      }
    }

    // Wenn String mit W�hrungssymbol beginnt, ist Eingabe ung�ltig
    if (cp == 0)
      throw new IllegalArgumentException("invalid currency position");

    String snum, sc;

    if (cp > 0) {
      snum = s.substring(0, cp).trim();
      sc = s.substring(cp).trim();
    } else {
      snum = s.trim();
      sc = getCurrencySymbol(defaultCurrency);
    }

    if (snum.length() == 0)
      throw new IllegalArgumentException("zerolength amount");

    if (sc.length() == 0)
      sc = getCurrencySymbol(defaultCurrency);

    int currency = parseCurrencySymbol(sc);

    DecimalFormat form =
        new DecimalFormat(
            "#,###,###,###,##0.00",
            new DecimalFormatSymbols(locale[currency]));

    try {
      Number num = form.parse(snum);
      return new CurrencyNumber(num.doubleValue(), currency);
    } catch (ParseException x) {
      throw new IllegalArgumentException("invalid number format");
    }
  }

  /**
   * This method parses a currency symbol and returns
   * a matched <tt>int</tt>.
   * <p>
   * This method is the reverse function of
   * <tt>getCurrenySymbol()</tt>
   */
  public static int parseCurrencySymbol(String symbol) {
    for (int i = 0; i < csymbols.length; i++) {
      StringTokenizer st = new StringTokenizer(csymbols[i]);
      while (st.hasMoreTokens())
        if (st.nextToken().equalsIgnoreCase(symbol)) {
          return i;
        }
    }

    throw new IllegalArgumentException("invalid currency symbol");
  }

  /**
   * Liefert Anzahl der ber�cksichtigten W�hrungen inklusive Euro.
   *
   * @return Anzahl der W�hrungen
   */
  public static int getCurrencies() {
    return cfactor.length;
  }

  /**
   * Liefert ganzahligen Anteil, sofern der ganzahlige Anteil des Betrags
   * als <code>int</code> dargestellt werden kann.
   */
  public int intValue() {
    return (int) (value / 10000L);
  }

  /**
   * Liefert interne Darstellung, d.h. nicht ausschlie�lich den ganzzahligen
   * Anteil, sondern den gesamten Betrag.
   */
  public long longValue() {
    return value;
  }

  /**
   * Genaueste Betragsdarstellung.
   *
   * Zum rechnen mit W�hrungen sollten jedoch die Funktionen
   * <code>add,sub,div,mul,neg</code> verwendet werden.
   *
   * @return <code>(double)value/10000.0</code>
   */
  public double doubleValue() {
    return (double) value / 10000.0;
  }

  public float floatValue() {
    return (float) value / 10000.0f;
  }

  public byte byteValue() {
    return (byte) (value / 10000L);
  }

  public short shortValue() {
    return (short) (value / 10000L);
  }
}