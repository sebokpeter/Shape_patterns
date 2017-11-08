/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.BLL.ShapeIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import shapepatterns.BLL.Point;
import shapepatterns.BLL.Shape;
import shapepatterns.BLL.ShapeInfo;
import shapepatterns.BLL.ShapeType;

/**
 * This class creates Shape object from a file
 * @author sebok
 */
public class ShapeReader
{
    
    private List<String> data;
    
    public ShapeReader()
    {       
        data = new ArrayList();
    }
    
    public Shape createShape(File file) throws Exception
    {
        Shape s;
        readFile(file);
        s = reconstructShape();
        return s;
    }

    private void readFile(File file) throws Exception
    {
        if (file == null)
        {
            return;
        }
        
        try
        {
            if (!file.getPath().endsWith(".shape"))
            {
                throw new Exception("Wrong file type");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        BufferedReader bf = new BufferedReader(new FileReader(file.getPath()));
        
        try
        {
            String next = bf.readLine();
            
            while(next != null)
            {
                data.add(next);
                next = bf.readLine();
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            bf.close(); //Close the reader, even if an exception occured
        }
    }

    private Shape reconstructShape() throws Exception
    {
        //All the parameters necesearry to reconstruct a shape
        Shape s = new Shape();
        ShapeInfo si = new ShapeInfo();
        ShapeType type = null;
        int size = 0;
        int lineWidth = 0;
        boolean isFilled = false;
        Color lineColor = null;
        Color fillColor = null;
        List<Point> points = new ArrayList();
        
        //Iterate through our parameters and assign them to the correct value
        for (String d : data)
        {
            String[] param = d.replaceAll(" ", "").split(":");
            
            //First item is the name of the paramater, the second is the value
            switch(param[0])
            {
                case "size":
                    size = Integer.parseInt(param[1]);
                    break;
                case "type":
                    type = ShapeType.valueOf(param[1]);
                    break;
                case "lineWidth":
                    lineWidth = Integer.parseInt(param[1]);
                    break;
                case "filled":
                    isFilled = Boolean.valueOf(param[1]);
                    break;
                case "fillColor":
                    fillColor = Color.valueOf(param[1]);
                    break;
                case "lineColor":
                    lineColor = Color.valueOf(param[1]);
                    break;
            }
            
            if (param[0].startsWith("p"))
            {
                String[] coords = param[1].split(";");
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);
                
                points.add(new Point(x, y));
            }
        }
        
        //Set the parameters
        s.setType(type);
        s.setSize(size);
        s.addPoint(points);
        
        //Apply parameters to our ShapeInfo
        si.setFillColor(fillColor);
        si.setLineColor(lineColor);
        si.setFilled(isFilled);
        si.setLineWidth(lineWidth);
        
        s.setShapeInfo(si);
        
        return s;
    }
}
