package MainSystem;

import StringText.Font;



import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Entity;
import Models.RawModel;
import Models.SphereModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import RenderEngine.SphereRenderer;
import SolarSystem.Lines;
import SolarSystem.Planet;
import SolarSystem.TrailLines;
import SpaceBackground.SpaceBackgroundRenderer;
import Textures.ModelTexture;
import ToolBox.RK3D;
import ToolBox.Time;

public class SolarSystemCreator{
	   private final double EARTH_MASS = 5.974*Math.pow(10, 24);
	   private final static double AU = 1.496*Math.pow(10, 11);
	   private final double EARTH_RADIUS = 6371000; // 6.371 mill
	   
	   private double G = 6.674*Math.pow(10, -11);
	   
	   public static double P = 0.05 * AU;
	   public static double R = 50 * AU; // Radius of solar system.
	   public static int pixels = 2000; // 200
	   public static float zScale;
	   public double timeStep = 100;
	   public double IntervalTime = 864;
	   public double IntervalAccel = 0;
	   public double time;
	   
	   public static double METERS_PER_FLOAT = AU / ((pixels/2) / (R/AU));
	   
	   public static double SOLAR_SYSTEM_MASS;
	   
	   public static String DESTROYED;
	   
	public SolarSystemCreator(){}
	
	private void initGL(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.create();
			Display.setTitle("Solar System Creator");
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);                    
 
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);                
        GL11.glClearDepth(1);                                       
 
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
 
        GL11.glViewport(0,0,width,height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
 
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static void main(String[] args) throws Throwable{
		
    	Loader loader = new Loader();
		
    	SolarSystemCreator ssc = new SolarSystemCreator();
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int WIDTH = (int) screenSize.getWidth();
		int HEIGHT = (int) screenSize.getHeight();
//		System.out.println(WIDTH + " / " + HEIGHT);
 //   	ml.initGL(2000, 2000);
    	
    	SphereModel sphere = new SphereModel();
     	sphere.drawSphereQuad(1, 50, 50);
     	float[] vertices = sphere.getVertices();
     	
     	float[] vertex = {
     			0f,0f,0f
     	};
     	
		boolean ExitProgram = true;
		do{
			DESTROYED = "Destroyed:";
			ssc.IntervalTime = 864;
			ssc.setTime(0);
		    ArrayList<Planet> planets = ssc.importSolarSystem(); // Populates the solar system if read correctly.
		    WIDTH = 3000;
		    HEIGHT = 2000;
		    ssc.initGL(WIDTH, HEIGHT); // CREATES THE DISPLAY!
		    int solarSystemObjectCount = planets.size();
		    ArrayList<Entity> entities = new ArrayList<Entity>(solarSystemObjectCount);
//		    Lines lines = new Lines(planets.size(), 1000);
//		    lines.initialize();
		
		    RawModel point = loader.loadToVAO(vertex);
		    float[] line;
//		    RawModel trail = loader.loadToVAO(line);
		    RawModel model = loader.loadToVAO(vertices);
		    
		    TrailLines trailLines = new TrailLines(planets.size(), 1000);
//		    trails.initialize();
		    for(int i = 0; i < planets.size(); i++){
			    float size = (float) (planets.get(i).getRadius() / METERS_PER_FLOAT);
			    float Xi = (float) adjustXByRatio(planets.get(i).getX());
			    float Yi = (float) adjustYByRatio(planets.get(i).getY());
			    float Zi = (float) adjustZByRatio(planets.get(i).getZ());
			    ModelTexture texture = new ModelTexture(loader.loadSphereTexture(planets.get(i).getTexture()));
				TexturedModel texturedModel = new TexturedModel(model, texture);
				entities.add(new Entity(texturedModel, new Vector3f(Xi/2, (Zi/2), (Yi/2)), 0, 0, 0, size*2));
//			    System.out.println("X[" + i + "]: " + Xi + "f");
//			    lines.addPoint(i, new Entity(point, new Vector3f(Xi/2, (Zi/2), (Yi/2)), 0, 0, 0, 0.1f));
			    
//			    float[] lineVertex = ;
//			    RawModel bigLine = loader.loadToVAO(lineVertex);
			    float[] dot = new float[3];
			    dot[0] = Xi/2;
			    dot[1] = Zi/2;
			    dot[2] = Yi/2;
			    trailLines.addPoint(i, dot);
			    
		    }	
//		    float[] lineVertex = { -1000f, -1000f, -1000f, 
//	     			1000f, 1000f, 1000f
//	     	};
//		    RawModel bigLine = loader.loadToVAO(lineVertex);
		
		    Camera camera = new Camera(entities.get(0));
		    camera.setName(planets.get(0).getName());

		    SphereRenderer sphereRenderer = new SphereRenderer(loader);
		    Matrix4f matrix = sphereRenderer.getProjectionMatrix();
		    SpaceBackgroundRenderer spaceRenderer = new SpaceBackgroundRenderer(loader, matrix);
		
		    String path = "res/monofonto.ttf"; // gets text font.
		    float size = 24;
		    // Using the TrueTypeFont class from slick ultils or java.awt is not optimal with new openGL pipeline.
		    Font helpText = new Font(path, size); // creates a true type font.
		    Font text = new Font(path, 40);
		
		    boolean enableHelp = false;
		    while(!Display.isCloseRequested()){
			    // simulate
                planets = ssc.updateSolarSystem(planets , ssc);
//			    ml.deletePlanet(planets, entities);
//			    System.out.println("camera: " + camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
			    double[] R = ssc.centerOfMass(planets);
			    for(int i = 0; i < planets.size(); i++){
				    float X = (float) adjustXByRatio(planets.get(i).getX());
				    float Y = (float) adjustYByRatio(planets.get(i).getY());
				    float Z = (float) adjustZByRatio(planets.get(i).getZ());
//				    System.out.println("X: " + X/2 + ", Y: " + Z/2 + ", Z: " + Y/2);
			        entities.get(i).setPosition(X/2, (Z/2), (Y/2)); 
			        entities.get(i).setScale(toggleSizeScale(planets.get(i)));
//					lines.addPoint(i, new Entity(point, new Vector3f(X/2, (Z/2), (Y/2)), 0, 0, 0, 0.1f));
//				    float[] lineVertex = lines.vertices(i);
//				    RawModel bigLine = loader.loadToVAO(lineVertex);
//				    lines.addPoint(i, new Entity(bigLine, new Vector3f(0, 0, 0), 0, 0, 0, 0.5f));
					
					float[] dot = new float[3];
				    dot[0] = X/2;
				    dot[1] = Z/2;
				    dot[2] = Y/2;
				    trailLines.addPoint(i, dot);
			    }

			    camera.changeCameraCenter(entities, planets);
			    camera.move();
			
			    sphereRenderer.prepare();		
			    spaceRenderer.render(camera);
			    sphereRenderer.renderTexture(entities, camera);
//			    sphereRenderer.renderMesh(entities, camera);
//			    sphereRenderer.renderSeam(entities, camera);
			    
//			    sphereRenderer.renderLine(lineEntity, camera, bigLine);

			    Time timeRec = new Time(ssc.getTime(), "sec");
			    timeRec.DaysToYears();
			    String timeDisplay = ssc.roundOff(timeRec.getTime()) + " " + timeRec.getUnit();
			    text.drawText("time: " + timeDisplay, 100, 50);
			    text.drawText("time step: " + ssc.getIntervalTime()*100+ " sec", 100, 100);
			    text.drawText("interval accel: " + ssc.getIntervalAccel()*100 + " sec", 100, 150);
			    text.drawText("current focus: " + camera.getName(), (WIDTH/2)-40, 50);
			
//			    String displayDestroy = DESTROYED;
//			    if(DESTROYED.charAt(DESTROYED.length()-1) == ',') displayDestroy = displayDestroy.substring(0, DESTROYED.length()-1);
//			    text.drawText(displayDestroy, 100, 1900);
			
/*			    for(int i = 0; i < lines.getObjectSize(); i++){
				    sphereRenderer.renderPoints(i, lines, camera, point);
			    } 
*/			    
			    // TOGGLE FOR TRAILS
			    boolean toggle = true;
			    if(toggleTrails(toggle)){
			        for(int i = 0; i < trailLines.getObjectSize(); i++){
			    	    Loader trailLoader = new Loader();
			    	    RawModel bigLine = trailLoader.loadToVAO(trailLines.getTrail(i).getVertices());
			    	    Entity e = new Entity(bigLine, new Vector3f(0, 0, 0), 0, 0, 0, 1);
			    	    sphereRenderer.renderLine(e, camera, bigLine);
			    	    trailLoader.cleanUp();
			        }
			    }
			
			    ssc.renderHelpDisplay(helpText, ssc.enableHelpDisplay(), WIDTH);
			
			    Display.update();
			    Display.sync(120);
		    }
		    sphereRenderer.cleanUp();
		    loader.cleanUp();
		    Display.destroy();
		
		    Scanner sc = new Scanner(System.in);
		    System.out.print("Does user wish to run a solar system? (yes or no)");
		    String response = sc.nextLine();
		    if(response.equals("no")) ExitProgram = true;
		    if(response.equals("yes")) ExitProgram = false;
		}while(!ExitProgram);
		System.out.println("Thank you for Using the Solar System Creator.");
		System.exit(0);
	} 
	
	private ArrayList<Planet> deletePlanet(ArrayList<Planet> planets, ArrayList<Entity> entities){
		String destroyed = "";
		int removeCount = 0;
		for(int i = 0; i < planets.size(); i++){
        	if(planets.get(i).getX() > this.R || planets.get(i).getY() > this.R || planets.get(i).getZ() > this.R){
        		destroyed += " " + planets.get(i).getName() + ",";
        		planets.remove(i);
        		entities.remove(i);
        		removeCount++;
        	}
        	else if(planets.get(i).getX() < -this.R || planets.get(i).getY() < -this.R || planets.get(i).getZ() < -this.R){
        		destroyed += " " + planets.get(i).getName() + ",";
        		planets.remove(i);
        		entities.remove(i);
           		removeCount++;
        	}
		}
		DESTROYED += destroyed;
		return planets;
	}
		
	private ArrayList<Planet> updateSolarSystem(ArrayList<Planet> planets, SolarSystemCreator ssc){
        // RK Method:
        changeIntervalTime();
        for(int c = 0; c < IntervalTime; c++){
        RK3D rk3d = new RK3D(planets, ssc.getTime(), timeStep);

        ArrayList<Planet> temp = new ArrayList<Planet>(4);
        for(int n = 0; n < planets.size(); n++){
        	Planet p = new Planet();
       		p = rk3d.newInterval(planets.get(n) ,planets, n);
           temp.add(p); 
        }
        planets = temp;
        rk3d.NewT();
        ssc.setTime(rk3d.getTime());  

        double[] R = centerOfMass(planets);
        double[] V = centerOfMassVelocity(planets);
        for(int i = 0; i < planets.size(); i++){
        	planets.get(i).setX(planets.get(i).getX() - R[0]);
        	planets.get(i).setY(planets.get(i).getY() - R[1]);
        	planets.get(i).setZ(planets.get(i).getZ() - R[2]);
        	
        	planets.get(i).setVx(planets.get(i).getVx() - V[0]);
        	planets.get(i).setVy(planets.get(i).getVy() - V[1]);
        	planets.get(i).setVz(planets.get(i).getVz() - V[2]);
//            System.out.println(planets.get(i).toString(i));
        }
                
        }
    	return planets;
	}
		
	private ArrayList<Planet> importSolarSystem() throws IOException{
		ArrayList<Planet> planets = new ArrayList<Planet>(100);
	    Scanner sc = new Scanner(System.in);
	    System.out.print("Enter Solar System's file name.");
	    String fileName = "res/";
	    fileName += sc.nextLine();
	    fileName += ".txt";
	    File file = new File(fileName);
	        
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			br.readLine();
	            String currentLine = "";
	            double TOTAL_MASS = 0;
	            double METERS_PER_FLOAT = AU / ((pixels/2) / (R/AU));
	            while((currentLine = br.readLine()) != null){
	                String SPACE = new String (" ");
	                String token = "";
	                StringTokenizer st = new StringTokenizer(currentLine, SPACE);

	                String name = st.nextToken(SPACE); // st.nextToken()
	                String type = st.nextToken(SPACE);
	    		    double mass = EARTH_MASS * Double.parseDouble(st.nextToken(SPACE));
	    		    double radius = EARTH_RADIUS * Double.parseDouble(st.nextToken(SPACE));
	    		    double size = Double.parseDouble(st.nextToken(SPACE)) / 10;
//	    		    size = radius / METERS_PER_FLOAT;
	    		    double eccentricity = Double.parseDouble(st.nextToken(SPACE));
	    		    double x = AU * Double.parseDouble(st.nextToken(SPACE));
	    		    double y = AU * Double.parseDouble(st.nextToken(SPACE));
	    		    double z = AU * Double.parseDouble(st.nextToken(SPACE));
	    		    String texture = st.nextToken(SPACE);
	    		    TOTAL_MASS += mass;
	    		    
	    		    Planet planet = new Planet(name, type, mass, radius, size, eccentricity, x, y, z);
	    		    planet.setTexture(texture);
	    		    planets.add(planet);
	            }
	            SOLAR_SYSTEM_MASS = TOTAL_MASS;
			br.close();
			planets.trimToSize();
			double[] R = centerOfMass(planets);
//			System.out.println("Center of Mass: X: " + R[0]);
			for(int i = 0; i < planets.size(); i++){
				planets.get(i).setX(planets.get(i).getX()-R[0]);
				planets.get(i).setY(planets.get(i).getY()-R[1]);
				planets.get(i).setZ(planets.get(i).getZ()-R[2]);
				double dx = planets.get(i).getX() - R[0];
				double dy = planets.get(i).getY() - R[1];
				double dz = planets.get(i).getZ() - R[2];
				double vx = 0;
				double vy = eccentricOrbit(dx, dy, dz, TOTAL_MASS - planets.get(i).getMass(), planets.get(i).getEccentricity());
				if(i == 0){ vy = 0; }
				double vz = 0;
    		    planets.get(i).setVx(vx);
    		    planets.get(i).setVy(vy);
    		    planets.get(i).setVz(vz);
//				System.out.println(planets.get(i).toString(i));
			}
			return planets;
	}
	
	private double[] centerOfMass(ArrayList<Planet> solarSystem){
		 double[] R = new double[3];
		double MASS = 0;
		for(Planet p: solarSystem){
			R[0] += p.getMass()*p.getX();
			R[1] += p.getMass()*p.getY();
			R[2] += p.getMass()*p.getZ();
			MASS += p.getMass();
		}
		R[0] = R[0] / MASS;
		R[1] = R[1] / MASS;
		R[2] = R[2] / MASS;
		return R;
	}
	
	private double[] centerOfMassVelocity(ArrayList<Planet> solarSystem){
		 double[] R = new double[3];
		double MASS = 0;
		for(Planet p: solarSystem){
			R[0] += p.getMass()*p.getVx();
			R[1] += p.getMass()*p.getVy();
			R[2] += p.getMass()*p.getVz();
			MASS += p.getMass();
		}
		R[0] = R[0] / MASS;
		R[1] = R[1] / MASS;
		R[2] = R[2] / MASS;
		return R;
	}
    
    private double circularOrbit(double x, double y, double z, double M){
        double r = Math.sqrt(x*x + y*y + z*z);
        return Math.sqrt(G * M / r);
    }
    
    private double eccentricOrbit(double x, double y, double z, double M, double e){
    	double r = Math.sqrt(x*x + y*y + z*z);
    	double a = r * (1 + e * Math.cos(0)) / (1 - e*e);
    	double u = G * M;
    	return Math.sqrt(u*((2/r) - (1/a)));
    }
    
    public boolean enableHelpDisplay(){
    	if(Keyboard.isKeyDown(Keyboard.KEY_P)){
    		return true;
    	}
    	return false;
    }
    
    public void renderHelpDisplay(Font text, boolean enable, int width){ // Explains control system
    	if(enable == true){
    	    text.drawText("help display:", (int) (width*0.75), 200);
    	    text.drawText("time speed +/-: L_Shift/L_CTRL", (int) (width*0.75), 250);
    	    text.drawText("alter time speed: 1/2", (int) (width*0.75), 300);
    	    text.drawText("camera angle: Left mouse click", (int) (width*0.75), 350);
    	    text.drawText("far zoom: d-wheel up/d-wheel down : in/out", (int) (width*0.75), 400);
    	    text.drawText("close zoom: up/down arrow keys : in/out", (int) (width*0.75), 450);
    	    text.drawText("orientation focus: left/right arrow keys", (int) (width*0.75), 500);
    	    text.drawText("close solar system: top left red X", (int) (width*0.75), 550);
    	}
    	else{
    		text.drawText("Press P for help display.", (int) (width*0.75), 200);
    	}
    }
    // SCALES DISTANCE in all 3 position vectors down (SPACE IS REALLY BIG!).
    public static double adjustXByRatio(double x){  // Adjust values to the Raito to fit on graph.
        x = x + R; // 0 + 2.5*10^11;
        return x / (R / (pixels / 2)) - (pixels / 2); // 2.5*10^11 / 2.5*10^8 = 1000; 3.996*10^11
    }
    
    public static double adjustYByRatio(double y){
        y = y + R;
        return y / (R / (pixels / 2)) - (pixels / 2);
    }
    
    public static double adjustZByRatio(double z){
    	z = z + R;
        return z / (R / (pixels / 2)) - (pixels / 2);
    }
    
    public double getTime(){
    	return this.time;
    }
    
    public void setTime(double time){
    	this.time = time;
    }
    
    public double getIntervalTime(){
    	return this.IntervalTime;
    }
    
    public double getIntervalAccel(){
    	return this.IntervalAccel;
    }
    //Rounds of time display to only 2 decimals.
	public String roundOff(double time){
		String text = "" + time;
		int decimalPosition = 0;
		for(int i = 0; i < text.length(); i ++){
			if(text.charAt(i) == '.') decimalPosition = i;
		}
		String decimal = text.substring(decimalPosition+1, text.length());
		if(decimal.length() == 1){
			return text.substring(0, decimalPosition+2);
		}
		if(decimal.length() == 0){
			return text.substring(0, decimalPosition);
		}
		return text.substring(0, decimalPosition+3);
	}
    // Alters the evolution speed of the system
    public void changeIntervalTime(){
    	if(Keyboard.isKeyDown(Keyboard.KEY_1)){
    		IntervalAccel ++;
    	}
    	if(Keyboard.isKeyDown(Keyboard.KEY_2)){
    		IntervalAccel --;
    		if(IntervalAccel < 0) IntervalAccel = 0;
    	}
    	if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
    		IntervalTime += IntervalAccel; // up 1 hour
    	}
    	if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
    		IntervalTime -= IntervalAccel; // down 1 hour
    		if(IntervalTime < 0) IntervalTime = 0;
    	}
    } 
    
    public static boolean toggleTrails(boolean toggle){
    	if(Keyboard.isKeyDown(Keyboard.KEY_L)){
    		toggle = false;
    	}
    	else{
    		toggle = true;
    	}
    	return toggle;
    }
    
    static public float toggleSizeScale(Planet planet){
    	if(Keyboard.isKeyDown(Keyboard.KEY_M)){
    		return (float) (planet.getSize());
    	}
    	else{
    		return  ((float) (planet.getRadius() / METERS_PER_FLOAT)) * 2;

    	}
    }
}
