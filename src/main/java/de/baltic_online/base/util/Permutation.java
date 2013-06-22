
package de.baltic_online.base.util;

import java.util.*;

/**
 * Permutationen �ndern die Reihenfolge von Elementen in
 * Arrays und Collections.
 * <p>
 * Permutationen k�nnen kombiniert werden und zu jeder Permutation
 * kann die Umkehrpermutation gebildet werden.
 */
public class Permutation
implements Cloneable,java.io.Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Dieses einzige Element repr�sentiert die Permutation.
   *
   * @see #set(int[] array)
   */
  protected int[] array;

  /**
   * Dieser Konstruktor setzt <tt>array</tt> auf <tt>null</tt>.
   */
  protected Permutation() {
    array = null;
  }

  /**
   * Erzeugt Permutation mit gegebenen Array.
   *
   * @param array Repr�sentant der Permutation
   * @see #set(int[] array)
   */
  public Permutation(int[] array) {
    set( array );
  }

  public Permutation(int n) {
    array = new int[n];
    for ( int i=0; i<n; array[i]=i++ );
  }

  /**
   * Setzen der Permutation.
   * <p>
   * Diese Methode pr�ft das Array. Das Array darf nicht <tt>null</tt>
   * sein, alle Elemente m�ssen gr��er oder gleich 0, kleiner als die
   * Gr��e des Arrays und voneinander verschieden sein.
   *
   * @param array Repr�sentant der Permutation
   * @exception IllegalArgumentException, falls obige Bedingungen
   * nicht erf�llt sind.
   */
  public void set(int[] array) {
    if ( array == null )
      throw new IllegalArgumentException();

    int n = array.length;
    for ( int i=0; i<n; i++ ) {
      int k = array[i];
      if ( k < 0 || k >= n )
	throw new IllegalArgumentException();
      for ( int j=0; j<i; j++ )
	if ( k == array[j] )
	  throw new IllegalArgumentException();
    }

    this.array = array;
  }

  public Object clone() {
    Permutation p = new Permutation();
    p.array = (int[])array.clone();

    return p;
  }

  public String toString() {
    return "("+StringTools.intArrayToStringList(array)+")";
  }

  /**
   * Liefert <tt>array</tt>.
   *
   * @return <tt>array</tt>
   * @see #array
   */
  public int[] getArray() {
    return array;
  }

  /**
   * Erzeugt inverse Permutation.
   *
   * @return inverse Permutaiton
   */
  public Permutation inverse() {
    MapEntry[] me = createMapping();
    Arrays.sort( me );
    return createPermutation( me );
  }

  /**
   * Erzeugt Hintereinanderausf�hrung von Permutationen.
   * <p>
   * Die neue Permutation entsteht durch Anwendung der Permutation
   * <tt>p</tt> auf die des aktuellen Objekts.
   *
   * @param p anzuwendende Permutation
   * @return neue Permutaiton
   */
  public Permutation combine(Permutation p) {
    Permutation q = new Permutation();
    int n = array.length;
    q.array = new int[n];
    for ( int i=0; i<n; i++ )
      q.array[i] = p.array[array[i]];

    return q;
  }

  /**
   * Anwendung der Permutation auf ein <tt>byte</tt>-Array.
   * <p>
   * Die Gr��e des Arrays darf gr��er als die der Permutation sein.
   * In diesem Fall wird die Reihenfolge der verbleibenden Elemente
   * nicht ge�ndert.
   *
   * @param src Quell-Array
   * @return neues umgeordnetes Array.
   * @see #map(byte[],int)
   */
  public byte[] map(byte[] src) {
    return map( src, 0 );
  }

  /**
   * Anwendung der Permutation ab bestimmter Position auf ein
   * <tt>byte</tt>-Array.
   * <p>
   * Die Gr��e des Arrays darf gr��er als die der Permutation sein.
   * In diesem Fall wird die Reihenfolge der verbleibenden Elemente
   * nicht ge�ndert.
   *
   * @param src Quell-Array
   * @param start Position, ab der die Umordnung wirken soll.
   * @return neues umgeordnetes Array.
   * @see #map(byte[])
   */
  public byte[] map(byte[] src,int start) {
    int n = array.length;
    int m = src.length;

    byte[] dest = new byte[m];
    System.arraycopy( src, 0, dest, 0, m );
    for ( int i=start; i<n+start; i++ )
      dest[i] = src[array[i-start]+start];

    return dest;
  }

  /**
   * @see #map(byte[])
   */
  public short[] map(short[] src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public short[] map(short[] src,int start) {
    int n = array.length;
    int m = src.length;

    short[] dest = new short[m];
    System.arraycopy( src, 0, dest, 0, m );
    for ( int i=start; i<n+start; i++ )
      dest[i] = src[array[i-start]+start];

    return dest;
  }

  /**
   * @see #map(byte[])
   */
  public char[] map(char[] src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public char[] map(char[] src,int start) {
    int n = array.length;
    int m = src.length;

    char[] dest = new char[m];
    System.arraycopy( src, 0, dest, 0, m );
    for ( int i=start; i<n+start; i++ )
      dest[i] = src[array[i-start]+start];

    return dest;
  }

  /**
   * @see #map(byte[])
   */
  public int[] map(int[] src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public int[] map(int[] src,int start) {
    int n = array.length;
    int m = src.length;

    int[] dest = new int[m];
    System.arraycopy( src, 0, dest, 0, m );
    for ( int i=start; i<n+start; i++ )
      dest[i] = src[array[i-start]+start];

    return dest;
  }

  /**
   * @see #map(byte[])
   */
  public long[] map(long[] src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public long[] map(long[] src,int start) {
    int n = array.length;
    int m = src.length;

    long[] dest = new long[m];
    System.arraycopy( src, 0, dest, 0, m );
    for ( int i=start; i<n+start; i++ )
      dest[i] = src[array[i-start]+start];

    return dest;
  }

  /**
   * @see #map(byte[])
   */
  public float[] map(float[] src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public float[] map(float[] src,int start) {
    int n = array.length;
    int m = src.length;

    float[] dest = new float[m];
    System.arraycopy( src, 0, dest, 0, m );
    for ( int i=start; i<n+start; i++ )
      dest[i] = src[array[i-start]+start];

    return dest;
  }

  /**
   * @see #map(byte[])
   */
  public double[] map(double[] src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public double[] map(double[] src,int start) {
    int n = array.length;
    int m = src.length;

    double[] dest = new double[m];
    System.arraycopy( src, 0, dest, 0, m );
    for ( int i=start; i<n+start; i++ )
      dest[i] = src[array[i-start]+start];

    return dest;
  }

  /**
   * @see #map(byte[])
   */
  public String map(String src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public String map(String src,int start) {
    return map( new StringBuffer(src) ).toString();
  }

  /**
   * @see #map(byte[])
   */
  public StringBuffer map(StringBuffer src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public StringBuffer map(StringBuffer src,int start) {
    int n = array.length;

    StringBuffer sb = new StringBuffer( src.toString() );
    for ( int i=start; i<n+start; i++ )
      sb.setCharAt( i, src.charAt(array[i-start]+start) );

    return sb;
  }

  /**
   * @see #map(byte[])
   */
  public <E> E[] map(E[] src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public <E> E[] map(E[] src,int start) {
    int n = array.length;
    int m = src.length;

    @SuppressWarnings("unchecked")
    E[] dest = (E[])
      java.lang.reflect.Array.
      newInstance( src.getClass().getComponentType(), m );

    System.arraycopy( src, 0, dest, 0, m );
    for ( int i=start; i<n+start; i++ )
      dest[i] = src[array[i-start]+start];

    return dest;
  }

  /**
   * @see #map(byte[])
   */
  public <E> Vector<E> map(Vector<E> src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   */
  public <E> Vector<E> map(Vector<E> src,int start) {
    int n = array.length;
    
    Vector<E> dest = new Vector<E>(src);

    for ( int i=start; i<n+start; i++ )
      dest.set(i, src.get(array[i-start]+start));

    return dest;
  }

  /**
   * @see #map(byte[])
   *
   * <b>Die Collection muss eine Instanz der implmentierenden Klassen
   * aus dem Pakte <tt>java.util</tt> sein.</b>
   */
  public <E> Collection<E> map(Collection<E> src) {
    return map( src, 0 );
  }

  /**
   * @see #map(byte[],int)
   *
   * <b>Die Collection muss eine Instanz der implmentierenden Klassen
   * aus dem Pakte <tt>java.util</tt> sein.</b>
   */
  public <E> Collection<E> map(Collection<E> src,int start) {
    Vector<E> tmp = map( new Vector<E>(src), start );
    Collection<E> dest;

    // FIXME: This depends on the types of the Collection and does different
    // things for different collections.
    if ( src instanceof LinkedList )
      dest = new LinkedList<E>();
    else if ( src instanceof ArrayList )
      dest = new ArrayList<E>();
    else if ( src instanceof Vector ) {
      return tmp;
    }
    else if ( src instanceof HashSet )
      dest = new HashSet<E>();
    else if ( src instanceof TreeSet )
      dest = new TreeSet<E>();
    else
      throw new IllegalArgumentException();

    for ( int i=0; i<tmp.size(); i++ )
      dest.add( tmp.get(i) );

    return dest;
  }

  public static Permutation getPermutation(int[] a,int[] b) {
    return getPermutation( createArray(a), createArray(b) );
  }

  public static Permutation getPermutation(char[] a,char[] b) {
    return getPermutation( createArray(a), createArray(b) );
  }

  public static Permutation getPermutation(byte[] a,byte[] b) {
    return getPermutation( createArray(a), createArray(b) );
  }

  public static Permutation getPermutation(short[] a,short[] b) {
    return getPermutation( createArray(a), createArray(b) );
  }

  public static Permutation getPermutation(long[] a,long[] b) {
    return getPermutation( createArray(a), createArray(b) );
  }

  public static Permutation getPermutation(float[] a,float[] b) {
    return getPermutation( createArray(a), createArray(b) );
  }

  public static Permutation getPermutation(double[] a,double[] b) {
    return getPermutation( createArray(a), createArray(b) );
  }

  public static Permutation getPermutation(String a,String b) {
    return getPermutation( createArray(a.toCharArray()),
			   createArray(b.toCharArray()) );
  }

  public static Permutation getPermutation(Object[] a,Object[] b) {
    int n = a.length;
    if ( n != b.length )
      throw new IllegalArgumentException();

    int[] pa = new int[n];

    for ( int i=0; i<n; i++ ) {
      pa[i] = -1;
      Object o1 = a[i];
      for ( int j=0; j<n; j++ ) {
	Object o2 = b[j];
	boolean ok = false;
	if ( o1.equals(o2) ) {
	  ok = true;
	  for ( int k=0; k<i; k++ )
	    if ( pa[k] == j ) {
	      ok = false;
	      break;
	    }
	}
	if ( ok ) {
	  pa[i] = j;
	  break;
	}
      }
    }

    try {
      return new Permutation( pa );
    }
    catch ( RuntimeException x ) {
      return null;
    }
  }

  public static Integer[] createArray(int[] a) {
    int n = a.length;
    Integer[] b = new Integer[n];
    for ( int i=0; i<n; i++ )
      b[i] = new Integer( a[i] );
    return b;
  }

  public static Character[] createArray(char[] a) {
    int n = a.length;
    Character[] b = new Character[n];
    for ( int i=0; i<n; i++ )
      b[i] = new Character( a[i] );
    return b;
  }

  public static Byte[] createArray(byte[] a) {
    int n = a.length;
    Byte[] b = new Byte[n];
    for ( int i=0; i<n; i++ )
      b[i] = new Byte( a[i] );
    return b;
  }

  public static Short[] createArray(short[] a) {
    int n = a.length;
    Short[] b = new Short[n];
    for ( int i=0; i<n; i++ )
      b[i] = new Short( a[i] );
    return b;
  }

  public static Long[] createArray(long[] a) {
    int n = a.length;
    Long[] b = new Long[n];
    for ( int i=0; i<n; i++ )
      b[i] = new Long( a[i] );
    return b;
  }

  public static Float[] createArray(float[] a) {
    int n = a.length;
    Float[] b = new Float[n];
    for ( int i=0; i<n; i++ )
      b[i] = new Float( a[i] );
    return b;
  }

  public static Double[] createArray(double[] a) {
    int n = a.length;
    Double[] b = new Double[n];
    for ( int i=0; i<n; i++ )
      b[i] = new Double( a[i] );
    return b;
  }

  /**
   * Erzeugt ein Mapping der Zahlen <tt>0</tt> bis <tt>n-1</tt>
   * auf die durch <tt>array</tt> gegebene Permutation.
   */
  protected MapEntry[] createMapping() {
    int n = array.length;
    MapEntry[] me = new MapEntry[n];

    for ( int i=0; i<n; i++ )
      me[i] = new MapEntry( i, array[i] );

    return me;
  }

  /**
   * Erzeugt Permutation aus dem gegebenen Mapping.
   */
  protected Permutation createPermutation(MapEntry[] me) {
    Permutation p = new Permutation();
    int n = me.length;
    p.array = new int[n];

    for ( int i=0; i<n; i++ )
      p.array[i] = me[i].from;

    return p;
  }

  /**
   * Diese Struktur dient zur Bildung und Sortierung von Mappings.
   * Durch simple Sortierung von Mappings kann z.B. die Inverse einer
   * Permutation berechnet werden.
   */
  protected static class MapEntry implements Comparable<MapEntry>
  {
    int from;
    int to;

    public MapEntry(int from,int to) {
      this.from = from;
      this.to = to;
    }

    public int compareTo(MapEntry o) {
      return to - o.to;
    }
  }
}
