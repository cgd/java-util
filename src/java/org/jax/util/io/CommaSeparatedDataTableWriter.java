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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * A data table writer that writes a comma separated file
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class CommaSeparatedDataTableWriter
{
    private static final String COMMA_SEPARATOR = ",";
    
    /**
     * Write the give table to CSV file
     * @param dataTable
     *          the table to write
     * @param commaSeperatedFile
     *          the CSV file to write to
     * @throws IOException
     *          if anything goes wrong with the data IO
     */
    public void writeTable(
            DataTable dataTable,
            File commaSeperatedFile)
    throws IOException
    {
        PrintStream printStream = new PrintStream(new BufferedOutputStream(
                new FileOutputStream(commaSeperatedFile)));
        Object[] header = dataTable.getHeader();
        if(header != null)
        {
            this.writeTableRow(header, printStream);
        }
        
        Object[][] data = dataTable.getData();
        for(Object[] row: data)
        {
            this.writeTableRow(row, printStream);
        }
        printStream.close();
    }

    /**
     * Write the given table row to the given stream
     * @param tableRow
     *          The table row that we're writing
     */
    private void writeTableRow(
            Object[] tableRow,
            PrintStream printStream)
    {
        for(int columnIndex = 0; columnIndex < tableRow.length; columnIndex++)
        {
            Object currItem = tableRow[columnIndex];
            
            if(currItem != null)
            {
                if(currItem instanceof FormattedData)
                {
                    FormattedData currFormattedData = (FormattedData)currItem;
                    printStream.print(currFormattedData.toUnformattedString());
                }
                else
                {
                    printStream.print(currItem.toString());
                }
            }
            
            if(columnIndex < tableRow.length - 1)
            {
                printStream.print(COMMA_SEPARATOR);
            }
        }
        printStream.println();
    }
}
