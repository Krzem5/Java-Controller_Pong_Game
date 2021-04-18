package com.krzem.controller_pong_game;



import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.Exception;
import java.lang.Math;
import javax.swing.JFrame;



public class Main extends Constants{
	public static void main(String[] args){
		new Main(args);
	}



	public double FPS=1;
	public Controller CONTROLLER;
	public Engine e;
	public JFrame frame;
	public Canvas canvas;
	private Runnable _ru;
	private boolean _break=false;



	public Main(String[] args){
		this.init();
		this.frame_init();
		this.run();
	}



	public void init(){
		this.CONTROLLER=Controller.list().get(0);
		this.e=new Engine(this);
	}



	public void frame_init(){
		Main cls=this;
		this.frame=new JFrame("2D Controller Game");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setUndecorated(true);
		this.frame.setResizable(false);
		this.frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				cls.quit();
			}
		});
		SCREEN.setFullScreenWindow(this.frame);
		this.canvas=new Canvas(this);
		this.canvas.setSize(WINDOW_SIZE.width,WINDOW_SIZE.height);
		this.canvas.setPreferredSize(new Dimension(WINDOW_SIZE.width,WINDOW_SIZE.height));
		this.frame.setContentPane(this.canvas);
		this.canvas.requestFocus();
		this.canvas.setCursor(this.canvas.getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(),null));
	}



	public void run(){
		Main cls=this;
		this._ru=new Runnable(){
			@Override
			public void run(){
				while (cls._break==false){
					long s=System.currentTimeMillis();
					try{
						cls.update();
						cls.canvas.repaint();
					}
					catch (Exception e){
						e.printStackTrace();
					}
					long d=System.currentTimeMillis()-s;
					if (d==0){
						d=1L;
					}
					if ((double)Math.floor(1/(double)d*1e8)/1e5>cls.MAX_FPS){
						try{
							Thread.sleep((long)(1/(double)cls.MAX_FPS*1e3)-d);
						}
						catch (InterruptedException e){}
					}
					cls.FPS=(double)Math.floor(1/(double)(System.currentTimeMillis()-s)*1e8)/1e5;
				}
			}
		};
		new Thread(this._ru).start();
	}



	public void update(){
		if (this.e!=null){
			this.e.update();
		}
		if (this.CONTROLLER.get("ps-button")==1){
			this.quit();
		}
	}



	public void draw(Graphics2D g){
		if (this.e==null){
			return;
		}
		this.e.draw(g);
	}



	private void quit(){
		if (this._break==true){
			return;
		}
		this._break=true;
		this.frame.dispose();
		this.frame.dispatchEvent(new WindowEvent(this.frame,WindowEvent.WINDOW_CLOSING));
	}
}
