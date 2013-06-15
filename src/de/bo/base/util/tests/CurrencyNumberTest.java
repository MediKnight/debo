package de.bo.base.util.tests;

import de.bo.base.util.CurrencyNumber;
import junit.framework.TestCase;

/**
 * @todo Some unit tests are not implemented.
 * 
 * @author Jan Bernhardt <jb@baltic-online.de>
 */
public class CurrencyNumberTest extends TestCase {

    public CurrencyNumberTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testIntValue() {
    }

    public void testLongValue() {
    }

    public void testFloatValue() {
    }

    public void testDoubleValue() {
    }

    public void testByteValue() {
    }

    public void testShortValue() {
    }

    /*
     * Test for void CurrencyNumber()
     */
    public void testCurrencyNumber() {
        CurrencyNumber cn = new CurrencyNumber();
        assertEquals(0L, cn.longValue());
        assertEquals(CurrencyNumber.EUR, cn.getCurrency());
    }

    /*
     * Test for void CurrencyNumber(int)
     */
    public void testCurrencyNumberI() {
        CurrencyNumber cn = new CurrencyNumber(CurrencyNumber.DEM);
        assertEquals(CurrencyNumber.DEM, cn.getCurrency());
    }

    /*
     * Test for void CurrencyNumber(long)
     */
    public void testCurrencyNumberJ() {
        CurrencyNumber cn = new CurrencyNumber(10000L);
        assertEquals(CurrencyNumber.EUR, cn.getCurrency());
        assertEquals(10000L, cn.longValue());
        assertEquals(1.0d, cn.doubleValue(), 0.0d);
    }

    /*
     * Test for void CurrencyNumber(long, int)
     */
    public void testCurrencyNumberJI() {
        CurrencyNumber cn = new CurrencyNumber(10000L, CurrencyNumber.DEM);
        assertEquals(CurrencyNumber.DEM, cn.getCurrency());
        assertEquals(10000L, cn.longValue());
        assertEquals(1.0d, cn.doubleValue(), 0.0d);
    }

    /*
     * Test for void CurrencyNumber(double)
     */
    public void testCurrencyNumberD() {
        CurrencyNumber cn = new CurrencyNumber(100.0d);
        assertEquals(CurrencyNumber.EUR, cn.getCurrency());
        assertEquals(1000000L, cn.longValue());
        assertEquals(100.0d, cn.doubleValue(), 0.0d);
    }

    /*
     * Test for void CurrencyNumber(double, int)
     */
    public void testCurrencyNumberDI() {
        CurrencyNumber cn = new CurrencyNumber(100.0d, CurrencyNumber.DEM);
        assertEquals(CurrencyNumber.DEM, cn.getCurrency());
        assertEquals(1000000L, cn.longValue());
        assertEquals(100.0d, cn.doubleValue(), 0.0d);
    }

    /*
     * Test for void CurrencyNumber(float)
     */
    public void testCurrencyNumberF() {
        CurrencyNumber cn = new CurrencyNumber(100.0f);
        assertEquals(CurrencyNumber.EUR, cn.getCurrency());
        assertEquals(1000000L, cn.longValue());
        assertEquals(100.0d, cn.doubleValue(), 0.0d);
    }

    /*
     * Test for void CurrencyNumber(float, int)
     */
    public void testCurrencyNumberFI() {
        CurrencyNumber cn = new CurrencyNumber(100.0f, CurrencyNumber.DEM);
        assertEquals(CurrencyNumber.DEM, cn.getCurrency());
        assertEquals(1000000L, cn.longValue());
        assertEquals(100.0d, cn.doubleValue(), 0.0d);
    }

    public void testGetCurrency() {
    }

    /*
     * Test for Object clone()
     */
    public void testClone() {
        CurrencyNumber cn1 = new CurrencyNumber(100.0d);
        CurrencyNumber cn2 = (CurrencyNumber) cn1.clone();
        
        assertNotSame(cn1, cn2);
        assertEquals(cn1, cn2);
        assertEquals(cn1.longValue(), cn2.longValue());
        assertEquals(cn1.doubleValue(), cn2.doubleValue(), 0.0d);
        assertEquals(cn1.getCurrency(), cn2.getCurrency());
    }

    public void testCreateClone() {
        CurrencyNumber cn1 = new CurrencyNumber(100.0d);
        CurrencyNumber cn2 = cn1.createClone();
        
        assertNotSame(cn1, cn2);
        assertEquals(cn1, cn2);
        assertEquals(cn1.longValue(), cn2.longValue());
        assertEquals(cn1.doubleValue(), cn2.doubleValue(), 0.0d);
        assertEquals(cn1.getCurrency(), cn2.getCurrency());
    }

    public void testRound() {
        CurrencyNumber cn1 = new CurrencyNumber(100.466d);
        CurrencyNumber cn2 = new CurrencyNumber(100.464d);
        CurrencyNumber cn3 = new CurrencyNumber(100.33336d);
        CurrencyNumber cn4 = new CurrencyNumber(100.33333d);
        
        assertEquals(100.47d, cn1.round(2).doubleValue(), 0.001d);
        assertEquals(100.46d, cn2.round(2).doubleValue(), 0.001d);
        assertEquals(100.3334d, cn3.round(4).doubleValue(), 0.001d);
        assertEquals(100.3333d, cn4.round(4).doubleValue(), 0.001d);
    }

    public void testToEuro() {
    }

    public void testNorm() {
    }

    public void testToCurrency() {
    }

    public void testNeg() {
    }

    public void testNegate() {
    }

    public void testAdd() {
    }

    public void testSub() {
    }

    /*
     * Test for CurrencyNumber mul(int)
     */
    public void testMulI() {
    }

    /*
     * Test for CurrencyNumber mul(long)
     */
    public void testMulJ() {
    }

    /*
     * Test for CurrencyNumber mul(double)
     */
    public void testMulD() {
    }

    /*
     * Test for CurrencyNumber mul(float)
     */
    public void testMulF() {
    }

    /*
     * Test for CurrencyNumber div(int)
     */
    public void testDivI() {
    }

    /*
     * Test for CurrencyNumber div(long)
     */
    public void testDivJ() {
    }

    /*
     * Test for CurrencyNumber div(double)
     */
    public void testDivD() {
    }

    /*
     * Test for CurrencyNumber div(float)
     */
    public void testDivF() {
    }

    /*
     * Test for double div(CurrencyNumber)
     */
    public void testDivCurrencyNumber() {
    }

    public void testCompareTo() {
    }

    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
    }

    /*
     * Test for String toString()
     */
    public void testToString() {
    }

    /*
     * Test for String toString(Locale)
     */
    public void testToStringLocale() {
    }

    /*
     * Test for String toString(Locale, boolean)
     */
    public void testToStringLocaleZ() {
    }

    /*
     * Test for String getCurrencySymbol()
     */
    public void testGetCurrencySymbol() {
    }

    /*
     * Test for String getCurrencySymbol(int)
     */
    public void testGetCurrencySymbolI() {
    }

    /*
     * Test for CurrencyNumber parse(String)
     */
    public void testParseString() {
    }

    /*
     * Test for CurrencyNumber parse(String, int)
     */
    public void testParseStringI() {
    }

    public void testParseCurrencySymbol() {
    }

    public void testGetCurrencies() {
    }

}
