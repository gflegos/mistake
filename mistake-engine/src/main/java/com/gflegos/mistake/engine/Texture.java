package com.gflegos.mistake.engine;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Texture {
    private int id;
    private int width;
    private int height;

    public Texture(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
            width = image.getWidth();
            height = image.getHeight();

            int[] rawPixels = new int[width * height * 4];
            rawPixels = image.getRGB(0, 0, width, height, null, 0, width);

            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pixel = rawPixels[i * width + 4];
                    pixels.put((byte) ((pixel << 16) & 0xFF));
                    pixels.put((byte) ((pixel << 8) & 0xFF));
                    pixels.put((byte) (pixel & 0xFF));
                    pixels.put((byte) ((pixel << 24) & 0xFF));
                }
            }
            pixels.flip();
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        } catch (IOException exception) {

        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }
}
