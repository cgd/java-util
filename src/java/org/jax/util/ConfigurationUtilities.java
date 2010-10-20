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

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A utility class for discovering and initializing an application
 * configuration directory.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ConfigurationUtilities
{
    /**
     * Our Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ConfigurationUtilities.class.getName());
    
    /**
     * Holds value of property baseDirectory.
     */
    private File baseDirectory;
    
    /**
     * Holds the value of the application name property
     */
    private String applicationName;
    
    /**
     * Holds the value of the application version property
     */
    private String applicationVersion; 
    
    /**
     * The default location for our properties.
     */
    public static final String DEFAULT_PROPERTIES_RESOURCE =
        "/application.properties";

    /**
     * Creates a new instance of {@link ConfigurationUtilities} using
     * {@link #DEFAULT_PROPERTIES_RESOURCE} as the path to the properties
     * file.
     * @throws IOException
     *          if we get an {@link IOException} during initialization
     */
    public ConfigurationUtilities() throws IOException
    {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream(
                DEFAULT_PROPERTIES_RESOURCE));
        this.initialize(properties);
    }
    
    /**
     * Creates a new instance of {@link ConfigurationUtilities}.
     * @param properties    the properties that contain values for
     *                      application.name and application.version
     */
    public ConfigurationUtilities(Properties properties)
    {
        this.initialize(properties);
    }
    
    /**
     * Creates a new instance of {@link ConfigurationUtilities}
     * @param applicationName       the name of the application
     * @param applicationVersion    the version of the application
     */
    public ConfigurationUtilities(String applicationName, String applicationVersion)
    {
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.initializeFromApplicationNameAndVersion();
    }
    
    /**
     * Initialize using the given properties.
     * @param applicationProperties
     *          the properties we're using for initialization
     */
    private void initialize(Properties applicationProperties)
    {
        this.applicationName =
                applicationProperties.getProperty("application.name");
        this.applicationVersion =
                applicationProperties.getProperty("application.version");
        this.initializeFromApplicationNameAndVersion();
    }
    
    /**
     * Initialize the configuration.
     */
    private void initializeFromApplicationNameAndVersion()
    {
        if(this.applicationName == null)
        {
            throw new NullPointerException("applicationName cannot be null.");
        }
        
        String userHome = System.getProperty("user.home");
        
        this.baseDirectory = new File(userHome);
        this.baseDirectory = new File(
                this.baseDirectory,
                "." + this.applicationName);
        
        if(this.applicationVersion != null)
        {
            this.baseDirectory = new File(
                    this.baseDirectory,
                    this.applicationVersion);
        }
        
        LOG.fine("Using application configuration dir: " +
                this.baseDirectory.getAbsolutePath());
    }
    
    /**
     * Getter for property exists.
     * @return Value of property exists.
     */
    public boolean getExists()
    {
        return this.getBaseDirectory().exists();
    }

    /**
     * Getter for property baseDirectory.
     * @return Value of property baseDirectory.
     */
    public File getBaseDirectory()
    {
        return this.baseDirectory;
    }
    
    /**
     * Getter for the application name property
     * @return the application name
     */
    public String getApplicationName()
    {
        return this.applicationName;
    }
    
    /**
     * Getter for the application version property
     * @return the application version
     */
    public String getApplicationVersion()
    {
        return this.applicationVersion;
    }
}
