/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.BLL;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author sebok
 * 
 * Represents the vertices of a shape.
 */
public class Point implements Drawable
{
    private double x;
    private double y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    @Override
    public void draw(GraphicsContext context)
    {
        context.setFill(Color.BLACK);
        context.setStroke(Color.BLACK);
       
        context.fillOval(x, y, 5, 5);
    }
    
    public double distance(Point p)
    {
       double x1 = x;
       double x2 = p.getX();
       
       double y1 = y;
       double y2 = p.getY();
       
       double pow1 = Math.pow((x2-x1), 2);
       double pow2 = Math.pow((y2-y1), 2);
       
       return Math.sqrt(pow1+pow2);
       
    }
}
