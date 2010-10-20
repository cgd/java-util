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
import java.awt.Container;
import java.awt.Window;

/**
 * Swing utility functions
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SwingUtilities
{
    /**
     * Getter for the containing window
     * @param component
     *          the component for which we're looking for a containing window
     * @return
     *          the containing window or null if we don't find one
     */
    public static Window getContainingWindow(Component component)
    {
        if(component instanceof Window)
        {
            return (Window)component;
        }
        else
        {
            Container parent;
            while((parent = component.getParent()) != null)
            {
                if(parent instanceof Window)
                {
                    // done!!
                    return (Window)parent;
                }
                
                component = parent;
            }
            
            return null;
        }
    }
}
