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

package org.jax.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A very simple formatter
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SuperSimpleFormatter extends Formatter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String format(LogRecord record)
    {
        StringBuffer sb = new StringBuffer();
        if(record.getMessage() != null)
        {
            sb.append(record.getMessage());
            sb.append("\n");
        }
        
        if(record.getThrown() != null)
        {
            ByteArrayOutputStream bytesOut =
                new ByteArrayOutputStream();
            record.getThrown().printStackTrace(
                    new PrintStream(bytesOut));
            sb.append(new String(bytesOut.toByteArray()));
        }
        
        return sb.toString();
    }
}
