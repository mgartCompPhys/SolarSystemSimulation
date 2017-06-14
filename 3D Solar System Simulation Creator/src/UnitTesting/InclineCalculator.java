package UnitTesting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class InclineCalculator {
	public String[] name;
	public String[] type;
	public double[] x;
	public double[] degree;
	public double[] incline;
	
	private int maxLength;
	
	public InclineCalculator()throws IOException{
	    importFile();
	    for(int i = 0; i < incline.length; i ++){
	    	incline[i] = calculateIncline(x[i], degree[i], i);
	    }
	}
	
	private double calculateIncline(double x, double degree, int i){
		return Math.sqrt( Math.pow(x / Math.cos(Math.toRadians(degree)), 2) - (x * x));
	}
	
    private void setSize(int size){
    	name = new String[size];
    	type = new String[size];
    	x = new double[size];
    	degree = new double[size];
    	incline = new double[size];
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
		    double x = Double.parseDouble(st.nextToken(SPACE));
		    double degree = Double.parseDouble(st.nextToken(SPACE));
		    
		    this.name[i] = name;
		    this.type[i] = type;
		    this.x[i] = x;
		    this.degree[i] = degree;
		    
		    maxLength(name);
        }
	}
	
	private void maxLength(String line){
		if(line.length() > maxLength){
			maxLength = line.length();
		}
	}
	
	public String toString(){
		String shift = String.format("%" + (maxLength - 4 + 1) + "s", "");
		String output = "name " + shift + "incline (AU)\n";
		for(int i = 0; i < name.length; i++){
	        int gap = maxLength - name[i].length() + 1;

	        String spaces = String.format("%" + gap + "s", ""); 
			output += name[i] + spaces + incline[i] + "\n";  
		}
		return output;
	}
	
	public static void main(String args[])throws IOException{
		System.out.println("Initiate Program!\n");
		InclineCalculator inclineCalculator = new InclineCalculator();
		System.out.print(inclineCalculator.toString() + "\n");
		System.out.print("End of Program!");
	}

}
