package univali.m3;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {
	Boolean running = false;
	Thread gamethread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(new GameSurface(GameActivity.this));
		
		gamethread = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				running=true;
				super.run();
				
				while(running){
					
				}
			}
		};
		gamethread.run();
	}
}
