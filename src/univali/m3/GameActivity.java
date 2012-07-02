package univali.m3;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;

public class GameActivity extends Activity {
	GameSurface gamesurface;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gamesurface = new GameSurface(this);
		setContentView(gamesurface);
	}
}
