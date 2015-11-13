/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine.graphics.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import voxengine.graphics.Camera;
import voxengine.graphics.CubeMapTexture;
import voxengine.graphics.Shader;
import voxengine.graphics.ShaderProgram;
import voxengine.graphics.VertexArrayObject;
import voxengine.graphics.VertexBufferObject;
import voxengine.math.joml.Matrix3f;
import voxengine.math.joml.Matrix4f;
import voxengine.math.joml.Vector3f;

/**
 *
 * @author AT347526
 */
public class SkyBoxRenderer {
    
    private VertexArrayObject vao;
    private VertexBufferObject vbo;
    private Shader vertexShader;
    private Shader fragmentShader;
    private ShaderProgram program;
    private CubeMapTexture texture;

    private int uniModel;
    private int uniView;
    private int uniProjection;

    private FloatBuffer vertices;
    private int numVertices = 36;
    private boolean drawing;

    private Camera camera;

    /**
     * Initializes the renderer.
     *
     * @param camera (use to set up the lookAt matrix)
     */
    public void init(Camera camera) {

        this.camera = camera;
        /* Generate Vertex Array Object */
        vao = new VertexArrayObject();
        vao.bind();

        /* Generate Vertex Buffer Object */
        vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);

        /* Create FloatBuffer */
        vertices = BufferUtils.createFloatBuffer(numVertices * 3);
        vertices.put(-1f).put(1f).put(-1f);
        vertices.put(-1f).put(-1f).put(-1f);
        vertices.put(1f).put(-1f).put(-1f);
        vertices.put(1f).put(-1f).put(-1f);
        vertices.put(1f).put(1f).put(-1f);
        vertices.put(-1f).put(1f).put(-1f);

        vertices.put(-1f).put(-1f).put(1f);
        vertices.put(-1f).put(-1f).put(-1f);
        vertices.put(-1f).put(1f).put(-1f);
        vertices.put(-1f).put(1f).put(-1f);
        vertices.put(-1f).put(1f).put(1f);
        vertices.put(-1f).put(-1f).put(1f);

        vertices.put(1f).put(-1f).put(-1f);
        vertices.put(1f).put(-1f).put(1f);
        vertices.put(1f).put(1f).put(1f);
        vertices.put(1f).put(1f).put(1f);
        vertices.put(1f).put(1f).put(-1f);
        vertices.put(1f).put(-1f).put(-1f);

        vertices.put(-1f).put(-1f).put(1f);
        vertices.put(-1f).put(1f).put(1f);
        vertices.put(1f).put(1f).put(1f);
        vertices.put(1f).put(1f).put(1f);
        vertices.put(1f).put(-1f).put(1f);
        vertices.put(-1f).put(-1f).put(1f);

        vertices.put(-1f).put(1f).put(-1f);
        vertices.put(-1f).put(1f).put(1f);
        vertices.put(1f).put(1f).put(1f);
        vertices.put(-1f).put(1f).put(-1f);
        vertices.put(1f).put(1f).put(-1f);
        vertices.put(1f).put(1f).put(1f);

        vertices.put(-1f).put(-1f).put(-1f);
        vertices.put(-1f).put(-1f).put(1f);
        vertices.put(1f).put(-1f).put(1f);
        vertices.put(1f).put(-1f).put(1f);
        vertices.put(-1f).put(-1f).put(-1f);
        vertices.put(1f).put(-1f).put(-1f);
        
        vertices.flip();
        
        vbo.uploadData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        /* Initialize variables */
        drawing = false;

        /* Load shaders */
        vertexShader = Shader.loadShader(GL_VERTEX_SHADER, "resources/skybox_vertex.glsl");
        fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, "resources/skybox_fragment.glsl");

        /* Create shader program */
        program = new ShaderProgram();
        program.attachShader(vertexShader);
        program.attachShader(fragmentShader);
        //program.bindFragmentDataLocation(0, "fragColor");
        program.link();
        program.use();

        /* Get width and height of framebuffer */
        long window = GLFW.glfwGetCurrentContext();
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        int width = widthBuffer.get();
        int height = heightBuffer.get();

        /* Specify Vertex Pointers */
        specifyVertexAttributes();

        /* Set view matrix to identity matrix 
         Matrix4f view = new Matrix4f();*/
        Matrix4f view = new Matrix4f().lookAt(camera.position, camera.view, new Vector3f(0f, 0f, 1f));
        uniView = program.getUniformLocation("view");
        program.setUniform(uniView, view);

        /* Set projection matrix to a perpective projection */
        Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) width / height, 0.01f, 1000f);
        uniProjection = program.getUniformLocation("projection");
        program.setUniform(uniProjection, projection);
        
        Map<String, String> skyBoxtextures = new HashMap<>();
        skyBoxtextures.put(CubeMapTexture.RIGHT, "resources/right.jpg");
        skyBoxtextures.put(CubeMapTexture.LEFT, "resources/left.jpg");
        skyBoxtextures.put(CubeMapTexture.TOP, "resources/top.jpg");
        skyBoxtextures.put(CubeMapTexture.BOTTOM, "resources/bottom.jpg");
        skyBoxtextures.put(CubeMapTexture.FRONT, "resources/front.jpg");
        skyBoxtextures.put(CubeMapTexture.BACK, "resources/back.jpg");
        texture = CubeMapTexture.loadTextures(skyBoxtextures);

    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDepthMask(false);
        program.use();
        updateUniView();
        vao.bind();
        program.setUniform(program.getUniformLocation("skybox"), 0);
        texture.bind();
        //vbo.bind(GL_ARRAY_BUFFER);
        
        
        glDrawArrays(GL_TRIANGLES, 0, numVertices);
        glDepthMask(true);
    }

    /**
     * Dispose renderer and clean up its used data.
     */
    public void dispose() {
        if (vao != null) {
            vao.delete();
        }
        vbo.delete();
        vertexShader.delete();
        fragmentShader.delete();
        program.delete();
    }

    /**
     * Specifies the vertex pointers.
     */
    private void specifyVertexAttributes() {
        /* Specify Vertex Pointer */
        int posAttrib = program.getAttributeLocation("position");
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 3, 3 * Float.BYTES, 0);
    }

    public void updateUniView() {
        Matrix4f view = new Matrix4f().lookAt(camera.position, camera.view, new Vector3f(0f, 0f, 1f));
        Matrix3f m3 = new Matrix3f();
        System.out.println("View matrix: " + view.toString());
        view.get3x3(m3);
        m3.get(view);
        System.out.println("New view matrix: " + view.toString());
        program.setUniform(uniView, view);
    }

    public void updateUniProjection(Matrix4f projection) {
        program.setUniform(uniProjection, projection);
    }
    
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    
}
