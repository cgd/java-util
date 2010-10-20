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

import java.awt.CardLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * A panel that flips through a wizard
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class WizardFlipPanel extends JPanel
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = 4573901499541756779L;
    
    /**
     * Listen to the wizard so we know when to flip
     */
    private final WizardListener wizardListener = new WizardListener()
    {
        /**
         * {@inheritDoc}
         */
        public void wizardCancelled(WizardController wizardController)
        {
            // don't care
        }
        
        /**
         * {@inheritDoc}
         */
        public void wizardFinished(WizardController wizardController)
        {
            // don't care
        }
        
        /**
         * {@inheritDoc}
         */
        public void wizardGoNextSucceeded(WizardController wizardController)
        {
            WizardFlipPanel.this.getLayout().next(
                    WizardFlipPanel.this);
        }
        
        /**
         * {@inheritDoc}
         */
        public void wizardGoPreviousSucceeded(WizardController wizardController)
        {
            WizardFlipPanel.this.getLayout().previous(
                    WizardFlipPanel.this);
        }
        
        /**
         * {@inheritDoc}
         */
        public void wizardValidityChanged(WizardController wizardController)
        {
            // don't care
        }
    };
    
    /**
     * Constructor
     * @param panelsToFlip
     *          the panels that this wizard flips through
     * @param wizardController
     *          the controller for this flip panel
     */
    public WizardFlipPanel(JPanel[] panelsToFlip, BroadcastingWizardController wizardController)
    {
        super(new CardLayout());
        
        // add all of the panels
        for(int i = 0; i < panelsToFlip.length; i++)
        {
            this.add(panelsToFlip[i], Integer.toString(i));
        }
        
        wizardController.addWizardListener(this.wizardListener);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setLayout(LayoutManager mgr)
    {
        if(!(mgr instanceof CardLayout))
        {
            throw new ClassCastException("only card layouts are permitted");
        }
        
        super.setLayout(mgr);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CardLayout getLayout()
    {
        return (CardLayout)super.getLayout();
    }
}
