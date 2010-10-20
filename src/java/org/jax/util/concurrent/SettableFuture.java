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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A simple implementation of the {@link java.util.concurrent.Future}
 * interface that allows you to {@link SettableFuture#set(Object)} a
 * future result. Note that this implementation does not support
 * the use of the {@link SettableFuture#cancel(boolean)} method.
 * 
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 * @param <V> the type of future object to set or get
 */
// TODO we really need a unit test here
public class SettableFuture<V> implements Future<V>
{
    /**
     * determines if put has already been called
     */
    private volatile boolean alreadySetFlag = false;
    
    /**
     * holds an atomic reference to our future value
     */
    private volatile V futurePayload = null;
    
    /**
     * the semaphore that we use to block calls to {@link #get()} until
     * a {@link #set(Object)} is called
     */
    private final Semaphore getBlocker = new Semaphore(0);
    
    /**
     * Constructor
     */
    public SettableFuture()
    {
    }
    
    /**
     * We don't allow cancellation.
     * @param mayInterruptIfRunning
     *          dont care
     * @return
     *          false always
     */
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public V get() throws InterruptedException, ExecutionException
    {
        if(this.alreadySetFlag)
        {
            // we're lucky... it's already set so we don't need
            // to bother with the semaphore
            return this.futurePayload;
        }
        else
        {
            // we have to wait until the lock is released
            this.getBlocker.acquire();
            this.getBlocker.release();
            return this.futurePayload;
        }
    }

    /**
     * {@inheritDoc}
     */
    public V get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException
    {
        if(this.alreadySetFlag)
        {
            // we're lucky... it's already set so we don't need
            // to bother with the lock
            return this.futurePayload;
        }
        else
        {
            // we have to try to acquire the lock before we can return the
            // payload.
            if(this.getBlocker.tryAcquire(timeout, unit))
            {
                // we were successful
                this.getBlocker.release();
                return this.futurePayload;
            }
            else
            {
                // we didn't get it in time
                throw new TimeoutException(
                        "Didn't get it in " + timeout +
                        " units = " + unit.toString());
            }
        }
    }

    /**
     * We don't support cancellation
     * @return
     *          always false
     * @see #cancel(boolean)
     */
    public boolean isCancelled()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDone()
    {
        return this.alreadySetFlag;
    }
    
    /**
     * set the value of this future. this must only be called once. once
     * this function is called, all pending {@link #get()}'s will return.
     * null values are allowed
     * @param futurePayload
     *          the value that this future will now take on
     * @throws IllegalStateException
     *          if this function is called more than once. this is a fail fast
     *          mechanism that should usually work, but is not guaranteed to
     * @throws InterruptedException
     *          if our thread is interrupted during the set
     */
    public void set(V futurePayload) throws IllegalStateException, InterruptedException
    {
        if(this.alreadySetFlag)
        {
            // called more than once!
            throw new IllegalStateException(
                    "set called more than once on " + SettableFuture.class.getName() +
                    " which is not allowed");
        }
        else
        {
            // ok, we can do the set now
            this.futurePayload = futurePayload;
            this.alreadySetFlag = true;
            this.getBlocker.release();
        }
    }
}
