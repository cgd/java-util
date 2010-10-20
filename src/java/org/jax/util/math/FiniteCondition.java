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

import org.jax.util.Condition;

/**
 * Tests for a number being finite (not NaN and not Infinite).
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class FiniteCondition implements Condition<Number>
{
    /**
     * Test to see if the given number is finite
     * @param input
     *          the input we're testing
     * @return 
     *          true if the number is finite
     */
    public boolean test(Number input)
    {
        double currDatumAsDouble = input.doubleValue();
        return !Double.isNaN(currDatumAsDouble) && !Double.isInfinite(currDatumAsDouble);
    }
}
