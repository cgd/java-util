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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.jax.util.Condition;

/**
 * Utility functions for operating on sequences of data.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SequenceUtilities
{
    /**
     * A comparator that differentiates two short arrays
     */
    public static final Comparator<short[]> SHORT_ARRAY_COMPARATOR =
        new Comparator<short[]>()
        {
            /**
             * {@inheritDoc}
             */
            public int compare(short[] array1, short[] array2)
            {
                int lengthDiff = array1.length - array2.length;
                if(lengthDiff != 0)
                {
                    return lengthDiff;
                }
                else
                {
                    for(int i = 0; i < array1.length; i++)
                    {
                        int elementDiff = array1[i] - array2[i];
                        if(elementDiff != 0)
                        {
                            return elementDiff;
                        }
                    }
                    
                    return 0;
                }
            }
        };
    
    /**
     * A comparator for arrays
     * @param <C>
     *          the type of array to compare against
     */
    public class ObjectArrayComparator<C> implements Comparator<C[]>
    {
        private Comparator<C> contentsComparator;

        /**
         * Constructor
         * @param contentsComparator
         *          the element comparator to use
         */
        public ObjectArrayComparator(Comparator<C> contentsComparator)
        {
            this.contentsComparator = contentsComparator;
        }

        /**
         * {@inheritDoc}
         */
        public int compare(C[] array1, C[] array2)
        {
            int minLen = Math.min(array1.length, array2.length);
            for(int i = 0; i < minLen; i++)
            {
                int compResult = this.contentsComparator.compare(array1[i], array2[i]);
                if(compResult != 0)
                {
                    return compResult;
                }
            }
            
            return array1.length - array2.length;
        }
    }
    
    /**
     * private constructor since these are all static functions
     */
    private SequenceUtilities()
    {
        // do nothing
    }
    
    /**
     * Gets the last element of an array
     * @param <T> the element type for the array
     * @param array the array
     * @return the element or null if the array is empty
     */
    public static <T> T lastArrayElement(T[] array)
    {
        if(array.length >= 1)
        {
            return array[array.length];
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Like {@link #getMaxDatum(Comparator, List)} except we don't need a
     * comparator since the data is {@link Comparable}
     * @param <C>
     *          the type of the data
     * @param data
     *          the data
     * @return
     *          see {@link #getMaxDatum(Comparator, List)}
     */
    public static <C extends Comparable<C>> C getMaxDatum(List<C> data)
    {
        ComparableComparator<C> comparableComparator =
            new ComparableComparator<C>();
        
        return SequenceUtilities.getMaxDatum(
                comparableComparator,
                data);
    }
    
    /**
     * Get the maximum datum from the given data
     * @param <C>
     *          the type of data
     * @param comparator
     *          the comparator to use
     * @param data
     *          the data
     * @return
     *          the maximum value or null if data.length == 0
     */
    public static <C> C getMaxDatum(Comparator<C> comparator, List<C> data)
    {
        Iterator<C> dataIter = data.iterator();
        C maxDatum = null;
        while(dataIter.hasNext())
        {
            C nextDatum = dataIter.next();
            if(nextDatum != null &&
               (maxDatum == null || comparator.compare(nextDatum, maxDatum) > 0))
            {
                maxDatum = nextDatum;
            }
        }
        
        return maxDatum;
    }
    
    /**
     * Like {@link #getMinDatum(Comparator, List)} except we don't need a
     * comparator since the data is {@link Comparable}
     * @param <C>
     *          the type of the data
     * @param data
     *          the data
     * @return
     *          see {@link #getMinDatum(Comparator, List)}
     */
    public static <C extends Comparable<C>> C getMinDatum(List<C> data)
    {
        ComparableComparator<C> comparableComparator =
            new ComparableComparator<C>();
        
        return SequenceUtilities.getMinDatum(
                comparableComparator,
                data);
    }
    
    /**
     * Get the minimum datum from the given data
     * @param <C>
     *          the type of data
     * @param comparator
     *          the comparator to use
     * @param data
     *          the data
     * @return
     *          the minimum value or null if data.length == 0
     */
    public static <C> C getMinDatum(Comparator<C> comparator, List<C> data)
    {
        Iterator<C> dataIter = data.iterator();
        C minDatum = null;
        while(dataIter.hasNext())
        {
            C nextDatum = dataIter.next();
            if(nextDatum != null &&
               (minDatum == null || comparator.compare(nextDatum, minDatum) < 0))
            {
                minDatum = nextDatum;
            }
        }
        
        return minDatum;
    }
    
    /**
     * Test a bunch of inputs against a condition
     * @param <I>
     *          the type of input that we're testing
     * @param conditionToTest
     *          the condition that we're using
     * @param data
     *          the inputs to test
     * @return
     *          the boolean array holding the test results
     */
    public static <I> boolean[] testInputs(Condition<I> conditionToTest, List<I> data)
    {
        // run the test against all inputs
        boolean[] results = new boolean[data.size()];
        
        Iterator<? extends I> dataIter = data.iterator();
        for(int i = 0; dataIter.hasNext(); i++)
        {
            results[i] = conditionToTest.test(dataIter.next());
        }
        
        return results;
    }
    
    /**
     * test if any of the given booleans are true
     * @param booleans
     *          the booleans to check
     * @return
     *          true iff any of the given booleans are true
     */
    public static boolean anyTrue(boolean[] booleans)
    {
        for(boolean currBool: booleans)
        {
            if(currBool)
            {
                // found a true
                return true;
            }
        }
        
        // there are no trues
        return false;
    }
    
    /**
     * count the number of trues in the given boolean array
     * @param booleans
     *          the booleans to cound
     * @return
     *          the count
     */
    public static int countTrues(boolean[] booleans)
    {
        int numTrues = 0;
        
        for(boolean currBool: booleans)
        {
            if(currBool)
            {
                numTrues++;
            }
        }
        
        return numTrues;
    }
    
    /**
     * transpose the given matrix
     * @param <T>       the type of elements the matrix holds 
     * @param matrix    the matrix to transpose
     * @return          the transpose of the given matrix
     */
    @SuppressWarnings("unchecked")
    public static <T> T[][] transposeMatrix(T[][] matrix)
    {
        if(matrix == null)
        {
            return null;
        }
        else if(matrix.length == 0)
        {
            return matrix;
        }
        else
        {
            int rows = matrix.length;
            int cols = matrix[0].length;
            T[][] tMatrix = instantiateGenericArray(
                    matrix.getClass().getComponentType(),
                    cols);
            
            Class colType = matrix[0].getClass().getComponentType();
            for(int col = 0; col < cols; col++)
            {
                // this cast is a little bit silly... it isn't required by
                // eclipse but javac fails if it isn't there
                tMatrix[col] = (T[])instantiateGenericArray(
                        colType,
                        rows);
                for(int row = 0; row < rows; row++)
                {
                    tMatrix[col][row] = matrix[row][col];
                }
            }
            
            return tMatrix;
        }
    }
    
    /**
     * A slightly more convenient way to create generic arrays
     * @param <T>
     *          the component type of the array
     * @param componentType
     *          the component type as a parameter (wish we didn't need this
     *          but we do)
     * @param size
     *          the array size
     * @return
     *          the array instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] instantiateGenericArray(Class componentType, int size)
    {
        T[] newInstance = (T[])Array.newInstance(
                componentType,
                size);
        
        return newInstance;
    }
    
    /**
     * Delete the given indices from the given array
     * @param <T>
     *          the type of object that the array holds
     * @param indicesToDelete
     *          The indices that should be deleted. The indices must be in
     *          increasing order.
     * @param array
     *          the array to remove indices from
     * @return
     *          the result of course
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] deleteIndices(int[] indicesToDelete, T[] array)
    {
        if(indicesToDelete == null || indicesToDelete.length == 0)
        {
            return array;
        }
        else
        {
            int newLength = array.length - indicesToDelete.length;
            T[] newArray = (T[])instantiateGenericArray(
                    array.getClass().getComponentType(),
                    newLength);
            
            int indexCursor = 0;
            for(int i = 0; i < array.length; i++)
            {
                if(indexCursor < indicesToDelete.length &&
                   i == indicesToDelete[indexCursor])
                {
                    indexCursor++;
                }
                else
                {
                    newArray[i - indexCursor] = array[i];
                }
            }
            
            if(indexCursor != indicesToDelete.length)
            {
                throw new IllegalArgumentException(
                        "The given indices to delete are either out of " +
                        "bounds or out of order");
            }
            else
            {
                return newArray;
            }
        }
    }
    
    /**
     * Retain the given indices from the given array and delete all others
     * @param <T>
     *          the type of object that the array holds
     * @param indicesToRetain
     *          The indices that should be retained
     * @param array
     *          the array to remove indices from
     * @return
     *          the result of course
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] retainIndices(int[] indicesToRetain, T[] array)
    {
        if(indicesToRetain == null || indicesToRetain.length == 0)
        {
            return array;
        }
        else
        {
            T[] newArray = (T[])instantiateGenericArray(
                    array.getClass().getComponentType(),
                    indicesToRetain.length);
            
            for(int i = 0; i < indicesToRetain.length; i++)
            {
                newArray[i] = array[indicesToRetain[i]];
            }
            
            return newArray;
        }
    }
    
    /**
     * Delete the given indices from the given array
     * @param indicesToDelete
     *          The indices that should be deleted. The indices must be in
     *          increasing order.
     * @param array
     *          the array to remove indices from
     * @return
     *          the result of course
     */
    public static double[] deleteDoubleIndices(int[] indicesToDelete, double[] array)
    {
        if(indicesToDelete == null || indicesToDelete.length == 0)
        {
            return array;
        }
        else
        {
            int newLength = array.length - indicesToDelete.length;
            double[] newArray = new double[newLength];
            
            int indexCursor = 0;
            for(int i = 0; i < array.length; i++)
            {
                if(indexCursor < indicesToDelete.length &&
                   i == indicesToDelete[indexCursor])
                {
                    indexCursor++;
                }
                else
                {
                    newArray[i - indexCursor] = array[i];
                }
            }
            
            if(indexCursor != indicesToDelete.length)
            {
                throw new IllegalArgumentException(
                        "The given indices to delete are either out of " +
                        "bounds or out of order");
            }
            else
            {
                return newArray;
            }
        }
    }
    
    /**
     * Retain the given indices from the given array and delete all others
     * @param indicesToRetain
     *          The indices that should be retained
     * @param array
     *          the array to remove indices from
     * @return
     *          the result of course
     */
    public static double[] retainDoubleIndices(int[] indicesToRetain, double[] array)
    {
        if(indicesToRetain == null || indicesToRetain.length == 0)
        {
            return array;
        }
        else
        {
            double[] newArray = new double[indicesToRetain.length];
            
            for(int i = 0; i < indicesToRetain.length; i++)
            {
                newArray[i] = array[indicesToRetain[i]];
            }
            
            return newArray;
        }
    }
    
    /**
     * Convert the double list to a primitive array
     * @param doubleList
     *          the double list
     * @return
     *          the double array
     */
    public static double[] toDoubleArray(List<Double> doubleList)
    {
        double[] doubleArray = new double[doubleList.size()];
        for(int i = 0; i < doubleArray.length; i++)
        {
            doubleArray[i] = doubleList.get(i);
        }
        return doubleArray;
    }
    
    /**
     * Convert the long list into a primitive array
     * @param longList
     *          the long list
     * @return
     *          the primitive array
     */
    public static long[] toLongArray(List<Long> longList)
    {
        long[] longArray = new long[longList.size()];
        for(int i = 0; i < longArray.length; i++)
        {
            longArray[i] = longList.get(i);
        }
        return longArray;
    }
    
    /**
     * Convert the integer list into a primitive array
     * @param intList
     *          the integer list
     * @return
     *          the primitive array
     */
    public static int[] toIntArray(List<Integer> intList)
    {
        int[] intArray = new int[intList.size()];
        for(int i = 0; i < intArray.length; i++)
        {
            intArray[i] = intList.get(i);
        }
        return intArray;
    }
    
    /**
     * Convert the given int array to an integer list
     * @param intArray
     *          the int array to convert
     * @return
     *          the list of integers
     */
    public static List<Integer> toIntegerList(int[] intArray)
    {
        List<Integer> integerList = new ArrayList<Integer>(intArray.length);
        for(int currInt: intArray)
        {
            integerList.add(currInt);
        }
        return integerList;
    }

    /**
     * Convert the short list into a primitive array
     * @param shortList
     *          the short list
     * @return
     *          the primitive array
     */
    public static short[] toShortArray(List<Short> shortList)
    {
        short[] shortArray = new short[shortList.size()];
        for(int i = 0; i < shortArray.length; i++)
        {
            shortArray[i] = shortList.get(i);
        }
        return shortArray;
    }
    
    /**
     * Convert the given short array to a short list
     * @param shortArray
     *          the short array to convert
     * @return
     *          the list of shorts
     */
    public static List<Short> toShortList(short[] shortArray)
    {
        List<Short> shortList = new ArrayList<Short>(shortArray.length);
        for(short currShort: shortArray)
        {
            shortList.add(currShort);
        }
        return shortList;
    }

    /**
     * Determine if the given list is sorted
     * @see java.util.Collections#sort(List)
     * @param list
     *          the list
     * @return
     *          true iff the list is sorted
     */
    @SuppressWarnings("unchecked")
    public static boolean isSorted(List<? extends Comparable> list)
    {
        Comparable prevItem = null;
        for(Comparable currItem: list)
        {
            if(prevItem != null && currItem.compareTo(prevItem) < 0)
            {
                return false;
            }
            
            prevItem = currItem;
        }
        
        return true;
    }

    /**
     * Determine if the given array is sorted
     * @param values
     *          the values
     * @return
     *          true iff the values are sorted
     */
    public static boolean isSorted(long[] values)
    {
        for(int i = 1; i < values.length; i++)
        {
            if(values[i - 1] > values[i])
            {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Determine if the given array is sorted
     * @param values
     *          the values
     * @return
     *          true iff the values are sorted
     */
    public static boolean isSorted(float[] values)
    {
        for(int i = 1; i < values.length; i++)
        {
            if(values[i - 1] > values[i])
            {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Determine if the given array is sorted
     * @param values
     *          the values
     * @return
     *          true iff the values are sorted
     */
    public static boolean isSorted(double[] values)
    {
        for(int i = 1; i < values.length; i++)
        {
            if(values[i - 1] > values[i])
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Convert the given collection into a string where the elements are
     * all separated by ", "
     * @param data
     *          the data to stringify
     * @return
     *          the string result
     */
    public static String toString(Collection<?> data)
    {
        return SequenceUtilities.toString(data, ", ");
    }
    
    /**
     * Convert the given collection into a string where the elements are
     * all separated by the given separator string
     * @param data
     *          the data to stringify
     * @param separator
     *          the separator to use
     * @return
     *          the string result
     */
    public static String toString(Collection<?> data, String separator)
    {
        StringBuffer sb = new StringBuffer();
        
        boolean firstIteration = true;
        for(Object datum: data)
        {
            // we only use the separator after the 1st iteration
            if(!firstIteration)
            {
                sb.append(separator);
            }
            
            sb.append(datum.toString());
            firstIteration = false;
        }
        
        return sb.toString();
    }
    
    /**
     * reverse the given integer array
     * @param intArray  the int array to reverse
     */
    public static void reverseIntArray(int[] intArray)
    {
        int halfLen = intArray.length / 2;
        for(int i = 0; i < halfLen; i++)
        {
            int j = (intArray.length - i) - 1;
            int tmp = intArray[i];
            
            intArray[i] = intArray[j];
            intArray[j] = tmp;
        }
    }

    /**
     * returns the unique values from the given int array in sort order.
     * this function sorts the given int array. if all of the given ints are
     * unique then a reference to the given object is returned
     * @param ints  the ints to pair down to a unique sorted list
     * @return      the unique sorted list
     */
    public static int[] uniqueInts(int[] ints)
    {
        Arrays.sort(ints);
        
        int duplicateCount = 0;
        for(int i = 1; i < ints.length; i++)
        {
            if(ints[i] == ints[i - 1])
            {
                duplicateCount++;
            }
        }
        
        if(duplicateCount == 0)
        {
            return ints;
        }
        else
        {
            int[] uniqueInts = new int[ints.length - duplicateCount];
            uniqueInts[0] = ints[0];
            
            int j = 1;
            for(int i = 1; i < ints.length; i++)
            {
                if(uniqueInts[j - 1] != ints[i])
                {
                    uniqueInts[j] = ints[i];
                    j++;
                }
            }
            
            assert j == uniqueInts.length;
            
            return uniqueInts;
        }
    }
}
