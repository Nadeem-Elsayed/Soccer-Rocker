package elsayed;

import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;

public class Ball extends Sprite{

	static boolean gameOver = false;
	private boolean intersects = false;
	
	AudioClip kick = new AudioClip ("file:ResourcesElsayed/SoccerKick.mp3");
	
	public Ball(Rectangle hitBox, double posX, double posY, double width, double height, String imagePath) {
		super(hitBox, posX, posY, width, height, imagePath);
	}

	public void update (double elapsedTime){
		yAcc = 500;// always wanting to go down
		yVelocity += yAcc*elapsedTime;
		
		//graphic rotates left or right based on speed
		graphic.setRotate(graphic.getRotate()+5*xVelocity/100);//set it to 10 for fun
		
		//--------------------------------bounces off wall
		//if ball is out of bounds
		if (x.get()<0 || x.get()>SoccerRocker.GAME_WIDTH-graphic.getFitWidth()) {
			
			xVelocity = -xVelocity;
			if (x.get()<0) {//left of screen
				x.set(0);
			} else if (x.get()>SoccerRocker.GAME_WIDTH-graphic.getFitWidth()) {//right of screen
				x.set(SoccerRocker.GAME_WIDTH-graphic.getFitWidth());
			}
		}
		//checking for intersection, also checking that the ball doesn't register twice
		if (this.intersects(SoccerRocker.shoe) && intersects ==false) {
			
			//sound effect plays for silly fun
			
			kick.play();
			
			//variation when bouncing off shoe
			yVelocity =(-550-Math.random()*50);//bounce up, a little variation
			this.setXVelocity(Math.random()*100-50);//move left or right
			
			//-------------ball changes trajectory if it hits the edge of shoe
			//if on right edge of shoe
			if (x.get()+getGraphicWidth()>SoccerRocker.shoe.getX()+SoccerRocker.shoe.getGraphicWidth()) {
				xVelocity = xVelocity+200;//moves right
				
			//if on left edge of shoe
			} else if (x.get()<SoccerRocker.shoe.getX()) {
				xVelocity = xVelocity-200;//moves left
			}
			
			
			//change score counter
			SoccerRocker.score ++;
			SoccerRocker.scoreLabel.setText(Integer.toString(SoccerRocker.score));
			intersects =true;//I do this to ensure that there are no double collisions
		}
		else intersects =false;
		
		//set a max on the velocity because stuff gets a little crazy
		if (xVelocity>200) {
			xVelocity = 200;
		} else if (xVelocity<-200) {
			xVelocity = -200;
		}
		
		//set position
		x.set(x.get()+xVelocity*elapsedTime);
		y.set(y.get()+yVelocity*elapsedTime);
		this.relocate(x.get(), y.get());
	}
}
