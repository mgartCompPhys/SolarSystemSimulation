package UnitTesting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.lwjgl.util.vector.Vector3f;


public class PlanetPositionCalculator {
	public String[] name;
	public String[] type;
	public double[] distance;
	public double[] degree;
	public Vector3f[] position;
	
	private int maxLength;
	
	public PlanetPositionCalculator()throws IOException{
	    importFile();
	    for(int i = 0; i < position.length; i ++){
	    	position[i] = calculatePosition(distance[i], degree[i], i);
	    }
	}
	
	private Vector3f calculatePosition(double distance, double degree, int i){
		Vector3f position = new Vector3f();
		position.x = (float) (Math.cos(Math.toRadians(degree)) * distance);
		position.y = 0;
		position.z = (float) (Math.sin(Math.toRadians(degree)) * distance);
		return position;
//		return Math.sqrt( Math.pow(x / Math.cos(Math.toRadians(degree)), 2) - (x * x));
	}
	
    private void setSize(int size){
    	name = new String[size];
    	type = new String[size];
    	distance = new double[size];
    	degree = new double[size];
    	position = new Vector3f[size];
    }
	
	private void importFile()throws IOException{
		Scanner sc = new Scanner(System.in);
		String fileName = "src/unitTesting/";
		System.out.print("FileName: ");
	    fileName += sc.nextLine();
	    fileName += ".txt";
	    File file = new File(fileName);
	    
	    FileReader fr = new FileReader(file.getAbsoluteFile());
	    System.out.println("file has been successfully recieved!");
		BufferedReader br = new BufferedReader(fr);
		int size = Integer.parseInt(br.readLine());
		br.readLine();
		
		setSize(size);		
		
		String currentLine = "";
        for(int i = 0; (currentLine = br.readLine()) != null; i++){
            String SPACE = new String (" ");
            String token = "";
            StringTokenizer st = new StringTokenizer(currentLine, SPACE);

            String name = st.nextToken(SPACE);
            String type = st.nextToken(SPACE);
		    double distance = Double.parseDouble(st.nextToken(SPACE));
		    double degree = Double.parseDouble(st.nextToken(SPACE));
		    
		    this.name[i] = name;
		    this.type[i] = type;
		    this.distance[i] = distance;
		    this.degree[i] = degree;
		    
		    maxLength(name, type);
        }
	}
	
	private void maxLength(String line, String type){
		if(line.length() > maxLength){
			maxLength = line.length();
		}
	}
	
	public String toString(){
		String shift = String.format("%" + (maxLength - 4 + 1) + "s", "");
		String fSpace = String.format("%" + 10 + "s", "");
		String zeroValue = String.format("%" + 2 + "s", "");
		String output = "name " + shift + "X " + fSpace  + "Y " + zeroValue + "Z\n";
		
		int largestNum = 0;
		for(int i = 0; i < name.length; i++){
	        int gap = maxLength - name[i].length() + 1;

	        String spaces = String.format("%" + gap + "s", "");
	        int num = 10 - (new String(position[i].x + "")).length() +1;
	        String xSpace = String.format("%" + num + "s", "");
	        String ySpace = String.format("%" + 2 + "s", "");
			output += name[i] + spaces + position[i].x + xSpace+ position[i].y + ySpace + position[i].z + "\n"; 
		}
		return output;
	}
	
	public static void main(String args[])throws IOException{
		System.out.println("Initiate Program!\n");
		PlanetPositionCalculator positionCalculator = new PlanetPositionCalculator();
		System.out.print(positionCalculator.toString() + "\n");
		System.out.print("End of Program!");
	}

}
