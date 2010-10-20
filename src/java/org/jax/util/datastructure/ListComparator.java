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
import java.util.Iterator;
import java.util.List;

/**
 * Compares a list of objects. 
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 * @param <C> the contents type for the lists we compare
 */
@SuppressWarnings("unchecked")
public class ListComparator<C> implements Comparator<List<C>>
{
    private final Comparator<C> contentsComparator;
    
    /**
     * Constructor
     */
    public ListComparator()
    {
        this(null);
    }
    
    /**
     * Constructor
     * @param contentsComparator
     *          the comparator for the list contents
     */
    public ListComparator(Comparator<C> contentsComparator)
    {
        this.contentsComparator = contentsComparator;
    }
    
    /**
     * {@inheritDoc}
     */
    public int compare(List<C> listOne, List<C> listTwo)
    {
        int sizeDifference = listOne.size() - listTwo.size();
        if(sizeDifference != 0)
        {
            return sizeDifference;
        }
        else
        {
            Iterator<C> iter1 = listOne.iterator();
            Iterator<C> iter2 = listTwo.iterator();
            
            // if we have on contents comparator, we should use it. otherwise
            // we need to hope that the contents are mutually comparable
            if(this.contentsComparator != null)
            {
                while(iter1.hasNext())
                {
                    int currCompare = this.contentsComparator.compare(
                            iter1.next(),
                            iter2.next());
                    if(currCompare != 0)
                    {
                        return currCompare;
                    }
                }
            }
            else
            {
                while(iter1.hasNext())
                {
                    int currCompare =
                        ((Comparable)iter1.next()).compareTo(iter2.next());
                    if(currCompare != 0)
                    {
                        return currCompare;
                    }
                }
            }
            
            // everything is the same
            return 0;
        }
    }
}
