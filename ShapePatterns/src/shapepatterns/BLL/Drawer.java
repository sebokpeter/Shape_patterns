/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.BLL;

import com.sun.javaws.LaunchDownload;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;

/**
 * This class is responsible for drawing on the canvas
 * @author sebok
 */
public class Drawer
{
    private GraphicsContext context;
    private Canvas canvas;
    private int triangleCount;
    private int squareCount;
    private int circleCount;
    private int hexCount;
    private int pentagonCount;
    private int customCount;
    
    public Drawer(GraphicsContext context)
    {
        this.context = context;
        canvas = context.getCanvas();
    }
    
     /**
     * Draws a X pattern, made out of the shapes in the list view, centered on the center of the canvas
     */
    public void drawCrossPattern(int spacing, List<Shape> shapesToDraw)
    {
        List<Point> drawPositions = new ArrayList();
        
        //Select both axis of the matrix representing the canvas
        for (int i = 0; i < canvas.getWidth(); i++)
        {
            for (int j = 0; j < canvas.getHeight(); j++)
            {
                if (i == j) //Get the ponts that are on the axis (\)
                {
                    if (i % spacing == 0)
                    {
                        drawPositions.add(new Point(i - spacing, j)); //Offset the point to be centered
                        continue;
                    }
                }
                if (i + j == canvas.getWidth()) //Get the points that are on the opposite axis (/)
                {
                    if (i % spacing == 0)
                    {
                        drawPositions.add(new Point(i - spacing, j));
                        continue;
                    }
                }
            }
        }

        //Loop through all the points where we can draw a shape, and if there is still a shape in the listView that has not been drawn, draw it
        drawAtPoints(drawPositions, shapesToDraw);
    }
    
    /**
     * Draw in a grid pattern
     * @param spacing
     * @param shapesToDraw 
     */
    public void drawGridPattern(int spacing, List<Shape> shapesToDraw)
    {
        List<Point> drawPositions = new ArrayList();
        
        for (int i = 0; i < canvas.getHeight(); i++)    //Go ffrom top to bottom, and select each correct row
        {
            if (i % spacing == 0)   //We are in a corect row
            {
                for (int j = 0; j < canvas.getWidth(); j+=spacing) //Go through the row,
                {
                    drawPositions.add(new Point(i, j));
                }
            }
        }
        
        drawAtPoints(drawPositions, shapesToDraw);
    }
    
    /**
    *Draw at a random position
    */
    public void drawRandomPattern(List<Shape> shapesToDraw)
    {
        Random random = new Random();
        
        for (Shape shape : shapesToDraw)
        {
            double rX = random.nextInt((int)canvas.getWidth());
            double rY = random.nextInt((int)canvas.getHeight());
            
            countShapeTypes(shape.getType());
            shape.draw(context, rX, rY);
        }
    }
    
     /**
     * Given a list of points, this method will draw the shapes found in the listViewCollection. 
     * If there are no more shapes in the listViewCollection, or more points in the supplied list, the method will return
     * @param drawPositions 
     */
    private void drawAtPoints(List<Point> drawPositions, List<Shape> shapesToDraw)
    {
        //Loop through all the points where we can draw a shape, and if there is still a shape in the listView that has not been drawn, draw it
        for (int i = 0; i < drawPositions.size(); i++)
        {
            if (i == shapesToDraw.size())
            {
                return;
            }
            
            Shape s = shapesToDraw.get(i);
            double x = drawPositions.get(i).getX();
            double y = drawPositions.get(i).getY();
            countShapeTypes(s.getType());
           
            s.draw(context, x, y);
        }
    }
    
    private void countShapeTypes(ShapeType type)
    {
        switch(type)
        {
             case Triangle:
                triangleCount++;
                break;
             case Square:
                squareCount++;
                break;
            case Circle:
                circleCount++; 
                break;
            case Hexagon:
                hexCount++;
                break;
            case Pentagon:
                pentagonCount++;
                break;
            case Custom:
                customCount++;
                break;   
        }
    }
    
    public void updateLabel(ShapeType type, Label lbl)
    {
        switch(type)
        {
             case Triangle:
                lbl.setText(Integer.toString(triangleCount));
                break;
             case Square:
                lbl.setText(Integer.toString(squareCount));
                break;
            case Circle:
                lbl.setText(Integer.toString(circleCount));
                break;
            case Hexagon:
                lbl.setText(Integer.toString(hexCount));
                break;
            case Pentagon:
                lbl.setText(Integer.toString(pentagonCount));
                break;
            case Custom:
                lbl.setText(Integer.toString(customCount));
                break;   
        }
    }

    public void resetLabelCounts()
    {
        triangleCount = 0;
        squareCount = 0;
        circleCount = 0;
        hexCount = 0;
        pentagonCount = 0;
        customCount = 0;
    }
}
