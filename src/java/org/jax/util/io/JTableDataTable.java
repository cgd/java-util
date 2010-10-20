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

package org.jax.util.io;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * A data table that's simply wrapped around a JTable
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class JTableDataTable implements DataTable
{
    private final JTable wrappedTable;

    /**
     * @param wrappedTable
     */
    public JTableDataTable(JTable wrappedTable)
    {
        this.wrappedTable = wrappedTable;
    }

    /**
     * {@inheritDoc}
     */
    public Object[][] getData()
    {
        TableModel tableModel = this.wrappedTable.getModel();
        Object[][] data =
            new Object[tableModel.getRowCount()][tableModel.getColumnCount()];
        for(int rowIndex = 0; rowIndex < data.length; rowIndex++)
        {
            Object[] row = data[rowIndex];
            for(int columnIndex = 0; columnIndex < row.length; columnIndex++)
            {
                row[columnIndex] = tableModel.getValueAt(rowIndex, columnIndex);
            }
        }
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object[] getHeader()
    {
        TableColumnModel tableColumnModel = this.wrappedTable.getColumnModel();
        if(tableColumnModel == null)
        {
            return null;
        }
        else
        {
            int columnCount = tableColumnModel.getColumnCount();
            Object[] header = new Object[columnCount];
            for(int columnIndex = 0; columnIndex < header.length; columnIndex++)
            {
                header[columnIndex] =
                    tableColumnModel.getColumn(columnIndex).getHeaderValue();
            }
            
            return header;
        }
    }

}
