package univali.m3;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;

public class GameActivity extends Activity {
	Boolean running = false;
	Thread gamethread;
	GameSurface gamesurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gamesurface = new GameSurface(GameActivity.this);
		setContentView(gamesurface);
		
		gamethread = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				running=true;
				super.run();
				Canvas c=null;
				while(running){
					update();
					try{
						c = gamesurface.getHolder().lockCanvas();
						synchronized (gamesurface.getHolder()) {
							gamesurface.onDraw(c);
						}
					}finally{
						if(c!=null){
							gamesurface.getHolder().unlockCanvasAndPost(c);
						}
					}
					
				}
			}
		};
		gamethread.run();
	}
	
	protected void update(){
		
	}
}
