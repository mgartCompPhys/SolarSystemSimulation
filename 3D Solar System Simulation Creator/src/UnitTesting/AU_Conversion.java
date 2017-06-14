package UnitTesting;

public class AU_Conversion {
	public double EARTH_RADIUS = 6371000;
	public int pixels = 2000;
	public double AU = 1.496*Math.pow(10, 11);
	public double SOLAR_SYSTEM_RADIUS = 50 * AU;
	
	public double METERS_PER_FLOAT = AU / ((pixels/2) / (SOLAR_SYSTEM_RADIUS/AU));
	// (pixels/2) / (SOLAR_SYSTEM_RADIUS/AU) = floats per AU
	// AU / floats per AU = meters per float
	
	public String[] name;
	public double[] radius;
	
	public AU_Conversion(int length){
		name = new String[length];
		radius = new double[length];
		planetNames();
		planetRadius();
		Conversion();
		printConversion();
	}
	
	private void planetNames(){
		name[0] = "sun";
		name[1] = "mercury";
		name[2] = "venus";
		name[3] = "earth";
		name[4] = "mars";
		name[5] = "jupiter";
		name[6] = "saturn";
		name[7] = "uranus";
		name[8] = "neptune";
		name[9] = "pluto";
		name[10] = "luna";
		name[11] = "io";
		name[12] = "eurpoa";
	}
	
	private void planetRadius(){
		radius[0] = 6.957*Math.pow(10, 8);
		radius[1] = 2.44*Math.pow(10, 6);
		radius[2] = 6.052*Math.pow(10, 6);
		radius[3] = 6.371*Math.pow(10, 6);
		radius[4] = 3.39*Math.pow(10, 6);
		radius[5] = 6.9911*Math.pow(10, 7);
		radius[6] = 5.8232*Math.pow(10, 7);
		radius[7] = 2.5362*Math.pow(10, 7);
		radius[8] = 2.4622*Math.pow(10, 7);
		radius[9] = 1.187*Math.pow(10, 6);
		radius[10] = 1.737*Math.pow(10, 6);
		radius[11] = 1.8216*Math.pow(10, 6);
		radius[12] = 1.5608*Math.pow(10, 6);
	}
	
	private void Conversion(){
		for(int i = 0; i < radius.length; i++){
			radius[i] = radius[i] / EARTH_RADIUS;
		}
	}
	
	private void printConversion(){
		String s = "";
		for(int i = 0; i < name.length; i++){
			// (pixels/2) / (SOLAR_SYSTEM_RADIUS/AU) = floats per AU
			// AU / floats per AU = meters per float
			radius[i] = (radius[i] * EARTH_RADIUS) / METERS_PER_FLOAT;
			s += name[i] + ": " + radius[i] + "\n";
		}
		System.out.print(s);
	}
	
	 public static void main(String args[]){
		 AU_Conversion au = new AU_Conversion(13);
	 }
}
