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
 * Some simple object utility functions
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class ObjectUtil
{
    /**
     * Private constructor. Access functionality through static functions.
     */
    private ObjectUtil()
    {
    }
    
    /**
     * A convenience function for hashing an object without getting
     * {@link NullPointerException}s
     * @param obj
     *          the object to hash
     * @return
     *          {@link Object#hashCode() obj.hashCode()} if obj is non-null.
     *          0 if obj is null
     */
    public static int hashObject(Object obj)
    {
        return obj == null ? 0 : obj.hashCode();
    }
    
    /**
     * A convenience function that compares for comparing the objects without
     * getting null pointer exceptions.
     * @param obj1
     *          the 1st object
     * @param obj2
     *          the 2nd object
     * @return
     *          true if they're both equal
     */
    public static boolean areEqual(Object obj1, Object obj2)
    {
        if(obj1 == obj2)
        {
            return true;
        }
        else if(obj1 == null || obj2 == null)
        {
            return false;
        }
        else
        {
            return obj1.equals(obj2);
        }
    }

    /**
     * A convenience function that compares for comparing the objects without
     * getting null pointer exceptions for null parameters.
     * @param obj1
     *          the 1st object
     * @param obj2
     *          the 2nd object
     * @return
     *          the comparison results
     */
    @SuppressWarnings("unchecked")
    public static int compare(Comparable obj1, Comparable obj2)
    {
        if(obj1 == obj2)
        {
            return 0;
        }
        else if(obj1 == null)
        {
            return -1;
        }
        else if(obj2 == null)
        {
            return 1;
        }
        else
        {
            return obj1.compareTo(obj2);
        }
    }
}
