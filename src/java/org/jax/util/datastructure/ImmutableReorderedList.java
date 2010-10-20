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

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

import org.jax.util.Condition;


/**
 * for reordering an immutable {@link List} (if the underlying list is
 * resized, then this class breaks). {@link ImmutableReorderedList} will
 * work with any original list but will give poor performance for anything
 * that isn't {@link RandomAccess}. Note that both data repeats and missing
 * data are allowed in this implementation. Also note that this implementation
 * supports conditional subsetting through its constructors.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 * @param <E>
 *          the contents type for this list
 */
public class ImmutableReorderedList<E> extends AbstractList<E> implements RandomAccess
{
    /**
     * holds the reordered mapping
     */
    private final int[] toReorderedMapping;
    
    /**
     * holds the original mapping
     */
    private final int[] toOriginalMapping;

    /**
     * @see #getOriginalList()
     */
    private final List<E> originalList;
    
    /**
     * Constructor for subsetting data
     * @param superset
     *          the superset
     * @param removeCondition
     *          our subsetting test condition (if true, remove data)
     */
    public ImmutableReorderedList(List<E> superset, Condition<E> removeCondition)
    {
        this(superset, SequenceUtilities.testInputs(removeCondition, superset));
    }
    
    /**
     * Constructor for subsetting data. The filter and superset
     * must be the same size
     * @param superset
     *          the superset
     * @param filter
     *          the filter we use to subset data (true means we
     *          filter that item out).
     */
    public ImmutableReorderedList(List<E> superset, boolean[] filter)
    {
        this(superset, SetUtilities.createFromSubsetToSupersetIndexMap(filter));
    }
    
    /**
     * Constructor.
     * @param originalList
     *          the original list to reorder
     * @param toOriginalOrderMapping
     *          the new ordering. see
     *          {@link SetUtilities#createFromOriginalToReorderedIndexMap(int, int[])}
     *          for more info about how this works
     */
    public ImmutableReorderedList(List<E> originalList, int[] toOriginalOrderMapping)
    {
        this.originalList = originalList;
        this.toOriginalMapping = toOriginalOrderMapping;
        this.toReorderedMapping = SetUtilities.createFromOriginalToReorderedIndexMap(
                originalList.size(),
                toOriginalOrderMapping);
    }
    
    /**
     * get the original list
     * @return the original
     */
    public List<E> getOriginalList()
    {
        return this.originalList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E get(int index)
    {
        return this.originalList.get(this.toOriginalIndex(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return this.toOriginalMapping.length;
    }
    
    /**
     * convert a reordered index into an original index
     * @param reorderedIndex
     *          the given reordered index
     * @return
     *          the original index
     */
    public int toOriginalIndex(int reorderedIndex)
    {
        return this.toOriginalMapping[reorderedIndex];
    }
    
    /**
     * inverse of {@link #toOriginalIndex(int)}
     * @param originalIndex
     *          the original index
     * @return
     *          the reordered index
     */
    public int toReorderedIndex(int originalIndex)
    {
        return this.toReorderedMapping[originalIndex];
    }

}
