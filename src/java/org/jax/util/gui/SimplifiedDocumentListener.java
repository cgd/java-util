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

package org.jax.util.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A simplified version of document listener which is useful if you only
 * want to know something changed and don't care what kind of edit it was
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class SimplifiedDocumentListener implements DocumentListener
{
    /**
     * {@inheritDoc}
     */
    public void changedUpdate(DocumentEvent e)
    {
        this.anyUpdate(e);
    }

    /**
     * {@inheritDoc}
     */
    public void insertUpdate(DocumentEvent e)
    {
        this.anyUpdate(e);
    }

    /**
     * {@inheritDoc}
     */
    public void removeUpdate(DocumentEvent e)
    {
        this.anyUpdate(e);
    }
    
    /**
     * Respond to any of the update events
     * @param e
     *          the event
     */
    protected abstract void anyUpdate(DocumentEvent e);
}
