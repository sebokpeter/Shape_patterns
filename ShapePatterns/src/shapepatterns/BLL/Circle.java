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
    private int size;
    
    public Circle(ShapeType type, int size)
    {
        super(type, size);
        origin = new Point(0, 0);
    }

    public Circle(Circle c)
    {
        this.origin = c.getOrigin();
        this.size = c.getSize();
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
        this.size = size*100;
    }
    
    public List<Point> getPoints()
    {
        List<Point> ol = new ArrayList<>();
        ol.add(origin);
        return ol;
    }
    
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
