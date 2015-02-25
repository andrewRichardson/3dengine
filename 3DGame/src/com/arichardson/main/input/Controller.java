package com.arichardson.main.input;

public class Controller {

	public double x, z, rotation, xa, za, rota;

	public void tick(boolean forward, boolean back, boolean left,
			boolean right, boolean turnLeft, boolean turnRight) {
		double rotSpeed = 0.04;
		int walkSpeed = 1;
		double xMove = 0;
		double zMove = 0;

		if (forward) {
			zMove++;
		}

		if (back) {
			zMove--;
		}

		if (left) {
			xMove--;
		}

		if (right) {
			xMove++;
		}

		if (turnLeft) {
			rota -= rotSpeed;
		}

		if (turnRight) {
			rota += rotSpeed;
		}

		za += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation))
				* walkSpeed;
		xa += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation))
				* walkSpeed;

		x += xa;
		z += za;
		xa *= 0.1;
		za *= 0.1;

		rotation += rota;
		rota *= 0.5;
	}

}
