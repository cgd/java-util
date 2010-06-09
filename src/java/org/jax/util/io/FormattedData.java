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

package org.jax.util.io;

/**
 * For getting at formatted and unformatted data
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public interface FormattedData
{
    /**
     * Get the formatted version (could contain HTML markup)
     * @return
     *          the formatted version
     */
    public String toFormattedString();
    
    /**
     * Get the unformatted version (no markup)
     * @return
     *          the unformatted version
     */
    public String toUnformattedString();
}