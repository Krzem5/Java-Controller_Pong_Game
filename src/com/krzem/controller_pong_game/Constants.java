package com.krzem.controller_pong_game;



import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;



public class Constants{
	public static final int DISPLAY_ID=0;
	public static final GraphicsDevice SCREEN=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[DISPLAY_ID];
	public static final Rectangle WINDOW_SIZE=SCREEN.getDefaultConfiguration().getBounds();
	public static final int MAX_FPS=60;

	public static final Color BG_COLOR=new Color(10,10,10);

	public static final Color TEXT_COLOR=new Color(55,55,55);
	public static final int TEXT_SIZE=500;

	public static final int BAT_WIDTH=65;
	public static final int BAT_HEIGHT=450;
	public static final int BAT_OFFSET_X=275;
	public static final int BAT_MIN_MOVE_SPEED=1;
	public static final int BAT_MAX_MOVE_SPEED=25;
	public static final double BAT_MOVE_LERP=0.1;
	public static final double BAT_MOVE_LERP_END_MAX_DIFF=1;

	public static final Color BAT_1_COLOR=new Color(90,210,240);
	public static final Color BAT_2_COLOR=new Color(235,180,85);

	public static final Color BALL_COLOR=new Color(190,105,240);
	public static final int BALL_RADIUS=50;
	public static final int BALL_SPEED=25;

	public static final int CONTROLLER_JOYSTICK_MOVE_BUFFOR=32;
}
