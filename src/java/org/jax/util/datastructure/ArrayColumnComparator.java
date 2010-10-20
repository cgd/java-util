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
 * A comparator to use on arrays of values. Arrays are compared only on
 * a single column
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 * @param <E>
 *          the array element type
 */
public class ArrayColumnComparator<E> implements Comparator<E[]>
{
    private final int[] columnIndices;
    
    private final Comparator<E> elementComparator;
    
    /**
     * Constructor to use when elements are mutually comparable
     * @param columnIndex
     *          the column index to compare
     */
    public ArrayColumnComparator(final int columnIndex)
    {
        this(columnIndex, null);
    }
    
    /**
     * Constructor
     * @param columnIndex
     *          the column index to compare
     * @param elementComparator
     *          the element comparator to use
     */
    public ArrayColumnComparator(final int columnIndex, final Comparator<E> elementComparator)
    {
        this(new int[] {columnIndex}, elementComparator);
    }
    
    /**
     * Constructor
     * @param columnIndices
     *          the column indices to compare
     */
    public ArrayColumnComparator(int[] columnIndices)
    {
        this(columnIndices, null);
    }
    
    /**
     * Constructor
     * @param columnIndices
     *          the column indices to compare
     * @param elementComparator
     *          the element comparator to use
     */
    public ArrayColumnComparator(int[] columnIndices, Comparator<E> elementComparator)
    {
        this.columnIndices = columnIndices;
        this.elementComparator = elementComparator;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public int compare(E[] array1, E[] array2)
    {
        if(this.elementComparator == null)
        {
            // the arrays must be comparable since we don't
            // have a comparator
            for(int columnIndex: this.columnIndices)
            {
                Comparable<E> currComparable1 = (Comparable<E>)array1[columnIndex];
                int comparisonValue = currComparable1.compareTo(array2[columnIndex]);
                
                if(comparisonValue != 0)
                {
                    return comparisonValue;
                }
            }
            
            // they're equal
            return 0;
        }
        else
        {
            // use the comparator to compare elements
            for(int columnIndex: this.columnIndices)
            {
                int comparisonValue = this.elementComparator.compare(
                        array1[columnIndex],
                        array2[columnIndex]);
                
                if(comparisonValue != 0)
                {
                    return comparisonValue;
                }
            }
            
            // they're equal
            return 0;
        }
    }
}
