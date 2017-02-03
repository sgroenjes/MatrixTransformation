package javagames.transform;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javagames.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class MatrixTransformation extends JFrame implements Runnable {
	private static final int SCREEN_W = 1280;
	private static final int SCREEN_H = 720;
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	private RelativeMouseInput mouse;
	private KeyboardInput keyboard;
	private Point point = new Point( 0, 0 );
	VectorObject square;
	VectorObject hexagon;
	VectorObject triangle;
	int deltax = 1;
	int deltay = 1;
	float hexRotateSpeed = .01f;
	float triRotateSpeed = .01f;

	public MatrixTransformation() {
	}

	protected void createAndShowGUI() {
		Canvas canvas = new Canvas();
		canvas.setSize(SCREEN_W,SCREEN_H);
		canvas.setBackground(Color.black);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setTitle("Matrix Transformation");
		setIgnoreRepaint(true);
		pack();
		// Add key listeners
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		// Add mouse listeners
		// For full screen : mouse = new RelativeMouseInput( this );
		mouse = new RelativeMouseInput(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		setVisible(true);
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();
		
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void run() {
		running = true;
		initialize();
		while (running) {
			gameLoop();
		}
	}

	private void gameLoop() {
		processInput();
		renderFrame();
		sleep(10L);
	}

	private void renderFrame() {
		do {
			do {
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					render(g);
				} finally {
					if (g != null) {
						g.dispose();
					}
				}
			} while (bs.contentsRestored());
			bs.show();
		} while (bs.contentsLost());
	}

	private void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException ex) {
		}
	}

	private void initialize() {
		square = new VectorObject(4,Color.red);
		hexagon = new VectorObject(6,Color.GREEN);
		triangle = new VectorObject(3,Color.cyan);
		disableCursor();
		
		square.setLocation(new Point(SCREEN_W/2,SCREEN_H/2));
		hexagon.setLocation(new Point(SCREEN_W/2,SCREEN_H/2));
		triangle.setLocation(new Point(SCREEN_W/2,SCREEN_H/2));
		
		triangle.setScale(.2f);
		square.setScale(.4f);
		hexagon.setScale(.2f);
		
	}

	private void processInput() {
		keyboard.poll();
		mouse.poll();
		
		//Automatic Object
		int bounds = (int) (100*square.getScale()/Math.sqrt(2.0f));//distance from center of object to midpoint of an edge
		
		if(square.getLocation().x < bounds)
			deltax=1;
		if(square.getLocation().x > SCREEN_W-bounds)
			deltax=-1;
		if(square.getLocation().y < bounds)
			deltay=1;
		if(square.getLocation().y > SCREEN_H-bounds)
			deltay=-1;
		
		square.setLocation(new Point(square.getLocation().x+deltax,square.getLocation().y+deltay));
		
		//Keyboard Controlled Object
		if(keyboard.keyDown(KeyEvent.VK_W)&&hexagon.getLocation().y>100*hexagon.getScale()){
			hexagon.setLocation(new Point(hexagon.getLocation().x,hexagon.getLocation().y-1));
		}
		if(keyboard.keyDown(KeyEvent.VK_A)&&hexagon.getLocation().x>100*hexagon.getScale()){
			hexagon.setLocation(new Point(hexagon.getLocation().x-1,hexagon.getLocation().y));
		}
		if(keyboard.keyDown(KeyEvent.VK_S)&&hexagon.getLocation().y<SCREEN_H-100*hexagon.getScale()){
			hexagon.setLocation(new Point(hexagon.getLocation().x,hexagon.getLocation().y+1));
		}
		if(keyboard.keyDown(KeyEvent.VK_D)&&hexagon.getLocation().x<SCREEN_W-100*hexagon.getScale()){
			hexagon.setLocation(new Point(hexagon.getLocation().x+1,hexagon.getLocation().y));
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_SPACE)){
			hexRotateSpeed *= -1;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_Q)&&hexRotateSpeed>.001){
			hexRotateSpeed *= .9;
		}
		if(keyboard.keyDownOnce(KeyEvent.VK_E)&&hexRotateSpeed<.1){
			hexRotateSpeed *= 1.1;
		}
		hexagon.setRotation(hexRotateSpeed+hexagon.getRotation());
		
		//Mouse Controlled Object
		if(mouse.buttonDown(MouseEvent.BUTTON1)){//left mouse button - rotate counter-clockwise
			triRotateSpeed = .01f;
		}
		else if(mouse.buttonDown(MouseEvent.BUTTON3)){//right mouse button - rotate clockwise
			triRotateSpeed = -.01f;
		}
		else
			triRotateSpeed = 0;
		triangle.setRotation(triRotateSpeed+triangle.getRotation());
		point.x = mouse.getPosition().x;
	    point.y = mouse.getPosition().y;
		
	    triangle.setLocation(point);
	    
	    //Update World Matrices of all vector objects
	    square.updateWorld();
	    hexagon.updateWorld();
	    triangle.updateWorld();
	}

	private void render(Graphics g) {
		square.render(g);
		hexagon.render(g);
		triangle.render(g);				
	}
	
	private void disableCursor() {
	      Toolkit tk = Toolkit.getDefaultToolkit();
	      Image image = tk.createImage( "" );
	      Point point = new Point( 0, 0 );
	      String name = "CrossHair";
	      Cursor cursor = tk.createCustomCursor( image, point, name );
	      setCursor( cursor );
	}

	protected void onWindowClosing() {
		try {
			running = false;
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		final MatrixTransformation app = new MatrixTransformation();
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.createAndShowGUI();
			}
		});
	}
}