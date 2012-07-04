package univali.m3;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

public class GameActivity extends Activity {
	GameSurface gamesurface=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		gamesurface = new GameSurface(this);
		setContentView(gamesurface);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		gamesurface.didTouch(event);
		return super.onTouchEvent(event);
	}
}
