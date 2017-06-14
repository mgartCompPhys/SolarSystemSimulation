package SolarSystem;

import Entities.Entity;

public class Planet {
	
	String type; // Star, Planet, moon, comet.
	String parent;
	Entity entity;
	String name;
	double x;
	double y;
	double z;
	double size;
	double radius;
	double mass;
	double eccentricity;
    double vx = 0;
    double vy = 0;
    double vz = 0;
    double time = 0;
    String texture = "";
    
    double period = 0;
    int periodCount = 0;
    
    public Planet(){}
    
    public Planet(String name, double x, double y, double size, double mass, double vx, double vy, double vz){
    	this.name = name;
    	this.x = x;
    	this.y = y;
    	this.size = size;
    	this.mass = mass;
    	this.vx = vx;
    	this.vy = vy;
    	this.vz = vz;
    }
    
	public Planet(String name, String type, double mass, double radius, double size, double eccentricity, double x, double y, double z) {
		this.name = name;
		this.type = type;
		this.mass = mass;
		this.radius = radius;
		this.size = size;
		this.eccentricity = eccentricity;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getParent(){
		return type;
	}
	
	public void setParent(String parent){
		this.parent = parent;
	}
	
	public double getEccentricity() {
		return eccentricity;
	}

	public void setEccentricity(double eccentricity) {
		this.eccentricity = eccentricity;
	}

	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	}
	public double getVx() {
		return vx;
	}
	public void setVx(double vx) {
		this.vx = vx;
	}
	public double getVy() {
		return vy;
	}
	public void setVy(double vy) {
		this.vy = vy;
	}
	public double getVz() {
		return vz;
	}
	public void setVz(double vz) {
		this.vz = vz;
	}

	public String getTexture(){
		return this.texture;
	}
	public void setTexture(String texture){
		this.texture = texture;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getPeriod() {
		return period;
	}
	public void setPeriod(double period) {
		this.period = period;
	}
	public int getPeriodCount() {
		return periodCount;
	}
	public void setPeriodCount(int periodCount) {
		this.periodCount = periodCount;
	}
	
	public void setVelocities(double vx, double vy, double vz){
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
	}
	
    public String toString(int i){
        String s = "Planet " + i + ": x = " + getX() + " y = " + getY() + " z = " + getZ();
        s += " vx = " + getVx() + " vy = " + getVy() + " vz = " + getVz() + "\n\n";
        return s;
    }
}
