package SpaceBackground;

import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
 
import Shaders.ShaderProgram;
import ToolBox.Maths;

public class SpaceBackgroundShader extends ShaderProgram{
	 
	private static final String VERTEX_FILE = "src/SpaceBackground/SpaceBackgroundVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/SpaceBackground/SpaceBackgroundFragmentShader.txt";
	    
	private int location_projectionMatrix;
	private int location_viewMatrix;
	    
	public SpaceBackgroundShader() {
	    super(VERTEX_FILE, FRAGMENT_FILE);
	}
	     
	public void loadProjectionMatrix(Matrix4f matrix){
	    super.loadMatrix(location_projectionMatrix, matrix);
	}
	 
	public void loadViewMatrix(Camera camera){
	    Matrix4f matrix = Maths.createViewMatrix(camera);
	    matrix.m30 = 0;
	    matrix.m31 = 0;
	    matrix.m32 = 0;
	    // put in vertexShader: viewMatrix *
	    super.loadMatrix(location_viewMatrix, matrix);
	}
	     
	@Override
	protected void getAllUniformLocations() {
	    location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	    location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	 
	@Override
	protected void bindAttributes() {
	    super.bindAttribute(0, "position");
	}
}
