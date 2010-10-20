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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.jax.util.TextWrapper;
import org.jax.util.io.FlatFileFormat;
import org.jax.util.io.IllegalFormatException;

/**
 * A dialog for showing a {@link CharacterDelimitedViewPanel}
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class CharacterDelimitedViewDialog extends javax.swing.JDialog
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -7588578327583822701L;
    
    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            CharacterDelimitedViewDialog.class.getName());
    
    /**
     * the panel that this dialog shows
     */
    private final CharacterDelimitedViewPanel characterDelimitedViewPanel;
    
    /**
     * Constructor
     * @param parent
     *          the parent frame for this dialog
     * @param title
     *          the title to use for this dialog
     * @param modal
     *          if true then this dialog is modal
     */
    public CharacterDelimitedViewDialog(
            Frame parent,
            String title,
            boolean modal)
    {
        super(parent, title, modal);
        
        this.characterDelimitedViewPanel = new CharacterDelimitedViewPanel();
        this.initComponents();
        this.postGuiInit();
    }
    
    /**
     * Constructor
     * @param owner
     *          the owner dialog for this dialog
     * @param title
     *          the title to use for this dialog
     * @param modal
     *          if true then this dialog is modal
     */
    public CharacterDelimitedViewDialog(
            Dialog owner,
            String title,
            boolean modal)
    {
        super(owner, title, modal);
        
        this.characterDelimitedViewPanel = new CharacterDelimitedViewPanel();
        this.initComponents();
        this.postGuiInit();
    }
    
    private void postGuiInit()
    {
        this.closeButton.addActionListener(new ActionListener()
        {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e)
            {
                CharacterDelimitedViewDialog.this.dispose();
            }
        });
    }

    /**
     * This function just delegates to the internal panel's
     * {@link CharacterDelimitedViewPanel#loadCharacterDelimitedFile(
     *  File, FlatFileFormat, boolean)}
     * function
     * @param flatFile
     *          the file to show
     * @param format
     *          the flat file format to use
     * @param showHeader
     *          if true we should show a header on the table view
     * @throws IOException
     *          if we can't load the file
     * @throws IllegalFormatException
     *          if the file is not correctly formatted
     */
    public void loadCharacterDelimitedFile(
            File flatFile,
            FlatFileFormat format,
            boolean showHeader)
    throws IOException, IllegalFormatException
    {
        this.characterDelimitedViewPanel.loadCharacterDelimitedFile(
                flatFile,
                format,
                showHeader);
    }
    
    /**
     * A convenience function for showing a preview dialog for the given
     * file
     * @param previewTitle
     *          the title to use for the preview dialog
     * @param fileName
     *          the file to preview
     * @param format
     *          the format to use
     * @param showHeader
     *          if true show the header
     * @param parentDialog
     *          the parent dialog to use for the preview or any error messages
     * @param maxRowCount
     *          the maximum row count
     */
    public static void viewFlatFile(
            String previewTitle,
            String fileName,
            FlatFileFormat format,
            boolean showHeader,
            Dialog parentDialog,
            int maxRowCount)
    {
        File file = new File(fileName);
        if(!file.isFile())
        {
            String message =
                "Failed to show " + previewTitle +
                ". The selected file \"" + fileName + "\" appears to either " +
                "be a directory or not exist. Please select an existing file " +
                "in order to generate a preview.";
            JOptionPane.showMessageDialog(
                    parentDialog,
                    TextWrapper.wrapText(message, TextWrapper.DEFAULT_DIALOG_COLUMN_COUNT),
                    "Error Showing Preview",
                    JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            CharacterDelimitedViewDialog characterDelimitedViewDialog =
                new CharacterDelimitedViewDialog(
                        parentDialog,
                        previewTitle,
                        false);
            characterDelimitedViewDialog.setVisible(true);
            try
            {
                characterDelimitedViewDialog.setMaxRowCount(maxRowCount);
                characterDelimitedViewDialog.loadCharacterDelimitedFile(
                        file,
                        format,
                        showHeader);
            }
            catch(Exception ex)
            {
                // something bad happened when we tried to generate the preview
                // tell the user if anything bad happens
                characterDelimitedViewDialog.dispose();
                String title = "Failed to Show Preview";
                LOG.log(Level.SEVERE,
                        title,
                        ex);
                MessageDialogUtilities.error(
                        parentDialog,
                        ex.getMessage(),
                        title);
            }
        }
    }

    private void setMaxRowCount(int maxRowCount)
    {
        this.characterDelimitedViewPanel.setMaxRowCount(maxRowCount);
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

        javax.swing.JPanel characterDelimitedViewPanelDownCast = this.characterDelimitedViewPanel;
        actionPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(characterDelimitedViewPanelDownCast, java.awt.BorderLayout.CENTER);

        closeButton.setText("Close");
        actionPanel.add(closeButton);

        getContentPane().add(actionPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton closeButton;
    // End of variables declaration//GEN-END:variables
}
