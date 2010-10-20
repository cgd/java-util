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

package org.jax.util.gui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A {@link TableCellRenderer} that renders table cells as disabled if the
 * table is disabled.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class CanDisableContentsTableCellRenderer implements TableCellRenderer
{
    private final TableCellRenderer tableCellRendererDelegate;
    
    /**
     * This renderer delegates to the given renderer. The only difference is
     * that we set the render component to enabled/disabled based on whether
     * the {@link JTable} is enabled or not.
     * @param tableCellRendererDelegate
     *          the renderer that we delegate to
     */
    public CanDisableContentsTableCellRenderer(TableCellRenderer tableCellRendererDelegate)
    {
        this.tableCellRendererDelegate = tableCellRendererDelegate;
    }
    
    /**
     * {@inheritDoc}
     */
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column)
    {
        TableCellRenderer tableCellRendererDelegate = this.tableCellRendererDelegate;
        if(tableCellRendererDelegate == null)
        {
            tableCellRendererDelegate = table.getDefaultRenderer(
                    table.getColumnClass(column));
        }
        
        Component renderComponent = tableCellRendererDelegate.getTableCellRendererComponent(
                table,
                value,
                isSelected,
                hasFocus,
                row,
                column);
        renderComponent.setEnabled(table.isEnabled());
        
        return renderComponent;
    }
}
