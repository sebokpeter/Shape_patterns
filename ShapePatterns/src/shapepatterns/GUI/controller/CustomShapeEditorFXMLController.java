/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.GUI.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    private TextField txtBoxSize;
    @FXML
    private TextField txtBoxLineWidth;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    
    
}
