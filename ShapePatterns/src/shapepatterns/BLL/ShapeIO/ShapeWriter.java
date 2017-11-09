/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.BLL.ShapeIO;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
import shapepatterns.BLL.Point;
import shapepatterns.BLL.Shape;
import shapepatterns.BLL.ShapeInfo;
import shapepatterns.BLL.ShapeType;

/**
 * Writes a shape into a file so it can be recreated later
 * @author sebok
 */
public class ShapeWriter
{   
    private String fileName;
    private List<String> lines;
     
    public ShapeWriter()
    {
        lines = new ArrayList<>();  
        File dir = new File("Shapes");
        if (!dir.exists())
        {
            dir.mkdir();
        }
    }
    
    
    public void createShapeFile(Shape shape) throws IOException
    {
        fillList(shape);
        createFile();
    }
    
    /**
     * Create hashmaps from the data from the shape
     * @param shape 
     */
    private void fillList(Shape shape)
    {
        ShapeInfo si = shape.getShapeInfo();
        
        //Get the data from the shape
        int size = shape.getSize();
        List<Point> points = shape.getPoints();
        this.fileName = shape.getType().toString(); //The filename is based on the type
        ShapeType type = shape.getType();
        
        int lineWidth = si.getLineWidth();
        boolean filled = si.isFilled();
        Color lineColor = si.getLineColor();
        Color fillColor = si.getFillColor();
        
        //Fill up the hashmap
        lines.add("type: " + type.toString());
        lines.add("size: " + size);
        lines.add("lineWidth: " + lineWidth);
        lines.add("filled: " + filled);
        lines.add("lineColor: " + lineColor);
        lines.add("fillColor: " +fillColor);
        
        int i = 0;
        
        //Fill out the hasmap containing the points
        for (Point point : points)
        {
            lines.add("p"+Integer.toString(i) + ": " + Double.toString(point.getX()) + "; "+ Double.toString(point.getX()));
            i++;
        }
    } 

    /**
     * Iterate through the hashmaps, and write their contents into a file
     * @throws IOException 
     */
    private void createFile() throws IOException
    {   
        //Create path
        File f = new File("Shapes/" + fileName + ".shape");
        
        int n = 1;
        while(f.exists())   //See if a file with this name already exist
        {
           f = new File("Shapes/"+fileName + Integer.toString(n) + ".shape"); //If is does, then append a number to its end, so we dont overwite already existing file
           n++;
        }
        
        Path path = Paths.get(f.getPath()); //Get the path of the file
        Files.write(path, lines, Charset.forName("UTF-8")); //Write the data into the file
    }
}
