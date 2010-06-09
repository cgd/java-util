/*
 * Copyright (c) 2009 The Jackson Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining  a copy
 * of this software and associated documentation files (the  "Software"), to
 * deal in the Software without restriction, including  without limitation the
 * rights to use, copy, modify, merge, publish,  distribute, sublicense, and/or
 * sell copies of the Software, and to  permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be  included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,  EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF  MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY  CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE  SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.jax.util.gui;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jax.util.io.FlatFileReader;
import org.jax.util.io.IllegalFormatException;

/**
 * A JTable that knows how to display the contents of a {@link FlatFileReader}
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class FlatFileTable extends JTable
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -8669473743362364317L;
    
    private static final int MIN_TABLE_COLUMN_WIDTH = 50;
    
    private int maxRowCount;
    
    /**
     * Constructor
     * @param maxRowCount
     *          the maximum row count to be displayed by this table. use -1
     *          to indicate that there is no bound on the row count
     */
    public FlatFileTable(int maxRowCount)
    {
        this.maxRowCount = maxRowCount;
        this.setModel(new DefaultTableModel()
        {
            /**
             * all serializable classes are supposed to have one of these
             */
            private static final long serialVersionUID = -3557880979438231684L;

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });
    }
    
    /**
     * Setter for the max row count
     * @param maxRowCount the maxRowCount to set (-1 means no limit)
     */
    public void setMaxRowCount(int maxRowCount)
    {
        this.maxRowCount = maxRowCount;
    }
    
    /**
     * Getter for the maximum row count
     * @return the max row count
     */
    public int getMaxRowCount()
    {
        return this.maxRowCount;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultTableModel getModel()
    {
        return (DefaultTableModel)super.getModel();
    }
    
    /**
     * Clear out the contents of this table
     */
    public void clearTable()
    {
        this.getTableHeader().setVisible(false);
        this.getTableHeader().setPreferredSize(
                new Dimension(0, 0));
        this.getModel().setRowCount(0);
    }

    /**
     * Load the file into a JTable
     * @param flatFileReader
     *          the flat file reader to use (will be closed after the last row
     *          is read)
     * @param showHeader
     *          if true, the first line of the file is a header
     * @param tableViewingWidth
     *          after the columnCount * {@link #MIN_TABLE_COLUMN_WIDTH} exceeds
     *          this width the table columns are fixed to
     *          {@link #MIN_TABLE_COLUMN_WIDTH} rather than stretching to fill
     *          the available space. This should normally be the width of a
     *          containing scroll panel
     * @throws IOException
     *          if an {@link IOException} gets raised while we're
     *          parsing the file
     * @throws IllegalFormatException
     *          if the flat file is not correctly formatted
     */
    public void loadTable(
            FlatFileReader flatFileReader,
            boolean showHeader,
            int tableViewingWidth)
    throws IOException, IllegalFormatException
    {
        this.getTableHeader().setVisible(showHeader);
        if(!showHeader)
        {
            this.getTableHeader().setPreferredSize(
                    new Dimension(0, 0));
        }
        else
        {
            this.getTableHeader().setPreferredSize(null);
        }
        
        // clear out the contents of the table
        DefaultTableModel tableModel = this.getModel();
        tableModel.setRowCount(0);
        
        try
        {
            boolean firstIteration = true;
            String[] currRow;
            for(int row = 0;
                (this.maxRowCount == -1 || row < this.maxRowCount) &&
                (currRow = flatFileReader.readRow()) != null;
                row++)
            {
                if(firstIteration)
                {
                    if(showHeader)
                    {
                        tableModel.setColumnIdentifiers(currRow);
                    }
                    else
                    {
                        tableModel.setColumnCount(currRow.length);
                        tableModel.addRow(currRow);
                    }
                    
                    firstIteration = false;
                }
                else
                {
                    tableModel.addRow(currRow);
                }
            }
        }
        finally
        {
            flatFileReader.close();
        }
        
        this.setModel(tableModel);
        
        TableColumnModel columnModel =
            this.getTableHeader().getColumnModel();
        int columnCount = columnModel.getColumnCount();
        if(columnCount * MIN_TABLE_COLUMN_WIDTH > tableViewingWidth)
        {
            // things aren't fitting in the table well so we need
            // to allow for horizontal scrolling
            this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for(int columnIndex = 0; columnIndex < columnCount; columnIndex++)
            {
                columnModel.getColumn(columnIndex).setMinWidth(
                        MIN_TABLE_COLUMN_WIDTH);
            }
        }
        else
        {
            // the columns seem to fit OK, so let's enable auto-resize
            // which also turns off scrolling
            this.setAutoResizeMode(
                    JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }
    }
}
