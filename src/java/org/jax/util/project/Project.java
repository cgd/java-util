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

package org.jax.util.project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base project class
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class Project
{
    /**
     * the name of the "name" property
     */
    public static final String NAME_PROPERTY_NAME = "name";
    
    /**
     * makes bean eventing easier...
     */
    protected final PropertyChangeSupport propertyChangeSupport =
        new PropertyChangeSupport(this);
    
    /**
     * {@link #getName()}
     */
    protected volatile String name;
    
    /**
     * Constructor
     * @param projectName
     *          the project name
     */
    public Project(String projectName)
    {
        this.name = projectName;
    }

    /**
     * Add a property listener
     * @param listener
     *          the listener
     * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
     * @see #NAME_PROPERTY_NAME
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Remove a property listener
     * @param listener
     *          the listener
     * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
     */
    public void removePropertyChangeListener(
            PropertyChangeListener listener)
    {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Get the name of this project
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Change the name of this project
     * @param name the name to set
     * @see #addPropertyChangeListener(PropertyChangeListener)
     */
    public void setName(String name)
    {
        String oldName = this.name;
        this.name = name;
        this.propertyChangeSupport.firePropertyChange(
                NAME_PROPERTY_NAME,
                oldName,
                name);
    }
}
