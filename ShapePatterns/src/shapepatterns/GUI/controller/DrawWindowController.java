/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.GUI.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;
import shapepatterns.BLL.Circle;
import shapepatterns.BLL.Drawer;
import shapepatterns.BLL.Point;
import shapepatterns.BLL.Shape;
import shapepatterns.BLL.ShapeInfo;

/**
 *
 * @author sebok
 */
public class DrawWindowController implements Initializable
{
    @FXML
    private TextField txtFieldSize;
    @FXML
    private ListView<Shape> lstViewShapes;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDraw;
    @FXML
    private Button btnClear;
    @FXML
    private Canvas canvas;
    @FXML
    private ComboBox<Shape> comboBxShapeSelect;
    @FXML
    private ComboBox<DrawStrategy> comboBxDrawStrategy; 
    @FXML
    private Button btnListClear;
    @FXML
    private TextField txtFieldAddAmount;
    @FXML
    private TextField txtFieldSpacing;   
    @FXML
    private CheckBox chckBoxFilled;
    @FXML
    private TextField txtBoxLineWidth;
    @FXML
    private ColorPicker clrPckerLine;
    @FXML
    private ColorPicker clrPickerFill;
    
    private ObservableList<Shape> listViewCollection = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Shape> shapes = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<DrawStrategy> drawStrategy = FXCollections.observableArrayList(new ArrayList<>());
    
    private GraphicsContext context;
    private Drawer drawer;
    private enum DrawStrategy {Grid, Cross, Random}
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.context = canvas.getGraphicsContext2D();
        setUpComboBox();
        setUpListView();
        setUpDrawStrategy();
        setUpShapes();
        drawer = new Drawer(context);
    }   
    

    /**
     * Draws the selected shape onto the canvas
     * @param event 
     */
    @FXML
    private void btnDrawClick(ActionEvent event)
    {
        DrawStrategy selection = comboBxDrawStrategy.getValue();
        
        if (selection != null)
        {
            switch(selection)
            {
                case Cross:
                    drawer.drawCrossPattern(getSpacing(), listViewCollection);
                    break;
                case Grid:
                    drawer.drawGridPattern(getSpacing(), listViewCollection);
                    break;
                case Random:
                    drawer.drawRandomPattern(listViewCollection);
                    break;
                default:
                    System.out.println("Unknown strategy");
            }
        }
    }
    
    /**
     * Adds the selected shape to the list view
     * @param event 
     */
    @FXML
    private void btnAddClick(ActionEvent event)
    {
        Shape selectedShape = (Shape)comboBxShapeSelect.getValue(); //Get the selected shape
        if (isInt(txtFieldSize.getText()) && selectedShape != null) //Check if there is a shape seleceted and a valid size has been entered
        {
            Shape addShape = Circle.class.isInstance(selectedShape) ? new Circle("Circle", 1) : new Shape(selectedShape);
            ShapeInfo si = new ShapeInfo();
            
            int size = Integer.parseInt(txtFieldSize.getText());
            addShape.setSize(size);
            
            si.setFillColor(clrPickerFill.getValue());
            si.setLineColor(clrPckerLine.getValue());
            si.setFilled(chckBoxFilled.isSelected());
        
            if (isInt(txtBoxLineWidth.getText())) //If the line width selector contains a valid number, set it to be the line width of the shape
            {
                si.setLineWidth(Integer.parseInt(txtBoxLineWidth.getText()));
            }
            else    //Else, set it to a default value
            {
                si.setLineWidth(2);
            }
            
            addShape.setShapeInfo(si);
            
            if (isInt(txtFieldAddAmount.getText())) //Check if we need to add more than one shape to the list view
            {
                int amount = Integer.parseInt(txtFieldAddAmount.getText()); //If yes, how much
                for (int i = 0; i < amount; i++)
                {
                    listViewCollection.add(addShape);
                }
            }
            else  //We only need to add one
            {
                listViewCollection.add(new Shape(selectedShape));
            }
        }
        else
        {
            System.out.println("Please write an integer you twat!");
        }
    }
    
    /**
     * Clear the list of shapes
     * @param event 
     */
    @FXML
    private void btnListClearClick(ActionEvent event)
    {
        this.listViewCollection.clear();
    }
    
    /**
     * Clears the canvas
     * @param event 
     */
    @FXML 
    private void btnClearClick(ActionEvent event)
    {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    /**
     * If the user entered a valid value as spacing, get it, else return a default value.
     * @return 
     */
    private int getSpacing()
    {   
        if (isInt(txtFieldSpacing.getText()))
        {
            return Integer.parseInt(txtFieldSpacing.getText()); //User selected value for spacing
        }
        else
        {
            return 50; //Default value for spacing
        }
    }
    
    /**
     * Sets up the comboBox
     */
    private void setUpComboBox()
    {
        comboBxShapeSelect.setItems(shapes);
        comboBxShapeSelect.setConverter(new StringConverter<Shape>()
        {
            @Override
            public String toString(Shape object)
            {
                return object.getName();
            }

            @Override
            public Shape fromString(String string)
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
    }
    
    /**
     * Connects the list view with the listViewCollection observableArrayList, and sets up a cellFactory, to correctly display the shapes
     */
    private void setUpListView()
    {
        lstViewShapes.setItems(listViewCollection);
        lstViewShapes.setCellFactory(new Callback<ListView<Shape>, ListCell<Shape>>()
        {
            @Override
            public ListCell<Shape> call(ListView<Shape> param)
            {
                ListCell<Shape> cell = new ListCell<Shape>()
                {
                    @Override
                    protected void updateItem(Shape s, boolean bln)
                    {
                        super.updateItem(s, bln);
                        if (s != null)
                        {
                            setText(s.getName() + " (" + Integer.toString(s.getSize()) + ")");
                        }
                        else
                        {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });

    }
    
    /**
     * Populate the draw strategy comboBox
     */
    private void setUpDrawStrategy()
    {    
        drawStrategy.addAll(DrawStrategy.values());
        comboBxDrawStrategy.setItems(drawStrategy);
    }

    /**
     * Populate the comboBox with shapes
     */
    private void setUpShapes()
    {
        shapes.add(Shape.getSquare());
        shapes.add(Shape.getTriangle());
        shapes.add(Shape.getCircle());
        shapes.add(Shape.getHexagon());
        shapes.add(Shape.getPentagon());
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
}
