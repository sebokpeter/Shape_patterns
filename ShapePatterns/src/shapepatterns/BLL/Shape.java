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

    public Shape()
    {
        this.points = new ArrayList();
    }
    
    public void addPoint(Point p)
    {
        points.add(p);
    }
    
    @Override
    public void draw(GraphicsContext context)
    {
        context.setFill(Color.BLACK);
        context.setStroke(Color.BLACK);
        
        for (int i = 0; i < points.size() - 1; i++)
        {
            context.strokeLine(points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
        }
    }
    
}
