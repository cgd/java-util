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

/**
 * A simple implementation for a long running task
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SimpleLongRunningTask extends AbstractLongRunningTask
{
    private String taskName = null;
    
    private int totalWorkUnits = 1;
    
    private int workUnitsCompleted = 0;
    
    /**
     * {@inheritDoc}
     */
    public String getTaskName()
    {
        return this.taskName;
    }
    
    /**
     * Setter for the task name
     * @param taskName the updated task name
     */
    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
        this.fireChangeEvent();
    }

    /**
     * {@inheritDoc}
     */
    public int getTotalWorkUnits()
    {
        return this.totalWorkUnits;
    }
    
    /**
     * Setter for the total work units
     * @param totalWorkUnits the updated total work units
     */
    public void setTotalWorkUnits(int totalWorkUnits)
    {
        this.totalWorkUnits = totalWorkUnits;
        this.fireChangeEvent();
    }

    /**
     * {@inheritDoc}
     */
    public int getWorkUnitsCompleted()
    {
        return this.workUnitsCompleted;
    }

    /**
     * Setter for the work units that have been completed
     * @param workUnitsCompleted
     *          the number of work units that have been completed
     */
    public void setWorkUnitsCompleted(int workUnitsCompleted)
    {
        this.workUnitsCompleted = workUnitsCompleted;
        this.fireChangeEvent();
    }
}
