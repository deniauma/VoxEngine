/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine;

import java.awt.Color;
import voxengine.graphics.Renderer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import voxengine.graphics.Shader;
import voxengine.graphics.ShaderProgram;
import voxengine.graphics.Texture;
import voxengine.graphics.VertexArrayObject;
import voxengine.graphics.VertexBufferObject;
import voxengine.math.Matrix4f;
import voxengine.util.Timer;
/**
 *
 * @author AT347526
 */
public class TextScene implements Scene{
    
    private Renderer renderer = new Renderer();

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
        
        int gameWidth = 640;
        int gameHeight = 480;
        
        /* Draw score */
        String scoreText = "Premier text";
        int scoreTextWidth = renderer.getTextWidth(scoreText);
        int scoreTextHeight = renderer.getTextHeight(scoreText);
        float scoreTextX = (gameWidth - scoreTextWidth) / 2f;
        float scoreTextY = gameHeight - scoreTextHeight - 5;
        renderer.drawText(scoreText, scoreTextX, scoreTextY, Color.RED);
    }

    @Override
    public void prepare() {
        long window = GLFW.glfwGetCurrentContext();
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        int width = widthBuffer.get();
        int height = heightBuffer.get();
        renderer.init(true);
    }

    @Override
    public void quit() {
        renderer.dispose();
    }
    
}
