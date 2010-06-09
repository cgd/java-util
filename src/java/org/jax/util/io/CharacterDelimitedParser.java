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

package org.jax.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.jax.util.TextWrapper;

/**
 * A utility for parsing character delimited files line by line.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
// TODO this should be capable of reading any RFC 4180 files as described
//      by http://en.wikipedia.org/wiki/Comma-separated_values
public class CharacterDelimitedParser implements Serializable
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 5466100908201662541L;

    /**
     * the separator character
     */
    public static final char DEFAULT_DELIMITER_CHAR = ',';
    
    /**
     * the character that starts a comment line
     */
    public static final char DEFAULT_COMMENT_CHAR = '#';
    
    private final char commentChar;
    
    private final char delimiterChar;
    
    private final int commentLineWrapBoundary;
    
    /**
     * Constructor
     */
    public CharacterDelimitedParser()
    {
        this(DEFAULT_DELIMITER_CHAR);
    }
    
    /**
     * Constructor
     * @param delimiterChar
     *          the delimiter to use
     */
    public CharacterDelimitedParser(char delimiterChar)
    {
        this(delimiterChar, DEFAULT_COMMENT_CHAR);
    }
    
    /**
     * Constructor
     * @param delimiterChar
     *          the delimiter character to use
     * @param commentChar
     *          the comment character to use
     */
    public CharacterDelimitedParser(
            char delimiterChar,
            char commentChar)
    {
        this(delimiterChar, commentChar, TextWrapper.DEFAULT_DIALOG_COLUMN_COUNT);
    }
    
    /**
     * Constructor
     * @param delimiterChar
     *          the delimiter character to use
     * @param commentChar
     *          the comment character to use
     * @param commentLineWrapBoundary
     *          the number of characters to allow on a comment line before
     *          wrapping the line (use -1 to indicate no wrapping)
     */
    public CharacterDelimitedParser(
            char delimiterChar,
            char commentChar,
            int commentLineWrapBoundary)
    {
        this.commentChar = commentChar;
        this.delimiterChar = delimiterChar;
        this.commentLineWrapBoundary = commentLineWrapBoundary;
    }
    
    /**
     * @return the commentLineWrapBoundary
     */
    public int getCommentLineWrapBoundary()
    {
        return this.commentLineWrapBoundary;
    }
    
    /**
     * Getter for the delimiter character being used
     * @return the delimiterChar
     */
    public char getDelimiterChar()
    {
        return this.delimiterChar;
    }

    /**
     * Getter for the comment character being used
     * @return the commentChar
     */
    public char getCommentChar()
    {
        return this.commentChar;
    }
    
    /**
     * Determine if the given line is a comment or is empty
     * @param line
     *          the line to test
     * @return
     *          true iff it's an empty (whitespace-only) line or if
     *          it's a comment (starts with {@link #getCommentChar()})
     */
    public boolean isCommentOrEmpty(String line)
    {
        line = line.trim();
        
        return line.length() == 0 || line.charAt(0) == this.commentChar;
    }
    
    /**
     * Read a line and parse it into an array of strings. Also uses
     * {@link java.lang.String#trim()} on all strings so that there's no leading or
     * trailing white space
     * @param reader
     *          the stream that we're reading from
     * @return
     *          the array of strings or null if we've reached the end of the
     *          stream
     * @throws IOException
     *          if we get an {@link java.io.IOException} from the reader
     */
    public String[] parseCharacterDelimitedLine(BufferedReader reader) throws IOException
    {
        String remainingLine = reader.readLine();
        return this.parseCharacterDelimitedLine(remainingLine);
    }
    
    /**
     * Take a line and parse it into an array of strings. Also uses
     * {@link java.lang.String#trim()} on all strings so that there's no leading or
     * trailing white space
     * @param line
     *          the line to parse
     * @return
     *          the array of strings
     * @throws IOException
     *          if we get an {@link java.io.IOException} from the reader
     */
    public String[] parseCharacterDelimitedLine(String line) throws IOException
    {
        if(line == null)
        {
            return null;
        }
        else
        {
            ArrayList<String> separatedStrings = new ArrayList<String>();
            
            // parse all of the tokens... include empty strings too
            for(int nextSeparatorIndex = line.indexOf(this.delimiterChar);
                nextSeparatorIndex != -1;
                nextSeparatorIndex = line.indexOf(this.delimiterChar))
            {
                String currSubString = line.substring(0, nextSeparatorIndex).trim();
                separatedStrings.add(currSubString);
                line = line.substring(nextSeparatorIndex + 1);
            }
            
            // ok... now we want the last string
            separatedStrings.add(line);
            
            return separatedStrings.toArray(new String[separatedStrings.size()]);
        }
    }
    
    /**
     * Create a character delimited line from the input tokens
     * @param lineTokens
     *          the tokens to turn into a line
     * @return
     *          the line
     */
    public String createCharacterDelimitedRow(String[] lineTokens)
    {
        StringBuffer sb = new StringBuffer();
        
        if(lineTokens.length > 0)
        {
            sb.append(lineTokens[0]);
            for(int i = 1; i < lineTokens.length; i++)
            {
                sb.append(this.delimiterChar);
                sb.append(lineTokens[i]);
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Write the character delimited row to an output stream
     * @param lineTokens
     *          the line to write
     * @param printStream
     *          the stream to write to
     */
    public void writeCharacterDelimitedRow(
            String[] lineTokens,
            PrintStream printStream)
    {
        if(lineTokens.length > 0)
        {
            printStream.print(lineTokens[0]);
            for(int i = 1; i < lineTokens.length; i++)
            {
                printStream.print(this.delimiterChar);
                printStream.print(lineTokens[i]);
            }
        }
        printStream.println();
    }
    
    /**
     * Write the given header comment to the given print stream. The comment
     * character prefix will be added to every new line
     * @param plainHeaderCommentText
     *          the text without any comment characters (text can contain
     *          newlines)
     * @param printStream
     *          the stream to write to
     */
    public void writeHeaderComment(
            String plainHeaderCommentText,
            PrintStream printStream)
    {
        // write out the header comment
        String[] wrappedHeaderComment = TextWrapper.wrapText(
                plainHeaderCommentText,
                this.commentLineWrapBoundary);
        for(String headerCommentLine: wrappedHeaderComment)
        {
            printStream.print(this.commentChar);
            printStream.println(headerCommentLine);
        }
    }
}
