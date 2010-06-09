/*
 * Copyright (c) 2009 The Jackson Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining  a copy
 * of this software and associated documentation files (the  "Software"), to
 * deal in the Software without restriction, including  without limitation the
 * rights to use, copy, modify, merge, publish,  distribute, sublicense, and/or
 * sell copies of the Software, and to  permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be  included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,  EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF  MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY  CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE  SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.jax.util.gui;

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * A panel that includes a function for validating the data within the panel
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class ValidatablePanel extends JPanel implements Validatable
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -1329940933871830545L;

    /**
     * Same as {@link JPanel#JPanel()}
     */
    public ValidatablePanel()
    {
        super();
    }

    /**
     * Same as {@link JPanel#JPanel(LayoutManager)}
     * @param layout see {@link JPanel}
     */
    public ValidatablePanel(LayoutManager layout)
    {
        super(layout);
    }

    /**
     * Same as {@link JPanel#JPanel(boolean)}
     * @param isDoubleBuffered  see {@link JPanel}
     */
    public ValidatablePanel(boolean isDoubleBuffered)
    {
        super(isDoubleBuffered);
    }

    /**
     * Same as {@link JPanel#JPanel(LayoutManager, boolean)}
     * @param layout            see {@link JPanel}
     * @param isDoubleBuffered  see {@link JPanel}
     */
    public ValidatablePanel(LayoutManager layout, boolean isDoubleBuffered)
    {
        super(layout, isDoubleBuffered);
    }
    
    /**
     * Validate the data in the panel possibly alerts the user if a problem
     * is discovered
     * @return  true iff the data is valid
     */
    public abstract boolean validateData();
}
