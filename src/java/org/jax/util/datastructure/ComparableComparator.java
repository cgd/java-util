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

package org.jax.util.datastructure;

import java.util.Comparator;

/**
 * A brain-dead implementation of {@link Comparator} that just delegates to
 * the {@link Comparator}s. Useful if a method requires you to provide a
 * comparator but you would rather rely on the {@link Comparator}s to do
 * the work.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 * @param <T>
 *          the type of comparator
 */
public class ComparableComparator<T extends Comparable<T>> implements Comparator<T>
{
    /**
     * {@inheritDoc}
     */
    public int compare(T o1, T o2)
    {
        return o1.compareTo(o2);
    }
}