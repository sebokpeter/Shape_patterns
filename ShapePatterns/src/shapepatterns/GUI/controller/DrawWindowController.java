/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.GUI.controller;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;
import shapepatterns.BLL.Shape;

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
    
    private ObservableList<Shape> listViewCollection = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Shape> shapes = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<DrawStrategy> drawStrategy = FXCollections.observableArrayList(new ArrayList<>());
    private GraphicsContext context;
    private enum DrawStrategy {Grid, Cross, Random}
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.context = canvas.getGraphicsContext2D();
        setUpComboBox();
        setUpListView();
        setUpDrawStrategy();
        setUpShapes();
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
                    drawCrossPattern();
                    break;
                case Grid:
                    drawGridPattern();
                    break;
                case Random:
                    drawRandomPattern();
                    break;
                default:
                    System.out.println("Unknown strategy");
            }
        }
    }
    
    
    private void drawCrossPattern()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void drawGridPattern()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void drawRandomPattern()
    {
        Random random = new Random();
        
        for (Shape shape : listViewCollection)
        {
            double rX = random.nextInt((int)canvas.getWidth());
            double rY = random.nextInt((int)canvas.getHeight());
            
            shape.draw(context, rX, rY);
        }
    }
    
    @FXML
    private void btnAddClick(ActionEvent event)
    {
        Shape selectedShape = (Shape)comboBxShapeSelect.getValue();
        if (isInt(txtFieldSize.getText()) && selectedShape != null)
        {
            int size = Integer.parseInt(txtFieldSize.getText());
            selectedShape.setSize(size);
            listViewCollection.add(new Shape(selectedShape));
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
    }
    /**
     * Checks if a string can be parsed into a integer
     * @param s 
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
