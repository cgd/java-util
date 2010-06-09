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

package org.jax.util.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

/**
 * An action for doing a "safe" object deletion
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class SafeDeleteAction extends AbstractAction
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -3745553212118278034L;
    
    /**
     * Allows caller to specify what kind of deletion message the user should
     * see
     * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
     */
    public enum DeleteMessage
    {
        /**
         * no need to warn the user
         */
        NO_MESSAGE,
        
        /**
         * tell them you can't undo
         */
        CANT_UNDO_WARNING,
        
        /**
         * tell them it's a recursive delete that you can't undo
         */
        CANT_UNDO_RECURSIVE_WARNING
    }

    private static final String DELETE_ICON_RESOURCE =
        "/images/action/delete-16x16.png";
    
    private final boolean cancelIfWindowsAreOpen;
    
    private final JDesktopPane desktopPane;

    private final Component parentComponent;

    private final String dataObjectName;

    private final DeleteMessage message;

    /**
     * Constructor
     * @param dataObjectName
     *          the data to delete
     * @param parentComponent
     *          the parent component to use for dialogs
     * @param desktopPane
     *          the desktop pane that we check for open internal windows
     */
    public SafeDeleteAction(
            String dataObjectName,
            Component parentComponent,
            JDesktopPane desktopPane)
    {
        this(dataObjectName,
             parentComponent,
             desktopPane,
             true,
             DeleteMessage.CANT_UNDO_WARNING);
    }
    
    /**
     * Constructor
     * @param dataObjectName
     *          the data to delete
     * @param parentComponent
     *          the parent component to use for dialogs
     * @param desktopPane
     *          the desktop pane that we check for open internal windows
     * @param cancelIfWindowsAreOpen
     *          if true we won't delete unless windows are closed
     * @param message
     *          what should we tell the user about the delete
     */
    public SafeDeleteAction(
            String dataObjectName,
            Component parentComponent,
            JDesktopPane desktopPane,
            boolean cancelIfWindowsAreOpen,
            DeleteMessage message)
    {
        super("Delete " + dataObjectName,
              new ImageIcon(SafeDeleteAction.class.getResource(
                      DELETE_ICON_RESOURCE)));
        
        this.dataObjectName = dataObjectName;
        this.cancelIfWindowsAreOpen = cancelIfWindowsAreOpen;
        this.message = message;
        this.desktopPane = desktopPane;
        this.parentComponent = parentComponent;
    }
    
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e)
    {
        if(this.cancelIfWindowsAreOpen && this.desktopPane.getComponentCount() != 0)
        {
            MessageDialogUtilities.warn(
                    this.parentComponent,
                    "Please close all of the internal panels before " +
                    "deleting " + this.dataObjectName + ".",
                    "Close Internal Panels");
        }
        else
        {
            switch(this.message)
            {
                case NO_MESSAGE:
                {
                    this.delete();
                }
                break;
                
                case CANT_UNDO_WARNING:
                {
                    boolean okToDelete = MessageDialogUtilities.ask(
                            this.parentComponent,
                            "Deleting " + this.dataObjectName + " cannot be undone. " +
                            "Would you like to continue anyway?",
                            "Confirm Delete");
                    if(okToDelete)
                    {
                        this.delete();
                    }
                }
                break;
                
                case CANT_UNDO_RECURSIVE_WARNING:
                {
                    boolean okToDelete = MessageDialogUtilities.ask(
                            this.parentComponent,
                            "Deleting " + this.dataObjectName +
                            " and its children cannot be undone. " +
                            "Would you like to continue anyway?",
                            "Confirm Delete");
                    if(okToDelete)
                    {
                        this.delete();
                    }
                }
                break;
            }
        }
    }

    /**
     * Perform the actual deletion
     */
    public abstract void delete();
}
