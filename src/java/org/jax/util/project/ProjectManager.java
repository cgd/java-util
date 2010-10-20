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
import java.io.File;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.filechooser.FileFilter;

/**
 * A general purpose project manager class
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class ProjectManager
{
    /**
     * the project change listeners
     */
    private final ConcurrentLinkedQueue<ProjectChangeListener> projectChangeListeners;
    
    /**
     * makes bean eventing easier...
     */
    private final PropertyChangeSupport propertyChangeSupport;
    
    /**
     * the bean name used when the active project changes
     */
    public static final String ACTIVE_PROJECT_PROPERTY_NAME =
        "activeProject";
    
    /**
     * @see #getActiveProject()
     */
    private volatile Project activeProject;
    
    /**
     * the bean name used when the active project file changes
     */
    public static final String ACTIVE_PROJECT_FILE_PROPERTY_NAME =
        "activeProjectFile";
    
    /**
     * @see #getActiveProjectFile()
     */
    private volatile File activeProjectFile;
    
    /**
     * the bean name used when the active project modified property changes
     */
    public static final String ACTIVE_PROJECT_MODIFIED_PROPERTY_NAME =
        "activeProjectModified";
    
    /**
     * @see #isActiveProjectModified()
     */
    private volatile boolean activeProjectModified;

    /**
     * Constructor
     */
    public ProjectManager()
    {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.activeProject = null;
        this.activeProjectFile = null;
        this.activeProjectModified = false;
        this.projectChangeListeners =
            new ConcurrentLinkedQueue<ProjectChangeListener>();
    }
    
    /**
     * Get the file filter for projects
     * @return
     *          the file filter
     */
    public abstract FileFilter getProjectFileFilter();
    
    /**
     * Add a property listener
     * @param listener
     *          the listener
     * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
     * @see #ACTIVE_PROJECT_PROPERTY_NAME
     * @see #ACTIVE_PROJECT_FILE_PROPERTY_NAME
     * @see #ACTIVE_PROJECT_MODIFIED_PROPERTY_NAME
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Add a property listener
     * @param propertyName
     *          the property to listen to
     * @param listener
     *          the listener
     * @see PropertyChangeSupport#addPropertyChangeListener(String, PropertyChangeListener)
     * @see #ACTIVE_PROJECT_PROPERTY_NAME
     * @see #ACTIVE_PROJECT_FILE_PROPERTY_NAME
     * @see #ACTIVE_PROJECT_MODIFIED_PROPERTY_NAME
     */
    public void addPropertyChangeListener(
            String propertyName,
            PropertyChangeListener listener)
    {
        this.propertyChangeSupport.addPropertyChangeListener(
                propertyName,
                listener);
    }
    
    /**
     * Remove a property listener
     * @param listener
     *          the listener
     * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
     * @see #ACTIVE_PROJECT_PROPERTY_NAME
     * @see #ACTIVE_PROJECT_FILE_PROPERTY_NAME
     * @see #ACTIVE_PROJECT_MODIFIED_PROPERTY_NAME
     */
    public void removePropertyChangeListener(
            PropertyChangeListener listener)
    {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Remove a property listener
     * @param propertyName
     *          the property to stop listening to
     * @param listener
     *          the listener
     * @see PropertyChangeSupport#removePropertyChangeListener(String, PropertyChangeListener)
     * @see #ACTIVE_PROJECT_PROPERTY_NAME
     * @see #ACTIVE_PROJECT_FILE_PROPERTY_NAME
     * @see #ACTIVE_PROJECT_MODIFIED_PROPERTY_NAME
     */
    public void removePropertyChangeListener(
            String propertyName,
            PropertyChangeListener listener)
    {
        this.propertyChangeSupport.removePropertyChangeListener(
                propertyName,
                listener);
    }

    /**
     * Adds the listener to the listener list
     * @param listenerToAdd
     *          the listener to add to the listener list
     */
    public void addProjectChangeListener(ProjectChangeListener listenerToAdd)
    {
        this.projectChangeListeners.add(listenerToAdd);
    }
    
    /**
     * Removes the listener from the listener list
     * @param listenerToRemove
     *          the listener to remove from the listener list
     */
    public void removeProjectChangeListener(ProjectChangeListener listenerToRemove)
    {
        this.projectChangeListeners.remove(listenerToRemove);
    }
    
    /**
     * tell all of the project change listeners that a change occured
     */
    private void fireProjectChangeOccurred()
    {
        Iterator<ProjectChangeListener> listenerIter =
            this.projectChangeListeners.iterator();
        while(listenerIter.hasNext())
        {
            listenerIter.next().projectChangeOccurred(this);
        }
    }
    
    /**
     * Determine if the active project has been modified.
     * @return
     *          true iff the active project has been modified
     */
    public boolean isActiveProjectModified()
    {
        return this.activeProjectModified;
    }
    
    /**
     * Update the property that says whether or not this project was updated
     * @param activeProjectModified
     *          true iff the active project has been modified
     */
    protected void setActiveProjectModified(boolean activeProjectModified)
    {
        boolean oldModifiedValue = this.activeProjectModified;
        this.activeProjectModified = activeProjectModified;
        this.propertyChangeSupport.firePropertyChange(
                ACTIVE_PROJECT_MODIFIED_PROPERTY_NAME,
                oldModifiedValue,
                this.activeProjectModified);
        this.fireProjectChangeOccurred();
    }

    /**
     * Getter for the active project
     * @return
     *          the active project
     */
    public Project getActiveProject()
    {
        return this.activeProject;
    }
    
    /**
     * Update the active project property
     * @param activeProject
     *          the new active project
     */
    protected void setActiveProject(Project activeProject)
    {
        Project oldProject = this.activeProject;
        
        this.activeProject = activeProject;
        this.propertyChangeSupport.firePropertyChange(
                ACTIVE_PROJECT_PROPERTY_NAME,
                oldProject,
                this.activeProject);
        this.fireProjectChangeOccurred();
    }
    
    /**
     * Get the file that is associated with the active project. This is the
     * file that the project gets saved to.
     * @return
     *          the file
     */
    public File getActiveProjectFile()
    {
        return this.activeProjectFile;
    }
    
    /**
     * Updates the active project file... this is the file that the current
     * project gets saved to
     * @param activeProjectFile
     *          the file
     */
    protected void setActiveProjectFile(File activeProjectFile)
    {
        File oldProjectFile = this.activeProjectFile;
        this.activeProjectFile = activeProjectFile;
        this.propertyChangeSupport.firePropertyChange(
                ACTIVE_PROJECT_FILE_PROPERTY_NAME,
                oldProjectFile,
                this.activeProjectFile);
        this.fireProjectChangeOccurred();
    }
    
    /**
     * Tells this project manager that edits have been made to the current
     * project
     */
    public void notifyActiveProjectModified()
    {
        this.setActiveProjectModified(true);
    }

    /**
     * Refresh all of the project data structures
     */
    public abstract void refreshProjectDataStructures();

    /**
     * Create a brand new active project
     * @return
     *          the newly created project
     */
    public abstract Project createNewActiveProject();

    /**
     * Save the active project to the given file
     * @param projectFile
     *          the file to save to
     * @return
     *          true iff the save succeeds
     */
    public abstract boolean saveActiveProject(File projectFile);
    
    /**
     * Load the active project from the given file/directory
     * @param projectFile
     *          the project file/directory
     * @return
     *          true iff the load is successful
     */
    public abstract boolean loadActiveProject(File projectFile);
}
