package com.krzem.controller_game;



import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.lang.Math;
import java.util.Random;



public class Engine extends Constants{
	public Main cls;
	private Random rg=new Random();
	private double b1dy=WINDOW_SIZE.height/2-BAT_HEIGHT/2;
	private double b2dy=WINDOW_SIZE.height/2-BAT_HEIGHT/2;
	private double b1y=WINDOW_SIZE.height/2-BAT_HEIGHT/2;
	private double b2y=WINDOW_SIZE.height/2-BAT_HEIGHT/2;
	private double b1o;
	private double b2o;
	private double bx;
	private double by;
	private double bvx;
	private double bvy;
	private boolean rl;
	private boolean rr;
	private long etm;
	private String tm;
	private int[] sl;



	public Engine(Main cls){
		this.cls=cls;
		this._reset();
	}



	public void update(){
		if (this.cls.CONTROLLER.get("left-click")==1){
			this.rl=true;
		}
		if (this.cls.CONTROLLER.get("right-click")==1){
			this.rr=true;
		}
		if (this.rl==true&&this.rr==true&&this.etm==-1){
			this._s_ball();
		}
		if (this.cls.CONTROLLER.get("left-joystick-y")<-CONTROLLER_JOYSTICK_MOVE_BUFFOR){
			this.b1dy-=this._map(this.cls.CONTROLLER.get("left-joystick-y"),-CONTROLLER_JOYSTICK_MOVE_BUFFOR,-128,BAT_MIN_MOVE_SPEED,BAT_MAX_MOVE_SPEED);
		}
		if (this.cls.CONTROLLER.get("left-joystick-y")>CONTROLLER_JOYSTICK_MOVE_BUFFOR){
			this.b1dy+=this._map(this.cls.CONTROLLER.get("left-joystick-y"),CONTROLLER_JOYSTICK_MOVE_BUFFOR,128,BAT_MIN_MOVE_SPEED,BAT_MAX_MOVE_SPEED);
		}
		if (this.cls.CONTROLLER.get("right-joystick-y")<-CONTROLLER_JOYSTICK_MOVE_BUFFOR){
			this.b2dy-=this._map(this.cls.CONTROLLER.get("right-joystick-y"),-CONTROLLER_JOYSTICK_MOVE_BUFFOR,-128,BAT_MIN_MOVE_SPEED,BAT_MAX_MOVE_SPEED);
		}
		if (this.cls.CONTROLLER.get("right-joystick-y")>CONTROLLER_JOYSTICK_MOVE_BUFFOR){
			this.b2dy+=this._map(this.cls.CONTROLLER.get("right-joystick-y"),CONTROLLER_JOYSTICK_MOVE_BUFFOR,128,BAT_MIN_MOVE_SPEED,BAT_MAX_MOVE_SPEED);
		}
		if (this.cls.CONTROLLER.get("l2")==1){
			this._calc_perfect_1();
		}
		if (this.cls.CONTROLLER.get("r2")==1){
			this._calc_perfect_2();
		}
		if (this.etm-System.nanoTime()<0){
			this.tm="";
		}
		else if (this.etm-System.nanoTime()<1e9&&this.bvx==0&&this.bvy==0){
			this.tm="GO!";
			this._ball();
		}
		else if (this.etm-System.nanoTime()<2e9){
			this.tm="1";
		}
		else if (this.etm-System.nanoTime()<3e9){
			this.tm="2";
		}
		else{
			this.tm="3";
		}
		this.b1dy=Math.min(Math.max(this.b1dy,0),WINDOW_SIZE.height-BAT_HEIGHT);
		this.b2dy=Math.min(Math.max(this.b2dy,0),WINDOW_SIZE.height-BAT_HEIGHT);
		if (Math.abs(this.b1y-this.b1dy)<=BAT_MOVE_LERP_END_MAX_DIFF){
			this.b1y=this.b1dy+0;
		}
		else{
			double pb1y=this.b1y+0;
			this.b1y=this.b1y+(this.b1dy-this.b1y)*BAT_MOVE_LERP;
			if (Math.abs(this.b1y-pb1y)>BAT_MAX_MOVE_SPEED){
				if (pb1y<this.b1y){
					this.b1y=pb1y+BAT_MAX_MOVE_SPEED;
				}
				else{
					this.b1y=pb1y-BAT_MAX_MOVE_SPEED;
				}
			}
		}
		if (Math.abs(this.b2y-this.b2dy)<=BAT_MOVE_LERP_END_MAX_DIFF){
			this.b2y=this.b2dy+0;
		}
		else{
			double pb2y=this.b2y+0;
			this.b2y=this.b2y+(this.b2dy-this.b2y)*BAT_MOVE_LERP;
			if (Math.abs(this.b2y-pb2y)>BAT_MAX_MOVE_SPEED){
				if (pb2y<this.b2y){
					this.b2y=pb2y+BAT_MAX_MOVE_SPEED;
				}
				else{
					this.b2y=pb2y-BAT_MAX_MOVE_SPEED;
				}
			}
		}
		this.b1y=Math.min(Math.max(this.b1y,0),WINDOW_SIZE.height-BAT_HEIGHT);
		this.b2y=Math.min(Math.max(this.b2y,0),WINDOW_SIZE.height-BAT_HEIGHT);
		this.bx+=this.bvx;
		this.by+=this.bvy;
		boolean h=false;
		if (this.by>=WINDOW_SIZE.height-BALL_RADIUS){
			this.by=WINDOW_SIZE.height-BALL_RADIUS;
			this.bvy*=-1;
			h=true;
		}
		if (this.by<=BALL_RADIUS){
			this.by=BALL_RADIUS+0;
			this.bvy*=-1;
			h=true;
		}
		if (this.bx>BAT_OFFSET_X&&this.bx<=BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS&&this.by>this.b1y-BALL_RADIUS&&this.by<this.b1y+BAT_HEIGHT+BALL_RADIUS){
			this.bx=BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS;
			this.bvx*=-1;
			h=true;
		}
		if (this.bx>=WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS&&this.bx<WINDOW_SIZE.width-BAT_OFFSET_X&&this.by>this.b2y-BALL_RADIUS&&this.by<this.b2y+BAT_HEIGHT+BALL_RADIUS){
			this.bx=WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS;
			this.bvx*=-1;
			h=true;
		}
		if (this.bx>BAT_OFFSET_X-BALL_RADIUS&&this.bx<BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS&&this.by>=this.b1y+BAT_HEIGHT+BALL_RADIUS&&this.by<this.b1y){
			this.by=this.b1y+BAT_HEIGHT+BALL_RADIUS;
			this.bvy*=-1;
			h=true;
		}
		if (this.bx>BAT_OFFSET_X-BALL_RADIUS&&this.bx<BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS&&this.by>this.b1y+BAT_HEIGHT&&this.by<=this.b1y-BALL_RADIUS){
			this.by=this.b1y-BALL_RADIUS;
			this.bvy*=-1;
			h=true;
		}
		if (this.bx>WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS&&this.bx<WINDOW_SIZE.width-BAT_OFFSET_X+BALL_RADIUS&&this.by>=this.b2y+BAT_HEIGHT+BALL_RADIUS&&this.by<this.b2y){
			this.by=this.b2y+BAT_HEIGHT+BALL_RADIUS;
			this.bvy*=-1;
			h=true;
		}
		if (this.bx>WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS&&this.bx<WINDOW_SIZE.width-BAT_OFFSET_X+BALL_RADIUS&&this.by>this.b2y+BAT_HEIGHT&&this.by<=this.b2y-BALL_RADIUS){
			this.by=this.b2y-BALL_RADIUS;
			this.bvy*=-1;
			h=true;
		}
		if (this.bx>=WINDOW_SIZE.width+BALL_RADIUS){
			this._s_ball();
			this.sl[1]++;
			h=true;
		}
		if (this.bx<=-BALL_RADIUS){
			this._s_ball();
			this.sl[0]++;
			h=true;
		}
		if (h==true){
			this.b1o=this._map(Math.max(Math.min(this.rg.nextGaussian(),1),-1),-1,1,-BAT_HEIGHT/3,BAT_HEIGHT/3);
			this.b2o=this._map(Math.max(Math.min(this.rg.nextGaussian(),1),-1),-1,1,-BAT_HEIGHT/3,BAT_HEIGHT/3);
		}
	}



	public void draw(Graphics2D g){
		g.setColor(BG_COLOR);
		g.fillRect(0,0,WINDOW_SIZE.width,WINDOW_SIZE.height);
		g.setColor(TEXT_COLOR);
		g.setFont(new Font("consolas",Font.PLAIN,TEXT_SIZE));
		FontMetrics fm=g.getFontMetrics();
		g.drawString(this.tm,WINDOW_SIZE.width/2-fm.stringWidth(this.tm)/2,WINDOW_SIZE.height/2-fm.getAscent());
		g.setColor(BAT_1_COLOR);
		g.fillRect(BAT_OFFSET_X,(int)this.b1y,BAT_WIDTH,BAT_HEIGHT);
		g.setColor(BAT_2_COLOR);
		g.fillRect(WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH,(int)this.b2y,BAT_WIDTH,BAT_HEIGHT);
		g.setColor(BALL_COLOR);
		g.fillOval((int)this.bx-BALL_RADIUS,(int)this.by-BALL_RADIUS,BALL_RADIUS*2,BALL_RADIUS*2);
	}



	private double _map(double v,double aa,double ab,double ba,double bb){
		return (v-aa)/(ab-aa)*(bb-ba)+ba;
	}



	private void _s_ball(){
		this.etm=System.nanoTime()+(long)4e9;
		this.bx=WINDOW_SIZE.width/2;
		this.by=WINDOW_SIZE.height/2;
		this.bvx=0;
		this.bvy=0;
	}



	private void _ball(){
		double a=this._map(this.rg.nextGaussian(),-1,1,-Math.PI/2,Math.PI/2);
		if (a<0){
			a-=Math.PI/2;
		}
		a-=Math.PI/4;
		this.bx=WINDOW_SIZE.width/2;
		this.by=WINDOW_SIZE.height/2;
		this.bvx=Math.cos(a)*BALL_SPEED;
		this.bvy=Math.sin(a)*BALL_SPEED;
	}



	private void _calc_perfect_1(){
		if (this.bvx==0&&this.bvy==0){
			this.b1dy=by-BAT_HEIGHT/2+this.b1o;
			return;
		}
		double bx=this.bx+0;
		double by=this.by+0;
		double bvx=this.bvx+0;
		double bvy=this.bvy+0;
		while (bx>BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS){
			bx+=bvx;
			by+=bvy;
			if (by>=WINDOW_SIZE.height-BALL_RADIUS){
				by=WINDOW_SIZE.height-BALL_RADIUS;
				bvy*=-1;
			}
			if (by<=BALL_RADIUS){
				by=BALL_RADIUS+0;
				bvy*=-1;
			}
			if (bx>BAT_OFFSET_X&&bx<=BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS&&by>this.b1y-BALL_RADIUS&&by<this.b1y+BAT_HEIGHT+BALL_RADIUS){
				bx=BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS;
				bvx*=-1;
			}
			if (bx>=WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS&&bx<WINDOW_SIZE.width-BAT_OFFSET_X&&by>this.b2y-BALL_RADIUS&&by<this.b2y+BAT_HEIGHT+BALL_RADIUS){
				bx=WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS;
				bvx*=-1;
			}
			if (bx>BAT_OFFSET_X-BALL_RADIUS&&bx<BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS&&by>=this.b1y+BAT_HEIGHT+BALL_RADIUS&&by<this.b1y){
				by=this.b1y+BAT_HEIGHT+BALL_RADIUS;
				bvy*=-1;
			}
			if (bx>BAT_OFFSET_X-BALL_RADIUS&&bx<BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS&&by>this.b1y+BAT_HEIGHT&&by<=this.b1y-BALL_RADIUS){
				by=this.b1y-BALL_RADIUS;
				bvy*=-1;
			}
			if (bx>WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS&&bx<WINDOW_SIZE.width-BAT_OFFSET_X+BALL_RADIUS&&by>=this.b2y+BAT_HEIGHT+BALL_RADIUS&&by<this.b2y){
				by=this.b2y+BAT_HEIGHT+BALL_RADIUS;
				bvy*=-1;
			}
			if (bx>WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS&&bx<WINDOW_SIZE.width-BAT_OFFSET_X+BALL_RADIUS&&by>this.b2y+BAT_HEIGHT&&by<=this.b2y-BALL_RADIUS){
				by=this.b2y-BALL_RADIUS;
				bvy*=-1;
			}
			if (bx>=WINDOW_SIZE.width+BALL_RADIUS){
				by=WINDOW_SIZE.height/2;
				return;
			}
			if (bx<=-BALL_RADIUS){
				by=WINDOW_SIZE.height/2;
				return;
			}
		}
		this.b1dy=by-BAT_HEIGHT/2+this.b1o;
	}



	private void _calc_perfect_2(){
		if (this.bvx==0&&this.bvy==0){
			this.b2dy=by-BAT_HEIGHT/2+this.b2o;
			return;
		}
		double bx=this.bx+0;
		double by=this.by+0;
		double bvx=this.bvx+0;
		double bvy=this.bvy+0;
		while (bx<WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS){
			bx+=bvx;
			by+=bvy;
			if (by>=WINDOW_SIZE.height-BALL_RADIUS){
				by=WINDOW_SIZE.height-BALL_RADIUS;
				bvy*=-1;
			}
			if (by<=BALL_RADIUS){
				by=BALL_RADIUS+0;
				bvy*=-1;
			}
			if (bx>BAT_OFFSET_X&&bx<=BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS&&by>this.b1y-BALL_RADIUS&&by<this.b1y+BAT_HEIGHT+BALL_RADIUS){
				bx=BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS;
				bvx*=-1;
			}
			if (bx>=WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS&&bx<WINDOW_SIZE.width-BAT_OFFSET_X&&by>this.b2y-BALL_RADIUS&&by<this.b2y+BAT_HEIGHT+BALL_RADIUS){
				bx=WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS;
				bvx*=-1;
			}
			if (bx>BAT_OFFSET_X-BALL_RADIUS&&bx<BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS&&by>=this.b1y+BAT_HEIGHT+BALL_RADIUS&&by<this.b1y){
				by=this.b1y+BAT_HEIGHT+BALL_RADIUS;
				bvy*=-1;
			}
			if (bx>BAT_OFFSET_X-BALL_RADIUS&&bx<BAT_OFFSET_X+BAT_WIDTH+BALL_RADIUS&&by>this.b1y+BAT_HEIGHT&&by<=this.b1y-BALL_RADIUS){
				by=this.b1y-BALL_RADIUS;
				bvy*=-1;
			}
			if (bx>WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS&&bx<WINDOW_SIZE.width-BAT_OFFSET_X+BALL_RADIUS&&by>=this.b2y+BAT_HEIGHT+BALL_RADIUS&&by<this.b2y){
				by=this.b2y+BAT_HEIGHT+BALL_RADIUS;
				bvy*=-1;
			}
			if (bx>WINDOW_SIZE.width-BAT_OFFSET_X-BAT_WIDTH-BALL_RADIUS&&bx<WINDOW_SIZE.width-BAT_OFFSET_X+BALL_RADIUS&&by>this.b2y+BAT_HEIGHT&&by<=this.b2y-BALL_RADIUS){
				by=this.b2y-BALL_RADIUS;
				bvy*=-1;
			}
			if (bx>=WINDOW_SIZE.width+BALL_RADIUS){
				by=WINDOW_SIZE.height/2;
				return;
			}
			if (bx<=-BALL_RADIUS){
				by=WINDOW_SIZE.height/2;
				return;
			}
		}
		this.b2dy=by-BAT_HEIGHT/2+this.b2o;
	}



	private void _reset(){
		this.rl=false;
		this.rr=false;
		this.bx=WINDOW_SIZE.width/2;
		this.by=WINDOW_SIZE.height/2;
		this.bvx=0;
		this.bvy=0;
		this.b1o=this._map(Math.max(Math.min(this.rg.nextGaussian(),1),-1),-1,1,-BAT_HEIGHT/3,BAT_HEIGHT/3);
		this.b2o=this._map(Math.max(Math.min(this.rg.nextGaussian(),1),-1),-1,1,-BAT_HEIGHT/3,BAT_HEIGHT/3);
		this.etm=-1;
		this.tm="";
		this.sl=new int[2];
	}
}
