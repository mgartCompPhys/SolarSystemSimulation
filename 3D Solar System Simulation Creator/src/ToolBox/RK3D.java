package ToolBox;

import SolarSystem.Planet;


import java.util.*;

public class RK3D{
    
    private ArrayList<Planet> planets;
    private int currentPlanet;
 
    private int N; // Number of current Interval of system.
    private double t; // s
    private double h; //.01; // change of time between intervals. 10 intervals equal 1 second.
    private double R = 2.50*Math.pow(10, 11);
    private int pixels = 2000;
    private double G = 6.674*Math.pow(10, -11);  // Universal Gravity Constant
 
    public RK3D(ArrayList<Planet> planets, double time, double h) {
        this.N = 0;
        this.planets = planets;
        this.t = time;
        this.h = h;
    }
    
    private double adjustXByRatio(double x){  // Adjust values to the Raito to fit on graph.
        x = x + R;
        return x / (R / (pixels / 2));
    }
    
    private double adjustYByRatio(double y){
        y = y + R;
        return y / (R / (pixels / 2));
    }
    
    private double adjustZByRatio(double z){
    	z = z + R;
    	return z / (R / (pixels / 2));
    }
    
    private double displacementX(int i, int j){
        return this.planets.get(i).getX() - this.planets.get(j).getX();
    }
    
    private double displacementY(int i, int j){
        return this.planets.get(i).getY() - this.planets.get(j).getY();
    }
    
    public void NewT(){
        this.t = this.t + h; // increment time by 0.1 seconds
//        System.out.println("new time = " + this.t);
    } 
            
    public Planet newInterval(Planet p, ArrayList<Planet> ps, int current){  // The y for the current interval. Currently set for only 1 interval! ACTUAL: Make this void!
       this.currentPlanet = current;
    	double K[] = new double[4]; 
        double L[] = new double[4];
        double P[] = new double[4];
        double Q[] = new double[4];
        double G[] = new double[4];
        double H[] = new double[4];
        double Vx = p.getVx();
        double Vy = p.getVy();
        double Vz = p.getVz();
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
          
        char xVect = 'x';
        char yVect = 'y';
        char zVect = 'z';
                    
//      private double Acceleration(double t, Planet p, ArrayList<Planet> ps, char vect);
                                  
        // This is the value of the current A(i).
        K[0] = integrateVelocity(t, p.getVx());
        L[0] = Acceleration(t, 0, 0, 0, p, ps, xVect);
        P[0] = integrateVelocity(t, p.getVy());
        Q[0] = Acceleration(t, 0, 0, 0, p, ps, yVect);
        G[0] = integrateVelocity(t, p.getVz());
        H[0] = Acceleration(t, 0, 0, 0, p, ps, zVect);
                    
        K[1] = integrateVelocity(t + (h/2), p.getVx() + (h/2)*L[0]);
        L[1] = Acceleration(t + (h/2), (h/2)*K[0], (h/2)*P[0], (h/2)*G[0], p, ps, xVect);
        P[1] = integrateVelocity(t + (h/2), p.getVy() + (h/2)*Q[0]);
        Q[1] = Acceleration(t + (h/2), (h/2)*K[0], (h/2)*P[0], (h/2)*G[0], p, ps, yVect);
        G[1] = integrateVelocity(t+(h/2), p.getVz() + (h/2)*H[0]);
        H[1] = Acceleration(t + (h/2), (h/2)*K[0], (h/2)*P[0], (h/2)*G[0], p, ps, zVect);
                    
        K[2] = integrateVelocity(t + (h/2), p.getVx() + (h/2)*L[1]);
        L[2] = Acceleration(t + (h/2), (h/2)*K[1], (h/2)*P[1], (h/2)*G[1], p, ps, xVect);
        P[2] = integrateVelocity(t + (h/2), p.getVy() + (h/2)*Q[1]);
        Q[2] = Acceleration(t + (h/2), (h/2)*K[1], (h/2)*P[1], (h/2)*G[1], p, ps, yVect);
        G[2] = integrateVelocity(t+(h/2), p.getVz() + (h/2)*H[1]);
        H[2] = Acceleration(t + (h/2), (h/2)*K[1], (h/2)*P[1], (h/2)*G[1], p, ps, zVect);
                    
        K[3] = integrateVelocity(t + h, p.getVx() + h*L[2]);
        L[3] = Acceleration(t + h, h*K[2], h*P[2], h*G[2], p, ps, xVect);
        P[3] = integrateVelocity(t + h, p.getVy() + h*Q[2]);
        Q[3] = Acceleration(t + h, h*K[2], h*P[2], h*G[2], p, ps, yVect);
        G[3] = integrateVelocity(t + h, p.getVz() + h*H[2]);
        H[3] = Acceleration(t + h, h*K[2], h*P[2], h*G[2], p, ps, zVect);
        // Gets the Velocity and Displacement affected by Force(i,j).
        x =  x + ((h/6) * (K[0] + 2*K[1] + 2*K[2] + K[3])); // get displacement in planet's x vector
        Vx =  Vx + ((h/6) * (L[0] + 2*L[1] + 2*L[2] + L[3])); // get displacement in planet's y vector
        y = y + ((h/6) * (P[0] + 2*P[1] + 2*P[2] + P[3])); // get displacement in planet's x vector
        Vy =  Vy + ((h/6) * (Q[0] + 2*Q[1] + 2*Q[2] + Q[3])); // get displacement in planet's y vector
        z = z + ((h/6) * (G[0] + 2*G[1] + 2*G[2] + G[3]));
        Vz =  Vz + ((h/6) * (H[0] + 2*H[1] + 2*H[2] + H[3])); // get displacement in planet's y vector
                    
        p.setX(x);
        p.setY(y);
        p.setZ(z);
        p.setVx(Vx);
        p.setVy(Vy);    
        p.setVz(Vz);
                    
        return p;                                    
    }
          
    private double integrateVelocity(double t, double v){
        return v;
    }
    
    private double planetForce(double t, double hx, double hy, double hz, Planet p, ArrayList<Planet> ps, char vect){ // cleared
        double xp = p.getX(); // + hx
        double yp = p.getY(); // + hy
        double zp = p.getZ();
        double mp = p.getMass();
        double F = 0;
        // Calculates Forces apart then adds them
        ps.trimToSize();
        for(int i = 0; i < ps.size(); i++){
        	double xo = ps.get(i).getX();
        	double yo = ps.get(i).getY();
        	double zo = ps.get(i).getZ();
        	double mo = ps.get(i).getMass();
        	double dx = (xp - xo) + hx;
        	double dy = (yp - yo) + hy;
        	double dz = (zp - zo) + hz; // cleared
        	double r = Math.sqrt(dx*dx + dy*dy + dz*dz);
        	
        	double vector = 0;
        	if(vect == 'x') vector = (dx)/r;
        	if(vect == 'y') vector = (dy)/r;
        	if(vect == 'z') vector = (dz)/r;
        	
            if(!p.getName().equals(ps.get(i).getName())){
                double f = Force(r, mp, mo);
//                if(vect == 'x' && this.currentPlanet == 0){
//                    System.out.println("f" + vect + ": (" + this.currentPlanet + "," + i + ") = " + f*vector);
//                }
                F += (f * vector);
            }
        }
//      System.out.println("Planet# " + n + ": Fx = " + Fx / planets.get(n).getMass() + ", Fy = " + Fy);
        return F;
    }
    
    private double Force(double r, double M1, double M2){ // Cleared
        double Fx = -this.G * M1 * M2 / (r*r);        
        return Fx;
    }
    
    private double Acceleration(double t, double hx, double hy, double hz, Planet p, ArrayList<Planet> ps, char vect){ // Cleared
    	double acceleration = planetForce(t, hx, hy, hz, p, ps, vect) / p.getMass();
    	return acceleration;
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
           
   public double getTime(){
       return this.t;
   }
   
   public String toStringPlanets(ArrayList<Planet> ps){
	   String s = "";
//	   for(int i = 0; i < ps.size(); i++){
		   s+= ps.get(1).toString(1);
//	   }
	   return s;
   }
} 
