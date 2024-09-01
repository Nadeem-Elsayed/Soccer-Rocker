package elsayed;

import javafx.scene.shape.Rectangle;

public class Shoe extends Sprite{

	public Shoe (Rectangle hitBox, double posX, double posY, double width, double height, String imagePath) {
		super (hitBox, posX, posY, width, height, imagePath);
	}
	
	public void update (double elapsedTime){
		
		//update position
		x.set(x.get()+xVelocity*elapsedTime);
		y.set(y.get()+yVelocity*elapsedTime);
		
		//---------------max value, shoe can not go off screen
		if (x.get()<0) {
			x.set(0);
			//if too far right;
		} else if (x.get()>SoccerRocker.GAME_WIDTH-this.getGraphicWidth()) {
			x.set(SoccerRocker.GAME_WIDTH-this.getGraphicWidth());
		}
		//update position
		this.relocate(x.get(), y.get());
	}
}
