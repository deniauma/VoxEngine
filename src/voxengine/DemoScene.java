/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine;

import java.awt.Color;
import java.nio.DoubleBuffer;
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
import voxengine.math.joml.Vector3f;

/**
 *
 * @author AT347526
 */
public class DemoScene implements Scene{
    
    private CubeRenderer renderer;
    private Texture texture;
    private Rect3d floor;
    private Cube cube;
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
            renderer.setCamera(camera);
        }
        
        state = glfwGetKey(window, GLFW_KEY_UP);
        if (state == GLFW_PRESS) {
            camera.moveForward(1);
            renderer.setCamera(camera);
        }
        
        state = glfwGetKey(window, GLFW_KEY_RIGHT);
        if (state == GLFW_PRESS) {
            camera.moveRight(1);
            renderer.setCamera(camera);
        }
        
        state = glfwGetKey(window, GLFW_KEY_LEFT);
        if (state == GLFW_PRESS) {
            camera.moveLeft(1);
            renderer.setCamera(camera);
        }
        
        state = glfwGetKey(window, GLFW_KEY_E);
        if (state == GLFW_PRESS) {
            camera.turnRight(360-10);
            renderer.setCamera(camera);
        }
        
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xpos, ypos);
        double deltaXpos = xpos.get(0) - previousXpos.get(0);
        double deltaYpos = ypos.get(0) - previousYpos.get(0);
        previousXpos = xpos;
        previousYpos = ypos;
        float horizontalAngle = mouseSpeed * (float) deltaXpos;
        float verticalAngle = mouseSpeed * (float) deltaYpos;
        //System.out.println("horizontalAngle: "+horizontalAngle+", verticalAngle: "+verticalAngle);
        camera.turnRight(horizontalAngle);
        renderer.setCamera(camera);
        //System.out.println("deltaXpos: "+deltaXpos+", deltaYpos: "+deltaYpos);
    }

    @Override
    public void update(float delta) {
        
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
        //renderer.render(floor.getVertices(), floor.getNbvertices());
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

        cube = new Cube(Color.WHITE, texture, 1, 1, 0, 10, 10, 10, 0, 0, texture.getWidth(), texture.getHeight());
        floor = new Rect3d(Color.GREEN, texture, -100, -100, 0, 1000, 1000, 0, 0, 0, 0);
        System.out.println("DONE");
        
        Vector3f newView = new Vector3f(1,0,1);
        Vector3f center = new Vector3f(1,-90,1);
        new Matrix4f().translate(center)
                      .rotate((float) Math.toRadians(360-90), 0f, 0f, 1f)
                      .translate(center.negate())
                      .transformPoint(newView);
        System.out.println("Camera position: "+newView.x+" "+newView.y+" "+newView.z);
        glfwGetCursorPos(window, previousXpos, previousYpos);
    }

    @Override
    public void quit() {
        renderer.dispose();
        texture.delete();
    }
    
}
