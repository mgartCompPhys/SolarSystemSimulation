package SolarSystem;



import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import Entities.Entity;
import Models.RawModel;

public class Lines {
	Entity[][] lines;
//	int [][] lines;
	int length;
	int maxLength;
	int objectSize;
	int maxPoints;
	
	int startPosition;
	int endPosition;
	
	public Lines(int objectSize, int maxLength){
		this.length = 0;
		this.maxLength = maxLength;
		this. objectSize = objectSize;
//		this.maxPoints = objectSize * maxLength;
		this.lines = new Entity[objectSize][maxLength];
//		this.lines = new int[objectSize][maxLength];
	    this.startPosition = 0;
		initialize();
	}
	
	public void initialize(){
		for(int n = 0; n < objectSize; n++){
			for(int i = 0; i < maxLength; i++){
				lines[n][i] = new Entity(new RawModel(), new Vector3f(0f, 0f, 0f), 0f, 0f, 0f, 0f);
			}
		}
	}
	
	public Entity[][] getLines(){
		return lines;
	}
	
	public Entity getPoint(int objectNumber, int index){
		return lines[objectNumber][index];
	}
	
	public int getLength(){
		return this.length;
	}
	
	public int getMaxLength(){
		return maxLength;
	}
	
	public int getObjectSize(){
		return objectSize;
	}
	
	public void setLength(int length){
		this.length = length;
	}
	
	public  void addPoint(int objectNumber, Entity entity){
//		for(int i = 0; i < objectSize; i++){
		    if(length == maxLength){
			    allocatePoints(objectNumber);
		    }
		    lines[objectNumber][length] = entity;
//		    startPosition += 1;
//		    if(startPosition == 2000) startPosition = 0;
//		}
//		if(objectNumber == objectSize-1 && length <= maxLength){
		if(length <= maxLength){
		    this.length += 1;
		}
//		System.out.println("path length is: " + this.length);
	}
	
	private void allocatePoints(int objectNumber){
		//for(int n = 0; n < objectSize; n++){
		    for(int i = 0; i < maxLength -1; i++){
			    lines[objectNumber][i] = lines[objectNumber][i+1];
		    }
		//}
//		    System.out.println("");
//		if(objectNumber != objectSize){
		    length -= 1;
//		}
	}
}
