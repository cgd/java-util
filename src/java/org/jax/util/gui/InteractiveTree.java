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

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 * A {@link JTree} with some interactive functionality
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class InteractiveTree extends JTree
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 5450815991013997615L;
    
    /**
     * A mouse listener that passes the events down to the relevent
     * tree nodes
     */
    private final MouseListener selfMouseListener = new MouseListener()
    {
        public void mouseClicked(MouseEvent e)
        {
            MouseListener mouseListenerClicked =
                this.getMouseListenerAt(e.getPoint());
            if(mouseListenerClicked != null)
            {
                mouseListenerClicked.mouseClicked(e);
            }
        }

        public void mouseEntered(MouseEvent e)
        {
            MouseListener mouseListenerClicked =
                this.getMouseListenerAt(e.getPoint());
            if(mouseListenerClicked != null)
            {
                mouseListenerClicked.mouseEntered(e);
            }
        }

        public void mouseExited(MouseEvent e)
        {
            MouseListener mouseListenerClicked =
                this.getMouseListenerAt(e.getPoint());
            if(mouseListenerClicked != null)
            {
                mouseListenerClicked.mouseExited(e);
            }
        }

        public void mousePressed(MouseEvent e)
        {
            MouseListener mouseListenerClicked =
                this.getMouseListenerAt(e.getPoint());
            if(mouseListenerClicked != null)
            {
                mouseListenerClicked.mousePressed(e);
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            MouseListener mouseListenerClicked =
                this.getMouseListenerAt(e.getPoint());
            if(mouseListenerClicked != null)
            {
                mouseListenerClicked.mouseReleased(e);
            }
        }
        
        private MouseListener getMouseListenerAt(Point point)
        {
            TreePath pathClicked =
                InteractiveTree.this.getPathForLocation(point.x, point.y);
            if(pathClicked != null)
            {
                Object lastPathComponent = pathClicked.getLastPathComponent();
                if(lastPathComponent instanceof MouseListener)
                {
                    return (MouseListener)lastPathComponent;
                }
            }
            
            return null;
        }
    };
    
    /**
     * Constructor
     */
    public InteractiveTree()
    {
        this.setCellRenderer(new IconifiableTreeCellRenderer());
        this.addMouseListener(this.selfMouseListener);
    }
}
