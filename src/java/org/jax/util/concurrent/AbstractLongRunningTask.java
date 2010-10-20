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

package org.jax.util.concurrent;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Abstract task class that takes care of most of the eventing.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class AbstractLongRunningTask implements LongRunningTask
{
    private static final Logger LOG = Logger.getLogger(
            AbstractLongRunningTask.class.getName());
    
    private final ConcurrentLinkedQueue<ChangeListener> concurrentLinkedQueue =
        new ConcurrentLinkedQueue<ChangeListener>();

    /**
     * {@inheritDoc}
     */
    public void addChangeListener(ChangeListener listener)
    {
        this.concurrentLinkedQueue.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeChangeListener(ChangeListener listener)
    {
        this.concurrentLinkedQueue.remove(listener);
    }
    
    /**
     * Fire a new change event
     */
    protected void fireChangeEvent()
    {
        ChangeEvent changeEvent = new ChangeEvent(this);
        Iterator<ChangeListener> listenerIter =
            this.concurrentLinkedQueue.iterator();
        while(listenerIter.hasNext())
        {
            try
            {
                listenerIter.next().stateChanged(changeEvent);
            }
            catch(Exception ex)
            {
                LOG.log(Level.SEVERE,
                        "One of the task listeners threw an exception",
                        ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isComplete()
    {
        return this.getWorkUnitsCompleted() == this.getTotalWorkUnits();
    }
}
