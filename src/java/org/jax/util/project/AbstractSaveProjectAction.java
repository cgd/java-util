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

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.jax.util.TextWrapper;
import org.jax.util.project.gui.CreateOrRenameProjectDialog;

/**
 * An abstract class that takes care of some of the work of saving a project
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class AbstractSaveProjectAction extends AbstractAction
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -8180834095444920367L;
    
    private static final Logger LOG = Logger.getLogger(
            AbstractSaveProjectAction.class.getName());

    /**
     * Constructor
     * @param name
     *          the action name
     * @param icon
     *          the action icon
     */
    public AbstractSaveProjectAction(String name, Icon icon)
    {
        super(name, icon);
    }
    
    /**
     * Constructor
     * @param name
     *          the action name
     */
    public AbstractSaveProjectAction(String name)
    {
        super(name);
    }
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        this.actionPerformed();
    }
    
    /**
     * This is the same as {@link #actionPerformed(ActionEvent)} (the event
     * parameter is ignored in that function)
     */
    public void actionPerformed()
    {
        ProjectManager projectManager = this.getProjectManager();
        Project activeProject = projectManager.getActiveProject();
        if(activeProject.getName() == null)
        {
            // call save only after the project has been named
            CreateOrRenameProjectDialog projectRenameDialog =
                new CreateOrRenameProjectDialog(
                        this.getParentFrame(),
                        projectManager,
                        activeProject);
            projectRenameDialog.addWindowListener(
                    new WindowAdapter()
                    {
                        /**
                         * {@inheritDoc}
                         */
                        @Override
                        public void windowClosed(WindowEvent e)
                        {
                            AbstractSaveProjectAction.this.renameDialogClosed();
                        }
                    });
            projectRenameDialog.pack();
            projectRenameDialog.setVisible(true);
        }
        else
        {
            // since the project is already named, we're OK to call save
            // project directly
            this.performSave();
        }
    }

    /**
     * respond to a rename dialog close event
     */
    private void renameDialogClosed()
    {
        // before we perform save, we should see if the user canceled the
        // rename
        ProjectManager projectManager = this.getProjectManager();
        Project activeProject = projectManager.getActiveProject();
        if(activeProject.getName() == null)
        {
            // they canceled
            String message =
                "Cannot save a project without a project name";
            LOG.info(message);
            
            JOptionPane.showMessageDialog(
                    this.getParentFrame(),
                    TextWrapper.wrapText(
                            message,
                            TextWrapper.DEFAULT_DIALOG_COLUMN_COUNT),
                    "No Project Name",
                    JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            this.performSave();
        }
    }
    
    /**
     * Getter for the parent component that should be used for any
     * message dialogs that we need to show
     * @return  the parent component
     */
    protected abstract Frame getParentFrame();

    /**
     * Getter for the project manager that should be used
     * @return  the project manager
     */
    protected abstract ProjectManager getProjectManager();

    /**
     * Do a save or a save as...
     */
    protected abstract void performSave();
}
