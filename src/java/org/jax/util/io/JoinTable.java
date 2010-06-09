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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jax.util.datastructure.FileBasedHybridSort;

/**
 * A table view that joins to other tables together
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class JoinTable extends AbstractTableReader
{
    private final Iterator<String[]> table1Iterator;
    private final int[] table1JoinIndices;
    private final Queue<String[]> table1Queue =
        new LinkedBlockingQueue<String[]>();
    
    private final Iterator<String[]> table2Iterator;
    private final int[] table2JoinIndices;
    private final Queue<String[]> table2Queue =
        new LinkedBlockingQueue<String[]>();
    
    private final List<String> rowBuilder = new ArrayList<String>();
    
    /**
     * Constructor
     * @param table1Iterator
     *          the iterator for table 1
     * @param table1JoinIndices
     *          the indices to join on for table 1
     * @param table2Iterator
     *          the iterator for table 2
     * @param table2JoinIndices
     *          the indices to join on for table 2
     * @throws IOException 
     *          if IO fails
     */
    public JoinTable(
            Iterator<String[]> table1Iterator,
            int[] table1JoinIndices,
            Iterator<String[]> table2Iterator,
            int[] table2JoinIndices) throws IOException
    {
        this(false,
             table1Iterator,
             table1JoinIndices,
             false,
             table2Iterator,
             table2JoinIndices);
    }

    /**
     * Constructor
     * @param table1PreSorted
     *          true if table 1 is presorted on join columns
     *          (saves a lot of work)
     * @param table1Iterator
     *          the iterator for table 1
     * @param table1JoinIndices
     *          the indices to join on for table 1
     * @param table2PreSorted 
     *          true if table 2 is presorted on join columns
     *          (saves a lot of work)
     * @param table2Iterator
     *          the iterator for table 2
     * @param table2JoinIndices
     *          the indices to join on for table 2
     * @throws IOException 
     *          if IO fails
     */
    public JoinTable(
            boolean table1PreSorted,
            Iterator<String[]> table1Iterator,
            int[] table1JoinIndices,
            boolean table2PreSorted,
            Iterator<String[]> table2Iterator,
            int[] table2JoinIndices) throws IOException
    {
        if(table1JoinIndices.length != table2JoinIndices.length)
        {
            throw new IllegalArgumentException(
                    "the join indices for both tables must be " +
                    "the same length");
        }
        
        if(table1PreSorted)
        {
            this.table1Iterator = table1Iterator;
        }
        else
        {
            FileBasedHybridSort fileBasedHybridSort = new FileBasedHybridSort();
            TableColumnComparator comparator = new TableColumnComparator(
                    table1JoinIndices);
            this.table1Iterator = fileBasedHybridSort.sort(
                    table1Iterator,
                    comparator);
        }
        this.table1JoinIndices = table1JoinIndices;
        
        if(table2PreSorted)
        {
            this.table2Iterator = table2Iterator;
        }
        else
        {
            FileBasedHybridSort fileBasedHybridSort = new FileBasedHybridSort();
            TableColumnComparator comparator = new TableColumnComparator(
                    table2JoinIndices);
            this.table2Iterator = fileBasedHybridSort.sort(
                    table2Iterator,
                    comparator);
        }
        this.table2JoinIndices = table2JoinIndices;
    }

    /**
     * {@inheritDoc}
     */
    public String[] readRow() throws IOException, IllegalFormatException
    {
        while(true)
        {
            final String[] table1Row;
            if(!this.table1Queue.isEmpty())
            {
                table1Row = this.table1Queue.remove();
            }
            else if(this.table1Iterator.hasNext())
            {
                table1Row = this.table1Iterator.next();
            }
            else
            {
                return null;
            }
            
            final String[] table2Row;
            if(!this.table2Queue.isEmpty())
            {
                table2Row = this.table2Queue.remove();
            }
            else if(this.table2Iterator.hasNext())
            {
                table2Row = this.table2Iterator.next();
            }
            else
            {
                return null;
            }
            
            int joinIndexComparison = 0;
            for(int joinColIndex = 0;
                joinIndexComparison == 0 && joinColIndex < this.table1JoinIndices.length;
                joinColIndex++)
            {
                final int index1 = this.table1JoinIndices[joinColIndex];
                final int index2 = this.table2JoinIndices[joinColIndex];
                joinIndexComparison =
                    table1Row[index1].compareTo(table2Row[index2]);
            }
            
            if(joinIndexComparison == 0)
            {
                // we can do the join
                assert this.rowBuilder.size() == 0;
                
                for(int i = 0; i < table1Row.length; i++)
                {
                    this.rowBuilder.add(table1Row[i]);
                }
                
                int joinColIndex = 0;
                for(int i = 0; i < table2Row.length; i++)
                {
                    if(joinColIndex >= this.table2JoinIndices.length ||
                       i < this.table2JoinIndices[joinColIndex])
                    {
                        this.rowBuilder.add(table2Row[i]);
                    }
                    else
                    {
                        joinColIndex++;
                    }
                }
                
                String[] joinedRow = this.rowBuilder.toArray(
                        new String[this.rowBuilder.size()]);
                this.rowBuilder.clear();
                return joinedRow;
            }
            else if(joinIndexComparison > 0)
            {
                // we can't do the join... keep the greater row (table1)
                this.table1Queue.add(table1Row);
            }
            else
            {
                // we can't do the join... keep the greater row (table2)
                this.table2Queue.add(table2Row);
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
                CommonFlatFileFormat.CSV_RFC_4180);
        
        File file2 = new File(args[2]);
        String[] joinColStrings2 = args[3].split(",");
        int[] joinCols2 = new int[joinColStrings2.length];
        for(int i = 0; i < joinColStrings2.length; i++)
        {
            joinCols2[i] = Integer.parseInt(joinColStrings2[i].trim());
        }
        TableReader tableReader2 = new FlatFileReader(
                new BufferedReader(new FileReader(file2)),
                CommonFlatFileFormat.CSV_RFC_4180);
        
        TableReader joinedReader = new JoinTable(
                tableReader1.iterator(),
                joinCols1,
                tableReader2.iterator(),
                joinCols2);
        TableWriter tableWriter = new FlatFileWriter(
                new BufferedWriter(new OutputStreamWriter(System.out)),
                CommonFlatFileFormat.CSV_RFC_4180);
        
        String[] nextJoinedRow;
        while((nextJoinedRow = joinedReader.readRow()) != null)
        {
            tableWriter.writeRow(nextJoinedRow);
        }
        tableWriter.flush();
    }
}
