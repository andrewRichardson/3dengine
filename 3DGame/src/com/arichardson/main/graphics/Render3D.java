package com.arichardson.main.graphics;

import com.arichardson.main.Game;

public class Render3D extends Render {
	
	public double[] zBuffer;
	private double renderDistance = 5000;

	public Render3D(int width, int height) {
		super(width, height);
		
		zBuffer = new double[width*height];
	}
	
	public void render(Game game) {
		
		double floor = 8;
		double ceiling = 8;
		double forward = game.controls.z;
		double right = game.controls.x;
		
		double rotation = game.controls.rotation;
		double cos = Math.cos(rotation);
		double sin = Math.sin(rotation);
		
		for (int y = 0; y < height; y++) {
			double yd = (y - height / 2.0) / height;

			double z = floor / yd;
			
			if (yd < 0) {
				z  = ceiling / -yd;
			}

			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cos + z * sin;
				double yy = z * cos - depth * sin;
				int xPix = (int) (xx + forward);
				int yPix = (int) (yy + right);
				zBuffer[x + y * width] = z;
				//pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
				pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) + 128];
				
				if(z > 500){
					pixels[x + y * width] = 0;
				}
			}
		}
	}
	
	public void renderDistanceLimiter(){
		for(int i = 0; i < width * height; i++){
			int color = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));
			
			if(brightness < 0) {
				brightness = 0;
			}
			if(brightness > 255){
				brightness = 255;
			}
			
			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;
			
			r = r*brightness / 255;
			g = g*brightness / 255;
			b = b*brightness / 255;
			
			pixels[i] = r << 16 | g << 8 | b;
		}
	}

}
