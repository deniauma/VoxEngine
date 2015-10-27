/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine;

import java.awt.Color;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import voxengine.graphics.Renderer;
import voxengine.graphics.Texture;
import voxengine.graphics.renderer.BasicRenderer;
import voxengine.graphics.renderer.CubeRenderer;
import voxengine.graphics.shape.Cube;
import voxengine.graphics.shape.Rect;

/**
 *
 * @author AT347526
 */
public class DemoScene implements Scene{
    
    private CubeRenderer renderer;
    private Texture texture;
    private Rect rect, rect1;
    private Cube cube;

    @Override
    public void input() {
        
    }

    @Override
    public void update(float delta) {
        
    }

    @Override
    public void render(float alpha) {
        /* Clear drawing area */
        renderer.clear();
        
        /* Draw objects */
        renderer.begin();
        //renderer.render(rect.getVertices(), rect.getNbvertices());
        //renderer.render(rect1.getVertices(), rect1.getNbvertices());
        renderer.render(cube.getVertices(), cube.getNbvertices());
        renderer.end();
    }

    @Override
    public void prepare() {
        long window = GLFW.glfwGetCurrentContext();
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        int width = widthBuffer.get();
        int height = heightBuffer.get();
        renderer = new CubeRenderer();
        renderer.init();
        
        /* Create texture */
        texture = Texture.loadTexture("resources/example.png");
        texture.bind();
        
        //rect = new Rect(Color.WHITE, texture, 0, 0, 100, 100, 0, 0, texture.getWidth(), texture.getHeight());
        //rect1 = new Rect(Color.WHITE, texture, 200, 200, 100, 100, 0, 0, texture.getWidth(), texture.getHeight());
        cube = new Cube(Color.WHITE, texture, 10, 10, 10, 100, 100, 100, 0, 0, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void quit() {
        renderer.dispose();
        texture.delete();
    }
    
}
