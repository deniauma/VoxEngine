/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine;

import java.awt.Color;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;
import voxengine.graphics.Camera;
import voxengine.graphics.Texture;
import voxengine.graphics.renderer.CubeRenderer;
import voxengine.graphics.shape.Cube;
import voxengine.graphics.shape.Rect;
import voxengine.graphics.shape.Rect3d;
import voxengine.math.joml.Matrix4f;

/**
 *
 * @author AT347526
 */
public class DemoScene implements Scene{
    
    private CubeRenderer renderer;
    private Texture texture;
    private Rect rect, rect1;
    private Rect3d floor;
    private Cube cube;
    private Camera camera;
    private float previousAngle = 0f;
    private float angle = 0f;
    private float anglePerSecond = 50f;
    
    private long window;

    @Override
    public void input() {
        int state = glfwGetKey(window, GLFW_KEY_DOWN);
        if (state == GLFW_PRESS) {
            //System.out.print("Camera position: " + camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
            camera.moveBackward(1);
            //System.out.println(" -> " + camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
            renderer.setCamera(camera);
        }
        
        state = glfwGetKey(window, GLFW_KEY_UP);
        if (state == GLFW_PRESS) {
            //System.out.print("Camera position: " + camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
            camera.moveForward(1);
            //System.out.println(" -> " + camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
            renderer.setCamera(camera);
        }
        
        state = glfwGetKey(window, GLFW_KEY_RIGHT);
        if (state == GLFW_PRESS) {
            //System.out.print("Camera position: " + camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
            camera.moveRight(1);
            //System.out.println(" -> " + camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
            renderer.setCamera(camera);
        }
        
        state = glfwGetKey(window, GLFW_KEY_LEFT);
        if (state == GLFW_PRESS) {
            //System.out.print("Camera position: " + camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
            camera.moveLeft(1);
            //System.out.println(" -> " + camera.getPosition().x + " " + camera.getPosition().y + " " + camera.getPosition().z);
            renderer.setCamera(camera);
        }
    }

    @Override
    public void update(float delta) {
        previousAngle = angle;
        angle += delta * anglePerSecond;
    }

    @Override
    public void render(float alpha) {
        /* Clear drawing area */
        float lerpAngle = (1f - alpha) * previousAngle + alpha * angle;
        Matrix4f model = new Matrix4f().rotate((float) Math.toRadians(lerpAngle), 0f, 0f, 1f);
        //renderer.updateUniModel(model);
        renderer.clear();
        
        /* Draw objects */
        renderer.begin();
        //renderer.render(rect.getVertices(), rect.getNbvertices());
        //renderer.render(rect1.getVertices(), rect1.getNbvertices());
        renderer.render(floor.getVertices(), floor.getNbvertices());
        renderer.render(cube.getVertices(), cube.getNbvertices());
        renderer.end();
    }

    @Override
    public void prepare() {
        System.out.print("preparing scene ... ");
        window = glfwGetCurrentContext();
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        int width = widthBuffer.get();
        int height = heightBuffer.get();
        renderer = new CubeRenderer();
        camera = new Camera(1f, -90f, 1f, 1f, 0, 1f);
        renderer.init(camera);
        
        /* Create texture */
        texture = Texture.loadTexture("resources/example.png");
        texture.bind();
        
        //rect = new Rect(Color.WHITE, texture, 0, 0, 100, 100, 0, 0, texture.getWidth(), texture.getHeight());
        //rect1 = new Rect(Color.WHITE, texture, 200, 200, 100, 100, 0, 0, texture.getWidth(), texture.getHeight());
        cube = new Cube(Color.WHITE, texture, 1, 1, 0, 10, 10, 10, 0, 0, texture.getWidth(), texture.getHeight());
        floor = new Rect3d(Color.GREEN, texture, -100, -100, 0, 1000, 1000, 0, 0, 0, 0);
        System.out.println("DONE");
    }

    @Override
    public void quit() {
        renderer.dispose();
        texture.delete();
    }
    
}
