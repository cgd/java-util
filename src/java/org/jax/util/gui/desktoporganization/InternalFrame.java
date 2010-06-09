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
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * The internal frame that works with the rest of the desktop organization
 * classes
 */
public class InternalFrame extends JInternalFrame implements InternalFrameListener
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 5640209117369777996L;
    
    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            InternalFrame.class.getName());
    
    // for positioning the internal windows
    private static int nextFrameX;
    private static int nextFrameY;
    
    private int frameDistance = 20;

    WindowMenuItem menuItem;
    Component content;

    String iframeid;

    private final Stack<InternalFrame> internalFrameStack;

    /**
     * Constructor
     * @param internalFrameStack
     *          the stack of internal frames in their layered order
     * @param component
     *          Component JPanel
     * @param title
     *          String title
     * @param icon
     *          ImageIcon
     * @param windowMenuItem
     *          WindowMenuItem corresponding menu item
     * @param id
     *          String InternalFrame id
     */
    public InternalFrame(
            Stack<InternalFrame> internalFrameStack,
            Component component,
            String title,
            ImageIcon icon,
            WindowMenuItem windowMenuItem,
            String id)
    {
        super(title, true, true, true, true);
        this.internalFrameStack = internalFrameStack;
        getContentPane().add(component);
        setFrameIcon(icon);
        addInternalFrameListener(this);
        this.menuItem = windowMenuItem;
        this.content = component;
        this.iframeid = id;
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        // position frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int desktopWidth = (int)(screenSize.width * 0.9);
        int desktopHeight = (int)(screenSize.height * 0.9);
        int width = (int)(desktopWidth * 0.6);
        int height = (int)(desktopHeight * 0.6);

        reshape(nextFrameX, nextFrameY, width, height);

        setVisible(true);
        toFront();

        try
        {
            this.setSelected(true);
        }
        catch(PropertyVetoException ex)
        {
            LOG.log(Level.WARNING,
                    "failed to select internal frame",
                    ex);
        }

        // compute placement for next frame
        nextFrameX += this.frameDistance;
        nextFrameY += this.frameDistance;
        if(nextFrameX + width > desktopWidth)
            nextFrameX = 0;
        if(nextFrameY + height > desktopHeight)
            nextFrameY = 0;

        pack();
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameClosing(InternalFrameEvent e)
    {
        this.closeMe();
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameClosed(InternalFrameEvent e)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameOpened(InternalFrameEvent e)
    {
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameIconified(InternalFrameEvent e)
    {
        this.internalFrameStack.remove(this);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeiconified(InternalFrameEvent e)
    {
        synchronized(this.internalFrameStack)
        {
            this.internalFrameStack.remove(this);
            this.internalFrameStack.push(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameActivated(InternalFrameEvent e)
    {
        activateFrame();
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeactivated(InternalFrameEvent e)
    {
        this.menuItem.setSelected(false);
        try
        {
            this.setSelected(false);
        }
        catch(PropertyVetoException ex)
        {
            LOG.log(Level.WARNING,
                    "selection change vetoed",
                    ex);
        }
    }

    /**
     * close this internal frame
     */
    public void closeMe()
    {
        // pop the next window to the top
        synchronized(this.internalFrameStack)
        {
            this.internalFrameStack.remove(this);
            if(!this.internalFrameStack.isEmpty())
            {
                InternalFrame nextFrame = this.internalFrameStack.pop();
                try
                {
                    nextFrame.setSelected(true);
                }
                catch(PropertyVetoException ex)
                {
                    LOG.log(Level.WARNING,
                            "selection change vetoed",
                            ex);
                }
            }
        }
        
        this.dispose();
        this.menuItem.removeMe();
    }

    private void activateFrame()
    {
        this.menuItem.addMe();
        this.menuItem.setSelected(true);

        try
        {
            this.setSelected(true);
        }
        catch(PropertyVetoException ex)
        {
            LOG.log(Level.WARNING,
                    "selection change vetoed",
                    ex);
        }
    }

    /**
     * Getter for the id
     * @return
     *          the ID
     */
    public String getIframeid()
    {
        return this.iframeid;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Desktop getDesktopPane()
    {
        return (Desktop)super.getDesktopPane();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelected(boolean selected) throws PropertyVetoException
    {
        if(selected)
        {
            synchronized(this.internalFrameStack)
            {
                this.internalFrameStack.remove(this);
                this.internalFrameStack.push(this);
            }
        }
        super.setSelected(selected);
    }
}
