package com.gflegos.mistake.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import java.util.Map;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Engine implements Runnable {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private Thread thread;
    public boolean running;

    public long window;

    public Map<Model, Texture> elements;

    public void start() {
        running = true;
        thread = new Thread(this, "Mistake");
        thread.start();
    }

    public void init() {
        if (!glfwInit()) {
            System.err.println("GLFW initialisation failed");
        }
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Mistake", NULL, NULL);

        if (window == NULL) {
            System.err.println("Could not create window");
        }

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,100, 100);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glClearColor(0,0,0,1);
        glEnable(GL_DEPTH_TEST);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));

        float[] vertices = new float[] {
            -0.5f, 0.5f, 0,
            0.5f, 0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,
            -0.5f, -0.5f, 0,
            -0.5f, 0.5f, 0
        };

        float[] textureCoordinates = new float[] {
            0,0,
            1,0,
            1,1,
            1,1,
            0,1,
            0,0
        };

        Model model = new Model(vertices, textureCoordinates);
        Texture texture = new Texture("./resources/Capture2.PNG");

        elements.put(model, texture);
    }

    public void update() {
        glfwPollEvents();

    }

    public void render() {
        glfwSwapBuffers(window);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
        for (Map.Entry<Model, Texture> element : elements.entrySet()) {
            element.getValue().bind();
            element.getKey().render();
        }
    }

    @Override
    public void run() {
        init();
        while (running) {
            update();
            render();

            if (glfwWindowShouldClose(window)) {
                running = false;
            }
        }
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.start();
    }
}