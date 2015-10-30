/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine.graphics.shape;

import java.awt.Color;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import voxengine.graphics.Texture;
import voxengine.math.Vector2f;
import voxengine.math.Vector3f;

/**
 *
 * @author deniauma
 */
public class Cube {
    
    protected Vector3f position;
    protected int width;
    protected int height;
    protected int depth;
    protected final Color color;
    protected final Texture texture;
    protected Vector2f tposition;
    protected int twidth;
    protected int theight;
    protected final int nbSides = 6;
    protected Rect3d[] sides;
    
    protected final int nbvertices = 36;
    protected final int nbElementsPerVertice = 8;
    protected FloatBuffer vertices;
    private float[] verticesArray;
    
    public Cube(Color color, Texture texture, float x, float y, float z, int width, int height, int depth, int tx, int ty, int twidth, int theight) {
        this.position = new Vector3f(x, y, z);
        this.color = color;
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.tposition = new Vector2f(tx, ty);
        this.twidth = twidth;
        this.theight = theight;
        
        sides = new Rect3d[nbSides];
        vertices = BufferUtils.createFloatBuffer(nbvertices * nbElementsPerVertice);
        
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        
        /* Vertex positions */
        float x1 = position.x;
        float y1 = position.y;
        float z1 = position.z;
        float x2 = x1 + width;
        float y2 = y1 + height;
        float z2 = z1 + depth;

        /* Texture coordinates */
        float s1 = tposition.x / texture.getWidth();
        float t1 = tposition.y / texture.getHeight();
        float s2 = (s1 + twidth) / texture.getWidth();
        float t2 = (t1 + theight) / texture.getHeight();
        
        vertices.put(x1).put(y1).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x1).put(y2).put(z1).put(r).put(g).put(b).put(s1).put(t2);
        vertices.put(x2).put(y2).put(z1).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x1).put(y1).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x2).put(y2).put(z1).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x2).put(y1).put(z1).put(r).put(g).put(b).put(s2).put(t1);
        
        vertices.put(x1).put(y1).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x1).put(y1).put(z2).put(r).put(g).put(b).put(s1).put(t2);
        vertices.put(x2).put(y1).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x1).put(y1).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x2).put(y1).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x2).put(y1).put(z1).put(r).put(g).put(b).put(s2).put(t1);
        
        vertices.put(x1).put(y1).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x1).put(y1).put(z2).put(r).put(g).put(b).put(s1).put(t2);
        vertices.put(x1).put(y2).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x1).put(y1).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x1).put(y2).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x1).put(y2).put(z1).put(r).put(g).put(b).put(s2).put(t1);
        
        vertices.put(x2).put(y2).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x2).put(y2).put(z2).put(r).put(g).put(b).put(s1).put(t2);
        vertices.put(x1).put(y2).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x2).put(y2).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x1).put(y2).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x1).put(y2).put(z1).put(r).put(g).put(b).put(s2).put(t1);
        
        vertices.put(x2).put(y1).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x2).put(y1).put(z2).put(r).put(g).put(b).put(s1).put(t2);
        vertices.put(x2).put(y2).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x2).put(y1).put(z1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x2).put(y2).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x2).put(y2).put(z1).put(r).put(g).put(b).put(s2).put(t1);
        
        vertices.put(x1).put(y1).put(z2).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x1).put(y2).put(z2).put(r).put(g).put(b).put(s1).put(t2);
        vertices.put(x2).put(y2).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x1).put(y1).put(z2).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x2).put(y2).put(z2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x2).put(y1).put(z2).put(r).put(g).put(b).put(s2).put(t1);
        
        vertices.flip();
        
        verticesArray = new float[nbvertices * nbElementsPerVertice];
        vertices.get(verticesArray);
    }
    
    public float[] getVertices() {
        return verticesArray;
    }
    
    public int getNbvertices() {
        return nbvertices;
    }
    
}
