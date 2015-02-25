package com.arichardson.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.arichardson.main.graphics.Screen;
import com.arichardson.main.input.InputHandler;

public class Display extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static int width = 800;
	public static int height = 600;

	private boolean running = false;

	private Thread thread;

	private JFrame frame;

	private int ups = 0;
	private int fps = 0;

	private static String title = "3D Game";

	private Screen screen;
	private BufferedImage img;
	private int[] pixels;
	private Game game;
	private InputHandler input;

	public Display() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);

		screen = new Screen(width, height);
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		game = new Game();
		input = new InputHandler();
		
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	public static void main(String[] args) {
		Display display = new Display();
		display.frame = new JFrame(title);
		display.frame.setResizable(false);
		display.frame.add(display);
		display.frame.pack();
		display.frame.setLocationRelativeTo(null);
		display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		display.frame.setVisible(true);
		display.start();

	}

	public synchronized void start() {
		if(running)
			return;
		
		running = true;

		thread = new Thread(this, "Game Thread");
		thread.start();
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long oldTime = System.nanoTime();
		long timer = System.currentTimeMillis();

		double ns = 1000000000.0 / 60.0;
		long newTime;
		double delta = 0;

		while (running) {
			newTime = System.nanoTime();
			delta += (double) (newTime - oldTime) / ns;
			oldTime = newTime;
			if (delta >= 1) {
				update();
				delta--;

				ups++;
			}
			render();
			fps++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + " | " + fps + " fps");
				System.out.println(ups + " ups, " + fps + " fps");
				fps = 0;
				ups = 0;
			}
		}
		stop();
	}

	private void update() {
		game.tick(input.keys);
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(game);

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		g.setColor(new Color(0x000000));
		g.fillRect(0, 0, width, height);

		for (int i = 0; i < width * height; i++) {
			pixels[i] = screen.pixels[i];
		}

		g.drawImage(img, 0, 0, null);

		g.dispose();
		bs.show();
	}

}
