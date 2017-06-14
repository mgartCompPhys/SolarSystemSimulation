
package RenderEngine;

import java.awt.Toolkit;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.Dimension;

public class DisplayManager{
	
	private static int WIDTH = 2000; // 1280
	private static int HEIGHT = 2000; // 720
	private static final int FPS_CAP = 120;
	
//	public DisplayManager(){}
	
	public static void createDisplay(){
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		try{
			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			WIDTH = (int) screenSize.getWidth();
//			HEIGHT = (int) screenSize.getHeight();
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Solar System Creator");
			Display.setVSyncEnabled(true);
		} catch(LWJGLException e){
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public static void updateDisplay(){
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	public static void closeDisplay(){
		Display.destroy();
	}
}
