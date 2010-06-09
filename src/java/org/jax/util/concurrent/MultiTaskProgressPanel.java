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
package org.jax.util.concurrent;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A panel for showing the progress of any number of long running tasks
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class MultiTaskProgressPanel extends javax.swing.JPanel
{
    /**
     * every {@link java.io.Serializable} is supposed to have one of these
     */
    private static final long serialVersionUID = -3192365148180380630L;
    
    private final ConcurrentLinkedQueue<TaskAndWithAutoRemoveSetting> tasks =
        new ConcurrentLinkedQueue<TaskAndWithAutoRemoveSetting>();
    
    private volatile boolean pendingUpdates = false;
    
    private final Runnable updateRunnable =
        new Runnable()
        {
            /**
             * {@inheritDoc}
             */
            public void run()
            {
                MultiTaskProgressPanel.this.maybeUpdateProgressNow();
            }
        };
    
    private final ChangeListener progressChangeListener =
        new ChangeListener()
        {
            /**
             * {@inheritDoc}
             */
            public void stateChanged(ChangeEvent e)
            {
                MultiTaskProgressPanel.this.updateProgressLater();
            }
        };
    
    private class TaskAndWithAutoRemoveSetting
    {
        private final LongRunningTask task;
        
        private final boolean autoRemove;

        public TaskAndWithAutoRemoveSetting(
                LongRunningTask task,
                boolean autoRemove)
        {
            this.task = task;
            this.autoRemove = autoRemove;
        }
        
        public LongRunningTask getTask()
        {
            return this.task;
        }
        
        public boolean getAutoRemove()
        {
            return this.autoRemove;
        }
    }
    
    /**
     * Constructor
     */
    public MultiTaskProgressPanel()
    {
        this.initComponents();
        this.updateProgressNow();
    }
    
    /**
     * maybe perform the progress update in the calling thread... this should
     * only be done in an AWT-safe call
     */
    private void maybeUpdateProgressNow()
    {
        // the updates pending queue is being used to avoid doing any
        // unnecessary work
        if(this.pendingUpdates)
        {
            this.pendingUpdates = false;
            this.updateProgressNow();
        }
    }

    /**
     * perform the progress update in the calling thread... this should
     * only be done in an AWT-safe call
     */
    private void updateProgressNow()
    {
        // clean out any completed tasks
        Iterator<TaskAndWithAutoRemoveSetting> taskIter = this.tasks.iterator();
        while(taskIter.hasNext())
        {
            TaskAndWithAutoRemoveSetting currTaskWSetting = taskIter.next();
            LongRunningTask currTask = currTaskWSetting.getTask();
            if(currTaskWSetting.getAutoRemove() && currTask.isComplete())
            {
                taskIter.remove();
                currTask.removeChangeListener(this.progressChangeListener);
            }
        }
        
        List<LongRunningTask> incompleteTasks = this.getIncompleteTasks();
        if(incompleteTasks.isEmpty())
        {
            //this.taskLabel.setVisible(false);
            //this.taskProgressBar.setVisible(false);
            
            this.taskLabel.setText("No Tasks Running:");
            this.taskProgressBar.setIndeterminate(false);
            this.taskProgressBar.setValue(this.taskProgressBar.getMinimum());
        }
        else
        {
            if(incompleteTasks.size() == 1)
            {
                LongRunningTask task = incompleteTasks.get(0);
                this.taskLabel.setText(task.getTaskName() + ":");
                if(task.getTotalWorkUnits() == -1 || task.getWorkUnitsCompleted() == 0)
                {
                    this.taskProgressBar.setIndeterminate(true);
                }
                else
                {
                    this.taskProgressBar.setIndeterminate(false);
                    this.taskProgressBar.setMaximum(task.getTotalWorkUnits());
                    this.taskProgressBar.setValue(task.getWorkUnitsCompleted());
                }
            }
            else
            {
                int taskCount = incompleteTasks.size();
                this.taskLabel.setText(taskCount + " Tasks Running:");
                this.taskProgressBar.setIndeterminate(true);
            }
            
            this.taskLabel.setVisible(true);
            this.taskProgressBar.setVisible(true);
        }
    }
    
    /**
     * Extract any tasks that are not yet complete
     * @return  the incomplete tasks
     */
    private List<LongRunningTask> getIncompleteTasks()
    {
        List<LongRunningTask> incompleteTasks = new ArrayList<LongRunningTask>();
        
        Iterator<TaskAndWithAutoRemoveSetting> taskIter = this.tasks.iterator();
        while(taskIter.hasNext())
        {
            TaskAndWithAutoRemoveSetting currTaskWSetting = taskIter.next();
            LongRunningTask currTask = currTaskWSetting.getTask();
            if(!currTask.isComplete())
            {
                incompleteTasks.add(currTask);
            }
        }
        
        return incompleteTasks;
    }

    /**
     * update the progress later in the AWT event thread. this function is
     * safe to call from any thread
     */
    private void updateProgressLater()
    {
        this.pendingUpdates = true;
        EventQueue.invokeLater(this.updateRunnable);
    }

    /**
     * Track the progress on the given task (without auto-removal)
     * @param task
     *          the task to track until removal
     */
    public void addTaskToTrack(LongRunningTask task)
    {
        this.addTaskToTrack(task, false);
    }

    /**
     * Track the progress on the given task
     * @param task
     *          the task to track
     * @param autoRemoveTask
     *          iff true the task will automaticaly be removed on completion
     */
    public void addTaskToTrack(LongRunningTask task, boolean autoRemoveTask)
    {
        this.tasks.add(new TaskAndWithAutoRemoveSetting(task, autoRemoveTask));
        task.addChangeListener(this.progressChangeListener);
        this.updateProgressLater();
    }
    
    /**
     * Stop tracking progress for the given task
     * @param task
     *          the task that we should stop tracking
     */
    public void removeTaskToTrack(LongRunningTask task)
    {
        Iterator<TaskAndWithAutoRemoveSetting> iter = this.tasks.iterator();
        while(iter.hasNext())
        {
            LongRunningTask currTask = iter.next().getTask();
            if(currTask.equals(task))
            {
                iter.remove();
                currTask.removeChangeListener(this.progressChangeListener);
                break;
            }
        }
        
        this.updateProgressLater();
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

        taskLabel = new javax.swing.JLabel();
        taskProgressBar = new javax.swing.JProgressBar();

        setLayout(new java.awt.GridBagLayout());

        taskLabel.setText("No Tasks Running:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(taskLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(taskProgressBar, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel taskLabel;
    private javax.swing.JProgressBar taskProgressBar;
    // End of variables declaration//GEN-END:variables
}
