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

package org.jax.util;


/**
 * Inverts the condition that we passed into the constructor. (false becomes
 * true and true becomes false)
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 * @param <I>
 *          the type of input we accept
 */
public class InverseCondition<I> implements Condition<I>
{
    /**
     * the condition that we invert
     */
    private final Condition<I> conditionToInvert;

    /**
     * Constructor
     * @param conditionToInvert
     *          the condition that we're inverting
     */
    public InverseCondition(Condition<I> conditionToInvert)
    {
        this.conditionToInvert = conditionToInvert;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean test(I input)
    {
        return !this.conditionToInvert.test(input);
    }

    /**
     * getter for the condition that we're inverting
     * @return
     *          the condition that we're inverting
     */
    public Condition<I> getConditionToInvert()
    {
        return this.conditionToInvert;
    }

}
