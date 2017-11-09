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
 * A circle is a special shape, with one point as the origin
 * @author sebok
 */
public class Circle extends Shape
{
    private Point origin;
    private Point drawPoint;
    private ShapeInfo shapeInfo;
    private ShapeType type;
    private int size;   //The size of the shape controls the radious of the circe
    
    public Circle(ShapeType type, int size)
    {
        origin = new Point(0, 0);
        this.type = type;
    }

    public Circle(Circle c)
    {
        this.origin = c.getOrigin();
        this.size = c.getSize();
    }
    
    public Circle(Shape c)
    {
        this.origin = c.getPoints().get(0);
        this.shapeInfo = c.getShapeInfo();
        this.size = c.getSize()*100;
        this.type = ShapeType.Circle;
    }
    
    public ShapeType getType()
    {
        return this.type;
    }
    
    public void setShapeInfo(ShapeInfo si)
    {
        this.shapeInfo = si;
    }
    
    public ShapeInfo getShapeInfo()
    {
        return shapeInfo;
    }
    
    public Point getOrigin()
    {
        return this.origin;
    }
    
    public void setSize(int size)
    {
        this.size = size*100; //Scale it by a 100, so it is more consistent with the other shapes
    }
    
    public int getSize()
    {
        return this.size/100;
    }
    
    public List<Point> getPoints()
    {
        List<Point> ol = new ArrayList<>();
        ol.add(origin);
        return ol;
    }
    
    /**
     * Draw a circle at the specified coordinates
     * @param context
     * @param x
     * @param y 
     */
    @Override
    public void draw(GraphicsContext context, double x, double y)
    {
        drawPoint = new Point(origin);
        drawPoint.updateCoordinates(x, y);
        
        if (shapeInfo.isFilled())
        {
            context.setFill(shapeInfo.getFillColor());
            context.setStroke(shapeInfo.getLineColor());
            
            context.fillOval(drawPoint.getX(), drawPoint.getY(), size, size);
        }
        else
        {
            context.setStroke(shapeInfo.getLineColor());
            context.setLineWidth(shapeInfo.getLineWidth());
            
            context.strokeOval(drawPoint.getX(), drawPoint.getY(), size, size);
        }
    }
    
}
