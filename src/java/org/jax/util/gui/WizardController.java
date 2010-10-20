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

package org.jax.util.gui;

/**
 * A wizard style controller interface
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public interface WizardController
{
    /**
     * Go to the next view in the wizard flow
     * @return
     *          true if the go next completes
     * @throws IllegalStateException
     *          if !{@link #isNextValid()} 
     */
    public boolean goNext() throws IllegalStateException;
    
    /**
     * Determine if it's valid for the user to go to the next view
     * @return
     *          true iff the user should be allowed to go to the next
     *          wizard view
     */
    public boolean isNextValid();
    
    /**
     * Go to the previous wizard view
     * @return
     *          true if the "go previous" completes
     * @throws IllegalStateException
     *          if !{@link #isPreviousValid()}
     */
    public boolean goPrevious() throws IllegalStateException;
    
    /**
     * Determine if it's valid for the user to go to the previous view
     * @return
     *          true iff the use should be allowed to go to the previous view
     */
    public boolean isPreviousValid();
    
    /**
     * "Finish" the wizard
     * @return
     *          true if the finish completes
     * @throws IllegalStateException
     *          if !{@link #isFinishValid()}
     */
    public boolean finish() throws IllegalStateException;
    
    /**
     * Determine if it's valid for the user to finish the wizard
     * @return
     *          true iff it's valid for the user to finish the wizard
     */
    public boolean isFinishValid();
    
    /**
     * Cancel the wizard
     * @return
     *          true if the cancel completes
     */
    public boolean cancel();
    
    /**
     * Show help for the current wizard view
     */
    public void help();
}
