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
 * A collection points (vertices), that define a shape
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
     * Draw a line from each point to the next
     * @param context The lines will be drawn on this 
     */
    @Override
    public void draw(GraphicsContext context)
    {
        context.setFill(Color.BLACK);
        context.setStroke(Color.BLACK);
        
        /*
        for (int i = 0; i < points.size() - 1; i++)
        {
            context.strokeLine(points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
        }
        
        
        context.strokeLine(points.get(points.size()  - 1).getX(), points.get(points.size() - 1).getY(), points.get(0).getX(), points.get(0).getY()); //Draw a line from the last point to the first
        */
        
        context.strokePolygon(getXCoordinates(), getYCoordinates(), points.size());
    }
    
}
