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

package org.jax.util.gui.desktoporganization;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A desktop pane with some extra functionality
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class Desktop extends JDesktopPane
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 7748680870393889301L;
    
    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            Desktop.class.getName());
    
    private static final int DEFAULT_INTERNAL_WIDTH = 800;
    private static final int DEFAULT_INTERNAL_HEIGHT = 800;
    
    private final JMenu windowMenu;
    private Action closeAllAction = new AbstractAction("Close All Windows")
    {
        /**
         * every {@link java.io.Serializable} is supposed to have one of these
         */
        private static final long serialVersionUID = -4310407503285994380L;

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent e)
        {
            Desktop.this.closeAllWindows();
        }
    };
    
    private final Stack<InternalFrame> internalFrameStack =
        new Stack<InternalFrame>();

    /**
     * Constructor
     */
    public Desktop()
    {
        super();
        this.windowMenu = new JMenu("Window");
        this.windowMenu.add(this.closeAllAction);
        this.windowMenu.addSeparator();

        final MenuSelectionManager menuSelectionManager =
            MenuSelectionManager.defaultManager();
        menuSelectionManager.addChangeListener(new ChangeListener()
        {
            /**
             * {@inheritDoc}
             */
            public void stateChanged(ChangeEvent e)
            {
                MenuElement[] selectedPath = menuSelectionManager
                        .getSelectedPath();

                if(selectedPath.length > 0
                        && selectedPath[selectedPath.length - 1] == Desktop.this.windowMenu)
                {
                    Desktop.this.closeAllAction.setEnabled(Desktop.this
                            .getAllFrames().length >= 1);
                }
            }
        });
    }

    /**
     * Getter for the window menu
     * 
     * @return the window menu
     */
    public JMenu getWindowMenu()
    {
        return this.windowMenu;
    }

    /**
     * Create a new internal frame holding the given component
     * @param component
     *        the component to contain
     * @param title
     *        the title to use
     * @param icon
     *        the icon
     * @param id
     *        the ID to use
     * @return the internal frame that was created
     */
    public InternalFrame createInternalFrame(
            Component component,
            String title,
            ImageIcon icon,
            String id)
    {
        // create new WindowMenuItem
        WindowMenuItem newItem = new WindowMenuItem(title, this.windowMenu);
        // create an internalFrame connected with this WindowMenuItem
        InternalFrame internalFrame = new InternalFrame(
                this.internalFrameStack,
                component,
                title,
                icon,
                newItem,
                id);

        Dimension newIframeDimensions = new Dimension(
                Math.min(DEFAULT_INTERNAL_WIDTH, this.getWidth()),
                Math.min(DEFAULT_INTERNAL_HEIGHT, this.getHeight()));
        internalFrame.setSize(newIframeDimensions);
        add(internalFrame);
        // connect this Window menuItem with the internalFrame
        newItem.setInternalFrame(internalFrame);
        internalFrame.show();

        // select the frame
        try
        {
            internalFrame.setSelected(true);
        }
        catch(Exception ex)
        {
            LOG.log(Level.WARNING,
                    "failed to select new internal frame",
                    ex);
        }

        return internalFrame;
    }

    /**
     * Select the frame with the given ID
     * 
     * @param iframeid
     *        the ID to select
     * @return true if we found and selected the frame
     */
    public boolean setIframeSelected(String iframeid)
    {
        boolean foundIframe = false;
        JInternalFrame[] allFrames = getAllFrames();
        int numFrames = allFrames.length;
        for(int i = 0; i < numFrames; i++)
        {
            InternalFrame internalFrame = (InternalFrame)allFrames[i];
            if(internalFrame.getIframeid().equals(iframeid))
            {
                try
                {
                    internalFrame.setSelected(true);
                }
                catch(Exception ex)
                {
                    LOG.log(Level.WARNING,
                            "failed to select internal frame",
                            ex);
                }
                foundIframe = true;
                break;
            }
        }
        return foundIframe;
    }

    /**
     * Close all of the windows in this desktop
     */
    public void closeAllWindows()
    {
        JInternalFrame[] allInternalFrames = getAllFrames();
        int totalFrames = allInternalFrames.length;
        // close all frames
        for(int i = 0; i < totalFrames; i++)
        {
            ((InternalFrame)allInternalFrames[i]).closeMe();
        }
    }
}
