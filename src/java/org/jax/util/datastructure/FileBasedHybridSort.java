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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This sorter is useful for huge collections of data. It sorts chunks of
 * data in memory and uses a file based merge sort for the rest of the data.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class FileBasedHybridSort
{
    private static final String TMP_FILE_PREFIX = "sorting-data-";
    
    /**
     * The default memory limit used by {@link #sort(Iterator, Comparator)}
     */
    public static final int DEFAULT_IN_MEMORY_LIMIT = 100000;
    
    /**
     * Perform a hybrid memory and file based merge sort. This is useful when
     * there are too many items to be able to sort them in memory.
     * This function calls {@link #sort(Iterator, Comparator, int)} with
     * {@link #DEFAULT_IN_MEMORY_LIMIT}.
     * @param <T>
     *          the type of item we're sorting (must be serializable)
     * @param itemsToSort
     *          the list to sort
     * @param comparator
     *          the comparator to use
     * @return
     *          a sorted version of the input list (a new instance)
     * @throws IOException 
     *          if we fail trying to write to file
     */
    public <T extends Serializable> Iterator<T> sort(
            Iterator<T> itemsToSort,
            Comparator<T> comparator) throws IOException
    {
        return this.sort(itemsToSort, comparator, DEFAULT_IN_MEMORY_LIMIT);
    }
    
    /**
     * Perform a hybrid memory and file based merge sort. This is useful when
     * there are too many items to be able to sort them in memory
     * @param <T>
     *          the type of item we're sorting (must be serializable)
     * @param itemsToSort
     *          the list to sort
     * @param comparator
     *          the comparator to use
     * @param inMemoryItemLimit
     *          the item limit to allow before writing the disk. the bigger
     *          this number is the better we should perform (assuming we
     *          don't go beyond memory limits)
     * @return
     *          a sorted version of the input list (a new instance)
     * @throws IOException 
     *          if we fail trying to write to file
     */
    public <T extends Serializable> Iterator<T> sort(
            Iterator<T> itemsToSort,
            Comparator<T> comparator,
            int inMemoryItemLimit) throws IOException
    {
        if(inMemoryItemLimit <= 0)
        {
            throw new IllegalArgumentException(
                    "item limit must be greater than 0");
        }
        
        List<T> inMemoryList = new ArrayList<T>(inMemoryItemLimit);
        List<Iterator<T>> iteratorsToMerge = new ArrayList<Iterator<T>>();
        
        while(itemsToSort.hasNext())
        {
            if(inMemoryList.size() < inMemoryItemLimit)
            {
                inMemoryList.add(itemsToSort.next());
            }
            else
            {
                // sort and dump
                Collections.sort(inMemoryList, comparator);
                File sortedTempFile =
                    this.dumpToTempFile(inMemoryList.iterator());
                inMemoryList.clear();
                iteratorsToMerge.add(new ObjectInputIterator<T>(
                        sortedTempFile,
                        inMemoryItemLimit));
            }
        }
        
        if(!inMemoryList.isEmpty())
        {
            // sort and dump
            Collections.sort(inMemoryList, comparator);
            iteratorsToMerge.add(inMemoryList.iterator());
        }
        
        if(iteratorsToMerge.isEmpty())
        {
            List<T> emptyList = Collections.emptyList();
            return emptyList.iterator();
        }
        else
        {
            // now that all of the chunks are sorted we should merge them together
            return this.mergeSort(iteratorsToMerge, comparator);
        }
    }

    /**
     * Merge the given sorted iterator into a single iterator
     * @param <T>
     *          the type of item
     * @param iteratorsToMerge
     *          the iterators to merge (these should be sorted already)
     * @param comparator
     *          the comparator to sort on
     * @return
     *          the sorted result
     */
    private <T extends Serializable> Iterator<T> mergeSort(
            List<Iterator<T>> iteratorsToMerge,
            Comparator<T> comparator)
    {
        return this.mergeSortRecursive(
                iteratorsToMerge,
                comparator,
                0,
                iteratorsToMerge.size());
    }

    /**
     * Merge the given sorted iterator into a single iterator
     * @param <T>
     *          the type of item
     * @param iteratorsToMerge
     *          the iterators to merge (these should be sorted already)
     * @param comparator
     *          the comparator to sort on
     * @param startIndex
     *          the start index of the merge sort operation
     * @param length
     *          the length of merge sort operation
     * @return
     *          the sorted result
     */
    private <T extends Serializable> Iterator<T> mergeSortRecursive(
            List<Iterator<T>> iteratorsToMerge,
            Comparator<T> comparator,
            int startIndex,
            int length)
    {
        if(length == 1)
        {
            return iteratorsToMerge.get(startIndex);
        }
        else
        {
            int halfLength = length / 2;
            Iterator<T> iter1 = this.mergeSortRecursive(
                    iteratorsToMerge,
                    comparator,
                    startIndex,
                    halfLength);
            Iterator<T> iter2 = this.mergeSortRecursive(
                    iteratorsToMerge,
                    comparator,
                    startIndex + halfLength,
                    length - halfLength);
            
            return new MergeIterator<T>(iter1, iter2, comparator);
        }
    }

    /**
     * Dump the given items to file in order
     * @param <T>
     *          the type of item to dump
     * @param itemsToDump
     *          the items
     * @return
     *          the file containing the items
     * @throws IOException
     *          if we fail to write to a temporary file
     */
    private <T extends Serializable> File dumpToTempFile(Iterator<T> itemsToDump) throws IOException
    {
        File tempFile = File.createTempFile(TMP_FILE_PREFIX, null);
        tempFile.deleteOnExit();
        
        ObjectOutputStream objOut = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(tempFile)));
        while(itemsToDump.hasNext())
        {
            objOut.writeObject(itemsToDump.next());
            objOut.reset();
        }
        objOut.flush();
        objOut.close();
        
        return tempFile;
    }

    /**
     * An iterator that reads serialized objects from a temporary file
     * @param <T>   the type of input data we're reading
     */
    private class ObjectInputIterator<T extends Serializable> implements Iterator<T>
    {
        private final ObjectInput objectInput;
        
        private final int objectCount;
        
        private int objectIndex;

        private final File tempInputFile;
        
        /**
         * Constructor
         * @param tempInputFile
         *          the input to read from
         * @param objectCount
         *          the object count
         * @throws IOException 
         * @throws FileNotFoundException 
         */
        public ObjectInputIterator(
                File tempInputFile,
                int objectCount) throws FileNotFoundException, IOException
        {
            this.tempInputFile = tempInputFile;
            this.objectInput = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream(tempInputFile),
                    100 * 1024));
            this.objectCount = objectCount;
            this.objectIndex = 0;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext()
        {
            boolean hasNext = this.objectIndex < this.objectCount;
            if(!hasNext && this.tempInputFile.exists())
            {
                this.tempInputFile.delete();
            }
            
            return hasNext;
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        public T next()
        {
            if(this.hasNext())
            {
                try
                {
                    this.objectIndex++;
                    return (T)this.objectInput.readObject();
                }
                catch(ClassNotFoundException ex)
                {
                    throw new RuntimeException(ex);
                }
                catch(IOException ex)
                {
                    throw new RuntimeException(ex);
                }
            }
            else
            {
                throw new NoSuchElementException();
            }
        }

        /**
         * {@inheritDoc}
         */
        public void remove()
        {
            throw new UnsupportedOperationException(
                    "Removal not supported");
        }
    }
    
    /**
     * Iterator that performs a merge sort on two given sorted iterators
     * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
     */
    private class MergeIterator<T> implements Iterator<T>
    {
        private final Iterator<T> sortedIter1;
        
        private final Iterator<T> sortedIter2;
        
        private final Comparator<T> comparator;
        
        private T iter1Head;
        
        private T iter2Head;
        
        /**
         * Constructor
         * @param sortedIter1
         *          the 1st iterator
         * @param sortedIter2
         *          the 2nd iterator
         * @param comparator
         *          the comparator
         */
        public MergeIterator(
                Iterator<T> sortedIter1,
                Iterator<T> sortedIter2,
                Comparator<T> comparator)
        {
            this.sortedIter1 = sortedIter1;
            this.sortedIter2 = sortedIter2;
            this.comparator = comparator;
            this.iter1Head = this.nextIter1();
            this.iter2Head = this.nextIter2();
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext()
        {
            return this.iter1Head != null || this.iter2Head != null;
        }
        
        private T nextIter1()
        {
            if(this.sortedIter1.hasNext())
            {
                return this.sortedIter1.next();
            }
            else
            {
                return null;
            }
        }
        
        private T nextIter2()
        {
            if(this.sortedIter2.hasNext())
            {
                return this.sortedIter2.next();
            }
            else
            {
                return null;
            }
        }
        
        /**
         * {@inheritDoc}
         */
        public T next()
        {
            if(this.iter1Head == null && this.iter2Head == null)
            {
                throw new NoSuchElementException();
            }
            else
            {
                if(this.iter1Head == null)
                {
                    T returnVal = this.iter2Head;
                    this.iter2Head = this.nextIter2();
                    return returnVal;
                }
                else if(this.iter2Head == null)
                {
                    T returnVal = this.iter1Head;
                    this.iter1Head = this.nextIter1();
                    return returnVal;
                }
                else
                {
                    if(this.comparator.compare(this.iter1Head, this.iter2Head) <= 0)
                    {
                        T returnVal = this.iter1Head;
                        this.iter1Head = this.nextIter1();
                        return returnVal;
                    }
                    else
                    {
                        T returnVal = this.iter2Head;
                        this.iter2Head = this.nextIter2();
                        return returnVal;
                    }
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        public void remove()
        {
            throw new UnsupportedOperationException(
                    "Object removal not supported");
        }
    }
    
    /**
     * Main tester function. Works like the unix sort command line
     * without any arguments
     * @param args
     *          don't care
     * @throws IOException
     *          if IO fails
     */
    public static void main(String[] args) throws IOException
    {
        final BufferedReader in = new BufferedReader(new InputStreamReader(
                System.in));
        
        Iterator<String> inputIter = new Iterator<String>()
        {
            private String next = in.readLine();
            
            private boolean eof = false;
            
            /**
             * {@inheritDoc}
             */
            public String next()
            {
                try
                {
                    if(this.next == null)
                    {
                        String returnVal = in.readLine();
                        if(returnVal == null)
                        {
                            this.eof = true;
                        }
                        return returnVal;
                    }
                    else
                    {
                        String returnVal = this.next;
                        this.next = null;
                        
                        return returnVal;
                    }
                }
                catch(IOException ex)
                {
                    throw new RuntimeException(ex);
                }
            }
            
            /**
             * {@inheritDoc}
             */
            public boolean hasNext()
            {
                try
                {
                    if(this.next != null)
                    {
                        return true;
                    }
                    else
                    {
                        if(this.eof)
                        {
                            return false;
                        }
                        else
                        {
                            this.next = in.readLine();
                            
                            if(this.next == null)
                            {
                                this.eof = true;
                                return false;
                            }
                            else
                            {
                                return true;
                            }
                        }
                    }
                }
                catch(IOException ex)
                {
                    throw new RuntimeException(ex);
                }
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
        
        FileBasedHybridSort fileBasedHybridSort = new FileBasedHybridSort();
        Iterator<String> sortedInput =
            fileBasedHybridSort.sort(inputIter, new ComparableComparator<String>(), 100000);
        
        String prev = null;
        while(sortedInput.hasNext())
        {
            String curr = sortedInput.next();
            System.out.println(curr);
            
            if(prev != null && curr.compareTo(prev) < 0)
            {
                throw new IllegalStateException(
                        "wrong order " + prev + " == VS == " + curr);
            }
            
            prev = curr;
        }
    }
}
