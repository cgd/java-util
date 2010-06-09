/*
 * Copyright (c) 2009 The Jackson Laboratory
 * 
 * This software was developed by Gary Churchill's Lab at The Jackson
 * Laboratory (see http://research.jax.org/faculty/churchill).
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

import java.util.Comparator;

/**
 * For comparing numbers... the only reason that we need this is
 * because {@link java.lang.Number}s are not {@link java.lang.Comparable}
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class NumberComparator implements Comparator<Number>
{
    /**
     * holds our singleton instance
     */
    private static final NumberComparator instance = new NumberComparator();
    
    /**
     * Private constructor. use {@link #getInstance()} instead
     */
    private NumberComparator() {}
    
    /**
     * Gets the shared instance of this comparator
     * @return
     *          the singleton instance of this comparator
     */
    public static NumberComparator getInstance()
    {
        return NumberComparator.instance;
    }
    
    /**
     * Compare the given numbers. This comparator uses checks to see if the numbers
     * are {@link Comparable}. If so we use {@link Comparable#compareTo(Object)}, if
     * not we fall back on {@link Double#compare(double, double)}
     * @param number1
     *          the 1st number
     * @param number2 
     *          the 2nd number
     * @return
     *          see {@link Comparator} for the rules on this
     */
    @SuppressWarnings("unchecked")
    public int compare(Number number1, Number number2)
    {
        if(number1 instanceof Comparable && number2 instanceof Comparable)
        {
            Comparable comperable1 = (Comparable)number1;
            Comparable comperable2 = (Comparable)number2;
            return comperable1.compareTo(comperable2);
        }
        else
        {
            return Double.compare(number1.doubleValue(), number2.doubleValue());
        }
    }
}
