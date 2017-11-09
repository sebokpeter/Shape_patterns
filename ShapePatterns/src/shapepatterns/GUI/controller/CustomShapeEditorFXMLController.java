/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.GUI.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import shapepatterns.BLL.Point;
import shapepatterns.BLL.Shape;
import shapepatterns.BLL.ShapeInfo;
import shapepatterns.BLL.ShapeType;

/**
 * FXML Controller class
 *
 * @author sebok
 */
public class CustomShapeEditorFXMLController implements Initializable
{

    @FXML
    private ColorPicker clrPickerLine;
    @FXML
    private ColorPicker clrPickerFill;
    @FXML
    private Label lblPointCounter;
    @FXML
    private TextField txtBoxLineWidth;
    @FXML
    private Canvas canvas;
    @FXML
    private CheckBox chckBoxFill;
    
    private GraphicsContext context;
    private Shape customShape;
    private Double dX, dY;          //Initial offset of the shape
    private int pointCount;
    private List<Point> points;
    ShapeInfo si;
    
    DrawWindowController parent;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        context = canvas.getGraphicsContext2D();
        customShape = new Shape();
        customShape.setSize(1);
        points = new ArrayList();
        pointCount = 0;
        si = new ShapeInfo();
        customShape.setShapeInfo(si);
        customShape.setType(ShapeType.Custom);
        
        clrPickerFill.setValue(Color.BLACK);
        clrPickerLine.setValue(Color.BLACK);
    }    

    /**
     * Determine the location of the click, and create two points:
     *  - One is added to the shape with the appropriate offset (the distance of the first point from 0;0)
     *  - One is used to draw on the location of the click
     * @param event 
     */
    @FXML
    private void mouseCanvasClick(MouseEvent event)
    {   
        Point p;
        
        if (dX == null || dY == null) //If they are null, this is the first click
        {
            dX = event.getX();  //Determine the offset of the X coordinate
            dY = event.getY();  //Determine the offset of the Y coordinate
        }
        
        double posX = event.getX(); //Get the click position
        double posY = event.getY();
        
        p = new Point(posX-dX, posY-dY); //Apply the offset
        customShape.addPoint(p);    //Add the offset point to the shape
        points.add(new Point(posX, posY));  //Create a new point at the location of the click for drawing
        pointCount++;
        update();
    }

    /**
     * Redraw the canvas
     */
    private void update()
    {   
        lblPointCounter.setText(Integer.toString(pointCount));
        clear();
        
        drawPoints();
        if (dX != null && dY != null)
        {
            customShape.draw(context, dX, dY);
        }
    }

    /**
     * The checkbox selection changed. Change the shape and the display accordingly.
     * @param event 
     */
    @FXML
    private void chckBoxFillAction(ActionEvent event)
    {
        si.setFilled(chckBoxFill.isSelected());
        update();
    }

    /**
     * New line color has been picked. Change the shape and the display accordingly.
     * @param event 
     */
    @FXML
    private void clrPickerLineAction(ActionEvent event)
    {
        si.setLineColor(clrPickerLine.getValue());
        update();
    }

    /**
     * New fill color has been selected. Change the shape and the display accordingly.
     * @param event 
     */
    @FXML
    private void clrPickerFillAction(ActionEvent event)
    {
        si.setFillColor(clrPickerFill.getValue());
        if (chckBoxFill.isSelected()) //Only draw again, if it has an effect
        {
            update();
        }
    }
    
    /**
     * New line width has been entered. Check if it is a valid number, and if yes, update the shape and display.
     * @param event 
     */
    @FXML
    private void txtBoxLineWidthAction(ActionEvent event)
    {
        if (!isInt(txtBoxLineWidth.getText()))
        {
            System.out.println("Please enter an integer (you twat)");
            return;
        }
        
        si.setLineWidth(Integer.parseInt(txtBoxLineWidth.getText()));
        update();
    }
    

    /**
     * Clear the canvas, and everything
     * @param event 
     */
    @FXML
    private void btnClearCanvasClick(ActionEvent event)
    {
        clear();
        resetShape();
    }
    
    /**
     * Draw the points onto the canvas
     */    
    private void drawPoints()
    {
        for (Point point : points)
        {
            point.draw(context, point.getX(), point.getY());
        }
    }
    
    /**
     * Clear the canvas
     */
    private void clear()
    {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    /**
    * Checks if a string can be parsed into a integer
    * @param s String to test
    * @return 
    */
    private boolean isInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Resets everything connected to the shape into their default values
     */
    private void resetShape()
    {
        dX = null;
        dY = null;
        
        customShape = new Shape();
        
        si = new ShapeInfo();
        si.setFillColor(clrPickerFill.getValue());
        si.setLineColor(clrPickerLine.getValue());
        if (!isInt(txtBoxLineWidth.getText()))
        {
            si.setLineWidth(1);
        }
        else
        {
            si.setLineWidth(Integer.parseInt(txtBoxLineWidth.getText()));
        }
       
        customShape.setShapeInfo(si);
        customShape.setSize(1);
        points.clear();
        
        pointCount = 0;
        lblPointCounter.setText(Integer.toString(pointCount));
    }
    
    public void setParentWindow(DrawWindowController parent)
    {
        this.parent = parent;
    }

    @FXML
    private void btnSaveClick(ActionEvent event) throws Exception
    {
        if (customShape == null || si == null)
        {
            throw new Exception("Shape or ShapeInfo not initialized");
        }
        
        //If the color have not been set, just set the to their default value
        if (si.getFillColor() == null)
        {
            si.setFillColor(clrPickerFill.getValue());
        }
        if (si.getLineColor()== null)
        {
            si.setLineColor(clrPickerLine.getValue());
        }
        parent.setCurrentCustomShape(customShape);
    }
    

}
