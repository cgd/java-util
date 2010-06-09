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

import java.awt.Component;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jax.util.TextWrapper;

/**
 * Some utility functions for sending a message to the user. Text is
 * wrapped using {@link TextWrapper#wrapText(String, int)}
 * with {@link TextWrapper#DEFAULT_DIALOG_COLUMN_COUNT}
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class MessageDialogUtilities
{
    /**
     * Pop up an info dialog
     * @param parentComponent
     *          the parent component
     * @param message
     *          the message
     * @param title
     *          the title to use on the message dialog
     */
    public static void inform(
            Component parentComponent,
            String message,
            String title)
    {
        MessageDialogUtilities.message(
                parentComponent,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Calls {@link #ask(Component, String, String, int)} with a
     * {@link JOptionPane#OK_CANCEL_OPTION} optionType
     * @param parentComponent
     *          the parent
     * @param question
     *          the question to ask
     * @param title
     *          the dialog title
     * @return
     *          true if the user says yes/OK
     */
    public static boolean ask(
            Component parentComponent,
            String question,
            String title)
    {
        return MessageDialogUtilities.ask(
                parentComponent,
                question,
                title,
                JOptionPane.OK_CANCEL_OPTION);
    }
    
    /**
     * Ask the user a question
     * @param parentComponent
     *          the parent
     * @param question
     *          the question to ask
     * @param title
     *          the dialog title
     * @param optionType
     *          the dialog option type to feed to
     *          {@link JOptionPane#showConfirmDialog(Component, Object, String, int)}
     * @return
     *          true if the user says yes/OK
     */
    public static boolean ask(
            Component parentComponent,
            String question,
            String title,
            int optionType)
    {
        if(question == null)
        {
            question = "";
        }
        
        int response = JOptionPane.showConfirmDialog(
                parentComponent,
                TextWrapper.wrapText(
                        question,
                        TextWrapper.DEFAULT_DIALOG_COLUMN_COUNT),
                title,
                optionType);
        
        return response == JOptionPane.OK_OPTION ||
               response == JOptionPane.YES_OPTION;
    }

    /**
     * Warn the user about something
     * @param parentComponent
     *          the parent
     * @param warningMessage
     *          the warning message
     * @param title
     *          the dialog title
     */
    public static void warn(
            Component parentComponent,
            String warningMessage,
            String title)
    {
        MessageDialogUtilities.message(
                parentComponent,
                warningMessage,
                title,
                JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Show an error message
     * @param parentComponent
     *          the parent
     * @param errorMessage
     *          the error message
     * @param title
     *          the dialog title
     */
    public static void error(
            Component parentComponent,
            String errorMessage,
            String title)
    {
        MessageDialogUtilities.message(
                parentComponent,
                errorMessage,
                title,
                JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Like {@link #error(Component, String, String)} but safe to call from any
     * thread
     * @param parentComponent
     *          the parent
     * @param errorMessage
     *          the error message
     * @param title
     *          the dialog title
     */
    public static void errorLater(
            Component parentComponent,
            String errorMessage,
            String title)
    {
        MessageDialogUtilities.messageLater(
                parentComponent,
                errorMessage,
                title,
                JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Like {@link #message(Component, String, String, int)} but safe to call
     * from any thread.
     * @param parentComponent
     *          the parent component for the popup dialog
     * @param message
     *          the message to the user
     * @param title
     *          the dialog title to use
     * @param messageType
     *          the kind of message which is passed on to
     *          {@link JOptionPane#showMessageDialog(Component, Object, String, int)}
     */
    public static void messageLater(
            final Component parentComponent,
            final String message,
            final String title,
            final int messageType)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            /**
             * {@inheritDoc}
             */
            public void run()
            {
                MessageDialogUtilities.message(
                        parentComponent,
                        message,
                        title,
                        messageType);
            }
        });
    }
    
    /**
     * Tell the user something
     * @param parentComponent
     *          the parent component for the popup dialog
     * @param message
     *          the message to the user
     * @param title
     *          the dialog title to use
     * @param messageType
     *          the kind of message which is passed on to
     *          {@link JOptionPane#showMessageDialog(Component, Object, String, int)}
     */
    public static void message(
            Component parentComponent,
            String message,
            String title,
            int messageType)
    {
        if(message == null)
        {
            message = "";
        }
        
        JOptionPane.showMessageDialog(
                parentComponent,
                TextWrapper.wrapText(
                        message,
                        TextWrapper.DEFAULT_DIALOG_COLUMN_COUNT),
                title,
                messageType);
    }
    
    /**
     * Ask the user if they'd like to replace a file that already exists
     * @param parentComponent
     *          the parent component for the popup dialog
     * @param selectedFile
     *          the file that we're asking the user about
     * @return  true if the user wants to replace the given file
     */
    public static boolean confirmOverwrite(
            Component parentComponent,
            File selectedFile)
    {
        String question =
            "Are you sure you want to overwrite \"" +
            selectedFile.getAbsolutePath() + "\"?";
        
        return MessageDialogUtilities.ask(
                parentComponent,
                question,
                "Confirm Overwrite");
    }
}
