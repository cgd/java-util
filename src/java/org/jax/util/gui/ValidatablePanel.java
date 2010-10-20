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

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * A panel that includes a function for validating the data within the panel
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class ValidatablePanel extends JPanel implements Validatable
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -1329940933871830545L;

    /**
     * Same as {@link JPanel#JPanel()}
     */
    public ValidatablePanel()
    {
        super();
    }

    /**
     * Same as {@link JPanel#JPanel(LayoutManager)}
     * @param layout see {@link JPanel}
     */
    public ValidatablePanel(LayoutManager layout)
    {
        super(layout);
    }

    /**
     * Same as {@link JPanel#JPanel(boolean)}
     * @param isDoubleBuffered  see {@link JPanel}
     */
    public ValidatablePanel(boolean isDoubleBuffered)
    {
        super(isDoubleBuffered);
    }

    /**
     * Same as {@link JPanel#JPanel(LayoutManager, boolean)}
     * @param layout            see {@link JPanel}
     * @param isDoubleBuffered  see {@link JPanel}
     */
    public ValidatablePanel(LayoutManager layout, boolean isDoubleBuffered)
    {
        super(layout, isDoubleBuffered);
    }
    
    /**
     * Validate the data in the panel possibly alerts the user if a problem
     * is discovered
     * @return  true iff the data is valid
     */
    public abstract boolean validateData();
}
