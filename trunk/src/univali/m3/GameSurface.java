package univali.m3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
	Boolean running = false;
	Thread gamethread;
	
	public GameSurface(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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
		
        canvas.drawColor(Color.WHITE);
//        canvas.drawBitmap(bmp, 10, 10, null);
        canvas.drawColor(Color.BLUE);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        
//        p.setAlpha(1);
        p.setStyle(Style.FILL);       
        p.setStyle(Style.STROKE);
        canvas.drawLine(0, canvas.getHeight()/3, canvas.getWidth(), canvas.getHeight()/3, p);
        canvas.drawLine(0, canvas.getHeight()/3*2, canvas.getWidth(), canvas.getHeight()/3*2, p);
        canvas.drawLine(canvas.getWidth()/6, 0, canvas.getWidth()/6, canvas.getHeight(), p);
        canvas.drawLine(canvas.getWidth()/6*2, 0, canvas.getWidth()/6*2, canvas.getHeight(), p);
        p.setColor(Color.BLACK);
        canvas.drawLine(canvas.getWidth()/3, 0, canvas.getWidth()/3, canvas.getHeight(), p);
        p.setColor(Color.WHITE);
        canvas.drawLine(canvas.getWidth()/6*4, 0, canvas.getWidth()/6*4, canvas.getHeight(), p);
        canvas.drawLine(canvas.getWidth()/6*5, 0, canvas.getWidth()/6*5, canvas.getHeight(), p);
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

}
