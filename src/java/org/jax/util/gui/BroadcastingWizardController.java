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
 * A wizard controller that broadcasts change events
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public interface BroadcastingWizardController extends WizardController
{
    /**
     * Add a new listener
     * @param wizardListener
     *          the wizard listener to add
     */
    public void addWizardListener(WizardListener wizardListener);
    
    /**
     * Remove the given listener
     * @param wizardListener
     *          the listener to remove
     */
    public void removeWizardListener(WizardListener wizardListener);
}
