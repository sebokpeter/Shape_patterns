/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.GUI.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import shapepatterns.BLL.Point;
/**
 *
 * @author sebok
 */
public class DrawWindowController implements Initializable
{
    
    private Label label;
    @FXML
    private ChoiceBox<?> cBoxShapeSelect;
    @FXML
    private TextField txtFieldSize;
    @FXML
    private ListView<?> lstViewShapes;
    @FXML
    private Button btnAdd;
    @FXML
    private ChoiceBox<?> cBoxDrawStrategy;
    @FXML
    private Button btnDraw;
    @FXML
    private Button btnClear;
    @FXML
    private Canvas canvas;
    
    private GraphicsContext context;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        context = canvas.getGraphicsContext2D();
    }    

    @FXML
    private void btnDrawClick(ActionEvent event)
    {
        double x = canvas.getWidth()/2;
        double y = canvas.getHeight()/2;
        Point p = new Point(x, y);
        
        p.draw(context);
    }
    
}
