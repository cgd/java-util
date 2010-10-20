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
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Abstract base class for {@link TableReader}s which takes care of implementing
 * the {@link #iterator()} function.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class AbstractTableReader implements TableReader
{
    /**
     * Get a handle on the row iterator
     * @return
     *          the iterator
     */
    public Iterator<String[]> iterator()
    {
        return new Iterator<String[]>()
        {
            private boolean eof = false;
            
            private Queue<String[]> nextQueue = new LinkedBlockingQueue<String[]>();
            
            /**
             * {@inheritDoc}
             */
            public boolean hasNext()
            {
                if(this.nextQueue.isEmpty())
                {
                    if(this.eof)
                    {
                        return false;
                    }
                    else
                    {
                        try
                        {
                            String[] nextRow = AbstractTableReader.this.readRow();
                            
                            if(nextRow == null)
                            {
                                this.eof = true;
                                return false;
                            }
                            else
                            {
                                this.nextQueue.add(nextRow);
                                return true;
                            }
                        }
                        catch(IOException ex)
                        {
                            throw new RuntimeException(ex);
                        }
                        catch(IllegalFormatException ex)
                        {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                else
                {
                    return true;
                }
            }

            /**
             * {@inheritDoc}
             */
            public String[] next()
            {
                if(this.nextQueue.isEmpty())
                {
                    if(this.eof)
                    {
                        throw new NoSuchElementException();
                    }
                    else
                    {
                        try
                        {
                            String[] nextRow = AbstractTableReader.this.readRow();
                            
                            if(nextRow == null)
                            {
                                this.eof = true;
                                throw new NoSuchElementException();
                            }
                            else
                            {
                                return nextRow;
                            }
                        }
                        catch(IOException ex)
                        {
                            throw new RuntimeException(ex);
                        }
                        catch(IllegalFormatException ex)
                        {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                else
                {
                    return this.nextQueue.remove();
                }
            }

            /**
             * {@inheritDoc}
             */
            public void remove()
            {
                throw new UnsupportedOperationException(
                        "Item removal not permitted");
            }
        };
    }
}
