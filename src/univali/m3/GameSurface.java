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
	
	@Override
	protected void onDraw(Canvas canvas) {
		
        canvas.drawColor(Color.WHITE);
//        canvas.drawBitmap(bmp, 10, 10, null);
        canvas.drawColor(Color.BLUE);
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setAlpha(1);
        p.setStyle(Style.FILL);
        canvas.drawRect(new Rect(0, 0, 100, 100), p);
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
