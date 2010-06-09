/*
 * Copyright (c) 2009 The Jackson Laboratory
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;

import org.jax.util.io.FlatFileFormat;
import org.jax.util.io.FlatFileReader;
import org.jax.util.io.IllegalFormatException;

/**
 * A panel for viewing character delimited files
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class CharacterDelimitedViewPanel extends javax.swing.JPanel
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -3662132605171096482L;
    
    private volatile int maxRowCount = -1;
    
    /**
     * Constructor
     */
    public CharacterDelimitedViewPanel()
    {
        this.initComponents();
        this.postGuiInit();
    }
    
    /**
     * Do the initialization that the GUI builder doesn't take care of
     */
    private void postGuiInit()
    {
        this.viewTable.setModel(new DefaultTableModel()
        {
            /**
             * every serializable is supposed to have one of these
             */
            private static final long serialVersionUID = 6589566482344804543L;
            
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });
    }
    
    /**
     * A convenience function that gets the viewTable and casts it to its more
     * specific type
     * @return  the table
     */
    private FlatFileTable getViewTable()
    {
        return (FlatFileTable)this.viewTable;
    }

    /**
     * Show the view for the given file
     * @param flatFile
     *          the file to show
     * @param format
     *          the flat file format to use
     * @param showHeader
     *          if true we should show a header on the table view
     * @throws IOException
     *          if we can't load the file
     * @throws IllegalFormatException
     *          if the flat file is not in the expected format
     */
    public void loadCharacterDelimitedFile(
            File flatFile,
            FlatFileFormat format,
            boolean showHeader)
    throws IOException, IllegalFormatException
    {
        BufferedReader br = new BufferedReader(new FileReader(flatFile));
        FlatFileReader ffr = new FlatFileReader(br, format);
        this.getViewTable().loadTable(
                ffr,
                showHeader,
                this.tabelViewScrollPane.getWidth());
        this.loadPlainText(flatFile);
    }
    
    /**
     * Load the file into the plain text view
     * @param characterDelimitedFile
     *          the file we're reading
     * @throws IOException
     *          if we get an exception trying to read the file
     */
    private void loadPlainText(
            File characterDelimitedFile)
    throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(
                characterDelimitedFile));
        
        StringBuffer sb = new StringBuffer();
        String currLine;
        for(int row = 0;
            (this.maxRowCount == -1 || row < this.maxRowCount) &&
            (currLine = reader.readLine()) != null;
            row++)
        {
            sb.append(currLine);
            sb.append('\n');
        }
        reader.close();
        
        this.viewTextArea.setText(sb.toString());
    }

    /**
     * the max row count that will be loaded
     * @param maxRowCount the max row count (-1 means no maximum)
     */
    public void setMaxRowCount(int maxRowCount)
    {
        this.maxRowCount = maxRowCount;
        this.getViewTable().setMaxRowCount(maxRowCount);
    }
    
    /**
     * Getter for the maximum row count
     * @return the maxRowCount
     */
    public int getMaxRowCount()
    {
        return this.maxRowCount;
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
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JTabbedPane fileViewTabbedPane = new javax.swing.JTabbedPane();
        javax.swing.JPanel tableViewPane = new javax.swing.JPanel();
        tabelViewScrollPane = new javax.swing.JScrollPane();
        viewTable = new FlatFileTable(-1);
        javax.swing.JPanel plainTextViewPane = new javax.swing.JPanel();
        javax.swing.JScrollPane plainTextViewScrollPane = new javax.swing.JScrollPane();
        viewTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        tableViewPane.setLayout(new java.awt.GridBagLayout());

        tabelViewScrollPane.setViewportView(viewTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        tableViewPane.add(tabelViewScrollPane, gridBagConstraints);

        fileViewTabbedPane.addTab("Table View", tableViewPane);

        plainTextViewPane.setLayout(new java.awt.GridBagLayout());

        viewTextArea.setEditable(false);
        plainTextViewScrollPane.setViewportView(viewTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        plainTextViewPane.add(plainTextViewScrollPane, gridBagConstraints);

        fileViewTabbedPane.addTab("Plain Text View", plainTextViewPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(fileViewTabbedPane, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane tabelViewScrollPane;
    private javax.swing.JTable viewTable;
    private javax.swing.JTextArea viewTextArea;
    // End of variables declaration//GEN-END:variables

}
