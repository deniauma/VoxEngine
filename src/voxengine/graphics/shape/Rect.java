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

/**
 *
 * @author Adminmatt
 */
public class Rect {
    
    protected Vector2f position;
    protected int width;
    protected int height;
    protected final Color color;
    protected final Texture texture;
    protected Vector2f tposition;
    protected int twidth;
    protected int theight;
    
    protected final int nbvertices = 6;
    protected FloatBuffer vertices;
    private float[] verticesArray;
    
    
    public Rect(Color color, Texture texture, float x, float y, int width, int height, int tx, int ty, int twidth, int theight) {
        this.position = new Vector2f(x, y);
        this.color = color;
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.tposition = new Vector2f(tx, ty);
        this.twidth = twidth;
        this.theight = theight;
        
        vertices = BufferUtils.createFloatBuffer(this.nbvertices * 7);
        
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        
        /* Vertex positions */
        float x1 = position.x;
        float y1 = position.y;
        float x2 = x1 + width;
        float y2 = y1 + height;

        /* Texture coordinates */
        float s1 = tposition.x / texture.getWidth();
        float t1 = tposition.y / texture.getHeight();
        float s2 = (s1 + twidth) / texture.getWidth();
        float t2 = (t1 + theight) / texture.getHeight();
        
        vertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x1).put(y2).put(r).put(g).put(b).put(s1).put(t2);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2);

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x2).put(y1).put(r).put(g).put(b).put(s2).put(t1);
        
        vertices.flip();
        
        verticesArray = new float[this.nbvertices * 7];
        vertices.get(verticesArray);
    }
    
    public float[] getVertices() {
        return verticesArray;
    }
    
    public int getNbvertices() {
        return nbvertices;
    }
    
}
