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

package org.jax.util.gui.desktoporganization;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;

/**
 * Represents a single internal frame (window)
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class WindowMenuItem extends JCheckBoxMenuItem
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -1516540449468433599L;

    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            WindowMenuItem.class.getName());
    
    JInternalFrame internalFrame;

    final JMenu parentMenu;

    /**
     * Constructor
     * @param text
     *          the text to use
     * @param parentMenu
     *          the main parent window for this menu item
     */
    public WindowMenuItem(
            String text,
            JMenu parentMenu)
    {
        setText(text);
        this.parentMenu = parentMenu;

        addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                // if the icon is iconified when select it, deiconified it
                try
                {
                    if(WindowMenuItem.this.internalFrame.isIcon())
                    {
                        WindowMenuItem.this.internalFrame.setIcon(false);
                    }
                }
                catch(Exception ex)
                {
                    LOG.log(Level.SEVERE,
                            "failed to de-iconify the internal frame",
                            ex);
                }

                // switch to the corresponding internal frame according to
                // selected menuItem
                try
                {
                    WindowMenuItem.this.internalFrame.setSelected(true);
                }
                catch(Exception ex)
                {
                    LOG.log(Level.SEVERE,
                            "failed to select internal frame",
                            ex);
                }
            }
        });
    }
    
    /**
     * Setter for the internal frame
     * @param internalFrame the internalFrame to set
     */
    public void setInternalFrame(JInternalFrame internalFrame)
    {
        this.internalFrame = internalFrame;
    }

    /**
     * Remove this menu
     */
    public void removeMe()
    {
        this.parentMenu.remove(this);
    }

    /**
     * Add self to parent menu
     */
    public void addMe()
    {
        boolean hasThisItem = false;
        int itemCount = this.parentMenu.getItemCount();
        for(int i = 0; i < itemCount; i++)
        {
            if(this.parentMenu.getItem(i) == this)
            {
                hasThisItem = true;
                this.setSelected(true);
                break;
            }
        }
        
        if(!hasThisItem)
        {
            this.parentMenu.add(this);
        }
    }
}
