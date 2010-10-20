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

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A layout manager that's like the null layout except it allows
 * users to drag and drop components.
 * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
 */
public class InteractiveLayout implements LayoutManager2
{
    /**
     * our logger
     */
    private static final Logger LOG = Logger.getLogger(
            InteractiveLayout.class.getName());
    
    /**
     * This is the size of the component border that the user can grab onto
     * for a resize action
     */
    public static final int COMPONENT_RESIZE_BORDER_WIDTH_PIXELS = 5;
    
    /**
     * The enumerated drag types
     */
    private enum InteractiveDragType
    {
        /**
         * Move drag
         */
        MOVE,
        
        /**
         * North resize drag
         */
        NORTH_RESIZE,
        
        /**
         * Northeast resize drag
         */
        NORTHEAST_RESIZE,
        
        /**
         * East resize drag
         */
        EAST_RESIZE,
        
        /**
         * Southeast resize drag
         */
        SOUTHEAST_RESIZE,
        
        /**
         * South resize drag
         */
        SOUTH_RESIZE,
        
        /**
         * Southwest resize drag
         */
        SOUTHWEST_RESIZE,
        
        /**
         * West resize drag
         */
        WEST_RESIZE,
        
        /**
         * Northwest resize drag
         */
        NORTHWEST_RESIZE
    }
    
    /**
     * Internal type for holding interactive drag information
     * @author <A HREF="mailto:keith.sheppard@jax.org">Keith Sheppard</A>
     */
    private class InteractiveDrag
    {
        private final InteractiveDragType dragType;
        
        private final Component component;
        
        private final Rectangle startingBounds;
        
        private final Point startingDragLocation;
        
        /**
         * Constructor
         * @param component
         *          the component being dragged
         * @param startingDragLocation
         *          the starting position of the drag
         */
        public InteractiveDrag(
                Component component,
                Point startingDragLocation)
        {
            this.component = component;
            this.startingDragLocation = startingDragLocation;
            this.startingBounds = new Rectangle(component.getBounds());
            this.dragType = InteractiveLayout.this.determineDragType(
                    component.getSize(),
                    this.startingDragLocation);
        }
        
        /**
         * Getter for the drag type
         * @return the dragType
         */
        public InteractiveDragType getDragType()
        {
            return this.dragType;
        }
        
        /**
         * Getter for the starting bounds rectangle
         * @return the startingBounds
         */
        public Rectangle getStartingBounds()
        {
            return this.startingBounds;
        }
        
        /**
         * Getter for the starting drag location
         * @return the startingDragLocation
         */
        public Point getStartingDragLocation()
        {
            return this.startingDragLocation;
        }
        
        /**
         * Getter for the component being dragged
         * @return the component
         */
        public Component getComponent()
        {
            return this.component;
        }
    }

    /**
     * Some interaction constraints
     */
    public class InteractionConstraints
    {
        private final boolean interactiveDraggingEnabled;
        
        private final boolean interactiveResizingEnabled;
        
        /**
         * Constructor
         * @param interactiveDraggingEnabled
         *          interactive dragging is enabled
         * @param interactiveResizingEnabled
         *          interactive resizing is enabled
         */
        public InteractionConstraints(
                boolean interactiveDraggingEnabled,
                boolean interactiveResizingEnabled)
        {
            this.interactiveDraggingEnabled = interactiveDraggingEnabled;
            this.interactiveResizingEnabled = interactiveResizingEnabled;
        }

        /**
         * Determine if interactive dragging is enabled
         * @return the interactiveDraggingEnabled
         */
        public boolean isInteractiveDraggingEnabled()
        {
            return this.interactiveDraggingEnabled;
        }
        
        /**
         * Determine if interactive resizing is enabled
         * @return the interactiveResizingEnabled
         */
        public boolean isInteractiveResizingEnabled()
        {
            return this.interactiveResizingEnabled;
        }
    }
    
    private final Map<Component, InteractionConstraints> interactionConstraintsMap =
        Collections.synchronizedMap(
                new HashMap<Component, InteractionConstraints>());
    
    private final MouseMotionListener managedComponentMotionListener =
        new MouseMotionListener()
        {
            public void mouseDragged(MouseEvent e)
            {
                InteractiveLayout.this.managedComponentMouseDragged(e);
            }

            public void mouseMoved(MouseEvent e)
            {
                InteractiveLayout.this.managedComponentMouseMoved(e);
            }
        };
    
    private final MouseListener managedComponentMouseListener =
        new MouseListener()
        {
            public void mouseClicked(MouseEvent e)
            {
                // no-op
            }

            public void mouseEntered(MouseEvent e)
            {
                // no-op
            }

            public void mouseExited(MouseEvent e)
            {
                // no-op
            }

            public void mousePressed(MouseEvent e)
            {
                InteractiveLayout.this.activeInteractiveDrag =
                    new InteractiveDrag(e.getComponent(), e.getPoint());
            }

            public void mouseReleased(MouseEvent e)
            {
                InteractiveLayout.this.activeInteractiveDrag = null;
            }
        };
    
    private final Map<InteractiveDragType, Cursor> dragTypeToCursorMap;
    
    private InteractiveDrag activeInteractiveDrag = null;
    
    /**
     * Constructor
     */
    public InteractiveLayout()
    {
        this.dragTypeToCursorMap =
            new EnumMap<InteractiveDragType, Cursor>(InteractiveDragType.class);
        this.dragTypeToCursorMap.put(
                InteractiveDragType.MOVE,
                Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        this.dragTypeToCursorMap.put(
                InteractiveDragType.NORTH_RESIZE,
                Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        this.dragTypeToCursorMap.put(
                InteractiveDragType.NORTHEAST_RESIZE,
                Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
        this.dragTypeToCursorMap.put(
                InteractiveDragType.EAST_RESIZE,
                Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        this.dragTypeToCursorMap.put(
                InteractiveDragType.SOUTHEAST_RESIZE,
                Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        this.dragTypeToCursorMap.put(
                InteractiveDragType.SOUTH_RESIZE,
                Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        this.dragTypeToCursorMap.put(
                InteractiveDragType.SOUTHWEST_RESIZE,
                Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
        this.dragTypeToCursorMap.put(
                InteractiveDragType.WEST_RESIZE,
                Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        this.dragTypeToCursorMap.put(
                InteractiveDragType.NORTHWEST_RESIZE,
                Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
    }
    
    /**
     * Respond to a mouse move
     * @param e
     *          the move event
     */
    private void managedComponentMouseMoved(MouseEvent e)
    {
        Component component = e.getComponent();
        InteractionConstraints constraints =
            this.interactionConstraintsMap.get(component);
        InteractiveDragType dragType = this.determineDragType(
                component.getSize(),
                e.getPoint());
        if(constraints != null && dragType != null)
        {
            if(dragType == InteractiveDragType.MOVE)
            {
                if(constraints.isInteractiveDraggingEnabled())
                {
                    component.setCursor(this.dragTypeToCursorMap.get(dragType));
                }
                else
                {
                    component.setCursor(Cursor.getDefaultCursor());
                }
            }
            else
            {
                if(constraints.isInteractiveResizingEnabled())
                {
                    component.setCursor(this.dragTypeToCursorMap.get(dragType));
                }
                else
                {
                    component.setCursor(Cursor.getDefaultCursor());
                }
            }
        }
    }

    /**
     * Determine which drag type should be used for the given starting size and
     * starting drag location
     * @param startingSize
     *          the starting size of the drag component
     * @param startingDragLocation
     *          the starting drag location for the cursor. this point is given
     *          in drag-component relative coordinates
     * @return
     *          the drag type or null if no drag type should be used
     */
    private InteractiveDragType determineDragType(
            Dimension startingSize,
            Point startingDragLocation)
    {
        // do a little bit of sanity checking
        if(startingDragLocation.x < 0 ||
           startingDragLocation.y < 0 ||
           startingDragLocation.x > startingSize.width ||
           startingDragLocation.y > startingSize.height)
        {
            LOG.warning(
                    "starting drag location is out of bounds: " +
                    "startingDragLocation=" + startingDragLocation +
                    ", startingSize=" + startingSize);
            return null;
        }
        else
        {
            // figure out what kind of drag action this is going to be
            boolean northDrag =
                startingDragLocation.y <= COMPONENT_RESIZE_BORDER_WIDTH_PIXELS;
            boolean southDrag =
                (startingSize.height - startingDragLocation.y) <=
                COMPONENT_RESIZE_BORDER_WIDTH_PIXELS;
            boolean eastDrag =
                (startingSize.width - startingDragLocation.x) <=
                COMPONENT_RESIZE_BORDER_WIDTH_PIXELS;
            boolean westDrag =
                startingDragLocation.x <= COMPONENT_RESIZE_BORDER_WIDTH_PIXELS;
            
            if(northDrag)
            {
                if(westDrag)
                {
                    return InteractiveDragType.NORTHWEST_RESIZE;
                }
                else if(eastDrag)
                {
                    return InteractiveDragType.NORTHEAST_RESIZE;
                }
                else
                {
                    return InteractiveDragType.NORTH_RESIZE;
                }
            }
            else if(southDrag)
            {
                if(westDrag)
                {
                    return InteractiveDragType.SOUTHWEST_RESIZE;
                }
                else if(eastDrag)
                {
                    return InteractiveDragType.SOUTHEAST_RESIZE;
                }
                else
                {
                    return InteractiveDragType.SOUTH_RESIZE;
                }
            }
            else if(westDrag)
            {
                return InteractiveDragType.WEST_RESIZE;
            }
            else if(eastDrag)
            {
                return InteractiveDragType.EAST_RESIZE;
            }
            else
            {
                return InteractiveDragType.MOVE;
            }
        }
    }

    /**
     * Respond to a mouse drag
     * @param e
     *          the drag event
     */
    private void managedComponentMouseDragged(MouseEvent e)
    {
        if(this.activeInteractiveDrag == null)
        {
            LOG.warning(
                    "failing to find component drag data");
        }
        else if(this.activeInteractiveDrag.getComponent() != e.getComponent())
        {
            LOG.warning(
                    "active drag component and mouse event component are different");
        }
        else
        {
            Component dragComponent = this.activeInteractiveDrag.getComponent();
            InteractionConstraints dragConstraints =
                this.interactionConstraintsMap.get(dragComponent);
            InteractiveDragType dragType = this.activeInteractiveDrag.getDragType();
            Point dragStart = this.activeInteractiveDrag.getStartingDragLocation();
            
            Rectangle newBounds = new Rectangle(
                    this.activeInteractiveDrag.getStartingBounds());
            
            // figure out the new drag location
            Point newDragLocation;
            {
                newDragLocation = new Point(e.getPoint());
                
                // now compensate for previous moves
                newDragLocation.x +=
                    dragComponent.getBounds().x -
                    this.activeInteractiveDrag.getStartingBounds().x;
                newDragLocation.y +=
                    dragComponent.getBounds().y -
                    this.activeInteractiveDrag.getStartingBounds().y;
            }
            
            int deltaX = newDragLocation.x - dragStart.x;
            int deltaY = newDragLocation.y - dragStart.y;
            
            if(dragType == InteractiveDragType.MOVE)
            {
                if(dragConstraints.isInteractiveDraggingEnabled())
                {
                    newBounds.x += deltaX;
                    newBounds.y += deltaY;
                }
            }
            else
            {
                if(dragConstraints.isInteractiveResizingEnabled())
                {
                    if(dragType == InteractiveDragType.NORTH_RESIZE ||
                       dragType == InteractiveDragType.NORTHEAST_RESIZE ||
                       dragType == InteractiveDragType.NORTHWEST_RESIZE)
                    {
                        // drag north
                        if(newBounds.height - deltaY < COMPONENT_RESIZE_BORDER_WIDTH_PIXELS * 2)
                        {
                            deltaY =
                                newBounds.height -
                                (COMPONENT_RESIZE_BORDER_WIDTH_PIXELS * 2);
                        }
                        
                        newBounds.y += deltaY;
                        newBounds.height -= deltaY;
                    }
                    else if(dragType == InteractiveDragType.SOUTH_RESIZE ||
                            dragType == InteractiveDragType.SOUTHEAST_RESIZE ||
                            dragType == InteractiveDragType.SOUTHWEST_RESIZE)
                    {
                        // drag south
                        if(newBounds.height + deltaY < COMPONENT_RESIZE_BORDER_WIDTH_PIXELS * 2)
                        {
                            deltaY =
                                (COMPONENT_RESIZE_BORDER_WIDTH_PIXELS * 2) -
                                newBounds.height;
                        }
                        
                        newBounds.height += deltaY;
                    }
                    
                    if(dragType == InteractiveDragType.EAST_RESIZE ||
                       dragType == InteractiveDragType.NORTHEAST_RESIZE ||
                       dragType == InteractiveDragType.SOUTHEAST_RESIZE)
                    {
                        // drag east
                        if(newBounds.width + deltaX < COMPONENT_RESIZE_BORDER_WIDTH_PIXELS * 2)
                        {
                            deltaX =
                                (COMPONENT_RESIZE_BORDER_WIDTH_PIXELS * 2) -
                                newBounds.width;
                        }
                        
                        newBounds.width += deltaX;
                    }
                    else if(dragType == InteractiveDragType.WEST_RESIZE ||
                            dragType == InteractiveDragType.NORTHWEST_RESIZE ||
                            dragType == InteractiveDragType.SOUTHWEST_RESIZE)
                    {
                        // drag west
                        if(newBounds.width - deltaX < COMPONENT_RESIZE_BORDER_WIDTH_PIXELS * 2)
                        {
                            deltaX =
                                newBounds.width -
                                (COMPONENT_RESIZE_BORDER_WIDTH_PIXELS * 2);
                        }
                        
                        newBounds.x += deltaX;
                        newBounds.width -= deltaX;
                    }
                }
            }
            
            dragComponent.setBounds(newBounds);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addLayoutComponent(Component comp, Object constraints)
    {
        this.addLayoutComponent(comp, (InteractionConstraints)constraints);
    }
    
    /**
     * Add a component using the given constraints
     * @param comp
     *          the component
     * @param constraints
     *          the interaction constraints
     */
    public void addLayoutComponent(Component comp, InteractionConstraints constraints)
    {
        if(constraints == null)
        {
            constraints = new InteractionConstraints(
                    true,
                    true);
        }
        
        this.interactionConstraintsMap.put(comp, constraints);
        comp.addMouseListener(this.managedComponentMouseListener);
        comp.addMouseMotionListener(this.managedComponentMotionListener);
    }

    /**
     * {@inheritDoc}
     */
    public float getLayoutAlignmentX(Container target)
    {
        return 0.0F;
    }

    /**
     * {@inheritDoc}
     */
    public float getLayoutAlignmentY(Container target)
    {
        return 0.0F;
    }

    /**
     * {@inheritDoc}
     */
    public void invalidateLayout(Container target)
    {
    }

    /**
     * {@inheritDoc}
     */
    public Dimension maximumLayoutSize(Container target)
    {
        return new Dimension(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    public void addLayoutComponent(String name, Component comp)
    {
        this.addLayoutComponent(comp, new InteractionConstraints(
                true,
                true));
    }

    /**
     * {@inheritDoc}
     */
    public void layoutContainer(Container parent)
    {
    }

    /**
     * {@inheritDoc}
     */
    public Dimension minimumLayoutSize(Container parent)
    {
        return new Dimension(5, 5);
    }

    /**
     * {@inheritDoc}
     */
    public Dimension preferredLayoutSize(Container parent)
    {
        return new Dimension(100, 100);
    }

    /**
     * {@inheritDoc}
     */
    public void removeLayoutComponent(Component comp)
    {
        comp.removeMouseListener(this.managedComponentMouseListener);
        comp.removeMouseMotionListener(this.managedComponentMotionListener);
        this.interactionConstraintsMap.remove(comp);
    }
}
