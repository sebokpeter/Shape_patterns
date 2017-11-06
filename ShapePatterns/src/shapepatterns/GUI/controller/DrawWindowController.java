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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import shapepatterns.BLL.Point;
import shapepatterns.BLL.Shape;
/**
 *
 * @author sebok
 */
public class DrawWindowController implements Initializable
{
    
    private Label label;
    private ChoiceBox<?> cBoxShapeSelect;
    @FXML
    private TextField txtFieldSize;
    @FXML
    private ListView<?> lstViewShapes;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDraw;
    @FXML
    private Button btnClear;
    @FXML
    private Canvas canvas;
    
    private GraphicsContext context;
    @FXML
    private ComboBox<Shape> comboBxShapeSelect;
    @FXML
    private ComboBox<?> comboBxDrawStrategy;
    
    private List<Shape> shapes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        shapes = new ArrayList<>();
        context = canvas.getGraphicsContext2D();
        setUpComboBox();
    }    

    /**
     * Draws the selected shape onto the canvas
     * @param event 
     */
    @FXML
    private void btnDrawClick(ActionEvent event)
    {
        
        double x = canvas.getWidth()/2;
        double y = canvas.getHeight()/2;
        Point p = new Point(x, y);
        Point p2 = new Point(x+50, y+50);
        Point p3 = new Point(x-50, y+50);
        
        Shape s = new Shape("Triangle");
        s.addPoint(p);
        s.addPoint(p2);
        s.addPoint(p3);
        
        s.draw(context);
        if (!shapes.contains(s))
        {
            shapes.add(s);
        }
        updateComboBox();
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
        comboBxShapeSelect.setItems(FXCollections.observableArrayList(shapes));
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
        shapes.add(new Shape("Name"));
        updateComboBox();
    }
    
    /**
     * Updates the content of the combo box. Used when a new item is added to the list
     */
    private void updateComboBox()
    {
        comboBxShapeSelect.setItems(FXCollections.observableArrayList(shapes));
    }
}
