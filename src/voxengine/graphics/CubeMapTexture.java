/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxengine.graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

/**
 *
 * @author AT347526
 */
public class CubeMapTexture {
    
    public static final String
            RIGHT = "right",
            LEFT = "left",
            TOP = "top",
            BOTTOM = "bottom",
            FRONT = "front",
            BACK = "back";
    
    /**
     * Stores the handle of the texture.
     */
    private final int id;

    /**
     * Creates a texture with specified width, height and data.
     *
     * @param data Picture Data in RGBA format
     */
    public CubeMapTexture(Map<String, SubCubeMapTexture> data) {
        id = glGenTextures();

        glBindTexture(GL_TEXTURE_CUBE_MAP, id);

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGBA8, data.get(RIGHT).sWidth, data.get(RIGHT).sHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, data.get(RIGHT).sData);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGBA8, data.get(LEFT).sWidth, data.get(LEFT).sHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, data.get(LEFT).sData);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGBA8, data.get(TOP).sWidth, data.get(TOP).sHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, data.get(TOP).sData);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGBA8, data.get(BOTTOM).sWidth, data.get(BOTTOM).sHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, data.get(BOTTOM).sData);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGBA8, data.get(BACK).sWidth, data.get(BACK).sHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, data.get(BACK).sData);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGBA8, data.get(FRONT).sWidth, data.get(FRONT).sHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, data.get(FRONT).sData);
    }

    /**
     * Binds the texture.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
    }

    /**
     * Delete the texture.
     */
    public void delete() {
        glDeleteTextures(id);
    }

    /**
     * Load textures from files.
     *
     * @param pathes File pathes of the textures
     * @return Texture from specified file
     */
    public static CubeMapTexture loadTextures(Map<String, String> pathes) {
        BufferedImage image = null;
        Map<String, SubCubeMapTexture> data = new HashMap<>();
        for(Map.Entry<String, String> entry : pathes.entrySet()) {
            try {
                InputStream in = new FileInputStream(entry.getValue());
                image = ImageIO.read(in);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load a texture file!"
                        + System.lineSeparator() + ex.getMessage());
            }
            if (image != null) {
                /* Flip image Horizontal to get the origin to bottom left */
                AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
                transform.translate(0, -image.getHeight());
                AffineTransformOp operation = new AffineTransformOp(transform,
                        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = operation.filter(image, null);

                /* Get width and height of image */
                int width = image.getWidth();
                int height = image.getHeight();

                /* Get pixel data of image */
                int[] pixels = new int[width * height];
                image.getRGB(0, 0, width, height, pixels, 0, width);

                /* Put pixel data into a ByteBuffer */
                ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        /* Pixel as RGBA: 0xAARRGGBB */
                        int pixel = pixels[y * width + x];
                        /* Red component 0xAARRGGBB >> 16 = 0x0000AARR */
                        buffer.put((byte) ((pixel >> 16) & 0xFF));
                        /* Green component 0xAARRGGBB >> 8 = 0x00AARRGG */
                        buffer.put((byte) ((pixel >> 8) & 0xFF));
                        /* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */
                        buffer.put((byte) (pixel & 0xFF));
                        /* Alpha component 0xAARRGGBB >> 24 = 0x000000AA */
                        buffer.put((byte) ((pixel >> 24) & 0xFF));
                    }
                }
                /* Do not forget to flip the buffer! */
                buffer.flip();

                data.put(entry.getKey(), new SubCubeMapTexture(width, height, buffer));
            } else {
                throw new RuntimeException("File extension not supported!"
                        + System.lineSeparator() + "The following file extensions "
                        + "are supported: "
                        + Arrays.toString(ImageIO.getReaderFileSuffixes()));
            }
        }      
        
        return new CubeMapTexture(data);
    }
    
}
