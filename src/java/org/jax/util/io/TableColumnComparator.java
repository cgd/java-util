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

package org.jax.util.io;

import java.util.Comparator;

/**
 * A class that does a cascading sort on table columns
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class TableColumnComparator implements Comparator<String[]>
{
    private final int[] columnIndicesToCompare;

    /**
     * Constructor
     * @param columnIndicesToCompare
     *          the indices that should be used for the comparison
     */
    public TableColumnComparator(int[] columnIndicesToCompare)
    {
        this.columnIndicesToCompare = columnIndicesToCompare;
    }

    /**
     * {@inheritDoc}
     */
    public int compare(String[] row1, String[] row2)
    {
        for(int i = 0; i < this.columnIndicesToCompare.length; i++)
        {
            final int index = this.columnIndicesToCompare[i];
            final int comparison = row1[index].compareTo(row2[index]);
            if(comparison != 0)
            {
                return comparison;
            }
        }
        
        // they're equal
        return 0;
    }
}
