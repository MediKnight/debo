package de.bo.base.util;

import java.util.*;

/**
 * This class provides a general <tt>Comparator</tt>.
 * <p>
 * This comparator assumes, that the objects which will be compared with this class
 * consists of <tt>n</tt> significant elements which comparation order are specified
 * by a permutation. The directions (ascend, descend) are given by the sign of each
 * permutation number so the permutation number starts with <tt>1</tt> instead of
 * <tt>0</tt>.
 * <p>
 * <pre>
 * // object to be compared
 * class Person {
 *   public String name;
 *   public String firstName;
 *   public int age;
 *   public int sex;
 *   public String email;
 * }
 *
 * // the comparator (email not ordered)
 * class PersonComparator extends AbstractComparator {
 *   public PersonComparator(int[] perm) {
 *     super( perm );
 *   }
 *   public Comparable getComparable(int index,Object o) {
 *     Person p = (Person)o;
 *     switch ( index ) {
 *     case 0:
 *       return p.name;
 *     case 1:
 *       return p.firstName;
 *     case 2:
 *       return new Integer(p.age);
 *     case 3:
 *       return new Integer(p.sex);
 *     }
 *     return null;  // should never happen
 *   }
 * }
 *
 * // calls:
 *
 * // Natural order (e.g. ordering by name, first name, age and sex):
 * new PersonComparator( {1,2,3,4} );
 *
 * // Ordering by age (descend), name, first name:
 * new PersonComparator( {-3,1,2} );
 * </pre>
 *
 * @see Comparator
 */
public abstract class AbstractComparator<O> implements Comparator<O>
{
  /**
   * "normalized" permutation.
   * This array holds the unsigned numbers between 0 (inclusive) and
   * <tt>permutation.length</tt> (exclusive).
   */
  protected int[] permutation;

  /**
   * Order directions.
   * Ascend is mapped to <tt>true</tt>
   */
  protected boolean[] ascend;

  /**
   * The constructor only calls <tt>setComparationOrder</tt>.
   *
   * @see #setComparationOrder(int[])
   */
  protected AbstractComparator(int[] perm) {
    setComparationOrder( perm );
  }

  /**
   * This method sets the comparation order for each significant object.
   *
   * The given permutation must satisfies following rules:
   * <ul>
   * <li>the array must not be <tt>null</tt> and it length must be greater
   * than <tt>0</tt>.</li>
   * <li>all absolute values of the numbers in the array must be distinct.</li>
   * <li>the absolute values of the numbers in the array must be in range of
   * <tt>1</tt> to <tt>perm.length</tt> inclusive.</li>
   * <li>each number can be signed.</li>
   * </ul>
   *
   * @param perm the permutation array described above
   * @exception IllegalArgumentException if the array does not satisfy the rules above
   */
  public void setComparationOrder(int[] perm) {
    if ( perm == null || perm.length == 0 )
      throw new IllegalArgumentException();

    // permutation will change, so we use a copy
    permutation = (int[])perm.clone();

    int n = permutation.length;
    ascend = new boolean[n];
    for ( int i=0; i<n; i++ ) {
      int x = permutation[i];
      ascend[i] = x > 0;
      x = Math.abs( x );
      if ( x < 1 || x > n )
	throw new IllegalArgumentException();
      x--;
      for ( int j=0; j<i; j++ )
	if ( x == permutation[j] )
	  throw new IllegalArgumentException();
      permutation[i] = x;
    }
  }

  /**
   * This method is public by the interface specification.
   * Do not use it directly.
   */
  public int compare(O o1, O o2) {
    int n = permutation.length;
    for ( int i=0; i<n; i++ ) {
      int k = permutation[i];
      Comparable<O> c1 = getComparable( k, o1 );
      int c = c1.compareTo( o2 );
      if ( c != 0 )
	return ascend[i] ? c : -c;
    }
    return 0;
  }

  /**
   * This method returns a significant element of the object which should be
   * compared. The comparable element is given by an index from <tt>0</tt> to
   * the number of all significant elements of the object (exclusive).
   *
   * @param index the index of the comparable element
   * @param o the object which should be compared
   * @return the comparable element
   */
  public abstract Comparable<O> getComparable(int index, O o);
}
