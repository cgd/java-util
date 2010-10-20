/*
 * Copyright (c) 2010 The Jackson Laboratory
 * 
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jax.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utilities for dealing with precise values. These functions are
 * pretty thin wrappers around the functionality in
 * {@link java.math.BigDecimal}.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public final class NumericUtilities
{
    // TODO unit test me
    /**
     * Private constructor to avoid an instance being created
     */
    private NumericUtilities()
    {
    }
    
    /**
     * Parse the given string as an integer as in
     * {@link Integer#valueOf(int)} without throwing a
     * {@link NumberFormatException}
     * @param stringToParse
     *          the string
     * @return
     *          the integer or null if the string can't be parsed as an integer
     */
    public static Integer safelyParseInteger(String stringToParse)
    {
        try
        {
            return Integer.valueOf(stringToParse);
        }
        catch(NumberFormatException ex)
        {
            return null;
        }
    }
    
    /**
     * This method just delegates to
     * {@link #getMostSignificantDecimalPosition(BigDecimal)}
     * @param value
     *          the value that we're testing for significance
     * @return
     *          the position of the most significant decimal
     */
    public static int getMostSignificantDecimalPosition(double value)
    {
        return NumericUtilities.getMostSignificantDecimalPosition(
                BigDecimal.valueOf(value));
    }
    
    /**
     * Get the position of the most significant decimal. Eg.
     * <pre>
     *      3.14   =>  0
     *      314    =>  2
     *      0.0314 => -2
     * </pre>
     * @param value
     *          the value that we're testing
     * @return
     *          the position of the most significant decimal
     */
    public static int getMostSignificantDecimalPosition(BigDecimal value)
    {
        return value.precision() - value.scale() - 1;
    }
    
    /**
     * Round using {@link RoundingMode#HALF_EVEN} to minimize bias.
     * @param value
     *          the value to round
     * @param decimalPosition
     *          the decimal position to round to
     * @return
     *          the result of rounding
     */
    public static double roundToDecimalPositionDouble(
            double value,
            int decimalPosition)
    {
        return NumericUtilities.roundToDecimalPositionDouble(
                value,
                decimalPosition,
                RoundingMode.HALF_EVEN);
    }
    
    /**
     * This method just delegates to
     * {@link #roundToDecimalPositionBigDecimal(double, int, RoundingMode)}.
     * @param value
     *          the value to round
     * @param decimalPosition
     *          the decimal position to round to
     * @param roundingMode
     *          the rounding mode to use
     * @return
     *          the result of rounding
     */
    public static double roundToDecimalPositionDouble(
            double value,
            int decimalPosition,
            RoundingMode roundingMode)
    {
        if(Double.isInfinite(value) || Double.isNaN(value))
        {
            return value;
        }
        else
        {
            BigDecimal bigResult =
                NumericUtilities.roundToDecimalPositionBigDecimal(value, decimalPosition, roundingMode);
            return bigResult.doubleValue();
        }
    }
    
    /**
     * Round using {@link RoundingMode#HALF_EVEN} to minimize bias.
     * @param value
     *          the value to round
     * @param decimalPosition
     *          the decimal position to round to
     * @return
     *          the result of rounding
     */
    public static BigDecimal roundToDecimalPositionBigDecimal(
            double value,
            int decimalPosition)
    {
        return NumericUtilities.roundToDecimalPositionBigDecimal(
                value,
                decimalPosition,
                RoundingMode.HALF_EVEN);
    }
    
    /**
     * This method just delegates to
     * {@link #roundToDecimalPositionBigDecimal(BigDecimal, int, RoundingMode)}.
     * @param value
     *          the value to round
     * @param decimalPosition
     *          the decimal position to round to
     * @param roundingMode
     *          the rounding mode to use
     * @return
     *          the result of rounding
     */
    public static BigDecimal roundToDecimalPositionBigDecimal(
            double value,
            int decimalPosition,
            RoundingMode roundingMode)
    {
        return NumericUtilities.roundToDecimalPositionBigDecimal(
                BigDecimal.valueOf(value),
                decimalPosition,
                roundingMode);
    }
    
    /**
     * Round using {@link RoundingMode#HALF_EVEN} to minimize bias.
     * @param value
     *          the value that we're rounding
     * @param decimalPosition
     *          the decimal position that we're rounding to
     * @return
     *          the result of rounding
     */
    public static BigDecimal roundToDecimalPositionBigDecimal(
            BigDecimal value,
            int decimalPosition)
    {
        return NumericUtilities.roundToDecimalPositionBigDecimal(
                value,
                decimalPosition,
                RoundingMode.HALF_EVEN);
    }
    
    /**
     * Rounds the given value to the given decimal position, getting rid of any
     * decimals less significant than the given position. Eg., PI rounded to
     * the -2 decimal position is 3.14 (depending on rounding). Another way to
     * think of decimal position is that it's the opposite of scale as defined
     * by {@link BigDecimal}.
     * @param value
     *          the value that we're rounding
     * @param decimalPosition
     *          the decimal position that we're rounding to
     * @param roundingMode
     *          the rounding mode to use
     * @return
     *          the result of rounding
     */
    public static BigDecimal roundToDecimalPositionBigDecimal(
            BigDecimal value,
            int decimalPosition,
            RoundingMode roundingMode)
    {
        return value.setScale(-decimalPosition, roundingMode);
    }
    
    /**
     * Round using {@link RoundingMode#HALF_EVEN}.
     * @param value
     *          the value to round
     * @param siginificantDigits
     *          the precision to round to
     * @return
     *          the rounded value
     */
    public static double roundToSignificantDigitsDouble(double value, int siginificantDigits)
    {
        return NumericUtilities.roundToSignificantDigitsDouble(
                value,
                siginificantDigits,
                RoundingMode.HALF_EVEN);
    }
    
    /**
     * this method just delegates to
     * {@link #roundToSignificantDigitsBigDecimal(double, int, RoundingMode)}
     * @param value
     *          the value to round
     * @param siginificantDigits
     *          the precision to round to
     * @param roundingMode
     *          the rounding mode to use
     * @return
     *          the rounded value
     */
    public static double roundToSignificantDigitsDouble(
            double value,
            int siginificantDigits,
            RoundingMode roundingMode)
    {
        if(Double.isInfinite(value) || Double.isNaN(value))
        {
            return value;
        }
        else
        {
            BigDecimal bigResult = NumericUtilities.roundToSignificantDigitsBigDecimal(
                    value,
                    siginificantDigits,
                    roundingMode);
            return bigResult.doubleValue();
        }
    }
    
    /**
     * Round using {@link RoundingMode#HALF_EVEN}.
     * @param value
     *          the value to round
     * @param siginificantDigits
     *          the precision to round to
     * @return
     *          the rounded value
     */
    public static BigDecimal roundToSignificantDigitsBigDecimal(
            double value,
            int siginificantDigits)
    {
        return NumericUtilities.roundToSignificantDigitsBigDecimal(
                value,
                siginificantDigits,
                RoundingMode.HALF_EVEN);
    }
    
    /**
     * this method just delegates to
     * {@link #roundToSignificantDigitsBigDecimal(BigDecimal, int, RoundingMode)}
     * @param value
     *          the value to round
     * @param siginificantDigits
     *          the precision to round to
     * @param roundingMode
     *          the rounding mode to use
     * @return
     *          the rounded value
     */
    public static BigDecimal roundToSignificantDigitsBigDecimal(
            double value,
            int siginificantDigits,
            RoundingMode roundingMode)
    {
        return NumericUtilities.roundToSignificantDigitsBigDecimal(
                BigDecimal.valueOf(value),
                siginificantDigits,
                roundingMode);
    }
    
    /**
     * Round using {@link RoundingMode#HALF_EVEN}.
     * @param value
     *          the value to round
     * @param siginificantDigits
     *          the precision to round to
     * @return
     *          the rounded value
     */
    public static BigDecimal roundToSignificantDigitsBigDecimal(
            BigDecimal value,
            int siginificantDigits)
    {
        return NumericUtilities.roundToSignificantDigitsBigDecimal(
                value,
                siginificantDigits,
                RoundingMode.HALF_EVEN);
    }
    
    /**
     * round the given value to the given precision (IE number of
     * significant digits). for a better description of precision
     * see {@link BigDecimal}
     * @param value
     *          the number that we're rounding
     * @param siginificantDigits
     *          the precision to round to (how many significant
     *          digits to keep)
     * @param roundingMode
     *          the rounding mode to use
     * @return
     *          the rounded result
     */
    public static BigDecimal roundToSignificantDigitsBigDecimal(
            BigDecimal value,
            int siginificantDigits,
            RoundingMode roundingMode)
    {
        // first lose any trailing 0's
        BigDecimal valueToRound = value.stripTrailingZeros();
        
        // see if we're already OK
        int currPrecision = valueToRound.precision();
        if(currPrecision > siginificantDigits)
        {
            // the difference will always be negative
            int precisionDifference = siginificantDigits - currPrecision;
            
            // do the rounding
            valueToRound = valueToRound.setScale(
                    valueToRound.scale() + precisionDifference,
                    roundingMode);
            return valueToRound.stripTrailingZeros();
        }
        else
        {
            // we're already ok
            return value;
        }
    }
}
