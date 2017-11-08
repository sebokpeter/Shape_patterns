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
    private final Map data;
    private final Map pointsMap;
    private String fileName;
    
    public ShapeWriter()
    {
        data = new HashMap<String, Object>();
        pointsMap = new HashMap<String, double[]>();
        File dir = new File("Shapes");
        if (!dir.exists())
        {
            dir.mkdir();
        }
    }
    
    public void createShapeFile(Shape shape) throws IOException
    {
        fillHashMap(shape);
        createFile();
    }
    
    /**
     * Create hashmaps from the data from the shape
     * @param shape 
     */
    private void fillHashMap(Shape shape)
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
        data.put("type", type);
        data.put("size", size);
        data.put("lineWidth", lineWidth);
        data.put("filled", filled);
        data.put("lineColor", lineColor);
        data.put("fillColor", fillColor);
        
        int i = 0;
        
        //Fill out the hasmap containing the points
        for (Point point : points)
        {
            pointsMap.put("p"+Integer.toString(i), new Double[]{point.getX(), point.getY()});
            i++;
        }
    } 

    /**
     * Iterate through the hashmaps, and write their contents into a file
     * @throws IOException 
     */
    private void createFile() throws IOException
    {
        List<String> lines = new ArrayList<>();
        
        for (Object e : data.entrySet()) 
        {
            Map.Entry<String,Object> entry = (Map.Entry<String,Object>)e;
            String key = entry.getKey();
            String value = entry.getValue().toString();
            
            String d = key+": "+value;
            lines.add(d);

            System.out.println(key + ": " + value);
        }
        
        for (Object e : pointsMap.entrySet()) 
        {
            Map.Entry<String,Object> entry = (Map.Entry<String,Object>)e;
            String key = entry.getKey();
            Object[] value = (Object[])entry.getValue();
            double d1 = (double)value[0];
            double d2 = (double)value[1];
            
            String d = key+": " + Double.toString(d1) + "; " + Double.toString(d2);
            lines.add(d);  
            
            System.out.println(key + ": " + Double.toString(d1) + "; " + Double.toString(d2));
        }
        
        File f = new File("Shapes/" + fileName + ".shape");
        
        int n = 1;
        while(f.exists())   //See if a file with this name already exist
        {
           f = new File("Shapes/"+fileName + Integer.toString(n) + ".txt"); //If is does, then append a number to its end, so we dont overwite
           n++;
        }
        
        Path path = Paths.get(f.getPath()); //Get the path of the file
        Files.write(path, lines, Charset.forName("UTF-8")); //Write the data into the file
    }
}
