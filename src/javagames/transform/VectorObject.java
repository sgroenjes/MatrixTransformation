/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagames.transform;

import java.awt.*;
import java.util.ArrayList;
import javagames.util.Matrix3x3f;
import javagames.util.Vector2f;

/**
 *
 * @author sgroenjes
 */
public class VectorObject implements Drawable {
   
    private ArrayList<Vector2f> points;
    private Matrix3x3f worldMatrix;
    private Color color;
    private Point location;
    private float rotation;
    private float scale;
    
    
    public VectorObject(int nodes, Color c){
        points = new ArrayList<Vector2f>();
        calculatePoints(nodes);
        color = c;
        location = new Point(0,0);
        rotation = 0;
        scale = 1;
        worldMatrix = Matrix3x3f.identity();
    }
    
    public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotate) {
		this.rotation = rotate;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location.setLocation(location);
	}

	public float getRadius(){
		return 100*scale;
	}
	
	private void calculatePoints(int nodes){
		if(nodes%2==1){//odd number
			for(int i=0;i<nodes;i++){
    			points.add(new Vector2f((float)Math.cos((i*2.0*Math.PI/nodes+Math.PI/2.0))*100,
    					(float)Math.sin(i*2.0*Math.PI/nodes+Math.PI/2.0)*-100));
    		}
		}
		else{//even number
			for(int i=0;i<nodes;i++){
				points.add(new Vector2f((float)Math.cos(Math.PI/nodes+i*2.0*Math.PI/nodes)*100,
						(float)Math.sin(Math.PI/nodes+i*2.0*Math.PI/nodes)*100));
			}
		}
    }
    
    @Override
    public void updateWorld(){
    	worldMatrix = Matrix3x3f.scale(scale,scale);
        worldMatrix = worldMatrix.mul(Matrix3x3f.rotate(rotation));
        worldMatrix = worldMatrix.mul(Matrix3x3f.translate(new Vector2f(location.x,location.y)));
    }
    
    @Override
    public void render(Graphics g){
    	Graphics2D g2 = (Graphics2D)g;
    	g2.setColor(color);
    	g2.setStroke(new BasicStroke(2));
    	
        for(int i=0;i<points.size()-1;i++){
        	g2.drawLine((int)worldMatrix.mul(points.get(i)).x, (int)worldMatrix.mul(points.get(i)).y, 
        			(int)worldMatrix.mul(points.get(i+1)).x, (int)worldMatrix.mul(points.get(i+1)).y);
        }
        g2.drawLine((int)worldMatrix.mul(points.get(points.size()-1)).x, (int)worldMatrix.mul(points.get(points.size()-1)).y, 
        		(int)worldMatrix.mul(points.get(0)).x, (int)worldMatrix.mul(points.get(0)).y);
    }
    
}
