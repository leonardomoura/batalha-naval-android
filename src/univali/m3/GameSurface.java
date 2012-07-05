package univali.m3;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
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

	float rawY = 0;
	float rawX = 0;
	float mouseX = 0;
	float mouseY = 0;
	
	int gridOffsetX = 23;
	int gridOffsetY = 21;
	int gridCellWidth = 45;
	int gridCellHeight = 41;
	
	ArrayList<Point> ataque = new ArrayList<Point>();
	
	boolean isMyTurn;

	// isso vai ser muito gambiarristico
	int cliques;

	Context c;

	public ArrayList<BaseShip> myships = new ArrayList<BaseShip>();
//	public BaseShip[] theirships = new BaseShip[3];

	public Bitmap grid;

	public BaseShip selectedShip = null;

	public boolean click = false;

	public GameSurface(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		c = context;
		cliques = 0;

		grid = BitmapFactory.decodeResource(getResources(), R.drawable.grid);

		Bitmap tmp;
		tmp = BitmapFactory.decodeResource(getResources(), R.drawable.boat);
		for (int i = 0; i < 3; i++) {
			myships.add(new BoatShip());
			myships.get(i).sprite = tmp;
			myships.get(i).x = grid.getWidth();
			myships.get(i).y = 10 + i * myships.get(i).sprite.getHeight();
//			theirships[i] = new BoatShip();
//			theirships[i].sprite = tmp;
//			theirships[i].x = grid.getWidth();
//			theirships[i].y = 10 + i * theirships[i].sprite.getHeight()
//					+ grid.getHeight();
		}

		getHolder().addCallback(this);
		gamethread = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				running = true;
				super.run();
				Canvas c = null;
				while (running) {
					try {
						c = getHolder().lockCanvas();

					} finally {
						if (c != null) {
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

	protected void Update() {

	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint p = new Paint();
		// limpa a tela
		p.setColor(Color.RED);
		p.setStyle(Style.FILL_AND_STROKE);
		canvas.drawColor(Color.BLACK);
//		if (click)
//			canvas.drawColor(Color.GREEN);
		// p = new Paint();
		// p.setColor(Color.WHITE);
		// p.setTextAlign(Align.LEFT);
		// p.setStyle(Style.FILL_AND_STROKE);
		// canvas.drawText("rawX "+rawX, 10, 10, p);
		// canvas.drawText("rawY "+rawY, 10, 30, p);
		// canvas.drawText("mouseX "+mouseX, 10, 50, p);
		// canvas.drawText("mouseY "+mouseY, 10, 70, p);

		canvas.drawBitmap(grid, 0, 0, null);
		canvas.drawBitmap(grid, 0, grid.getHeight(), null);

		if(selectedShip!=null){
			canvas.drawRect(selectedShip.x, selectedShip.y, selectedShip.x+selectedShip.sprite.getWidth(), selectedShip.y+selectedShip.sprite.getHeight(), p);
		}
		
		for (BaseShip s : myships) {
			canvas.drawBitmap(s.sprite, s.x, s.y, null);
		}
//		for (BaseShip s : theirships) {
//			canvas.drawBitmap(s.sprite, s.x, s.y, null);
//		}

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
		running = false;

	}

	public void didTouch(MotionEvent e) {
		click = false;
		for (BaseShip s : myships) {
			click = s.WasClicked((int) e.getX(), (int) e.getY());
			if (click && s.canSelect) {
				selectedShip = s;
				break;
			}else{
				click=false;
			}
		}
		//if not click and selectedShip!=null means player is eligible to select a position to put the ship
		if(!click && selectedShip!=null){
			if(e.getX()<grid.getWidth() && e.getY()<grid.getHeight()){
				int tx=(int) (e.getX()-gridOffsetX)/gridCellWidth;
				int ty=(int) (e.getY()-gridOffsetY)/gridCellHeight;
			
				System.out.println("tx "+tx+" ty "+ty);
				
				selectedShip.canSelect=false;
				selectedShip.x = (tx*gridCellWidth)+gridOffsetX;
				selectedShip.y = (ty*gridCellHeight)+gridOffsetY;
				click = false;
				
				selectedShip=null;
			}
		}else{
			if(!click && selectedShip==null){
				if(e.getY()>grid.getHeight()){
					int ty = (int) (e.getY()-grid.getHeight());
				}
			}
		}
//		for (BaseShip s : theirships) {
//			click = s.WasClicked((int) e.getX(), (int) e.getY());
//			if (click) {
//
//				break;
//			}
//
//		}

	}

}
