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

import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;


/**
 * A dialog with wizard control buttons (next, back, finish, cancel)
 */
public class WizardDialog extends JDialog
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -7359086492112822373L;
    
    /**
     * Used to decide whether the user see's OK/Cancel or
     * back/next/finish/cancel
     */
    public static enum WizardDialogType
    {
        /**
         * Indicates wizard should have back, next, finish and cancel buttons
         */
        BACK_NEXT_FINISH_CANCEL_DIALOG,
        
        /**
         * Indicates wizard should have OK, Cancel buttons
         */
        OK_CANCEL_DIALOG
    }
    
    private final WizardController wizard;

    private final JPanel wizardView;
    
    /**
     * Create a new wizard dialog with the given title
     * @param wizard
     *          the wizard
     * @param wizardView
     *          the "view" for the wizard (Eg. a {@link WizardFlipPanel})
     * @param parent
     *          the parent frame to use
     * @param title
     *          the title to use
     * @param modal
     *          should this dialog be modal?
     * @param wizardDialogType
     *          what kind of dialog is this? see {@link WizardDialogType}
     */
    public WizardDialog(
            WizardController wizard,
            JPanel wizardView,
            Frame parent,
            String title,
            boolean modal,
            WizardDialogType wizardDialogType)
    {
        super(parent, title, modal);
        this.wizard = wizard;
        this.wizardView = wizardView;
        this.initComponents();
        this.postGuiInit();
        
        if(wizardDialogType == WizardDialogType.OK_CANCEL_DIALOG)
        {
            this.actionPanel.remove(this.nextButton);
            this.actionPanel.remove(this.backButton);
            this.finishButton.setText("OK");
        }
    }

    /**
     * Create a new wizard dialog with the given title
     * @param wizard
     *          the wizard
     * @param wizardView
     *          the "view" for the wizard (Eg. a {@link WizardFlipPanel})
     * @param parent
     *          the parent dialog to use
     * @param title
     *          the title to use
     * @param modal
     *          should this dialog be modal?
     * @param wizardDialogType
     *          what kind of dialog is this? see {@link WizardDialogType}
     */
    public WizardDialog(
            WizardController wizard,
            JPanel wizardView,
            JDialog parent,
            String title,
            boolean modal,
            WizardDialogType wizardDialogType)
    {
        super(parent, title, modal);
        this.wizard = wizard;
        this.wizardView = wizardView;
        this.initComponents();
        this.postGuiInit();
        
        if(wizardDialogType == WizardDialogType.OK_CANCEL_DIALOG)
        {
            this.actionPanel.remove(this.nextButton);
            this.actionPanel.remove(this.backButton);
            this.finishButton.setText("OK");
        }
    }

    /**
     * do initialization after GUI builder finishes
     */
    private void postGuiInit()
    {
        this.backButton.setIcon(new ImageIcon(WizardDialog.class.getResource(
                "/images/action/back-16x16.png")));
        this.nextButton.setIcon(new ImageIcon(WizardDialog.class.getResource(
                "/images/action/forward-16x16.png")));
        this.helpButton.setIcon(new ImageIcon(WizardDialog.class.getResource(
                "/images/action/help-16x16.png")));
        this.pack();
        
        this.refreshWizardButtons();
    }
    
    /**
     * Ask the wizard which buttons to turn on/off
     */
    private void refreshWizardButtons()
    {
        this.backButton.setEnabled(this.wizard.isPreviousValid());
        this.nextButton.setEnabled(this.wizard.isNextValid());
        this.finishButton.setEnabled(this.wizard.isFinishValid());
    }
    
    /**
     * Cancel the wizard
     */
    private void cancel()
    {
        if(this.wizard.cancel())
        {
            this.dispose();
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("all")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainContentsPanel = this.wizardView;
        actionPanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        finishButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().add(mainContentsPanel, java.awt.BorderLayout.CENTER);

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        actionPanel.add(backButton);

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        actionPanel.add(nextButton);

        finishButton.setText("Finish");
        finishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishButtonActionPerformed(evt);
            }
        });
        actionPanel.add(finishButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        actionPanel.add(cancelButton);

        helpButton.setText("Help...");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });
        actionPanel.add(helpButton);

        getContentPane().add(actionPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        this.wizard.goPrevious();
        this.refreshWizardButtons();
    }//GEN-LAST:event_backButtonActionPerformed
    
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        this.wizard.goNext();
        this.refreshWizardButtons();
    }//GEN-LAST:event_nextButtonActionPerformed
    
    private void finishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishButtonActionPerformed
        if(this.wizard.finish())
        {
            this.dispose();
        }
        else
        {
            this.refreshWizardButtons();
        }
    }//GEN-LAST:event_finishButtonActionPerformed
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.cancel();
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        this.wizard.help();
    }//GEN-LAST:event_helpButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.cancel();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton backButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton finishButton;
    private javax.swing.JButton helpButton;
    private javax.swing.JPanel mainContentsPanel;
    private javax.swing.JButton nextButton;
    // End of variables declaration//GEN-END:variables

}
