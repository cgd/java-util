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

import java.util.EventListener;

/**
 * A listener interface for wizard events
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public interface WizardListener extends EventListener
{
    /**
     * Called to notify listener when the wizard is finished
     * @param wizardController  the wizard that's finished
     */
    public void wizardFinished(WizardController wizardController);
    
    /**
     * Called to notify listener when the wizard has been canceled
     * @param wizardController  the wizard that's canceled
     */
    public void wizardCancelled(WizardController wizardController);
    
    /**
     * There was a "next" that succeeded
     * @param wizardController  the wizard that has done a next
     */
    public void wizardGoNextSucceeded(WizardController wizardController);
    
    /**
     * There was a "back" that succeeded
     * @param wizardController  the wizard that has done a prev
     */
    public void wizardGoPreviousSucceeded(WizardController wizardController);
    
    /**
     * This function is used to indicate that the state of one of the is*Valid
     * properties has changed.
     * 
     * <p>
     * This notification may only be sent when the
     * change in validity does not correspond to a prev/next/finish event.
     * </p>
     * 
     * <p>
     * TODO at some point we should make sure that this caveat isn't needed any
     *      longer (this should always fire for a validity change)
     * </p>
     * 
     * @param wizardController  the wizard that changed its validity
     */
    public void wizardValidityChanged(WizardController wizardController);
}
