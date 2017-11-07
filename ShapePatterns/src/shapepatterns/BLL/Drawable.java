/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapepatterns.BLL;

import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author sebok
 */
public interface Drawable
{
    public void draw(GraphicsContext context, double x, double y);
    
}
