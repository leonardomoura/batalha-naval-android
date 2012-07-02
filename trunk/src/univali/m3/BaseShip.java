package univali.m3;

import android.graphics.Canvas;

public class BaseShip {
	public int x;
	public int y;
	public Boolean isAlive;
	public Boolean hit;
	public String className;
	
	public BaseShip() {
		x = 0;
		y = 0;
		isAlive = true;
		hit = false;
		className = "";
	}
	public void Draw(Canvas c){
		
	}
	public void Update(){
		
	}
}
