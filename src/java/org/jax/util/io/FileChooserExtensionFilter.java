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

import javax.swing.filechooser.FileFilter;

/**
 * Filter for determining if a file is a J/qtl project or not
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class FileChooserExtensionFilter extends FileFilter
{
    private final String description;
    
    private final String extensionWithDot;
    
    /**
     * Constructor
     * @param extension
     *          the extension (without the dot)
     * @param description
     *          the description
     */
    public FileChooserExtensionFilter(
            String extension,
            String description)
    {
        this.extensionWithDot = "." + extension;
        this.description = description;
    }
    
    /**
     * Getter for the extension with a '.' prepended
     * @return
     *          the extension with a '.' prepended
     */
    public String getExtensionWithDot()
    {
        return this.extensionWithDot;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(File file)
    {
        return file.isDirectory()
               ||
               (file.isFile() &&
               file.getName().toLowerCase().endsWith(this.extensionWithDot.toLowerCase()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription()
    {
        return this.description;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object otherObject)
    {
        if(otherObject instanceof FileChooserExtensionFilter)
        {
            FileChooserExtensionFilter otherFilter =
                (FileChooserExtensionFilter)otherObject;
            return this.extensionWithDot.equals(otherFilter.extensionWithDot) &&
                   this.description.equals(otherFilter.description);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return this.extensionWithDot.hashCode();
    }
}
