// Copyright (c) 2009 Amazon.com, Inc.  All rights reserved.

package com.amazon.ion;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Tests illustrating semantics of built-in Java numeric types.
 */
public class JavaNumericsTest
    extends IonTestCase
{
    public void testDoubleEquality()
    {
        Double posZeroDotZero = new Double( 0.0d);
        Double negZeroDotZero = new Double(-0.0d);

        // Object Double can distinguish +/- zero
        assertFalse(negZeroDotZero.equals(posZeroDotZero));
        assertTrue(negZeroDotZero.compareTo(posZeroDotZero) < 0);

        // Primitive double cannot
        assertTrue(-0.0d == 0.0d);

        // But:
        assertTrue(Double.compare(-0.0d, 0.0d) < 0);

        // Neither type can distinguish by precision
        assertTrue(new Double(0.0d).equals(new Double(0.00000d)));
        assertTrue(0.0d == 0.00000d);
    }

    public void testDoubleNegativeZeroResult()
    {
        assertTrue(Double.compare(-1 * 0.0d, 0.0d) < 0);
        assertTrue(Double.compare(0.0d * -1, 0.0d) < 0);

        assertEquals(0, Double.compare(-1 * 0.0d, -0.0d));
    }

    public void testDoublePrinting()
    {
        assertEquals("-0.0", "" + -0.d);
        assertEquals("-0.0", "" + -0.0d);
        assertEquals("-0.0", "" + -0.00d);

        assertEquals("-0.0", "" + new Double(-0.d));
        assertEquals("-0.0", "" + new Double(-0.0d));
        assertEquals("-0.0", "" + new Double(-0.00d));
    }

    /**
     * BigInteger doesn't distinguish negative zero.
     */
    public void testBigIntegerSignum()
    {
        BigInteger bi = new BigInteger(-1, new byte[]{ 0 });
        assertEquals(0, bi.signum());
    }

    public void testBigDecimalScale()
    {
        final BigDecimal one_00 = new BigDecimal("1.00");

        assertEquals(1,   one_00.intValue());
        assertEquals(100, one_00.unscaledValue().intValue());
        assertEquals(2,   one_00.scale());
    }

    /**
     * Creating a BigDecimal from negative-zero double loses the negative.
     */
    public void testBigDecimalNegativeZero()
    {
        BigDecimal bd = new BigDecimal(-0.0d);
        assertEquals(0, bd.signum());
        assertEquals(0, Double.compare(0.0d, bd.doubleValue()));
        assertEquals(bd, bd.negate());
        assertEquals(0, bd.compareTo(new BigDecimal(0.0d)));
    }

    public void testBigDecimalParsing()
    {
        badBigDecimalFormat(" 0");
        badBigDecimalFormat("0 ");

        assertEquals(BigDecimal.ZERO, new BigDecimal("0"));
        assertEquals(BigDecimal.ZERO, new BigDecimal("+0"));
        assertEquals(BigDecimal.ZERO, new BigDecimal("-0"));

        assertEquals("0.1000000000000000055511151231257827021181583404541015625",
                     new BigDecimal(0.1).toString());
        assertEquals("0.1",
                     new BigDecimal("0.1").toString());
    }

    public void badBigDecimalFormat(String val)
    {
        try {
            new BigDecimal(val);
            fail("Expected exception");
        }
        catch (NumberFormatException e) { /* expected */ }
    }
}
