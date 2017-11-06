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
 * A collection points (vertices), that define a shape. The Shape class only stores the relative position of the points to each other
 */
public class Shape implements Drawable
{
    private List<Point> points;
    private String name;
    private int size;
    
    public Shape()
    {
        this.points = new ArrayList();
    }
    
    public Shape(String name)
    {
        this.points = new ArrayList();
        this.name = name;
    }
    
    public Shape(String name, int size)
    {
        this.points = new ArrayList();
        this.name = name;
        this.size = size;
    }
    
    /**
     * Create a copy of another shape
     * @param s 
     */
    public Shape(Shape s)
    {
        this.points = new ArrayList<>();
        this.points = s.getPoints();
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
        points.add(p);
    }

    public List<Point> getPoints()
    {
        List<Point> currentPoints = new ArrayList<>();
        
        for (Point p : points)
        {
          currentPoints.add(new Point(p.getX(), p.getY()));
        }
        
        return currentPoints;
    }
    
    private double[] getXCoordinates()
    {
        double[] xCoords = new double[points.size()];
        
        for (int i = 0; i < points.size(); i++)
        {
            xCoords[i] = points.get(i).getX();
        }
        
        return xCoords;
    }
    
    private double[] getYCoordinates()
    {
        double[] yCoords = new double[points.size()];
        
        for (int i = 0; i < points.size(); i++)
        {
            yCoords[i] = points.get(i).getY();
        }
        
        return yCoords;
    }
    
    
    /**
     * Since the Shape class only stores the relative position of the points, we need to update them if we want to draw them on different coordinates
     * @param x
     * @param y 
     */
    public void updatePoints(double x, double y)
    {
        for (Point point : points)
        {
            point.updateCoordinates(x, y);
        }
    }
    
    /**
     * Draw a line from each point to the next
     * @param context The lines will be drawn on this 
     */
    @Override
    public void draw(GraphicsContext context)
    {
        context.setFill(Color.BLACK);
        context.setStroke(Color.BLACK);
        
        
        context.strokePolygon(getXCoordinates(), getYCoordinates(), points.size());
    }
    
}
