package univali.m3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class BaseShip {
	public int x;
	public int y;
	public Boolean isAlive;
	public Boolean hit;
	public String className;
	public int length;
	public Bitmap sprite;
	public boolean canSelect;
	
	public BaseShip() {
		x = 0;
		y = 0;
		length = 1;
		isAlive = true;
		hit = false;
		className = "";
		canSelect=true;
	}
	
	public boolean WasClicked(int x, int y){
		Rect r = new Rect(this.x, this.y, x+sprite.getWidth(), y+sprite.getHeight());
//		System.out.println("x "+this.x+" y "+this.y+" sprite.getWidth() "+sprite.getWidth()+" sprite.getHeight "+sprite.getHeight()+" x "+x+ " y "+y );
		return r.contains(x, y);
	}
	
	/*
	 * Draw method allows BaseShip to draw itself
	 */
	public void Draw(Canvas canvas){
		canvas.drawBitmap(sprite, x, y, null);
	}
	
	/*
	 * Originally intended to play animations
	 */
	public void Update(){
		
	}
}
