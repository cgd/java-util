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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Utility functions for probability and statistics
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class StatisticUtilities
{
    /**
     * Calculate a p-value for the data point, given the sorted null
     * distribution sample.
     * @param dataPoint
     *          the data point that we're calculating the p-value for
     * @param sortedNullHypothesisSample
     *          a sample from the null hypothesis
     * @return
     *          the p-value calculated for the given data
     * @throws IllegalArgumentException
     *          if the given sample is empty
     */
    public static double calculatePValueForDataPoint(
            double dataPoint,
            double[] sortedNullHypothesisSample)
    throws
            IllegalArgumentException
    {
        // TODO validate me
        if(sortedNullHypothesisSample.length == 0)
        {
            throw new IllegalArgumentException(
                    "can't calculate a p-value with zero permutations");
        }
        else
        {
            int searchResult = Arrays.binarySearch(sortedNullHypothesisSample, dataPoint);
            
            int permutationIndexMarker;
            if(searchResult < 1)
            {
                // the index wasn't found, but we got an insertion point... we just
                // need to massage it a little to turn it into a permutation
                // index
                permutationIndexMarker = (-searchResult) - 1;
            }
            else
            {
                // we found the index of a match
                permutationIndexMarker = searchResult;
                
                // if there are multiple matches we need the index of the
                // 1st one
                while(permutationIndexMarker > 0 &&
                      sortedNullHypothesisSample[permutationIndexMarker - 1] == dataPoint)
                {
                    permutationIndexMarker--;
                }
            }
            
            int numPermutationsGreaterThanOrEqualToSamplePoint =
                sortedNullHypothesisSample.length - permutationIndexMarker;
            return numPermutationsGreaterThanOrEqualToSamplePoint /
                   (double)sortedNullHypothesisSample.length;
        }
    }
    
    /**
     * Determine which data point corresponds to the given p-value 
     * @param pValue
     *          the p-value that we're looking for
     * @param sortedNullHypothesisSample
     *          a sample from the null hypothesis distribution
     * @return
     *          the data point from the given null hypothesis data that
     *          best represents the given p-value
     * @throws IllegalArgumentException
     *          if the p-value is out of range [0-1] or if the null
     *          hypothesis sample is empty
     */
    public static double calculateDataPointForPValue(
            double pValue,
            double[] sortedNullHypothesisSample)
    throws
            IllegalArgumentException
    {
        // TODO validate me
        if(sortedNullHypothesisSample.length == 0)
        {
            throw new IllegalArgumentException(
                    "can't calculate a data point with zero permutations");
        }
        else if(pValue < 0.0 || pValue > 1.0)
        {
            throw new IllegalArgumentException(
                    "p-value should be between 0 and 1, not: " + pValue);
        }
        else
        {
            int index =
                (int)Math.ceil((sortedNullHypothesisSample.length - 1) * (1.0 - pValue));
            if(index < 0)
            {
                index = 0;
            }
            else if(index > sortedNullHypothesisSample.length - 1)
            {
                index = sortedNullHypothesisSample.length - 1;
            }
            
            return sortedNullHypothesisSample[index];
        }
    }

    /**
     * Calculate a mean for the input
     * @param values
     *          the values we're calculating a mean for
     * @return
     *          a mean value
     * @throws IllegalArgumentException
     *          if the size of the input is empty
     */
    public static double calculateMean(Collection<? extends Number> values)
    throws
            IllegalArgumentException
    {
        if(values.isEmpty())
        {
            throw new IllegalArgumentException(
                    "can't calculate a mean for an empty collection");
        }
        else
        {
            double sum = 0.0;
            for(Number currValue: values)
            {
                sum += currValue.doubleValue();
            }
            return sum / values.size();
        }
    }
    
    /**
     * Calculate a mean for the input
     * @param values
     *          the values we're calculating a mean for
     * @return
     *          a mean value
     * @throws IllegalArgumentException
     *          if the size of the input is empty
     */
    public static double calculateMean(double[] values)
    throws
            IllegalArgumentException
    {
        if(values.length == 0)
        {
            throw new IllegalArgumentException(
                    "can't calculate a mean for an empty list");
        }
        else
        {
            double sum = 0.0;
            for(Number currValue: values)
            {
                sum += currValue.doubleValue();
            }
            return sum / values.length;
        }
    }
    
    /**
     * Shuffle the given list. This function delegates to
     * {@link #shuffle(List, Random)}.
     * @param <T>
     *          the type of the list contents (can be anything)
     * @param listToSuffle
     *          the list we're shuffling
     */
    public static <T> void shuffle(List<T> listToSuffle)
    {
        StatisticUtilities.shuffle(listToSuffle, new Random());
    }
    
    /**
     * Shuffle the given list. This function will perform very slowly if
     * you don't use a random access list.
     * @param <T>
     *          the type of the list contents (can be anything)
     * @param listToSuffle
     *          the list we're shuffling
     * @param random
     *          the random variable to use
     */
    public static <T> void shuffle(List<T> listToSuffle, Random random)
    {
        int listSize = listToSuffle.size();
        for(int i = 0; i < listSize; i++)
        {
            int randomIndex = random.nextInt(listSize);
            if(randomIndex != i)
            {
                // do a random swap
                T temp = listToSuffle.get(i);
                listToSuffle.set(i, listToSuffle.get(randomIndex));
                listToSuffle.set(randomIndex, temp);
            }
        }
    }
}
