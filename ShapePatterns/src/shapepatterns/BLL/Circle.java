/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.BLL;

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
    private int size;
    
    public Circle(String name, int size)
    {
        super(name, size);
        origin = new Point(0, 0);
    }
    
    public void setShapeInfo(ShapeInfo si)
    {
        this.shapeInfo = si;
    }
    
    public void setSize(int size)
    {
        this.size = size*100;
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
