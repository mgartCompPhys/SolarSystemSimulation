package Models;



import java.util.ArrayList;
import java.util.Vector;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

public class SphereModel {
	
	ArrayList<Float> vertices;
	
	ArrayList<Float> textureCoords;
	
	ArrayList<Integer> indices;
	
	public SphereModel(){
		this.vertices = new ArrayList<Float>();
		this.textureCoords = new ArrayList<Float>();
		this.indices = new ArrayList<Integer>();
	}
	
	public void addVertex(float x, float y, float z){
		 vertices.add(x);
		 vertices.add(y);
		 vertices.add(z);
	}
	
	public void addIndex(int v1, int v2, int v3){
	    indices.add(v1);
	    indices.add(v2);
		indices.add(v3);
	}
	
	public void addTextureCoord(float x, float y){
		textureCoords.add(x);
		textureCoords.add(y);
	}
	
    public float[] getVertices() {
    	float[] v = new float[vertices.size()];
    	for(int i = 0; i < vertices.size(); i++){
    		v[i] = vertices.get(i);
    	}
    	return v;
	}
    

	public float[] getTextureCoords() {
		float[] tc = new float[textureCoords.size()];
    	for(int i = 0; i < textureCoords.size(); i++){
    		tc[i] = textureCoords.get(i);
    	}
    	return tc;
	}

	public int[] getIndices() {
		int[] indices = new int[this.indices.size()];
    	for(int i = 0; i < this.indices.size(); i++){
    		indices[i] = this.indices.get(i);
    	}
    	return indices;
	}
	
	public void drawSphereQuad(double radius, int slices, int stacks) {
        double stack = (2*Math.PI)/stacks; // Controls it for quadradic formation
        double slice = (2*Math.PI)/slices; // Controls it for quadradic formation
        int quad = 0;
        for (double theta = 0; theta < 2 * Math.PI; theta += stack) {
            for (double phi = 0; phi < 2 * Math.PI; phi += slice) {
                Vector3f p1 = getPoints(phi, theta, radius);
                Vector3f p2 = getPoints(phi + slice, theta, radius);
                Vector3f p3 = getPoints(phi + slice, theta + stack, radius);
                Vector3f p4 = getPoints(phi, theta + stack, radius);
                Vector2f uv = uvCoords(p1.x, p1.y, p1.z);
                addTextureCoord(uv.x, uv.y);
                addVertex(p1.x, p1.y, p1.z);
                
                uv = uvCoords(p2.x, p2.y, p2.z);
                addTextureCoord(uv.x, uv.y);
                addVertex(p2.x, p2.y, p2.z);
                
                uv = uvCoords(p3.x, p3.y, p3.z);
                addTextureCoord(uv.x, uv.y);
                addVertex(p3.x, p3.y, p3.z);
                
                uv = uvCoords(p4.x, p4.y, p4.z);
                addTextureCoord(uv.x, uv.y);
                addVertex(p4.x, p4.y, p4.z);
            }
        }
//        duplicateSeam();
    }
// SEAM FIX CODE
	public ArrayList<Integer> FindSeam(){
		ArrayList<Integer> seam = new ArrayList<Integer>();
		for(int i = 0; i < textureCoords.size(); i+=2){
			if(textureCoords.get(i) < 0.25 || textureCoords.get(i) > 0.75){
				seam.add(i/2);
			}
		}
		return seam;
	}
	
	public boolean findSeam(float u1, float current, float u2){
		boolean isSeam = false;
			if(Math.abs(current- u1) > 0.25 || Math.abs(current - u2) > 0.25 ){
				isSeam = true;
			}
		return isSeam;
	}
	
	public ArrayList<Float> findEdge(int vertexNumber){
		ArrayList<float> edges = new ArrayList<float>();
		for(int i = 0; i < vertices.size()/12; i+= 12){ // captures 12 floats at a time.
//			for(int j = 1; i < 5; j++){
				if(findSeam(textureCoords.get(i), textureCoords.get(i+1), textureCoords.get(i+2))){
					edges.add(vertices.get(i), vertices.get(i+1), vertices.get(i+2));
				}
                if(findSeam(textureCoords.get(i+3), textureCoords.get(i+4), textureCoords.get(i+5))){
                	edges.add(vertices.get(i+3), vertices.get(i+4), vertices.get(i+5));
                }
                if(findSeam(textureCoords.get(i+6), textureCoords.get(i+7), textureCoords.get(i+8))){
                	edges.add(vertices.get(i+6), vertices.get(i+7), vertices.get(i+8));
				}
                if(findSeam(textureCoords.get(i+9), textureCoords.get(i+10), textureCoords.get(i+11))){
                	edges.add(vertices.get(i+9), vertices.get(i+10), vertices.get(i+11));
                }
//			}
		}
		return edges;
	}
	
	public ArrayList<Float> detectEdge(int vertexNumber){ // Edges for given vertex.
		ArrayList<Float> edges = new ArrayList<Float>();
		for(int i = 0; i < vertices.size()/2; i+=2){ // Get QUAD
			int condition = 0;
			if(vertices.get(vertexNumber*3) == vertices.get(i)){
				condition++;
			}
			if(vertices.get(vertexNumber*3 + 1) == vertices.get(i+1)){
				condition++;
			}
			if(vertices.get(vertexNumber*3 + 2) == vertices.get(i+2)){
				condition++;
			}
			if(condition == 2){
				edges.add(vertices.get(i));
				edges.add(vertices.get(i+1));
				edges.add(vertices.get(i+2));
			}
		}
		return edges;
	}
	
	public void duplicateSeam(){
		ArrayList<Integer> seam = FindSeam();
		float[] texture = getTextureCoords();
		float[] vertex = getVertices();
		for(int i = 0; i < seam.size();i++){
			System.out.println("dup- " + seam.get(i));
			// ADD duplicates:
			addVertex(vertex[(seam.get(i)*3)], vertex[(seam.get(i)*3)+1], vertex[(seam.get(i)*3)+2]);
			addTextureCoord(texture[(seam.get(i)*2)]-1, texture[(seam.get(i)*2)+1]);
		}
	}
	
	public int maxTextureCoord(){
		int maxCoord = 0;
		for(int i = 0; i < textureCoords.size(); i += 2){
			for(int j = 0; j < textureCoords.size(); j += 2){
				if(textureCoords.get(i) > textureCoords.get(j) && textureCoords.get(i) != textureCoords.get(j)){
					if(textureCoords.get(maxCoord) <= textureCoords.get(j)){
						maxCoord = j;
					}
				}
			}
		}
		return maxCoord;
	}
	
	public int minTextureCoord(){
		int minCoord = 0;
		for(int i = 0; i < textureCoords.size(); i += 2){
			for(int j = 0; j < textureCoords.size(); j += 2){
				if(textureCoords.get(i) > textureCoords.get(j) && textureCoords.get(i) != textureCoords.get(j)){
					if(textureCoords.get(minCoord) >= textureCoords.get(j)){
						minCoord = j;
					}
				}
			}
		}
		return minCoord;
	}
	
	
	public float[] seamCoords(){
		int maxTex = maxTextureCoord();
		System.out.println("seamCoods maxTex: " + maxTex);
		float[] maxCoords = {
			vertices.get(maxTex), vertices.get(maxTex), vertices.get(maxTex)
		};
		return maxCoords;
	}
	
	public int wrapTexCount(int maxU){
		int count = 0;
		for(int i = 0; i < textureCoords.size(); i++){
			if(textureCoords.get(i) == textureCoords.get(maxU)){
				count += 1;
			}
		}
		return count;
	}
	
	public void WrapQuad(){
		int maxTexUCoord = maxTextureCoord();
		int min = minTextureCoord();
		int wrapTexCount = wrapTexCount(maxTexUCoord) + wrapTexCount(min);
		float[] texture = getTextureCoords();
		float[] vertex = getVertices();
		this.vertices = new ArrayList<Float>(vertices.size()+wrapTexCount*3);
		this.textureCoords = new ArrayList<Float>(textureCoords.size()+wrapTexCount*2);
		int oldTexQuad = 0;
		int oldQuad = 0;
		for(int i = 0; i < texture.length; i += 8){
			float u = texture[oldTexQuad];
			float v = texture[oldTexQuad+1];
			if(texture[oldTexQuad] >= texture[maxTexUCoord]){
				addVertex(vertex[oldQuad], vertex[oldQuad+1], vertex[oldQuad+2]);
				addTextureCoord(texture[oldTexQuad], texture[oldTexQuad+1]);
				addVertex(vertex[oldQuad+3], vertex[oldQuad+4], vertex[oldQuad+5]);
				addTextureCoord(texture[oldTexQuad+2], texture[oldTexQuad+3]);
				addVertex(vertex[oldQuad+6], vertex[oldQuad+7], vertex[oldQuad+8]);
				addTextureCoord(texture[oldTexQuad+4], texture[oldTexQuad+5]);
				addVertex(vertex[oldQuad+9], vertex[oldQuad+10], vertex[oldQuad+11]);
				addTextureCoord(texture[oldTexQuad+6], texture[oldTexQuad+7]);
		//		Wrap Quad
				addVertex(vertex[oldQuad], vertex[oldQuad+1], vertex[oldQuad+2]);
				addTextureCoord(1f, texture[oldTexQuad+1]);
				addVertex(vertex[oldQuad+3], vertex[oldQuad+4], vertex[oldQuad+5]);
				addTextureCoord(1f, texture[oldTexQuad+3]);
				addVertex(vertex[oldQuad+6], vertex[oldQuad+7], vertex[oldQuad+8]);
				addTextureCoord(1f, texture[oldTexQuad+5]);
				addVertex(vertex[oldQuad+9], vertex[oldQuad+10], vertex[oldQuad+11]);
				addTextureCoord(1f, texture[oldTexQuad+7]);
			}
			if(texture[oldTexQuad] <= texture[min]){
				//		Wrap Quad
				addVertex(vertex[oldQuad], vertex[oldQuad+1], vertex[oldQuad+2]);
				addTextureCoord(0f, texture[oldTexQuad+1]);
				addVertex(vertex[oldQuad+3], vertex[oldQuad+4], vertex[oldQuad+5]);
				addTextureCoord(0f, texture[oldTexQuad+3]);
				addVertex(vertex[oldQuad+6], vertex[oldQuad+7], vertex[oldQuad+8]);
				addTextureCoord(0f, texture[oldTexQuad+5]);
				addVertex(vertex[oldQuad+9], vertex[oldQuad+10], vertex[oldQuad+11]);
				addTextureCoord(0f, texture[oldTexQuad+7]);
				//		Existing Quad
				addVertex(vertex[oldQuad], vertex[oldQuad+1], vertex[oldQuad+2]);
				addTextureCoord(texture[oldTexQuad], texture[oldTexQuad+1]);
				addVertex(vertex[oldQuad+3], vertex[oldQuad+4], vertex[oldQuad+5]);
				addTextureCoord(texture[oldTexQuad+2], texture[oldTexQuad+3]);
				addVertex(vertex[oldQuad+6], vertex[oldQuad+7], vertex[oldQuad+8]);
				addTextureCoord(texture[oldTexQuad+4], texture[oldTexQuad+5]);
				addVertex(vertex[oldQuad+9], vertex[oldQuad+10], vertex[oldQuad+11]);
				addTextureCoord(texture[oldTexQuad+6], texture[oldTexQuad+7]);
			}
			else{
				addVertex(vertex[oldQuad], vertex[oldQuad+1], vertex[oldQuad+2]);
				addTextureCoord(texture[oldTexQuad], texture[oldTexQuad+1]);
				addVertex(vertex[oldQuad+3], vertex[oldQuad+4], vertex[oldQuad+5]);
				addTextureCoord(texture[oldTexQuad+2], texture[oldTexQuad+3]);
				addVertex(vertex[oldQuad+6], vertex[oldQuad+7], vertex[oldQuad+8]);
				addTextureCoord(texture[oldTexQuad+4], texture[oldTexQuad+5]);
				addVertex(vertex[oldQuad+9], vertex[oldQuad+10], vertex[oldQuad+11]);
				addTextureCoord(texture[oldTexQuad+6], texture[oldTexQuad+7]);
//				oldTexIndex += 2;
//				oldVertexIndex += 3;
			}
			oldTexQuad += 8;
			oldQuad += 12;
		}
//		System.out.println("oldIndex: " + oldQuad + " : " + oldTexQuad);
		textureCoords.trimToSize();
		vertices.trimToSize();
	}
	
	public ArrayList<Float> addBufferCoord(){
		ArrayList<Float> texture = new ArrayList<Float>(textureCoords.size()+2);
		for(int i = 0; i < textureCoords.size(); i += 2){
			if(maxTextureCoord() == i){
				texture.add(1.0f);
				texture.add(1.0f);
			}
			else{
				texture.add(textureCoords.get(i));
				texture.add(textureCoords.get(i+1));
			}
		}
		return texture;
	}
	
	Vector3f getPoints(double phi, double theta, double radius) {
        double x = radius * Math.cos(theta) * Math.sin(phi);
        double y = radius * Math.sin(theta) * Math.sin(phi);
        double z = radius * Math.cos(phi);
        return new Vector3f((float)x, (float)y, (float)z);
    }
	
	public void Equality(float[] vertices){
		int equalCount = 0;
		for(int i = 0; i < vertices.length; i+= 3){
			int sameVertexCount = 0;
			Vector3f v1 = new Vector3f(vertices[i], vertices[i+1], vertices[i+2]);
			for(int j = 0; j < vertices.length; j += 3){
				Vector3f v2 = new Vector3f(vertices[j], vertices[j+1], vertices[j+2]);
				if(equalVertex(v1, v2) && i != j){ 
					equalCount += 1;
					sameVertexCount += 1;
				}
			}
			System.out.println("Vertex: x: " + v1.x + ", y: " + v1.y + ", z: " + v1.z + " = " + sameVertexCount);
		}
		System.out.println("equal vertex count: " + equalCount);
	}
	
	public boolean equalVertex(Vector3f v1, Vector3f v2){
		if(v1.x == v2.x && v1.y == v2.y && v1.z == v2.z){
			return true;
		}
		return false;
	}
	
	public Vector3f Normalize(float x, float y, float z, float r){
		Vector3f n = new Vector3f(x/r, y/r, z/r);
		return n;
	}
	
	public Vector2f uvCoords(double x, double y, double z){
		Vector2f uv = new Vector2f();
		uv.x = (float) (0.5 + Math.atan2(z, x)/(2*Math.PI));
		uv.y = (float) (0.5 - Math.asin(y)/Math.PI);
		return uv;
	}
	
	public float[] setSphereColor(int vertexNumber, Color color){
		float[] verticiesColor = new float[vertexNumber*3];
		for(int i = 0; i < vertexNumber; i++){
//			float[] c = color.; // rgb
			verticiesColor[i] = color.r;
			verticiesColor[i+1] = color.g;
			verticiesColor[i+2] = color.b;
		}
		return verticiesColor;
	}
	
	public int getVertexNumber(float[] vertices){
		return  vertices.length / 3;
	}
	
	public ArrayList<Vector3f> sortVertex(float[] vertices){
		ArrayList<Vector3f> vertex = new ArrayList<Vector3f>(vertices.length/3);
		for(int i = 0; i < vertices.length/3; i++){
			Vector3f v = new Vector3f(0, 0, 0);
			System.out.println(i);
			v.x = vertices[i];
			v.y = vertices[i+1];
			v.z = vertices[i+2];
			if(v.y == 1){
			    vertex.add(v);
			}
		}
		return vertex;	
	}
	
	public String sortPrint(ArrayList<Vector3f> vect){
		String print = new String("\n Sorted Print \n \n");
		for(int i = 0; i < vect.size(); i++){
			print += "Vertex " + i + " : " + vect.get(i).x + ", " + vect.get(i).y + ", " + vect.get(i).z + "\n"; 
		}
		print += "\n";
		return print;
	}
	
    public static void main(String args[]){ // UNIT TESTING PURPOSES FOR DEBUGGING VERTEX AND TEXTURE COORDINATES!
     	SphereModel model = new SphereModel();
//     	model.drawSphere(1, 5, 5);
     	model.drawSphereQuad(1, 10, 10);
     	float[] vertices = model.getVertices();
     	float[] textureCoords = model.getTextureCoords();
//     	SphereModel sm = new SphereModel();
     	
     	int max = model.maxTextureCoord();
     	int min = model.minTextureCoord();
     	int count = model.wrapTexCount(max);
//     	System.out.println("max tex u: " + max);
     	
//     	Vector3f sc = new Vector3f(0, 0, 0);
//        Vector3f sp = new Vector3f(1f, 1f, 1f);
     	
//     	Vector3f normalizer = sm.Normalize(sp.x, sp.y, sp.z, 2f);
//        double uu = Math.atan2((double) normalizer.x, (double) normalizer.z) / (2* Math.PI) + 0.5;
//        float u = (float) uu;
//        float v = normalizer.y * 0.5f + 0.5f;
        
//        System.out.print("u: " + u);
//        System.out.println(" v: " + v);
//     	int seamCount = 0;
//     	int maxCoord = model.maxTextureCoord();
//     	int error = 0;
     	int j = 0;
     	for(int i = 0; i < vertices.length; i += 3){
//     		for(int j = 0; j < textureCoords.length; j += 2){
//     		if(textureCoords[maxCoord] == textureCoords[j]){
//     			seamCount++;
     			System.out.print("Vetex " + i/3 + " : ");
         		System.out.print(vertices[i] + ", ");
         		System.out.print(vertices[i+1] + ", ");
         		System.out.print(vertices[i+2] + ", \t");
     			
         		System.out.print(textureCoords[j] + ", ");
         		System.out.println(textureCoords[j+1] + ", ");
//     		}
         		//         		if(textureCoords[j] < 0 || textureCoords[j] > 1) error += 1;
//         		if(textureCoords[j+1] < 0 || textureCoords[j+1] > 1) error += 1;
         		j += 2;
//         	} 
     	} 
//     	System.out.println("max/min tex u: " + max + " / " + min);
//     	System.out.println("count: " + count);
     	System.out.println("vertex size:" + vertices.length/3);
     	
//    	System.out.println("seam number: " + seamCount);
    	
//     	sm.Equality(vertices);
/*     	System.out.println("\n TEXTURE COORDS: \n");
     	for(int i = 0; i < textureCoords.length; i += 2){
     		System.out.print("texture coord[ " + i/2 + "] :");
     		System.out.print(textureCoords[i] + ", ");
     		System.out.println(textureCoords[i+1] + ", ");
     		if(textureCoords[i] < 0 || textureCoords[i] > 1) error += 1;
     		if(textureCoords[i+1] < 0 || textureCoords[i+1] > 1) error += 1;
     	} 
     	System.out.println("vertex count: " + vertices.length/3);
     	System.out.println("error: " + error + ", coord length: " + textureCoords.length);
     	double percent = error / textureCoords.length;
 */    	
/*    	int maxCoord = model.maxTextureCoord();
     	System.out.println("MAX COORD: " + maxCoord/2);
     	System.out.println("MAX COORDS - X: " + textureCoords[maxCoord] + " Y: " + textureCoords[maxCoord+1]);
     	System.out.println("MAX SEAM:");
//     	System.out.println("Vertices float length: " + vertices.length);
 //    	float[] maxSeam = sm.seamCoords();
     	int seam = maxCoord / 2;
		float[] maxSeam = {
			vertices[seam*3], vertices[seam*3+1], vertices[seam*3+2]
		};
     	System.out.println("X: " + maxSeam[0] + " Y: " + maxSeam[1] + " Z: " + maxSeam[2]);
     	System.out.print("Textcoord: ");
     	System.out.println("X: " + textureCoords[maxCoord] + " Y: " + textureCoords[maxCoord+1]);

//        System.out.println("percentage: " + percent);

//     	System.out.println("vertex size:" + vertices.length);
     	
     	
/*     	int line = 0;
     	int texLoc = 0;
     	for(int i = 0; i < vertices.length/3; i++){
     		if(vertices[i] == maxSeam[0]){
     			line += 1;
     			System.out.print("seam[" + line + "]: ");
     			System.out.print("vertex[" + i/3 + "]: ");
     			System.out.print("X: " + vertices[i] + " Y: " + vertices[i+1] + " Z: " + vertices[i+2]);
     			System.out.println(" U: " + textureCoords[texLoc] + " V: " + textureCoords[texLoc+1]);
     		}
     		texLoc = 0;
     	}
     	System.out.println("seam line: " + line);
*/
//     	ArrayList<Vector3f> vert = model.sortVertex(vertices);
//     	System.out.print(model.sortPrint(vert));
     	
    }

}

