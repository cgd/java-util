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

package org.jax.util.gui;

import java.util.Arrays;

import javax.swing.table.DefaultTableModel;

/**
 * Table model can be used where the first n columns will be editable
 * checkboxes (as specified in the constructors).
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class CheckableListTableModel extends DefaultTableModel
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 6642207585269606500L;
    
    private final int numberOfCheckColumns;
    
    private final int[] checkableColumnIndices;

    /**
     * Constructor which assumes there is only one column after the check
     * columns
     * @param numberOfCheckColumns
     *          the number of check columns (an additional "label" column is
     *          added too)
     */
    public CheckableListTableModel(
            int numberOfCheckColumns)
    {
        this(numberOfCheckColumns, 1);
    }
    
    /**
     * Constructor
     * @param numberOfCheckColumns
     *          the number of check columns
     * @param numberOfOtherColumns
     *          the number of non-check columns
     */
    public CheckableListTableModel(
            int numberOfCheckColumns,
            int numberOfOtherColumns)
    {
        if(numberOfCheckColumns < 0)
        {
            throw new IllegalArgumentException(
                    "number of check columns can't be negative: " +
                    numberOfCheckColumns);
        }
        else if(numberOfOtherColumns < 0)
        {
            throw new IllegalArgumentException(
                    "number of non-check columns can't be negative: " +
                    numberOfOtherColumns);
        }
        
        this.numberOfCheckColumns = numberOfCheckColumns;
        this.checkableColumnIndices = null;
        this.setColumnCount(numberOfCheckColumns + numberOfOtherColumns);
    }
    
    /**
     * Constructor
     * @param columnNames
     *          the column names to use
     * @param numberOfCheckColumns
     *          the number of check columns
     */
    public CheckableListTableModel(
            String[] columnNames,
            int numberOfCheckColumns)
    {
        super(columnNames, 0);
        
        if(numberOfCheckColumns < 0)
        {
            throw new IllegalArgumentException(
                    "number of check columns can't be negative: " +
                    numberOfCheckColumns);
        }
        else if(numberOfCheckColumns > columnNames.length)
        {
            throw new IllegalArgumentException(
                    "number of check columns can't be greater than the " +
                    "number of column names: " + numberOfCheckColumns +
                    " vs " + columnNames.length);
        }
        
        this.numberOfCheckColumns = numberOfCheckColumns;
        this.checkableColumnIndices = null;
        this.setColumnCount(columnNames.length);
    }
    
    /**
     * Constructor
     * @param columnNames
     *          the column names to use
     * @param checkableColumnIndices
     *          the column indices that the user can check
     */
    public CheckableListTableModel(
            String[] columnNames,
            int[] checkableColumnIndices)
    {
        super(columnNames, 0);
        
        Arrays.sort(checkableColumnIndices);
        this.numberOfCheckColumns = checkableColumnIndices.length;
        this.checkableColumnIndices = checkableColumnIndices;
        this.setColumnCount(columnNames.length);
    }
    
    /**
     * Determine if all of the check boxes have the given selection state
     * @param selectionState
     *          true if we are checking for selected and false if we're
     *          checking for not selected
     * @return
     *          true iff they all have the given selection state
     */
    public boolean areAllCheckBoxesSetTo(boolean selectionState)
    {
        int numRows = this.getRowCount();
        for(int row = 0; row < numRows; row++)
        {
            for(int column = 0; column < this.numberOfCheckColumns; column++)
            {
                Boolean currCheckBoxValue =
                    (Boolean)this.getValueAt(row, column);
                if(currCheckBoxValue.booleanValue() != selectionState)
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * toggle the selected state of all checkboxes. if only some are checked,
     * then we check everything
     */
    public void toggleSelectAllCheckBoxes()
    {
        this.setAllCheckBoxValuesTo(!this.areAllCheckBoxesSetTo(true));
    }
    
    /**
     * Set all checkbox values to the given selection state
     * @param selectionState
     *          the selection state that all of the checkboxes will
     *          now take on
     */
    public void setAllCheckBoxValuesTo(boolean selectionState)
    {
        int numRows = this.getRowCount();
        for(int row = 0; row < numRows; row++)
        {
            for(int column = 0; column < this.numberOfCheckColumns; column++)
            {
                this.setValueAt(
                        Boolean.valueOf(selectionState),
                        row,
                        column);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if(this.isColumnCheckable(columnIndex))
        {
            return Boolean.class;
        }
        else
        {
            return super.getColumnClass(columnIndex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return this.isColumnCheckable(column);
    }
    
    private boolean isColumnCheckable(int columnIndex)
    {
        if(this.checkableColumnIndices != null)
        {
            if(Arrays.binarySearch(this.checkableColumnIndices, columnIndex) >= 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(columnIndex < this.numberOfCheckColumns)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
    /**
     * @return the checkedColumnIndices
     */
    public int[] getCheckableColumnIndices()
    {
        if(this.checkableColumnIndices == null)
        {
            int[] checkedIndices = new int[this.numberOfCheckColumns];
            for(int checkedIndex = 0;
                checkedIndex < checkedIndices.length;
                checkedIndex++)
            {
                checkedIndices[checkedIndex] = checkedIndex;
            }
            return checkedIndices;
        }
        else
        {
            return this.checkableColumnIndices;
        }
    }
}
