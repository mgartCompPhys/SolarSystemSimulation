package SolarSystem;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import Entities.Entity;
import Models.RawModel;

public class Trail {
	private float[] vertices;
	private int length;
	private int maxLength;
	
	public Trail(int maxLength){
		this.vertices = new float[maxLength*3];
		initialize();
	}
	
	public void initialize(){
		for(int i = 0; i < vertices.length/3; i+=2){
			vertices[i] = 0;
			vertices[i+1] = 0;
			vertices[i+2] = 0;
		}
	}
	
	public Trail(float[] vertices){
		this.vertices = vertices;
	}
	
	public int getLength(){
		return this.length;
	}
	
	public float[] getVertices(){
		return vertices;
	}
	
	public void setVertices(float[] vertices){
		this.vertices = vertices;
	}
	
	public float[] getPoint(int index){
		float[] point = new float[3];
		point[0] = vertices[index*3];
		point[1] = vertices[index*3+1];
		point[2] = vertices[index*3+2];
		return point;
	}
	
	public void setPoint(int index, float[] point){
		vertices[index*3] = point[0];
		vertices[index*3+1] = point[1];
		vertices[index*3+2] = point[2];
	}
	
	public void addLength(){
		this.length++;
	}
	
	public void subtractLength(){
		this.length--;
	}
	
}