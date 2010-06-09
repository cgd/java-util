/*
 * Copyright (c) 2010 The Jackson Laboratory
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

package org.jax.util.gui;

/**
 * An abstract class which should make it easier to create an OK/cancel panel
 * for a {@link WizardDialog}
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public abstract class OKCancelWizardController
implements BroadcastingWizardController
{
    private final WizardEventSupport wizardEventSupport = new WizardEventSupport(this);
    
    /**
     * {@inheritDoc}
     */
    public boolean finish() throws IllegalStateException
    {
        return this.ok();
    }

    /**
     * For when the OK button is pressed
     * @return true when the OK is successful
     * @throws IllegalStateException this exception should not occur
     */
    public abstract boolean ok() throws IllegalStateException;

    /**
     * {@inheritDoc}
     */
    public boolean goNext() throws IllegalStateException
    {
        throw new IllegalStateException(
                "This is an OK/Cancel panel but goNext was called");
    }

    /**
     * {@inheritDoc}
     */
    public boolean goPrevious() throws IllegalStateException
    {
        throw new IllegalStateException(
                "This is an OK/Cancel panel but goPrevious was called");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFinishValid()
    {
        // we always want the OK button to be enabled
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNextValid()
    {
        // next is never valid for an OK/cancel dialog
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPreviousValid()
    {
        // previous is never valid for an OK/cancel dialog
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void addWizardListener(WizardListener listenerToAdd)
    {
        this.wizardEventSupport.addWizardListener(listenerToAdd);
    }

    /**
     * {@inheritDoc}
     */
    public void removeWizardListener(WizardListener listenerToRemove)
    {
        this.wizardEventSupport.removeWizardListener(listenerToRemove);
    }
}
