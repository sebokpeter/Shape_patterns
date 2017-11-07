/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.BLL;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author sebok
 * A collection points (vertices), that define a shape. The Shape class only stores the relative position of the points to each other.
 * One list holds the relative position of the points. The scaling is applied to these points, by multiplying the coordinates of the points.
 * This list is the copied to a new one, where the points are 'moved' to the correct position
 */
public class Shape implements Drawable
{
    private List<Point> original; //This list defines the shape by telling us the relative position of points to each other
    private List<Point> drawPoints; //This list is used to draw the shape to the correct position
    private String name;
    private int size;
    
    public Shape()
    {
        this.original = new ArrayList();
        drawPoints = new ArrayList();
    }
    
    public Shape(String name)
    {
        this.original = new ArrayList();
        this.name = name;
    }
    
    public Shape(String name, int size)
    {
        this.original = new ArrayList();
        this.name = name;
        this.size = size;
    }
    
    /**
     * Create a copy of another shape
     * @param s 
     */
    public Shape(Shape s)
    {
        this.original = new ArrayList<>();
        this.original = s.getPoints();
        this.name = s.getName();
        this.size = s.getSize();
    }
    
    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void addPoint(Point p)
    {
        original.add(p);
    }

    public List<Point> getPoints()
    {
        List<Point> currentPoints = new ArrayList<>();
        
        for (Point p : original)
        {
          currentPoints.add(new Point(p.getX(), p.getY()));
        }
        
        return currentPoints;
    }
    
    private double[] getXCoordinates()
    {
        double[] xCoords = new double[original.size()];
        
        for (int i = 0; i < original.size(); i++)
        {
            xCoords[i] = original.get(i).getX();
        }
        
        return xCoords;
    }
    
    private double[] getYCoordinates()
    {
        double[] yCoords = new double[original.size()];
        
        for (int i = 0; i < original.size(); i++)
        {
            yCoords[i] = original.get(i).getY();
        }
        
        return yCoords;
    }
    
    
    /**
     * Since the Shape class only stores the relative position of the points, we need to update them if we want to draw them on different coordinates
     * @param x
     * @param y 
     */
    private void updatePoints(double x, double y)
    {
        for (Point point : drawPoints)
        {
            point.updateCoordinates(x, y);
        }
    }
    
    /**
     * Multiplying the coordinates of all the points scales the shape
     * @param size 
     */
    private void modifySize(double size)
    {
        for (Point point : original)
        {
            point.applySize(size);
        }
    }
    
    /**
     * Draw a line from each point to the next
     * @param context The lines will be drawn on this 
     */
    @Override
    public void draw(GraphicsContext context, double x, double y)
    {
        context.setFill(Color.BLACK);
        context.setStroke(Color.BLACK);
        
        modifySize(size);
        drawPoints = original;
        updatePoints(x, y);
        
        context.strokePolygon(getXCoordinates(), getYCoordinates(), original.size());
    }
    
    
    public static Shape getTriangle()
    {
        double x = 0;
        double y = 0;
        Shape t = new Shape("Triangle");
        t.addPoint(new Point(x, y));
        t.addPoint(new Point(x-50, y-50));
        t.addPoint(new Point(x+50, y-50));
        
        t.setSize(2);
        
        return t;
    }
    
    public static Shape getSquare()
    {
        double x = 0;
        double y = 0;
        Shape s = new Shape("Square");
        s.addPoint(new Point(x, y));
        s.addPoint(new Point(x+50, y));
        s.addPoint(new Point(x+50, y-50));
        s.addPoint(new Point(x, y-50));
        
        return s;
    }
}
