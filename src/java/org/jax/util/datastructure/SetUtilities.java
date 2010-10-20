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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;

/**
 * Utility functions for sets
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SetUtilities
{
    /**
     * A bit set that's empty
     */
    private static final BitSet EMPTY_BIT_SET = new BitSet(0);
    
    /**
     * A bit set comparator that will compare the bit sets as if they are
     * integer values
     */
    public static final Comparator<BitSet> BIT_SET_COMPARATOR =
        new Comparator<BitSet>()
        {
            /**
             * {@inheritDoc}
             */
            public int compare(BitSet bitSet1, BitSet bitSet2)
            {
                int bitSet1Length = bitSet1.length();
                int bitSet2Length = bitSet2.length();
                
                int bitSetLengthDiff = bitSet1Length - bitSet2Length;
                if(bitSetLengthDiff != 0)
                {
                    return bitSetLengthDiff;
                }
                else
                {
                    // we can use "- 2" because we know that "- 1" is a
                    // 1 bit for both sets
                    for(int i = bitSet1Length - 2; i >= 0; i--)
                    {
                        int bit1 = bitSet1.get(i) ? 1 : 0;
                        int bit2 = bitSet2.get(i) ? 1 : 0;
                        
                        int bitDiff = bit1 - bit2;
                        if(bitDiff != 0)
                        {
                            return bitDiff;
                        }
                    }
                    
                    // they're equal
                    return 0;
                }
            }
        };
    
    /**
     * Private constructor. all of the functionality is in static methods
     */
    private SetUtilities()
    {
        // do nothing
    }
    
    /**
     * Convert a bit representation of a set to its index representation
     * @param bitSet
     *          the bit set representations
     * @return
     *          the index array representation
     */
    public static int[] getSetBitIndices(BitSet bitSet)
    {
        int[] setBitIndices = new int[bitSet.cardinality()];
        int currSetBitIndex = 0;
        int currBitIndex = 0;
        while(currSetBitIndex < setBitIndices.length)
        {
            if(bitSet.get(currBitIndex))
            {
                setBitIndices[currSetBitIndex] = currBitIndex;
                currSetBitIndex++;
            }
            
            currBitIndex++;
        }
        
        return setBitIndices;
    }
    
    /**
     * Create an index map for: subset->superset mapping
     * @param filter
     *          the filter that was used to create the subset. see
     *          {@link SequenceUtilities#testInputs(org.jax.util.Condition, java.util.List)}
     *          for an easy way to create this filter
     * @return
     *          the mapping
     */
    public static int[] createFromSubsetToSupersetIndexMap(boolean[] filter)
    {
        int filterTrueCount = SequenceUtilities.countTrues(filter);
        int[] fromSubsetIndexMap = new int[filter.length - filterTrueCount];
        
        int filteredIndex = 0;
        for(int i = 0; i < filter.length; i++)
        {
            if(!filter[i])
            {
                fromSubsetIndexMap[filteredIndex] = i;
                filteredIndex++;
            }
        }
        
        return fromSubsetIndexMap;
    }

    /**
     * Create an original->reordered mapping from a reordered->original
     * mapping
     * @param originalSize
     *          the size of the original data
     * @param toOriginalIndexMap
     *          the mapping of reordered indices (none of these
     *          should be >= originalSize)
     * @return
     *          the inverse mapping (-1 is used where there
     *          is no valid mapping)
     */
    public static int[] createFromOriginalToReorderedIndexMap(
            int originalSize,
            int[] toOriginalIndexMap)
    {
        int[] toReorderedIndexMap = new int[originalSize];
        
        // initialize everything to -1 (invalid)
        Arrays.fill(toReorderedIndexMap, -1);
        
        // set any valid values
        for(int reorderedIndex = 0; reorderedIndex < toOriginalIndexMap.length; reorderedIndex++)
        {
            toReorderedIndexMap[toOriginalIndexMap[reorderedIndex]] = reorderedIndex;
        }
        
        return toReorderedIndexMap;
    }

    /**
     * check to see if the 1st is a subset of the second. this function does
     * not test whether or not it is a "proper" subset relationship. note that
     * this function only works correctly if the input parameters are sorted
     * on their natural ordering
     * @param <T>
     *          the type of elements that we're testing the array for
     * @param potentialOrderedSubset
     *          the ordered array that we're testing as a subset
     * @param orderedSuperset
     *          the ordered array that we're testing as a superset
     * @return
     *          (assuming that the array elements are sorted according to their
     *          ordering) true iff the subset relationship holds
     */
    public static <T extends Comparable<T>> boolean isOrderedSubset(
            T[] potentialOrderedSubset,
            T[] orderedSuperset)
    {
        if(potentialOrderedSubset.length > orderedSuperset.length)
        {
            return false;
        }
        
        int supersetCursor = 0;
        int subsetCursor = 0;
        
        while(supersetCursor < orderedSuperset.length && subsetCursor < potentialOrderedSubset.length)
        {
            int currComparison =
                orderedSuperset[supersetCursor].compareTo(potentialOrderedSubset[subsetCursor]);
            
            if(supersetCursor >= 1)
            {
                assert orderedSuperset[supersetCursor].compareTo(
                       orderedSuperset[supersetCursor - 1]) >= 0;
            }
            
            if(subsetCursor >= 1)
            {
                assert potentialOrderedSubset[subsetCursor].compareTo(
                       orderedSuperset[subsetCursor - 1]) >= 0;
            }
            
            if(currComparison == 0)
            {
                supersetCursor++;
                subsetCursor++;
            }
            else if(currComparison < 0)
            {
                supersetCursor++;
            }
            else
            {
                return false;
            }
        }
        
        return subsetCursor == potentialOrderedSubset.length;
    }

    /**
     * Create an intersection of the given ordered arrays. If the arrays
     * dont have the same relative ordering of their elements,
     * the function will usually return an incorrect result
     * (ie. the return value will not represent an intersection of the two
     * given arrays). Also, if the given arrays are not sets (ie. they contain
     * repeat elements) then the return value may not be a set.
     * @param <T>
     *          the type of elements that we're creating an intersection for
     * @param orderedSet1
     *          the 1st array that we're intersecting
     * @param orderedSet2
     *          the 2nd array that we're intersecting
     * @return
     *          the intersection of the two given arrays
     */
    public static <T extends Comparable<T>> T[] createOrderedIntersection(
            T[] orderedSet1,
            T[] orderedSet2)
    {
        ArrayList<T> intersectionList =
            new ArrayList<T>(Math.min(orderedSet1.length, orderedSet2.length));
        
        int cursor1 = 0;
        int cursor2 = 0;
        
        while(cursor1 < orderedSet1.length && cursor2 < orderedSet2.length)
        {
            int currComparison =
                orderedSet1[cursor1].compareTo(orderedSet2[cursor2]);
            if(currComparison == 0)
            {
                // they're equal, so add to the intersection and move the
                // cursors together
                intersectionList.add(orderedSet1[cursor1]);
                cursor1++;
                cursor2++;
            }
            else if(currComparison < 0)
            {
                // cursor 1 is less, increment it
                cursor1++;
            }
            else
            {
                // cursor 2 is less, increment it
                cursor2++;
            }
        }
        
        T[] intersectionArray = SequenceUtilities.<T>instantiateGenericArray(
                orderedSet1.getClass().getComponentType(),
                intersectionList.size());
        
        intersectionArray = intersectionList.toArray(
                intersectionArray);
        
        return intersectionArray;
    }

    /**
     * Convert the bit set to its string representation
     * @param strainBitSet
     *          the bit set
     * @return
     *          the string representation (ones and zeros)
     */
    public static String bitSetToBinaryString(BitSet strainBitSet)
    {
        StringBuffer sb = new StringBuffer(strainBitSet.length());
        
        for(int i = 0; i < strainBitSet.length(); i++)
        {
            sb.append(strainBitSet.get(i) ? '1' : '0');
        }
        
        return sb.toString();
    }
    
    /**
     * Convert the string representation to a bit set
     * @param stringRepresentation
     *          the string representation
     * @return
     *          the bit set
     */
    public static BitSet binaryStringToBitSet(String stringRepresentation)
    {
        BitSet bitSet = new BitSet(stringRepresentation.length());
        for(int i = 0; i < stringRepresentation.length(); i++)
        {
            if(stringRepresentation.charAt(i) == '1')
            {
                bitSet.set(i);
            }
            else if(stringRepresentation.charAt(i) != '0')
            {
                throw new IllegalArgumentException(
                        "bad format: " + stringRepresentation);
            }
        }
        return bitSet;
    }
    
    /**
     * Test if the given set is empty
     * @param setToTest
     *          the set we're testing to see if it's empty
     * @return
     *          true iff its empty
     * @throws NullPointerException
     *          if the given set to test is null
     */
    public static boolean isEmptySet(BitSet setToTest) throws NullPointerException
    {
        return setToTest.equals(EMPTY_BIT_SET);
    }
    
    /**
     * Determine if the 1st argument is a subset of the 2nd. Since this
     * isn't a strict-subset test two equal sets will return true
     * @param subsetToTest
     *          the candidate subset
     * @param supersetToTest
     *          the candidate superset
     * @return
     *          true iff the subset relationship holds
     */
    public static boolean isSubset(
            BitSet subsetToTest,
            BitSet supersetToTest)
    {
        BitSet intersection = (BitSet)supersetToTest.clone();
        intersection.and(subsetToTest);
        
        return intersection.equals(subsetToTest);
    }
}
