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

package org.jax.util;

/**
 * A simple timer class suitable for debug print statements
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public final class SimpleTimer
{
    private volatile long startTimeMillis = System.currentTimeMillis();
    
    /**
     * Reset the timer
     */
    public void reset()
    {
        this.startTimeMillis = System.currentTimeMillis();
    }
    
    /**
     * Get the ellapsed time in seconds
     * @return
     *          the ellapsed time
     */
    public double getTimeEllapsedInSeconds()
    {
        return (System.currentTimeMillis() - this.startTimeMillis) / 1000.0;
    }
}
