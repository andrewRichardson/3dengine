package com.arichardson.main.graphics;

import com.arichardson.main.Game;

public class Screen extends Render {

	private Render3D render;

	public Screen(int width, int height) {
		super(width, height);

		render = new Render3D(width, height);
	}

	public void render(Game game) {
		render.render(game);
		render.renderDistanceLimiter();
		draw(render, 0, 0);
	}

}
