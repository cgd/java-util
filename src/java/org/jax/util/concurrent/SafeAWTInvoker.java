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

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * Creates a safe variant of {@link java.awt.EventQueue#invokeLater(Runnable)}
 * that doesn't throw an {@link Error} if we're in the AWT event handling thread
 * already.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public final class SafeAWTInvoker
{
    /**
     * No instances allowed
     */
    private SafeAWTInvoker()
    {
    }

    /**
     * This function is basically the same as
     * {@link EventQueue#invokeAndWait(Runnable)} except that it is safe to call
     * from the event dispatching thread. If it is called from the event
     * dispatch thread is just executes right away.
     * @param runnable
     *          the runnable to call
     * @throws InvocationTargetException
     *          see {@link EventQueue#invokeAndWait(Runnable)} for a description
     *          of when this can happen
     * @throws InterruptedException
     *          see {@link EventQueue#invokeAndWait(Runnable)} for a description
     *          of when this can happen
     */
    public static void safeInvokeAndWait(Runnable runnable)
    throws InvocationTargetException, InterruptedException
    {
        if(EventQueue.isDispatchThread())
        {
            // since we're in the event queue already, we must invoke
            // the runnable directly
            try
            {
                runnable.run();
            }
            catch(Exception ex)
            {
                throw new InvocationTargetException(ex);
            }
        }
        else
        {
            // delegate to the event queue
            EventQueue.invokeAndWait(runnable);
        }
    }
    
    /**
     * A call and wait function that is safe to call even if you are in the
     * AWT thread. This is similar to {@link #safeInvokeAndWait(Runnable)}
     * except that we want to return an actual result
     * @param <T>
     *          the type of result
     * @param callable
     *          the callable
     * @return
     *          the result
     * @throws ExecutionException
     *          if the callable throws an exception we'll wrap it up in this
     * @throws InterruptedException
     *          if there is a thread interruption when we
     *          {@link EventQueue#invokeLater(Runnable)}
     */
    public static <T> T safeCallAndWait(final Callable<T> callable)
    throws InterruptedException, ExecutionException
    {
        if(EventQueue.isDispatchThread())
        {
            try
            {
                return callable.call();
            }
            catch(Exception ex)
            {
                throw new ExecutionException(ex);
            }
        }
        else
        {
            FutureTask<T> futureTask = new FutureTask<T>(callable);
            EventQueue.invokeLater(futureTask);
            
            // block until the computation is complete then return the
            // result
            return futureTask.get();
        }
    }
}
