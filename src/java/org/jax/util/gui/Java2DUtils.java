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

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 * Utility functions related to Java2D
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class Java2DUtils
{
    /**
     * Creates a multiline text outline shape using a center alignment
     * @param textLines
     *          the text to turn into a shape
     * @param font
     *          the font to use
     * @param frc
     *          the rendering context to use
     * @return
     *          the text outline shape
     */
    public static Shape createCenteredMultilineTextShape(
            String[] textLines,
            Font font,
            FontRenderContext frc)
    {
        double totalHeight = 0;
        GlyphVector[] vectors = new GlyphVector[textLines.length];
        for(int i = 0; i < textLines.length; i++)
        {
            vectors[i] = font.createGlyphVector(frc, textLines[i]);
            
            Rectangle2D currBounds = vectors[i].getLogicalBounds();
            totalHeight += currBounds.getHeight();
        }
        
        // y starts negative and increases because java2d is wacky and likes to
        // define y=0 as the top of the screen
        GeneralPath multilineShape = new GeneralPath();
        double currY = -(totalHeight / 2.0);
        for(int i = 0; i < vectors.length; i++)
        {
            GlyphVector currVector = vectors[i];
            Rectangle2D currBounds = currVector.getLogicalBounds();
            currY += currBounds.getHeight();
            double currX = -(currBounds.getWidth() / 2.0);
            
            Shape currShape = currVector.getOutline((float)currX, (float)currY);
            multilineShape.append(currShape, false);
        }
        
        return multilineShape;
    }
}
