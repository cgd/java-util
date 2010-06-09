/*
 * Copyright (c) 2009 The Jackson Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining  a copy
 * of this software and associated documentation files (the  "Software"), to
 * deal in the Software without restriction, including  without limitation the
 * rights to use, copy, modify, merge, publish,  distribute, sublicense, and/or
 * sell copies of the Software, and to  permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be  included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,  EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF  MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY  CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE  SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
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
