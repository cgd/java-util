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

package org.jax.util.project.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import org.jax.util.gui.InteractiveTree;
import org.jax.util.project.Project;
import org.jax.util.project.ProjectChangeListener;
import org.jax.util.project.ProjectManager;

/**
 * An interactive tree with some project functionality included
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class ProjectTree extends InteractiveTree
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 1311505098334995285L;

    private volatile ProjectManager projectManager;
    
    private volatile Project activeProject;
    
    private final ProjectChangeListener projectChangeListener = new ProjectChangeListener()
    {
        public void projectChangeOccurred(final ProjectManager projectManager)
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    if(projectManager.getActiveProject() != ProjectTree.this.activeProject)
                    {
                        ProjectTree.this.setActiveProject(
                                projectManager.getActiveProject());
                    }
                    else
                    {
                        ProjectTree.this.refreshProjectTree();
                    }
                }
            });
        }
    };
    
    /**
     * listens to the active project for changes
     */
    private PropertyChangeListener activeProjectListener = new PropertyChangeListener()
    {
        public void propertyChange(PropertyChangeEvent evt)
        {
            ProjectTree.this.refreshProjectTree();
        }
    };

    /**
     * Set the project manager for this tree
     * @param projectManager the projectManager to set
     */
    public void setProjectManager(ProjectManager projectManager)
    {
        {
            ProjectManager currProjMgr = this.projectManager;
            if(currProjMgr != null)
            {
                currProjMgr.removeProjectChangeListener(
                        this.projectChangeListener);
            }
        }
        
        this.projectManager = projectManager;
        
        // null managers are not allowed, so we don't need a null check
        projectManager.addProjectChangeListener(this.projectChangeListener);
        this.setActiveProject(projectManager.getActiveProject());
    }
    
    /**
     * Getter for the active project
     * @return the active project
     */
    public Project getActiveProject()
    {
        return this.activeProject;
    }
    
    /**
     * Getter for the project manager
     * @return the projectManager
     */
    public ProjectManager getProjectManager()
    {
        return this.projectManager;
    }
    
    /**
     * Set the currently active project
     * @param activeProject
     *          the new active project
     */
    public void setActiveProject(Project activeProject)
    {
        {
            Project oldActiveProject = this.getActiveProject();
            if(oldActiveProject != null)
            {
                oldActiveProject.removePropertyChangeListener(
                        this.activeProjectListener);
            }
        }
        
        this.activeProject = activeProject;
        
        if(activeProject != null)
        {
            activeProject.addPropertyChangeListener(
                    this.activeProjectListener);
        }
        
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                ProjectTree.this.refreshProjectTree();
            }
        });
    }
    
    /**
     * Refresh the project tree
     */
    protected abstract void refreshProjectTree();
}
