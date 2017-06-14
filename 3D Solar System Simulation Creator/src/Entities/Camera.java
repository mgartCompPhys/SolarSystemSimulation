package Entities;

import java.util.ArrayList;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import SolarSystem.Planet;
//import SpaceFlight.SpaceShip;

public class Camera {
	private Vector3f position = new Vector3f(0,0,0);
	private float distanceFromObject = 50;
	private float angle = 0;
	private float angleY = 0;
	private float scale;
	private float pitch = 0;
	private float yaw = 0;
	private float roll = 0;
	
	private int index;
	
	private Entity entity;
	private ArrayList<Entity> entities;
	
	public String name;
	
	public Camera(ArrayList<Entity> entities){
		this.entities = entities;
	}
	public Camera(Entity entity){
		this.entity = entity;
		index = 0;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setEntity(Entity entity){
		this.entity = entity;
	}
	
	public Camera(){}
	
	public void move(){
//		if(Mouse.isButtonDown(0) || Mouse.isButtonDown(1)){
		    calculateZoom();
		    calculatePitch();
		    calculateAngle();
		    
		    float horizontalDistance = calculateHorizontalDistance();
		    float verticalDistance = calculateVerticalDistance();
		    calculateCameraPosition(entity, horizontalDistance, verticalDistance);
		    this.yaw = 180 - (entity.getRotY() + angle);
	}
	
	public void changeCameraCenter(ArrayList<Entity> entities, ArrayList<Planet> planets){
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
        	if(index == 0){
        		index = entities.size();
        	}
        	index--;
        	setEntity(entities.get(index));
        	name = planets.get(index).getName();
        }  
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
        	if(index == entities.size() - 1){
        		index = -1;
        	}
        	index++;
        	setEntity(entities.get(index));
        	name = planets.get(index).getName();
        }
    }
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getScale(){
		return scale;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(Entity e, float horizontalDistance, float verticalDistance){
		float theta = e.getRotY() + angle;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = e.getPosition().x - offsetX;
		position.z = e.getPosition().z - offsetZ;
		position.y = e.getPosition().y + verticalDistance;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromObject * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromObject * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom(){
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			distanceFromObject -= 0.001f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			distanceFromObject += 0.001f;
		}
		else{
			float zoomLevel = Mouse.getDWheel() * 0.005f;
		    distanceFromObject -= zoomLevel;
//		System.out.println("camera to object distance: " + distanceFromObject);
		}
		if(distanceFromObject > 1000){
			distanceFromObject = 1000;
		}
		if(distanceFromObject < 0.005f){
			distanceFromObject = 0.005f;
		} 
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(0)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	private void calcuateAngleY(){
		if(Mouse.isButtonDown(1)){
			float angleChange = Mouse.getDY() * 0.1f;
			angleY -= angleChange;
		}
	}
	
	private void calculateAngle(){
		if(Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.1f;
			angle -= angleChange;
		}
	}
}
