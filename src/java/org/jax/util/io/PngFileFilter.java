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
 * File filter for the PNG format
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class PngFileFilter extends FileChooserExtensionFilter
{
    /**
     * the png file extension
     */
    public static final String PNG_EXTENSION = "png";
    
    private static final PngFileFilter instance = new PngFileFilter();
    
    /**
     * Constructor
     */
    private PngFileFilter()
    {
        super(PNG_EXTENSION, "PNG Image (*.png)");
    }
    
    /**
     * Get the PNG filter instance
     * @return
     *          the instance
     */
    public static PngFileFilter getInstance()
    {
        return PngFileFilter.instance;
    }
}
