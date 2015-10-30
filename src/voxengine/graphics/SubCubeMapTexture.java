/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine.graphics;

import java.nio.ByteBuffer;

/**
 *
 * @author AT347526
 */
public class SubCubeMapTexture {

    public final int sWidth;
    public final int sHeight;
    public final ByteBuffer sData;

    SubCubeMapTexture(int width, int height, ByteBuffer data) {
        this.sWidth = width;
        this.sHeight = height;
        this.sData = data;
    }

}
