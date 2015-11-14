/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine;

import java.awt.Color;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;
import voxengine.graphics.Camera;
import voxengine.graphics.Texture;
import voxengine.graphics.renderer.CubeRenderer;
import voxengine.graphics.renderer.SkyBoxRenderer;
import voxengine.graphics.shape.Cube;
import voxengine.graphics.shape.Rect3d;
import voxengine.graphics.shape.Renderable;
import voxengine.math.joml.Matrix4f;
import voxengine.math.joml.Vector3f;

/**
 *
 * @author AT347526
 */
public class DemoScene implements Scene{
    
    private CubeRenderer renderer;
    private SkyBoxRenderer skyRenderer;
    private Texture texture;
    private Renderable floor, sky;
    private Renderable cube;
    private Camera camera;
    private float previousAngle = 0f;
    private float angle, cAngle = 0f;
    private float anglePerSecond = 50f;
    private DoubleBuffer previousXpos = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer previousYpos = BufferUtils.createDoubleBuffer(1);
    private float mouseSpeed = 0.1f;
    
    private long window;

    @Override
    public void input() {
        int state = glfwGetKey(window, GLFW_KEY_DOWN);
        if (state == GLFW_PRESS) {
            camera.moveBackward(1);
        }
        
        state = glfwGetKey(window, GLFW_KEY_UP);
        if (state == GLFW_PRESS) {
            camera.moveForward(1);
        }
        
        state = glfwGetKey(window, GLFW_KEY_RIGHT);
        if (state == GLFW_PRESS) {
            camera.moveRight(1);
        }
        
        state = glfwGetKey(window, GLFW_KEY_LEFT);
        if (state == GLFW_PRESS) {
            camera.moveLeft(1);
        }
        
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xpos, ypos);
        double deltaXpos = xpos.get(0) - previousXpos.get(0);
        double deltaYpos = previousYpos.get(0) - ypos.get(0);
        previousXpos = xpos;
        previousYpos = ypos;
        //System.out.println("Delta yPos= "+deltaYpos);
        float horizontalAngle = mouseSpeed * (float) deltaXpos;
        float verticalAngle = mouseSpeed * (float) deltaYpos;
        camera.turn(horizontalAngle, verticalAngle);
    }

    @Override
    public void update(float delta) {
        previousAngle = angle;
        angle += delta * anglePerSecond;
    }

    @Override
    public void render(float alpha) {
        /* Clear drawing area */
        /*float lerpAngle = (1f - alpha) * previousAngle + alpha * angle * 0.00001f;
        Matrix4f model = new Matrix4f().rotate((float) Math.toRadians(lerpAngle), 0f, 0f, 1f);
        renderer.updateUniModel(model);*/
        
        /* Update camera if needed */
        if(camera.isUpdated()){
            skyRenderer.setCamera(camera);
            renderer.setCamera(camera);
        }
        renderer.clear();    
        skyRenderer.render();
        
        /* Draw objects */
        renderer.begin();
        //renderer.render(floor);
        //renderer.render(sky);
        renderer.render(cube);
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
        camera = new Camera(0f, 0f, 0f, 0f, 1f, 0f);
        renderer.init(camera);
        
        skyRenderer = new SkyBoxRenderer();
        skyRenderer.init(camera);
        
        /* Create texture */
        texture = Texture.loadTexture("resources/example.png");
        texture.bind();

        cube = new Cube(Color.WHITE, texture, 1, 1, 0, 10, 10, 10, 0, 0, texture.getWidth(), texture.getHeight());
        floor = new Rect3d(Color.GREEN, texture, -100, -100, 0, 1000, 1000, 0, 0, 0, 0);
        sky = new Rect3d(Color.BLUE, texture, -1000, -1000, 500, 10000, 10000, 0, 0, 0, 0);
        System.out.println("DONE");
        
        /*Vector3f view = new Vector3f(1,2,1);
        Vector3f pos = new Vector3f(1,1,1);
        Vector3f dir = Camera.calculateNormalizedDirection(pos, view);
        new Matrix4f().rotate((float) Math.toRadians(45), 1f, 0f, 0f)
                      .rotate((float) Math.toRadians(360-45), 0f, 0f, 1f)
                      .transformPoint(dir);
        pos.add(dir, view);
        System.out.println("Camera position: "+view.x+" "+view.y+" "+view.z);*/
        glfwGetCursorPos(window, previousXpos, previousYpos);
    }

    @Override
    public void quit() {
        renderer.dispose();
        texture.delete();
    }
    
}
