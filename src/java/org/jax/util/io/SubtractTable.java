/*
 * Copyright (c) 2009 The Jackson Laboratory
 * 
 * This software was developed by Gary Churchill's Lab at The Jackson
 * Laboratory (see http://research.jax.org/faculty/churchill).
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jax.util.datastructure.FileBasedHybridSort;

/**
 * A table view that joins to other tables together
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SubtractTable extends AbstractTableReader
{
    private final Iterator<String[]> minuendIterator;
    private final int[] minuendJoinIndices;
    
    private final Iterator<String[]> subtrahendIterator;
    private final int[] subtrahendJoinIndices;
    private final Queue<String[]> subtrahendQueue =
        new LinkedBlockingQueue<String[]>();
    
    /**
     * Constructor
     * @param minuendTableIterator
     *          the table that we're subtracting from
     * @param minuendJoinIndices
     *          the indices we're matching up to subtract
     * @param subtrahendTableIterator
     *          the table whose indices we're using to do the subtraction
     * @param subtrahendJoinIndices
     *          the subtrahend indices that we're matching up to subtract
     * @throws IOException 
     *          if IO fails
     */
    public SubtractTable(
            Iterator<String[]> minuendTableIterator,
            int[] minuendJoinIndices,
            Iterator<String[]> subtrahendTableIterator,
            int[] subtrahendJoinIndices) throws IOException
    {
        this(false,
             minuendTableIterator,
             minuendJoinIndices,
             false,
             subtrahendTableIterator,
             subtrahendJoinIndices);
    }

    /**
     * Constructor
     * @param munuendIsPreSorted
     *          true if table 1 is presorted on join columns
     *          (saves a lot of work)
     * @param minuendTableIterator
     *          the table that we're subtracting from
     * @param minuendJoinIndices
     *          the indices we're matching up to subtract
     * @param subtrahendIsPreSorted 
     *          true if table 2 is presorted on join columns
     *          (saves a lot of work)
     * @param subtrahendTableIterator
     *          the table whose indices we're using to do the subtraction
     * @param subtrahendJoinIndices
     *          the subtrahend indices that we're matching up to subtract
     * @throws IOException 
     *          if IO fails
     */
    public SubtractTable(
            boolean munuendIsPreSorted,
            Iterator<String[]> minuendTableIterator,
            int[] minuendJoinIndices,
            boolean subtrahendIsPreSorted,
            Iterator<String[]> subtrahendTableIterator,
            int[] subtrahendJoinIndices) throws IOException
    {
        if(minuendJoinIndices.length != subtrahendJoinIndices.length)
        {
            throw new IllegalArgumentException(
                    "the join indices for both tables must be " +
                    "the same length");
        }
        
        if(munuendIsPreSorted)
        {
            this.minuendIterator = minuendTableIterator;
        }
        else
        {
            FileBasedHybridSort fileBasedHybridSort = new FileBasedHybridSort();
            TableColumnComparator comparator = new TableColumnComparator(
                    minuendJoinIndices);
            this.minuendIterator = fileBasedHybridSort.sort(
                    minuendTableIterator,
                    comparator);
        }
        this.minuendJoinIndices = minuendJoinIndices;
        
        if(subtrahendIsPreSorted)
        {
            this.subtrahendIterator = subtrahendTableIterator;
        }
        else
        {
            FileBasedHybridSort fileBasedHybridSort = new FileBasedHybridSort();
            TableColumnComparator comparator = new TableColumnComparator(
                    subtrahendJoinIndices);
            this.subtrahendIterator = fileBasedHybridSort.sort(
                    subtrahendTableIterator,
                    comparator);
        }
        this.subtrahendJoinIndices = subtrahendJoinIndices;
    }

    /**
     * {@inheritDoc}
     */
    public String[] readRow() throws IOException, IllegalFormatException
    {
        while(this.minuendIterator.hasNext())
        {
            final String[] minuendRow = this.minuendIterator.next();
            
            if(this.acceptMinuend(minuendRow))
            {
                return minuendRow;
            }
        }
        
        // there are no minuends left to return
        return null;
    }
    
    private boolean acceptMinuend(String[] minuendRow)
    {
        while(true)
        {
            String[] subtrahendRow = this.subtrahendQueue.peek();
            if(subtrahendRow == null)
            {
                if(this.subtrahendIterator.hasNext())
                {
                    subtrahendRow = this.subtrahendIterator.next();
                    this.subtrahendQueue.add(subtrahendRow);
                }
                else
                {
                    // since we're out of subtrahend rows we can just return
                    // true, the minuend is accepted
                    return true;
                }
            }
            
            int joinIndexComparison = 0;
            for(int joinColIndex = 0;
                joinIndexComparison == 0 && joinColIndex < this.minuendJoinIndices.length;
                joinColIndex++)
            {
                final int minuendIndex = this.minuendJoinIndices[joinColIndex];
                final int subtrahendIndex = this.subtrahendJoinIndices[joinColIndex];
                joinIndexComparison =
                    minuendRow[minuendIndex].compareTo(subtrahendRow[subtrahendIndex]);
            }
            
            if(joinIndexComparison == 0)
            {
                // filter out the minuend... keep the subtrahend since we
                // may hit the same minuend again
                return false;
            }
            else if(joinIndexComparison < 0)
            {
                // accept the minuend... keep the subtrahend though since
                // we may bump into it later
                return true;
            }
            else // joinIndexComparison > 0
            {
                // move on to the next subtrahend
                this.subtrahendQueue.remove();
            }
        }
    }
    
    /**
     * Main tester
     * @param args
     *          file1 (csv format), comma-delimited join cols,
     *          file2 (csv format), comma-delimited join cols
     * @throws IOException
     *          if we can't read/write
     * @throws IllegalFormatException
     *          if file format is bad
     */
    public static void main(String[] args) throws IOException, IllegalFormatException
    {
        File file1 = new File(args[0]);
        String[] joinColStrings1 = args[1].split(",");
        int[] joinCols1 = new int[joinColStrings1.length];
        for(int i = 0; i < joinColStrings1.length; i++)
        {
            joinCols1[i] = Integer.parseInt(joinColStrings1[i].trim());
        }
        TableReader tableReader1 = new FlatFileReader(
                new BufferedReader(new FileReader(file1)),
                CommonFlatFileFormat.CSV_UNIX);
        
        File file2 = new File(args[2]);
        String[] joinColStrings2 = args[3].split(",");
        int[] joinCols2 = new int[joinColStrings2.length];
        for(int i = 0; i < joinColStrings2.length; i++)
        {
            joinCols2[i] = Integer.parseInt(joinColStrings2[i].trim());
        }
        TableReader tableReader2 = new FlatFileReader(
                new BufferedReader(new FileReader(file2)),
                CommonFlatFileFormat.CSV_UNIX);
        
        TableReader joinedReader = new SubtractTable(
                tableReader1.iterator(),
                joinCols1,
                tableReader2.iterator(),
                joinCols2);
        TableWriter tableWriter = new FlatFileWriter(
                new BufferedWriter(new OutputStreamWriter(System.out)),
                CommonFlatFileFormat.CSV_UNIX);
        
        String[] nextJoinedRow;
        while((nextJoinedRow = joinedReader.readRow()) != null)
        {
            tableWriter.writeRow(nextJoinedRow);
        }
        tableWriter.flush();
    }
}
