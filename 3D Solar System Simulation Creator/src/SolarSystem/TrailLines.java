package SolarSystem;

public class TrailLines {
	Trail[] trails;
	int length;
	int maxLength;
	int objectSize;
	int maxPoints;
	
	int startPosition;
	int endPosition;
	
	public TrailLines(int objectSize, int maxLength){
		this.length = 0;
		this.maxLength = maxLength;
		this. objectSize = objectSize;
//		this.maxPoints = objectSize * maxLength;
		trails = new Trail[objectSize];
		for(int i = 0; i < objectSize; i++){
		    this.trails[i] = new Trail(maxLength);
		}
	    this.startPosition = 0;
//		initialize();
	}
	
	public void initialize(){
		for(int n = 0; n < objectSize; n++){
		}
	}
	
	public Trail[] getTrails(){
		return this.trails;
	}
	
	public Trail getTrail(int objectNumber){
		return this.trails[objectNumber];
	}
	
	public float[] getPoint(int objectNumber, int index){
		return this.trails[objectNumber].getPoint(index);
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
	
	public  void addPoint(int objectNumber, float[] point){
	    if(trails[objectNumber].getLength() == maxLength){
		    allocatePoints(objectNumber);
	    }
		trails[objectNumber].setPoint(this.trails[objectNumber].getLength(), point);
		if(trails[objectNumber].getLength() <= maxLength){
		    this.trails[objectNumber].addLength();
		}
	}
	
	private void allocatePoints(int objectNumber){
		//for(int n = 0; n < objectSize; n++){
		    for(int i = 0; i < maxLength -1; i++){
//			    lines[objectNumber][i] = lines[objectNumber][i+1];
			    float[] point = trails[objectNumber].getPoint(i+1);
			    trails[objectNumber].setPoint(i, point);
		    }
		//}
//		    System.out.println("");
//		if(objectNumber != objectSize){
		    trails[objectNumber].subtractLength();
//		}
	}
	
	public static void main(String[] args) {
		TrailLines trailLines = new TrailLines(3, 10);
		int val = 0;
		for(int i= 0; i < trailLines.getObjectSize(); i++){
			for(int j = 0; j < trailLines.getMaxLength(); j++){
//				System.out.println("inserts = " + val);
				val++;
				float[] vertex = new float[3];
				vertex[0] = j;
				vertex[1] = j;
				vertex[2] = j;
				trailLines.addPoint(i, vertex);
			}
		}
		String s = "";
		for(int i = 0; i < trailLines.getObjectSize(); i ++){
			for(int j = 0; j < trailLines.getMaxLength(); j++){
				float[] point = trailLines.getPoint(i, j);
				s += "trail[" + i + "][" + j + "] = (" + point[0] + ", " + point[1] + ", " + point[2] + ") \n";
			}
			 s += "\n";
		}
		System.out.println(s);
	}
	
}
