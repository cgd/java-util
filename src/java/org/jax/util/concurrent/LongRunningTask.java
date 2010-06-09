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

package org.jax.util.concurrent;

import javax.swing.event.ChangeListener;

/**
 * An interface that can be implemented if you have a long running
 * task that you would like to keep track of.
 * @see MultiTaskProgressPanel
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public interface LongRunningTask
{
    /**
     * Add the given listener. This task will fire a change whenever
     * {@link #getTaskName()}, {@link #getTotalWorkUnits()} or
     * {@link #getWorkUnitsCompleted()} changes (in general
     * {@link #getTotalWorkUnits()} should not be changing though)
     * @param listener
     *          the listener
     */
    public void addChangeListener(ChangeListener listener);
    
    /**
     * Remove the given listener
     * @param listener
     *          the listener
     */
    public void removeChangeListener(ChangeListener listener);
    
    /**
     * The name of the task
     * @return
     *          the name of the task
     */
    public String getTaskName();
    
    /**
     * Getter for the total number of work units. As an example if a task's
     * runtime is completely indeterminant this function should return 1 and
     * {@link #getWorkUnitsCompleted()} should return 0 until the task is
     * complete.
     * @return
     *          the total number of work units
     */
    public int getTotalWorkUnits();
    
    /**
     * Getter for the number of work units completed (don't care for
     * indeterminant tasks)
     * @return
     *          the number of work units completed
     */
    public int getWorkUnitsCompleted();
    
    /**
     * Returns true iff the task is complete
     * @return
     *          true iff the task is complete
     */
    public boolean isComplete();
}
