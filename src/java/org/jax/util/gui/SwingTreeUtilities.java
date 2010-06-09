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

import java.util.Collection;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jax.util.ObjectUtil;

/**
 * Some utility functions for {@link javax.swing.JTree}s
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class SwingTreeUtilities
{
    /**
     * A simple interface for a tree node factory
     * @param <D>
     *              the generic type for the user data
     * @param <T>
     *              the generic type for the child tree nodes
     */
    public interface TreeNodeFactory<D, T extends TreeNode>
    {
        /**
         * Create a new tree node using the given data
         * @param data
         *          the data
         * @return
         *          the new tree node
         */
        public T createTreeNode(D data);
    }
    
    /**
     * Function for updating child nodes using a list of data representing the
     * user data in those nodes. For newData without a corresponding
     * child node, a new child node is added and for child nodes without
     * corresponding newData a new child will be created using the given
     * factory.
     * @param <D>
     *              the generic type for the user data
     * @param <T>
     *              the generic type for the child tree nodes
     * @param treeModel
     *              the tree model that the parent node lives in
     * @param treeNodeFactory
     *              the factory that should be used if we need to create
     *              new nodes
     * @param parentNode
     *              the parent node that all of the children live under
     * @param newData
     *              the collection of data that this function is trying to force
     *              the child nodes to match (in their user data)
     */
    public static <D, T extends MutableTreeNode> void updateChildNodes(
            DefaultTreeModel treeModel,
            TreeNodeFactory<D, T> treeNodeFactory,
            DefaultMutableTreeNode parentNode,
            Collection<D> newData)
    {
        // remove old data. count down so that indices don't change
        // as we modify the node list
        for(int i = parentNode.getChildCount() - 1;
            i >= 0;
            i--)
        {
            DefaultMutableTreeNode childNode =
                (DefaultMutableTreeNode)parentNode.getChildAt(i);
            Object childData = childNode.getUserObject();
            
            if(!newData.contains(childData))
            {
                // remove the child node
                treeModel.removeNodeFromParent(childNode);
            }
            else
            {
                treeModel.nodeChanged(childNode);
            }
        }
        
        // add in new data using the factory we've been given
        for(D datum: newData)
        {
            int matchingIndex = indexOfChildWithUserObject(
                    parentNode,
                    datum);
            if(matchingIndex == -1)
            {
                T newNode = treeNodeFactory.createTreeNode(datum);
                treeModel.insertNodeInto(
                        newNode,
                        parentNode,
                        parentNode.getChildCount());
            }
        }
        
        treeModel.nodeChanged(parentNode);
    }
    
    /**
     * Like {@link #indexOfChildWithUserObject(DefaultMutableTreeNode, Object)}
     * except that we're getting the node instead of its index
     * @param parentNode
     *          the parent node to search through
     * @param userObject
     *          the user object that we're looking for
     * @return
     *          the child node with the user object or null if none of the nodes
     *          has the user object
     */
    public static DefaultMutableTreeNode childWithUserObject(
            DefaultMutableTreeNode parentNode,
            Object userObject)
    {
        int numChildren = parentNode.getChildCount();
        
        for(int i = 0; i < numChildren; i++)
        {
            DefaultMutableTreeNode currChildNode =
                (DefaultMutableTreeNode)parentNode.getChildAt(i);
            if(ObjectUtil.areEqual(userObject, currChildNode.getUserObject()))
            {
                return currChildNode;
            }
        }
        
        return null;
    }
    
    /**
     * A simple utility function to get the index of the user object that is
     * equal to the give user object
     * @param parentNode
     *          the parent
     * @param userObject
     *          the user object that we're looking for
     * @return
     *          the index where we find it or -1 if we can't find it
     */
    public static int indexOfChildWithUserObject(
            DefaultMutableTreeNode parentNode,
            Object userObject)
    {
        int numChildren = parentNode.getChildCount();
        
        for(int i = 0; i < numChildren; i++)
        {
            DefaultMutableTreeNode currChildNode =
                (DefaultMutableTreeNode)parentNode.getChildAt(i);
            if(ObjectUtil.areEqual(userObject, currChildNode.getUserObject()))
            {
                return i;
            }
        }
        
        return -1;
    }
}
