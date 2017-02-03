/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagames.transform;

import java.awt.Graphics;

/**
 *
 * @author sgroenjes
 */
public interface Drawable {
    void updateWorld();//Update the World Matrix
    
    void render(Graphics g); // Draw the object with the passed Graphics
}
