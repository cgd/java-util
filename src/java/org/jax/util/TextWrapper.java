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

package org.jax.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Contains functionality for wrapping long lines of text to a given
 * margin.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class TextWrapper
{
    /**
     * The default column count to use for Dialog text, such as the text
     * shown in a {@link javax.swing.JOptionPane}
     */
    public static final int DEFAULT_DIALOG_COLUMN_COUNT = 80;

    /**
     * Private constructor. This class's functionality should be accessed
     * through its static functions.
     */
    private TextWrapper()
    {
    }
    
    /**
     * Wraps text at wrap column while keeping words intact. A "word" is any
     * contiguous string of characters that are not white space as
     * definded by {@link Character#isWhitespace(char)}
     * @param textToWrap
     *          the text for us to wrap
     * @param columnCount
     *          the number of columns to allow before wrapping. a negative
     *          value indicates that there is no column boundary and that we
     *          should only break up the text based on newline characters
     * @return
     *          the wrapped version of the input text
     */
    public static String[] wrapText(
            String textToWrap,
            int columnCount)
    {
        StringTokenizer newLineTok = new StringTokenizer(textToWrap, "\n");
        if(newLineTok.countTokens() > 1)
        {
            List<String> lines = new ArrayList<String>();
            while(newLineTok.hasMoreTokens())
            {
                String[] currLines =
                    wrapTextWithoutNewlines(newLineTok.nextToken(), columnCount);
                lines.addAll(Arrays.asList(currLines));
            }
            return lines.toArray(new String[lines.size()]);
        }
        else
        {
            return wrapTextWithoutNewlines(textToWrap, columnCount);
        }
    }
    
    /**
     * Like {@link #wrapText(String, int)} except that we assume there are
     * no embedded newlines.
     * @param textToWrap
     *          the text for us to wrap
     * @param columnCount
     *          the number of columns to allow before wrapping
     * @return
     *          the wrapped version of the input text
     */
    private static String[] wrapTextWithoutNewlines(
            String textToWrap,
            int columnCount)
    {
        if(columnCount <= 0)
        {
            // negative column count indicates that we should not do any
            // wrapping
            return new String[] {textToWrap};
        }
        else
        {
            List<String> lines = new ArrayList<String>();
            int currLeftMarginInclusiveCharIndex = 0;
            while(currLeftMarginInclusiveCharIndex < textToWrap.length())
            {
                // scan from the right margin toward the left margin looking for
                // white space
                int currWrapIndex = -1;
                int currRightMarginExclusiveCharIndex =
                    currLeftMarginInclusiveCharIndex + columnCount;
                if(currRightMarginExclusiveCharIndex < textToWrap.length())
                {
                    for(int cursor = currRightMarginExclusiveCharIndex - 1;
                        cursor >= currLeftMarginInclusiveCharIndex && currWrapIndex == -1;
                        cursor--)
                    {
                        if(Character.isWhitespace(textToWrap.charAt(cursor)))
                        {
                            // found next wrap index
                            currWrapIndex = cursor;
                        }
                    }
                    
                    if(currWrapIndex != -1)
                    {
                        // scanning backwards failed, we're going to have to keep
                        // scanning forward until we reach white space or run
                        // out of text
                        for(int cursor = currRightMarginExclusiveCharIndex;
                            cursor < textToWrap.length() && currWrapIndex == -1;
                            cursor++)
                        {
                            if(Character.isWhitespace(textToWrap.charAt(cursor)))
                            {
                                // found next wrap index
                                currWrapIndex = cursor;
                            }
                        }
                    }
                }
                
                if(currWrapIndex == -1)
                {
                    // just take what is left of the string, then we're done
                    lines.add(textToWrap.substring(
                            currLeftMarginInclusiveCharIndex));
                    currLeftMarginInclusiveCharIndex = textToWrap.length();
                }
                else
                {
                    lines.add(textToWrap.substring(
                            currLeftMarginInclusiveCharIndex,
                            currWrapIndex));
                    currLeftMarginInclusiveCharIndex = currWrapIndex + 1;
                }
            }
            
            return lines.toArray(new String[lines.size()]);
        }
    }
}
