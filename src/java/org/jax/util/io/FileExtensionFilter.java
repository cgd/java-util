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

import java.io.File;
import java.io.FileFilter;

/**
 * A general implementation of the file filter interface that uses a filename
 * extension.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class FileExtensionFilter implements FileFilter
{
    /**
     * @see #getExtensionToAccept()
     */
    private final String extensionToAccept;
    
    /**
     * a dot '.' plus the extension
     */
    private final String endingString;
    
    /**
     * Constructor
     * @param extensionToAccept
     *          see {@link #getExtensionToAccept()}
     */
    public FileExtensionFilter(String extensionToAccept)
    {
        this.extensionToAccept = extensionToAccept;
        this.endingString = "." + extensionToAccept;
    }

    /**
     * {@inheritDoc}
     */
    public boolean accept(File pathname)
    {
        return pathname.getName().endsWith(this.endingString);
    }

    /**
     * The extensions for files that we should accept
     * @return
     *          the extension that we're using
     */
    public String getExtensionToAccept()
    {
        return this.extensionToAccept;
    }
    
    /**
     * Getter for the ending string that we check for on files. This is
     * just {@link #getExtensionToAccept()} with a dot prepended.
     * @return
     *          the ending string
     */
    public String getEndingString()
    {
        return this.endingString;
    }
}
