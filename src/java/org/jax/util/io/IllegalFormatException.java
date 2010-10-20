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

package org.jax.util.io;

/**
 * An exception indicating a bad file format
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class IllegalFormatException extends Exception
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 4429150884734444045L;
    
    /**
     * Constructor
     */
    public IllegalFormatException()
    {
        super();
    }

    /**
     * Constructor
     * @param message
     *          the exception message
     * @param cause
     *          the root cause
     */
    public IllegalFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor
     * @param message
     *          the exception message
     */
    public IllegalFormatException(String message)
    {
        super(message);
    }

    /**
     * Constructor
     * @param cause
     *          the root cause
     */
    public IllegalFormatException(Throwable cause)
    {
        super(cause);
    }
}
