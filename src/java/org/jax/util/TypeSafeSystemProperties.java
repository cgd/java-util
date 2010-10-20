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

/**
 * A collection of (relatively) type safe utility functions for reading and
 * manipulating system properties.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class TypeSafeSystemProperties
{
    /**
     * the standard OS name property name
     */
    public static final String OS_NAME_PROP_NAME = "os.name";
    
    /**
     * the java home property name
     */
    public static final String JAVA_HOME_PROP_NAME = "java.home";
    
    /**
     * the property name for the java library path
     */
    public static final String JAVA_LIB_PATH_PROP_NAME = "java.library.path";
    
    /**
     * the property name for the classpath
     */
    public static final String CLASS_PATH_PROP_NAME = "java.class.path";
    
    /**
     * the working directory
     */
    public static final String WORKING_DIRECTORY_PROP_NAME = "user.dir";
    
    /**
     * the temporary directory
     */
    public static final String TEMP_DIR = "java.io.tmpdir";
    
    /**
     * Enum values for representing different OS families
     */
    public enum OsFamily
    {
        /**
         * linux
         */
        LINUX_OS_FAMILY
        {
            private static final String FAMILY_NAME_PREFIX = "Linux";
            
            /**
             * {@inheritDoc}
             */
            @Override
            public String getUniqueFamilyNamePrefix()
            {
                return FAMILY_NAME_PREFIX;
            }
        },
        
        /**
         * mac os and mac os x
         */
        MAC_OS_FAMILY
        {
            private static final String FAMILY_NAME_PREFIX = "Mac";
            
            /**
             * {@inheritDoc}
             */
            @Override
            public String getUniqueFamilyNamePrefix()
            {
                return FAMILY_NAME_PREFIX;
            }
        },
        
        /**
         * windows
         */
        WINDOWS_OS_FAMILY
        {
            private static final String FAMILY_NAME_PREFIX = "Windows";
            
            /**
             * {@inheritDoc}
             */
            @Override
            public String getUniqueFamilyNamePrefix()
            {
                return FAMILY_NAME_PREFIX;
            }
        };
        
        /**
         * Get an {@value TypeSafeSystemProperties#OS_NAME_PROP_NAME} prefix
         * which is unique to this OS family.
         * @return
         *          the unique prefix
         */
        public abstract String getUniqueFamilyNamePrefix();
    }
    
    /**
     * Get the OS family that the active JVM is running in.
     * @return
     *          the OS family, or null if we don't know
     */
    public static OsFamily getOsFamily()
    {
        String osName = getOsName();
        for(OsFamily currOsFamily: OsFamily.values())
        {
            if(osName.startsWith(currOsFamily.getUniqueFamilyNamePrefix()))
            {
                // found it!
                return currOsFamily;
            }
        }
        
        // we couldn't find the right family
        return null;
    }
    
    /**
     * Get the OS name for the active JVM
     * @return
     *          the OS name
     */
    public static String getOsName()
    {
        return System.getProperty(OS_NAME_PROP_NAME);
    }
    
    /**
     * get the java home property value
     * @return
     *          the java home
     */
    public static String getJavaHome()
    {
        return System.getProperty(JAVA_HOME_PROP_NAME);
    }
    
    /**
     * get the java classpath property
     * @return
     *          the classpath
     */
    public static String getSystemClassPath()
    {
        return System.getProperty(CLASS_PATH_PROP_NAME);
    }

    /**
     * get the java library path property
     * @return
     *          the lib path
     */
    public static String getJavaLibraryPath()
    {
        return System.getProperty(JAVA_LIB_PATH_PROP_NAME);
    }

    /**
     * Get the users working directory.
     * @return
     *          the working directory
     */
    public static String getWorkingDirectory()
    {
        return System.getProperty(WORKING_DIRECTORY_PROP_NAME);
    }
    
    /**
     * Get the system temporary directory
     * @return  the system's temp dir
     */
    public static String getTempDirectory()
    {
        return System.getProperty(TEMP_DIR);
    }
}
