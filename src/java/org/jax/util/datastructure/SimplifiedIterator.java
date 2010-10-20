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

package org.jax.util.datastructure;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A super simple implementation of {@link Iterator}
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 * @param <E>
 *              the element type to iterate over
 */
public abstract class SimplifiedIterator<E> implements Iterator<E>
{
    private final Queue<E> elementBuffer = new LinkedBlockingQueue<E>();
    
    private boolean done = false;
    
    /**
     * {@inheritDoc}
     */
    public E next()
    {
        if(this.elementBuffer.isEmpty())
        {
            return this.getNextInternal();
        }
        else
        {
            return this.elementBuffer.remove();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasNext()
    {
        if(this.done)
        {
            return false;
        }
        else if(this.elementBuffer.isEmpty())
        {
            E next = this.getNextInternal();
            if(next == null)
            {
                this.done = true;
                return false;
            }
            else
            {
                this.elementBuffer.add(next);
                return true;
            }
        }
        else
        {
            return true;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void remove()
    {
        throw new UnsupportedOperationException("remove() not supported");
    }
    
    /**
     * Internal function to get next... this function should only be called
     * by this class
     * @return
     *          the next element or null if there is no next element
     */
    protected abstract E getNextInternal();
}
