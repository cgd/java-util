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

import java.io.File;
import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * A logger configuration that reads settings from a resource instead of reading
 * it from a file.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ResourceBasedLoggerConfiguration
{
    // TODO make this configurable
    /**
     * the properties resource to feed to the log manager
     */
    private static final String LOG_RESOURCE_NAME = "/logging.properties";
    
    /**
     * the directory that we log to
     */
    private static final String LOG_DIR = "logs";
    
    /**
     * Constructor
     */
    public ResourceBasedLoggerConfiguration()
    {
        // read the logging properties
        try
        {
            // create the application's logging dir if it doesn't yet exist
            ConfigurationUtilities configurationUtilities =
                new ConfigurationUtilities();
            File baseDir = configurationUtilities.getBaseDirectory();
            File logDir = new File(baseDir, LOG_DIR);
            logDir.mkdirs();
            
            // now pass on the configuration from the resource
            InputStream logPropsStream =
                this.getClass().getResourceAsStream(LOG_RESOURCE_NAME);
            LogManager.getLogManager().readConfiguration(logPropsStream);
        }
        catch(Exception ex)
        {
            // well, we can't really log this...
            System.err.println(
                    "caught an exception trying to initialize logging " +
                    "manager");
            ex.printStackTrace();
        }
    }
}
