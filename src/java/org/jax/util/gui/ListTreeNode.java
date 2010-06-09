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

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A tree node type for representing a list of subitems
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 * @param <E> the list element type
 */
public class ListTreeNode<E> extends DefaultMutableTreeNode
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 9031914890413713919L;
    
    private final String name;

    /**
     * Constructor
     * @param name
     *          the name to use
     * @param list
     *          the list
     */
    public ListTreeNode(
            String name,
            List<E> list)
    {
        super(list);
        
        this.setAllowsChildren(true);
        this.name = name;
    }
    
    /**
     * Get the list which is just a typecast version of {@link #getUserObject()}
     * @return
     *          the list
     */
    @SuppressWarnings("unchecked")
    public List<E> getList()
    {
        return (List<E>)this.getUserObject();
    }
    
    /**
     * Getter for the name
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        List<E> list =
            this.getList();
        
        StringBuffer sb = new StringBuffer(this.getName());
        sb.append(" (");
        if(list.isEmpty())
        {
            sb.append("empty");
        }
        else
        {
            sb.append(list.size());
        }
        sb.append(")");
        return sb.toString();
    }
}
