/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine;

import java.nio.ByteBuffer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import voxengine.util.Timer;

/**
 *
 * @author AT347526
 */
public class Engine {
    
    private static GLFWErrorCallback errorCallback
            = Callbacks.errorCallbackPrint(System.err);

    /**
     * This key callback will check if ESC is pressed and will close the window
     * if it is pressed.
     */
    private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, GL_TRUE);
            }
        }
    };
    
    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;
    
    private Scene scene;
    private long window;
    protected Timer timer;

    
    public void run() {
        timer = new Timer();
        scene = new TextScene();
        try {
            init();
            scene.prepare();
            loop();
            scene.quit();
            exit();
            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWErrorCallback
            glfwTerminate();
            errorCallback.release();
        }
    }
    
    private void init() {
        /* Set the error callback */
        glfwSetErrorCallback(errorCallback);

        /* Initialize GLFW */
        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        /* Create window */
        glfwDefaultWindowHints();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_ANY_PROFILE);
        window = glfwCreateWindow(640, 480, "Simple example", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        /* Center the window on screen */
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,
                (GLFWvidmode.width(vidmode) - 640) / 2,
                (GLFWvidmode.height(vidmode) - 480) / 2
        );

        /* Create OpenGL context */
        glfwMakeContextCurrent(window);
        
        /* Enable vertical synchronization */
        glfwSwapInterval(1);

        /* Set the key callback */
        glfwSetKeyCallback(window, keyCallback);
        
        /* Initialize timer */
        timer.init();
        
        GL.createCapabilities();
    }
    
    private void loop() {
        /* Timer useful variables */
        float delta;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;
        float alpha;
        
        /* Loop until window gets closed */
        while (glfwWindowShouldClose(window) != GL_TRUE) {
            /* Get delta time and update the accumulator */
            delta = timer.getDelta();
            accumulator += delta;
            
            /* Update game and timer UPS if enough time has passed */
            while (accumulator >= interval) {
                scene.update(interval);
                timer.updateUPS();
                accumulator -= interval;
            }

            /* Calculate alpha value for interpolation */
            alpha = accumulator / interval;

            /* Render game and update timer FPS */
            scene.render(alpha);
            timer.updateFPS();

            /* Update timer */
            timer.update();
            
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        
    }
    
    private void exit() {
        
    }

    
    public static void main(String[] args) {
        new Engine().run();
    }
    
}
