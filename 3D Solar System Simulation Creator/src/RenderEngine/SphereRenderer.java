package RenderEngine;

import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import Entities.Entity;
import Models.RawModel;
import Models.SphereModel;
import Models.TexturedModel;
import Shaders.PointShader;
import Shaders.StaticShader;
import Shaders.TexturedShader;
import SolarSystem.Lines;
import ToolBox.Maths;

public class SphereRenderer {
	private RawModel model;
	private TexturedModel texturedModel;
	private SphereModel sphere;
	private StaticShader shader;
	private TexturedShader textureShader;
	private PointShader pointShader;
	private RawModel line;
	private RawModel Point;
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.0001f;
	private static final float FAR_PLANE = 2500;
	
	private Matrix4f projectionMatrix;
	
	public SphereRenderer(Loader loader){
		float[] linePoint = { 0f, 0f, 0f };
		
		this.sphere = new SphereModel();
     	this.sphere.drawSphereQuad(1, 50, 50);

     	this.model = loader.loadToVAO2(this.sphere.getVertices(), sphere.getTextureCoords());
     	this.line = loader.loadToVAO(linePoint);
     	
     	int maxCoord = this.sphere.maxTextureCoord();
		int seam = maxCoord / 2;
		float[] vertices = this.sphere.getVertices();
		float[] maxSeam = {
				vertices[seam*3], vertices[seam*3+1], vertices[seam*3+2]
		};
     	
    	this.Point = loader.loadToVAO(maxSeam);
		shader = new StaticShader();
		textureShader = new TexturedShader();
		pointShader = new PointShader();
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		
		textureShader.start();
		textureShader.loadProjectionMatrix(projectionMatrix);
		textureShader.stop();
		
		pointShader.start();
		pointShader.loadProjectionMatrix(projectionMatrix);
		pointShader.stop();
	}
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Tells which triangles should be rendered first.
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1); // red, green, blue, alpha
	}
	
	
	public void renderTexture(List<Entity> entities, Camera camera){
		for(Entity entity: entities){
			textureShader.start();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getID());
		textureShader.loadViewMatrix(camera);
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
			textureShader.loadTransformationMatrix(transformationMatrix);
			GL11.glDrawArrays(GL11.GL_QUAD_STRIP, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_CULL_FACE);
		textureShader.stop();
		}
	} 	
	
// This Method is for Future installment testing	
	public void renderShip(Entity entity, Camera camera){
			textureShader.start();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getID());
		textureShader.loadViewMatrix(camera);
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
			textureShader.loadTransformationMatrix(transformationMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_CULL_FACE);
		textureShader.stop();
	} 	
	
	
	public void render(List<Entity> entities, Camera camera){
		for(Entity entity: entities){
		shader.start();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		

		shader.loadViewMatrix(camera);
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
			
		shader.loadTransformationMatrix(transformationMatrix);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		}
	}
	
	public void renderMesh(List<Entity> entities, Camera camera){
		for(Entity entity: entities){
		shader.start();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		shader.loadViewMatrix(camera);
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		GL11.glDrawArrays(GL11.GL_LINE_STRIP, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		}
	}
	
	public void renderPoints(int objectNumber, Lines lines, Camera camera, RawModel point){
		for(int i = 0; i < lines.getLength(); i++){
		shader.start();
		GL30.glBindVertexArray(line.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		shader.loadViewMatrix(camera);
		
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(lines.getPoint(objectNumber, i).getPosition(), 
					lines.getPoint(objectNumber, i).getRotX(), lines.getPoint(objectNumber, i).getRotY(), 
					lines.getPoint(objectNumber, i).getRotZ(), lines.getPoint(objectNumber, i).getScale());
			shader.loadTransformationMatrix(transformationMatrix);
			GL11.glDrawArrays(GL11.GL_POINTS, 0, line.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	}
	
	public void renderLine(Entity entity, Camera camera, RawModel bigLine){
		shader.start();
		GL30.glBindVertexArray(bigLine.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		shader.loadViewMatrix(camera);
		
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
					entity.getRotX(), entity.getRotY(), 
					entity.getRotZ(), entity.getScale());
			shader.loadTransformationMatrix(transformationMatrix);
			GL11.glDrawArrays(GL11.GL_LINE_STRIP, 0, bigLine.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void renderSeam(List<Entity> entities, Camera camera){
		for(Entity entity: entities){
			pointShader.start();
		GL30.glBindVertexArray(Point.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		pointShader.loadViewMatrix(camera);
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
			pointShader.loadTransformationMatrix(transformationMatrix);
		GL11.glDrawArrays(GL11.GL_POINTS, 0, Point.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		pointShader.stop();
		}
	}
	
	private void createProjectionMatrix(){ // Converts coordinates from ortho to eye.
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) /frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
}
