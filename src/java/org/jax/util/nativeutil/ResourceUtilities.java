// NOTE: file copied with minimal changes from:
// svn export http://svncisd.ethz.ch/repos/cisd/base/tags/release/10.05.x/10.05.0/base

/*
 * Copyright 2009 ETH Zuerich, CISD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jax.util.nativeutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jax.util.io.FileUtilities;

/**
 * Utilities for handling Java resources.
 * 
 * @author Bernd Rinn
 */
public class ResourceUtilities
{

    /**
     * Tries to copy the resource with the given name to a temporary file.
     * 
     * @param resource The name of the resource to copy.
     * @param prefix The prefix to use for the temporary name.
     * @param postfix The postfix to use for the temporary name.
     * @return The name of the temporary file, or <code>null</code>, if the resource could not be
     *         copied.
     */
    public static String tryCopyResourceToTempFile(final String resource, final String prefix,
            final String postfix)
    {
        try
        {
            return copyResourceToTempFile(resource, prefix, postfix);
        } catch (final Exception ex)
        {
            return null;
        }
    }

    /**
     * Copies the resource with the given name to a temporary file. The file will be deleted on
     * program exit.
     * 
     * @param resource The name of the resource to copy.
     * @param prefix The prefix to use for the temporary name.
     * @param postfix The postfix to use for the temporary name.
     * @return The name of the temporary file.
     * @throws IOException in case of an IO error
     * @throws IllegalArgumentException If the resource cannot be found in the class path.
     */
    public static String copyResourceToTempFile(final String resource, final String prefix,
            final String postfix) throws IOException
    {
        final InputStream resourceStream = ResourceUtilities.class.getResourceAsStream(resource);
        if (resourceStream == null)
        {
            throw new IllegalArgumentException("Resource '" + resource + "' not found.");
        }
        try
        {
            final File tempFile = File.createTempFile(prefix, postfix);
            tempFile.deleteOnExit();
            final OutputStream fileStream = new FileOutputStream(tempFile);
            try
            {
                FileUtilities.writeSourceToSink(resourceStream, fileStream);
            }
            finally
            {
                fileStream.close();
            }
            return tempFile.getAbsolutePath();
        }
        finally
        {
            resourceStream.close();
        }
    }

}
