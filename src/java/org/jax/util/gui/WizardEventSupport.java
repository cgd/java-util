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

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A convenience class for implementing wizard events
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class WizardEventSupport
{
    private final ConcurrentLinkedQueue<WizardListener> listeners =
        new ConcurrentLinkedQueue<WizardListener>();
    
    private final BroadcastingWizardController wizard;
    
    /**
     * Constructor
     * @param wizard
     *          the wizard that we broadcast events for
     */
    public WizardEventSupport(BroadcastingWizardController wizard)
    {
        this.wizard = wizard;
    }
    
    /**
     * Register the given listener to receive events
     * @param listenerToAdd
     *          the listener
     */
    public void addWizardListener(WizardListener listenerToAdd)
    {
        this.listeners.add(listenerToAdd);
    }
    
    /**
     * Deregister the given listener for wizard events
     * @param listenerToRemove
     *          the listener to deregister
     */
    public void removeWizardListener(WizardListener listenerToRemove)
    {
        this.listeners.remove(listenerToRemove);
    }
    
    /**
     * notify listeners that the wizard finished
     * @see WizardListener#wizardFinished(WizardController)
     */
    public void fireWizardFinished()
    {
        Iterator<WizardListener> iter = this.listeners.iterator();
        while(iter.hasNext())
        {
            iter.next().wizardFinished(this.wizard);
        }
    }
    
    /**
     * notify listeners that the wizard cancelled
     * @see WizardListener#wizardCancelled(WizardController)
     */
    public void fireWizardCancelled()
    {
        Iterator<WizardListener> iter = this.listeners.iterator();
        while(iter.hasNext())
        {
            iter.next().wizardCancelled(this.wizard);
        }
    }
    
    /**
     * notify listeners that the wizard moved to the next view
     * @see WizardListener#wizardGoNextSucceeded(WizardController)
     */
    public void fireWizardGoNextSucceeded()
    {
        Iterator<WizardListener> iter = this.listeners.iterator();
        while(iter.hasNext())
        {
            iter.next().wizardGoNextSucceeded(this.wizard);
        }
    }
    
    /**
     * notify listeners that the wizard moved to the previous view
     * @see WizardListener#wizardGoPreviousSucceeded(WizardController)
     */
    public void fireWizardGoPreviousSucceeded()
    {
        Iterator<WizardListener> iter = this.listeners.iterator();
        while(iter.hasNext())
        {
            iter.next().wizardGoPreviousSucceeded(this.wizard);
        }
    }
    
    /**
     * notify listeners that the wizard validity has changed
     * @see WizardListener#wizardValidityChanged(WizardController)
     */
    public void fireWizardValidityChanged()
    {
        Iterator<WizardListener> iter = this.listeners.iterator();
        while(iter.hasNext())
        {
            iter.next().wizardValidityChanged(this.wizard);
        }
    }
}
