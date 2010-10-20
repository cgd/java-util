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

package org.jax.util.io;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * For reading character delimited files.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class FlatFileReader extends AbstractTableReader
{
    private static final String EMPTY_STRING = "";
    
    private CharSequence rowDelimiter;
    
    private final CharSequence[] rowDelimiterChoices;
    
    private final char fieldDelimiter;
    
    private final int quoteChar;
    
    private final int commentChar;
    
    private final Reader reader;
    
    private final ArrayList<String> fields = new ArrayList<String>();
    
    private final char[] charBuffer = new char[1024*4];
    
    private final StringBuilder fieldBuilder = new StringBuilder();

    private int charBufferLength = 0;
    
    private int charBufferCursor = 0;

    /**
     * enum for keeping track of the state that we're in from reading the file
     */
    private enum CursorMode {
        FIELD_START,
        PLAIN_FIELD,
        QUOTED_FIELD,
        QUOTE_IN_QUOTED_FIELD}
    
    /**
     * Convenience constructor for more commonly used formats.
     * @param reader
     *          the base reader
     * @param format
     *          the format to use
     */
    public FlatFileReader(Reader reader, FlatFileFormat format)
    {
        this(reader,
             format.getRowDelimiterChoices(),
             format.getFieldDelimiter(),
             format.getQuoteChar(),
             format.getCommentChar());
    }
    
    /**
     * Constructor
     * @param reader
     *          the base reader
     * @param rowDelimiterChoices
     *          Character sequence choices that separates lines. If more
     *          than one of these is given, the first one detected in the
     *          given reader is chosen as the delimiter. NOTE that this
     *          detection is based on the first character only, so the following
     *          combination of choices would not make any sense {"ab", "ac"}
     *          because the match is only done on the 'a' character.
     *          <br><br>
     *          If any character sequence in the given reader matches the start
     *          of this delimiter it must also match the entire delimiter or an
     *          {@link IOException} will be thrown during the {@link #readRow()}
     * @param fieldDelimiter
     *          character that separates fields
     * @param quoteChar
     *          character used to quote fields
     * @param commentChar 
     */
    public FlatFileReader(
            Reader reader,
            CharSequence[] rowDelimiterChoices,
            char fieldDelimiter,
            int quoteChar,
            int commentChar)
    {
        this.rowDelimiterChoices = rowDelimiterChoices;
        if(rowDelimiterChoices.length == 0)
        {
            throw new IllegalArgumentException(
                    "Must have at least one row delimiter");
        }
        else if(rowDelimiterChoices.length == 1)
        {
            this.rowDelimiter = rowDelimiterChoices[0];
        }
        
        this.commentChar = commentChar;
        
        this.fieldDelimiter = fieldDelimiter;
        this.quoteChar = quoteChar;
        this.reader = reader;
    }
    
    /**
     * Getter for the row delimiter. If only 1 row choice was given
     * in the constructor than the returned sequence is that string, else
     * the detected choice is returned (if none of the choices is detected
     * a null is returned)
     * @return the (possibly detected) row delimiter
     */
    public CharSequence getRowDelimiter()
    {
        return this.rowDelimiter;
    }
    
    /**
     * Getter for the field delimiter
     * @return the fieldDelimiter
     */
    public char getFieldDelimiter()
    {
        return this.fieldDelimiter;
    }
    
    /**
     * Get the quote character
     * @return the quoteChar
     */
    public int getQuoteChar()
    {
        return this.quoteChar;
    }
    
    /**
     * Getter for the comment character
     * @return the comment char
     */
    public int getCommentChar()
    {
        return this.commentChar;
    }
    
    /**
     * If the next row is a comment row read through it
     * @return
     *          true if it was a comment row
     * @throws IOException
     *          if we fail to read through
     * @throws IllegalFormatException
     *          if the formatting is bad
     */
    private boolean readCommentRow() throws IOException, IllegalFormatException
    {
        // read in the next chunk of characters if we have to
        if(this.charBufferCursor == this.charBufferLength)
        {
            this.charBufferLength = this.reader.read(this.charBuffer);
            this.charBufferCursor = 0;
            
            if(this.charBufferLength == -1)
            {
                return false;
            }
        }
        
        if(this.charBuffer[this.charBufferCursor] == this.getCommentChar())
        {
            this.charBufferCursor++;
            this.readThroughRowDelimiterOrEndOfFile();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Read through the next row delimiter or EOF... whichever comes first
     * @throws IOException
     *          if the IO fails
     * @throws IllegalFormatException
     *          if we find out that the file format is bad
     */
    private void readThroughRowDelimiterOrEndOfFile() throws IOException, IllegalFormatException
    {
        if(this.charBufferLength == -1)
        {
            return;
        }

        int rowDelimiterRelativeCursor = 0;
        while(true)
        {
            // read in the next chunk of characters if we have to
            if(this.charBufferCursor == this.charBufferLength)
            {
                this.charBufferLength = this.reader.read(this.charBuffer);
                this.charBufferCursor = 0;
                
                if(this.charBufferLength == -1)
                {
                    // we hit EOF... if we were reading a row delimiter
                    // this is an error
                    if(rowDelimiterRelativeCursor >= 1)
                    {
                        throw new IllegalFormatException(
                                "Incomplete row delimiter at end of file");
                    }
                    else
                    {
                        return;
                    }
                }
            }
            
            while(this.charBufferCursor < this.charBufferLength)
            {
                final char currChar = this.charBuffer[this.charBufferCursor];
                if(rowDelimiterRelativeCursor == 0)
                {
                    if(this.matchesRowDelimiterStart(currChar))
                    {
                        rowDelimiterRelativeCursor++;
                    }
                }
                else
                {
                    if(currChar == this.rowDelimiter.charAt(rowDelimiterRelativeCursor))
                    {
                        rowDelimiterRelativeCursor++;
                    }
                    else
                    {
                        throw new IllegalFormatException(
                        "Incomplete row delimiter");
                    }
                }
                
                // move the cursor forward
                this.charBufferCursor++;
                
                if(rowDelimiterRelativeCursor >= 1 && rowDelimiterRelativeCursor == this.rowDelimiter.length())
                {
                    return;
                }
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String[] readRow() throws IOException, IllegalFormatException
    {
        // skip over any comment rows
        while(this.readCommentRow())
        {
            // no-op
        }
        
        if(this.charBufferLength == -1)
        {
            // we're sitting at EOF. there's no data left
            return null;
        }
        else if(this.charBufferCursor == this.charBufferLength)
        {
            // try to read the next block of data into the buffer
            this.charBufferLength = this.reader.read(this.charBuffer);
            this.charBufferCursor = 0;
            
            if(this.charBufferLength == -1)
            {
                // we're sitting at EOF. there's no data left
                return null;
            }
        }

        CursorMode cursorMode = CursorMode.FIELD_START;
        boolean rowBreak = false;
        while(!rowBreak)
        {
            // read in the next chunk of characters if we have to
            if(this.charBufferCursor == this.charBufferLength)
            {
                this.charBufferLength = this.reader.read(this.charBuffer);
                this.charBufferCursor = 0;
                
                if(this.charBufferLength == -1)
                {
                    // we hit EOF... figure out what to do depending on
                    // the cursor mode
                    switch(cursorMode)
                    {
                        case FIELD_START:
                        {
                            // close out the field
                            this.fields.add(EMPTY_STRING);
                        }
                        break;
                        
                        case PLAIN_FIELD:
                        {
                            // close out the field
                            this.fields.add(this.fieldBuilder.toString());
                        }
                        break;
                        
                        case QUOTED_FIELD:
                        {
                            // bad format here
                            throw new IllegalFormatException(
                                    "Unclosed quoted field at end of file");
                        }
                        
                        case QUOTE_IN_QUOTED_FIELD:
                        {
                            // close out the quoted field
                            this.fields.add(this.fieldBuilder.toString());
                        }
                        break;
                    }
                    
                    // we made it through w/o an exception so we can treat
                    // the EOF as a row break
                    rowBreak = true;
                }
            }
            
            while(!rowBreak && this.charBufferCursor < this.charBufferLength)
            {
                boolean incrementCursor = true;
                
                final char currChar = this.charBuffer[this.charBufferCursor];
                switch(cursorMode)
                {
                    case FIELD_START:
                    {
                        assert this.fieldBuilder.length() == 0;
                        
                        if(currChar == this.quoteChar)
                        {
                            // we're entering a quoted field
                            cursorMode = CursorMode.QUOTED_FIELD;
                        }
                        else if(currChar == this.fieldDelimiter)
                        {
                            // This field is empty, stay in field start mode
                            this.fields.add(EMPTY_STRING);
                        }
                        else if(this.matchesRowDelimiterStart(currChar))
                        {
                            // This field is empty, enter row delimiter mode
                            this.fields.add(EMPTY_STRING);
                            
                            if(this.rowDelimiter.length() >= 2)
                            {
                                this.readThroughRowDelimiterOrEndOfFile();
                                
                                // we don't need to increment the cursor since
                                // the read through took care of that for us
                                incrementCursor = false;
                            }
                            
                            cursorMode = CursorMode.FIELD_START;
                            rowBreak = true;
                        }
                        else
                        {
                            // this is a plain (unquoted) field
                            this.fieldBuilder.append(currChar);
                            cursorMode = CursorMode.PLAIN_FIELD;
                        }
                    }
                    break;
                    
                    case PLAIN_FIELD:
                    {
                        if(currChar == this.quoteChar)
                        {
                            throw new IllegalFormatException(
                                    "Illegal quote found in unquoted field.");
                        }
                        else if(currChar == this.fieldDelimiter)
                        {
                            // close out this field and start a new one
                            this.fields.add(this.fieldBuilder.toString());
                            this.fieldBuilder.setLength(0);
                            cursorMode = CursorMode.FIELD_START;
                        }
                        else if(this.matchesRowDelimiterStart(currChar))
                        {
                            // close out this field and go into row delimiter
                            // mode (assuming the row delimiter is more
                            // than one char)
                            this.fields.add(this.fieldBuilder.toString());
                            this.fieldBuilder.setLength(0);
                            
                            if(this.rowDelimiter.length() >= 2)
                            {
                                this.readThroughRowDelimiterOrEndOfFile();
                                
                                // we don't need to increment the cursor since
                                // the read through took care of that for us
                                incrementCursor = false;
                            }
                            
                            cursorMode = CursorMode.FIELD_START;
                            rowBreak = true;
                        }
                        else
                        {
                            // it's just a part of the field
                            this.fieldBuilder.append(currChar);
                        }
                    }
                    break;
                    
                    case QUOTED_FIELD:
                    {
                        if(currChar == this.quoteChar)
                        {
                            cursorMode = CursorMode.QUOTE_IN_QUOTED_FIELD;
                        }
                        else
                        {
                            this.fieldBuilder.append(currChar);
                        }
                    }
                    break;
                    
                    case QUOTE_IN_QUOTED_FIELD:
                    {
                        if(currChar == this.quoteChar)
                        {
                            // this is an escaped quote character. go back
                            // to quoted field mode
                            this.fieldBuilder.append(currChar);
                            cursorMode = CursorMode.QUOTED_FIELD;
                        }
                        else if(currChar == this.fieldDelimiter)
                        {
                            // close out this field and start another
                            this.fields.add(this.fieldBuilder.toString());
                            this.fieldBuilder.setLength(0);
                            cursorMode = CursorMode.FIELD_START;
                        }
                        else if(this.matchesRowDelimiterStart(currChar))
                        {
                            // close out this field and go into row delimiter
                            // mode (assuming the row delimiter is more
                            // than one char)
                            this.fields.add(this.fieldBuilder.toString());
                            this.fieldBuilder.setLength(0);
                            
                            if(this.rowDelimiter.length() >= 2)
                            {
                                this.readThroughRowDelimiterOrEndOfFile();
                                
                                // we don't need to increment the cursor since
                                // the read through took care of that for us
                                incrementCursor = false;
                            }
                            
                            cursorMode = CursorMode.FIELD_START;
                            rowBreak = true;
                        }
                    }
                    break;
                    
                    default:
                    {
                        throw new IllegalStateException(
                                "Internal error. We should never reach " +
                                "this exception.");
                    }
                }
                
                if(incrementCursor)
                {
                    // move the cursor forward
                    this.charBufferCursor++;
                }
            }
        }
        
        String[] returnVal = this.fields.toArray(new String[this.fields.size()]);
        this.fields.clear();
        return returnVal;
    }
    
    /**
     * Determine if the given character matches our row delimiter. If the
     * row delimiter choice has not yet been selected, this method
     * attempts to detect the correct delimiter
     * @param charToTest
     *          the character we're trying to match against a delimiter starting
     *          character
     * @return
     *          true iff the match is successful
     */
    private boolean matchesRowDelimiterStart(char charToTest)
    {
        if(this.rowDelimiter != null)
        {
            return this.rowDelimiter.charAt(0) == charToTest;
        }
        else
        {
            // try to detect one of the row delimiter choices
            for(CharSequence rowDelimiterChoice: this.rowDelimiterChoices)
            {
                if(rowDelimiterChoice.charAt(0) == charToTest)
                {
                    this.rowDelimiter = rowDelimiterChoice;
                    return true;
                }
            }
            
            // no match found
            return false;
        }
    }

    /**
     * calls {@link Reader#close()} on the underlying reader
     * @throws IOException 
     *          if the reader throws an exception
     */
    public void close() throws IOException
    {
        this.reader.close();
    }
}
