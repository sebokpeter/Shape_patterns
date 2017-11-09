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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    private Canvas canvas;
    @FXML
    private ComboBox<ShapeType> comboBxShapeSelect;
    @FXML
    private ComboBox<DrawStrategy> comboBxDrawStrategy; 
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
    private Label lblTriangleCount;
    @FXML
    private Label lblSquareCount;
    @FXML
    private Label lblHexagonCount;
    @FXML
    private Label lblPentagonCount;
    @FXML
    private Label lblCustomCount;
    @FXML
    private Label lblCircleCount;

    private enum DrawStrategy {Grid, Cross, Random}
    
    private ObservableList<Shape> listViewCollection = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<ShapeType> shapes = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<DrawStrategy> drawStrategy = FXCollections.observableArrayList(new ArrayList<>());
    
    private GraphicsContext context;
    private Drawer drawer;    
    
    private Shape currentCustomShape;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.context = canvas.getGraphicsContext2D();
        setUpComboBox();
        setUpListView();
        setUpDrawStrategy();
        setUpShapes();
        drawer = new Drawer(context);
        
        clrPickerFill.setValue(Color.BLACK);    //Set color values to black by default
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
            
            updateLabels();
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
        if (addShape == null)
        {
            System.out.println("Selected shape does not exist");
            return;
        }
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
        if (s.getType() == ShapeType.Circle)    //Check to see if the shape is a circele, and if it is, apply the correct setting
        {
            s = new Circle(s);
        }
        else if (s.getType() == ShapeType.Custom)    //If it is a custom shape, set the field
        {
            currentCustomShape = s;
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
        drawer.resetLabelCounts();
        updateLabels();
    }

    @FXML
    private void btnCustomShapeEditClick(ActionEvent event) throws IOException
    {
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("shapepatterns/GUI/view/CustomShapeEditorFXML.fxml"));
        FXMLLoader loader = new FXMLLoader((getClass().getClassLoader().getResource("shapepatterns/GUI/view/CustomShapeEditorFXML.fxml")));
        Parent root = loader.load();
        
        CustomShapeEditorFXMLController controller = loader.getController();
        controller.setParentWindow(this);
        
        Scene scene = new Scene(root);
        
        Stage stage = new Stage();
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the custom shape to the shape created by the user, and updates the UI
     * @param currentCustomShape 
     */
    public void setCurrentCustomShape(Shape currentCustomShape)
    {
        this.currentCustomShape = currentCustomShape;
        updateUIWithShapeInfo(currentCustomShape);
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
                case Custom:
                    if (currentCustomShape != null)
                    {
                        selectedShape = currentCustomShape;
                    }
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
            return addShape;
        }
        else
        {
            System.out.println("Please write an integer you twat, or check your shape!");
            return null;
        }
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
     * Updates the labels displaying the amount of shapes drawn
     */
    private void updateLabels()
    {
        drawer.updateLabel(ShapeType.Triangle, lblTriangleCount);
        drawer.updateLabel(ShapeType.Square, lblSquareCount);
        drawer.updateLabel(ShapeType.Circle, lblCircleCount);
        drawer.updateLabel(ShapeType.Hexagon, lblHexagonCount);
        drawer.updateLabel(ShapeType.Pentagon, lblPentagonCount);
        drawer.updateLabel(ShapeType.Custom, lblCustomCount);
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
