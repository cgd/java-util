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

import java.awt.Dimension;
import java.awt.geom.Dimension2D;

/**
 * Like {@link Dimension} except this class holds double precision reals
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class Dimension2DDouble extends Dimension2D
{
    private double height;
    
    private double width;
    
    /**
     * Constructor
     */
    public Dimension2DDouble()
    {
    }
    
    /**
     * Constructor
     * @param width
     *          the width
     * @param height
     *          the height
     */
    public Dimension2DDouble(double width, double height)
    {
        this.width = width;
        this.height = height;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double getHeight()
    {
        return this.height;
    }
    
    /**
     * Setter for the height
     * @param height the height to set
     */
    public void setHeight(double height)
    {
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getWidth()
    {
        return this.width;
    }
    
    /**
     * Setter for the width
     * @param width the width to set
     */
    public void setWidth(double width)
    {
        this.width = width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(double width, double height)
    {
        this.width = width;
        this.height = height;
    }
}
