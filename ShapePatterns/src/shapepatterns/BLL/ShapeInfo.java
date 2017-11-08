/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.BLL;

import javafx.scene.paint.Color;

/**
 * A support class for Shape. Holds info that describe the look of a shape (is it filled, line color, fill color, line width)
 * @author sebok
 */
public class ShapeInfo
{
    private int lineWidth;
    private boolean isFilled;
    private Color lineColor;
    private Color fillColor;

    public ShapeInfo(int lineWidth, boolean filled, Color lineColor, Color fillColor)
    {
        this.lineWidth = lineWidth;
        this.isFilled = filled;
        this.lineColor = lineColor;
        this.fillColor = fillColor;
    }

    public ShapeInfo()
    {
        
    }

    public int getLineWidth()
    {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth)
    {
        this.lineWidth = lineWidth;
    }

    public boolean isFilled()
    {
        return isFilled;
    }

    public void setFilled(boolean filled)
    {
        this.isFilled = filled;
    }

    public Color getLineColor()
    {
        return lineColor;
    }

    public void setLineColor(Color lineColor)
    {
        this.lineColor = lineColor;
    }

    public Color getFillColor()
    {
        return fillColor;
    }

    public void setFillColor(Color fillColor)
    {
        this.fillColor = fillColor;
    }
}
