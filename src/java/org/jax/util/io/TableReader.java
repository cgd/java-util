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

import java.io.IOException;

/**
 * An interface for reading table rows
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public interface TableReader extends Iterable<String[]>
{
    /**
     * Read a table row
     * @return
     *          the row or null at end of table
     * @throws IOException
     *          if the read fails
     * @throws IllegalFormatException
     *          if the format is bad 
     */
    public String[] readRow() throws IOException, IllegalFormatException;
}
