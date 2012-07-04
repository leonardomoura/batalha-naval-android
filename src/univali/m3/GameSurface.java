package univali.m3;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
	Boolean running = false;
	Thread gamethread;
	
	float rawY=0;
	float rawX=0;
	float mouseX=0;
	float mouseY=0;
	
	//isso vai ser muito gambiarristico
	int cliques;
	
	Context c;
	
	public BaseShip[] myships = new BaseShip[3];
	public BaseShip[] theirships = new BaseShip[3];
	
	public Bitmap grid;
	
	public GameSurface(Context context) {
		super(context);
		// TODO Auto-generated constructor stub		
		c = context;
		cliques=0;
		
		Bitmap grid = BitmapFactory.decodeResource(getResources(), R.drawable.grid);
		
		for(int i=0; i<3; i++){
			myships[i] = new BoatShip();
			myships[i].x = new Random().nextInt(6);
			myships[i].y = new Random().nextInt(3);
			theirships[i] = new BoatShip();
			theirships[i].x = new Random().nextInt(6);
			theirships[i].y = new Random().nextInt(3);
		}
		
		
		getHolder().addCallback(this);
		gamethread = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				running=true;
				super.run();
				Canvas c=null;
				while(running){
					try{
						c = getHolder().lockCanvas();
						
					}finally{
						if(c!=null){
							onDraw(c);
							getHolder().unlockCanvasAndPost(c);
						}
						try {
							sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}
		};
	}
	
	protected void Update(){
	
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint p = new Paint();
		// limpa a tela
		p.setColor(Color.BLACK);
		p.setStyle(Style.FILL_AND_STROKE);
		canvas.drawColor(Color.BLACK);
//		p = new Paint();
//		p.setColor(Color.WHITE);
//		p.setTextAlign(Align.LEFT);
//		p.setStyle(Style.FILL_AND_STROKE);
//		canvas.drawText("rawX "+rawX, 10, 10, p);
//		canvas.drawText("rawY "+rawY, 10, 30, p);
//		canvas.drawText("mouseX "+mouseX, 10, 50, p);
//		canvas.drawText("mouseY "+mouseY, 10, 70, p);
		
		
	    
	    canvas.drawBitmap(grid, 0, 0, p);
    }
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		gamethread.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		running=false;
		
	}
	
	public void didTouch(MotionEvent e){
		this.rawX = e.getRawX();
		this.rawY = e.getRawY();
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}

}
