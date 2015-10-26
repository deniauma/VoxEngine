/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine.text;

/**
 * This class represents a font glyph.
 *
 * @author Heiko Brumme
 */
public class Glyph {

    public final int width;
    public final int height;
    public final int x;
    public final int y;

    /**
     * Creates a font Glyph.
     *
     * @param width Width of the Glyph
     * @param height Height of the Glyph
     * @param x X coordinate on the font texture
     * @param y Y coordinate on the font texture
     */
    public Glyph(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }
}
