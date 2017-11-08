/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.GUI.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import shapepatterns.BLL.Circle;
import shapepatterns.BLL.Drawer;
import shapepatterns.BLL.Shape;
import shapepatterns.BLL.ShapeIO.ShapeReader;
import shapepatterns.BLL.ShapeIO.ShapeWriter;
import shapepatterns.BLL.ShapeInfo;
import shapepatterns.BLL.ShapeType;

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
    private ComboBox<ShapeType> comboBxShapeSelect;
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
    private ColorPicker clrPickerLine;
    @FXML
    private ColorPicker clrPickerFill;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnLoadShape;
    @FXML
    private Button btnCustomShapeEdit;

    private enum DrawStrategy {Grid, Cross, Random}
    
    private ObservableList<Shape> listViewCollection = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<ShapeType> shapes = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<DrawStrategy> drawStrategy = FXCollections.observableArrayList(new ArrayList<>());
    
    private GraphicsContext context;
    private Drawer drawer;    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.context = canvas.getGraphicsContext2D();
        setUpComboBox();
        setUpListView();
        setUpDrawStrategy();
        setUpShapes();
        drawer = new Drawer(context);
        
        clrPickerFill.setValue(Color.BLACK);
        clrPickerLine.setValue(Color.BLACK);
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
        Shape addShape = createShapeFromSettings();
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
                listViewCollection.add(new Shape(addShape));
            }
    }
    
    /**
     * Saves a shape into a file
     * @param event
     * @throws IOException 
     */
    @FXML
    private void btnSaveClick(ActionEvent event) throws IOException
    {
        ShapeWriter writer = new ShapeWriter();
        writer.createShapeFile(createShapeFromSettings());
        System.out.println("");
    }
    
    
    /**
     * Loads a saved shape from a file
     * @param event
     * @throws Exception 
     */
    @FXML
    private void btnLoadShapeClick(ActionEvent event) throws Exception
    {
        ShapeReader reader = new ShapeReader();
        Window stage;
        stage = new PopupWindow(){};
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Shape File");
        Shape s = reader.createShape(fileChooser.showOpenDialog(stage));
        if (Circle.class.isInstance(s))
        {
            s = new Circle((Circle)s);
        }
        updateUIWithShapeInfo(s);
    }
    
    /**
     * Applies the parameters of a give Shape onto the UI, so we can add as many of this shapes as we would like
     * @param s 
     */
    private void updateUIWithShapeInfo(Shape s)
    {
        int size = s.getSize();
        int lineWidth = s.getShapeInfo().getLineWidth();
        Color lineColor = s.getShapeInfo().getLineColor();
        Color fillColor = s.getShapeInfo().getFillColor();
        boolean isFilled = s.getShapeInfo().isFilled();
        
        comboBxShapeSelect.setValue(s.getType());
        txtFieldSize.setText(Integer.toString(size));
        txtBoxLineWidth.setText(Integer.toString(lineWidth));
        chckBoxFilled.setSelected(isFilled);
        clrPickerLine.setValue(lineColor);
        clrPickerFill.setValue(fillColor);
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
    
    
    
    @FXML
    private void btnCustomShapeEditClick(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("shapepatterns/GUI/view/CustomShapeEditorFXML.fxml"));
        
        Scene scene = new Scene(root);
        
        Stage stage = new Stage();
        
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Creates a shape based on the settings (fill color, line width etc)
     * @return 
     */
    private Shape createShapeFromSettings()
    {
        ShapeType selection = comboBxShapeSelect.getValue();
        Shape selectedShape = null;
        if (selection == null)
        {
            System.out.println("No shape");
            return null;
        }
        else
        {
            switch(selection)
            {
                case Triangle:
                    selectedShape = Shape.getTriangle();
                    break;
                case Square:
                    selectedShape = Shape.getSquare();
                    break;
                case Circle:
                    selectedShape = new Circle(Shape.getCircle());
                    break;
                case Hexagon:
                    selectedShape = Shape.getHexagon();
                    break;
                case Pentagon:
                    selectedShape = Shape.getPentagon();
                    break;
            }
        }
        
        Shape addShape = null;
        if (isInt(txtFieldSize.getText()) && selectedShape != null) //Check if there is a shape seleceted and a valid size has been entered
        {
            addShape = Circle.class.isInstance(selectedShape) ? new Circle(ShapeType.Circle, 1) : new Shape(selectedShape);
            ShapeInfo si = new ShapeInfo();
            
            int size = Integer.parseInt(txtFieldSize.getText());
            addShape.setSize(size);
            
            si.setFillColor(clrPickerFill.getValue());
            si.setLineColor(clrPickerLine.getValue());
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
        }
        else
        {
            System.out.println("Please write an integer you twat!");
        }
        return addShape;
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
                            setText(s.getType().toString() + " (" + Integer.toString(s.getSize()) + ")");
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
        shapes.addAll(ShapeType.values());
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
