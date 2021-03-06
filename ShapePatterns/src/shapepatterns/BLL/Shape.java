/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.BLL;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

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
    private int size;
    private ShapeInfo shapeInfo; //A collection of different parameters (line width, is the shape filled, line and fill color)
    private ShapeType type;
    
    //There are several constructors, each serving a different purpose
    
    /**
     * An empty constructor. Used when the the shape will be modified after creation
     */
    public Shape()
    {
        this.original = new ArrayList();
        this.drawPoints = new ArrayList();
    }
    
    /**
     * 
     * @param type 
     */
    public Shape(ShapeType type)
    {
        this.original = new ArrayList();
        this.drawPoints = new ArrayList();
        this.type = type;
    }
    
    public Shape(ShapeType type, int size)
    {
        this.original = new ArrayList();
        this.drawPoints = new ArrayList();
        this.type = type;
        this.size = size;
    }

    /**
     * Create a copy of another shape
     * @param s The other shape
     */
    public Shape(Shape s)
    {
        this.original = new ArrayList<>();
        this.original = s.getPoints();
        this.drawPoints = new ArrayList();
        this.type = s.getType();
        this.size = s.getSize();
    }
    
    public ShapeType getType()
    {
        return type;
    }

    public void setType(ShapeType type)
    {
        this.type = type;
    }
    
    
    public void setShapeInfo(ShapeInfo si)
    {
        this.shapeInfo = si;
    }
    
    public ShapeInfo getShapeInfo()
    {
        return this.shapeInfo;
    }
    
    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }
    
    public void addPoint(Point p)
    {
        original.add(p);
    }
    
    public void addPoint(List<Point> points)
    {
        original.addAll(points);
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
    
    /**
     * Returns the x coordinates of all of the points that make up this shape
     * Used to draw a polygon
     * @return 
     */
    private double[] getXCoordinates()
    {
        double[] xCoords = new double[drawPoints.size()];
        
        for (int i = 0; i < drawPoints.size(); i++)
        {
            xCoords[i] = drawPoints.get(i).getX();
        }
        
        return xCoords;
    }
    
     /**
     * Returns the y coordinates of all of the points that make up this shape
     * Used to draw a polygon
     * @return 
     */
    private double[] getYCoordinates()
    {
        double[] yCoords = new double[drawPoints.size()];
        
        for (int i = 0; i < drawPoints.size(); i++)
        {
            yCoords[i] = drawPoints.get(i).getY();
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
        for (Point point : drawPoints)
        {
            point.applySize(size);
        }
    }
    
    /**
     * Create a copy of the originals list, so that it remains the same, even when the other list is modified
     * @return 
     */
    private List<Point> copyList()
    {
        List<Point> newList = new ArrayList();
        
        for (Point point : original)
        {
            newList.add(new Point(point.getX(), point.getY()));
        }
        
        return newList;
    }
    
    /**
     * Draw a polygon, using the settings find in the shapeInfo
     * @param context The lines will be drawn on this 
     * @param x 
     * @param y 
     */
    @Override
    public void draw(GraphicsContext context, double x, double y)
    {
        drawPoints = copyList();    //Create a copy of the original list
        modifySize(size);       //Apply the size 
        updatePoints(x, y);     //Move the points of the selected position (x,y), while keeping the shape
        
        if (shapeInfo.isFilled())
        {
            context.setFill(shapeInfo.getFillColor());
            context.setStroke(shapeInfo.getLineColor());
            
            context.fillPolygon(getXCoordinates(), getYCoordinates(), original.size());
        }
        else
        {
            context.setStroke(shapeInfo.getLineColor());
            context.setLineWidth(shapeInfo.getLineWidth());

            context.strokePolygon(getXCoordinates(), getYCoordinates(), original.size());       
        }
    }
    
    /**
     * Make a triangle
     * @return 
     */
    public static Shape getTriangle()
    {
        double x = 0;
        double y = 0;
        Shape t = new Shape(ShapeType.Triangle);
        t.addPoint(new Point(x, y));
        t.addPoint(new Point(x-50, y+50));
        t.addPoint(new Point(x+50, y+50));
        
        t.setSize(2);
        
        return t;
    }
    
    /**
     * Make a square
     * @return 
     */
    public static Shape getSquare()
    {
        double x = 0;
        double y = 0;
        Shape s = new Shape(ShapeType.Square);
        s.addPoint(new Point(x, y));
        s.addPoint(new Point(x+50, y));
        s.addPoint(new Point(x+50, y-50));
        s.addPoint(new Point(x, y-50));
        
        return s;
    }
    
    /**
     * make a circle
     * @return 
     */
    public static Circle getCircle()
    {
        double x = 0;
        double y = 0;
        
        Circle s = new Circle(ShapeType.Circle, 0);
        
        s.addPoint(new Point(x, y));
        
        return s;
    }

    /**
     * Make a hexagon
     * @return 
     */
    public static Shape getHexagon(){
        
        double x = 0;
        double y = 0 ;
        
        Shape h = new Shape(ShapeType.Hexagon);
        
        h.addPoint(new Point(x, y));
        h.addPoint(new Point(x, y+25));
        h.addPoint(new Point(x+25, y+40));
        h.addPoint(new Point(x+50, y+25));
        h.addPoint(new Point(x+50, y));
        h.addPoint(new Point(x+25, y-15));
        
        return h;
    }
    
    /**
     * Make a pentagon
     * @return 
     */
    public static Shape getPentagon(){
        
        double x = 0;
        double y = 0 ;
        Shape p = new Shape(ShapeType.Pentagon);
        p.addPoint(new Point(x, y));
        p.addPoint(new Point(x-25, y+75));
        p.addPoint(new Point(x+50, y+125));
        p.addPoint(new Point(x+125, y+75));
        p.addPoint(new Point(x+100, y));
        
        return p;
    }
}
