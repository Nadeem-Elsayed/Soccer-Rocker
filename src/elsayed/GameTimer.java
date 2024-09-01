package elsayed;
import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer{
	
	//mainly for pausing and calculating elapsed time
	private long previousTime = -1;
	
	//what the timer does
	private Tick handleMethod;
	
	/*
	 * this constructor requires a handle method, which is why we can use a lambda expression
	 * for the timer
	 */
	public GameTimer (Tick handler) {
		handleMethod = handler;
	}
	@Override
	/*
	 * handle method, in which the magic happens
	 * don't ask me about specifics
	 * I'm not an expert in sorcery
	 */
	public void handle(long currentTime) {
		//calculate the elapsed time
				double elapsedTime = (currentTime - previousTime) / 1000000000.0;//nanoseconds to seconds
				//call the handling class' gameTick event
				if (handleMethod != null) handleMethod.tick(elapsedTime);		
				//save the current time
				previousTime = currentTime;
	}
	/**
	 * Starts the game timer
	 */
	public void start() {
		previousTime = System.nanoTime();
		super.start();
	}
	
	/**
	 * Stops the game timer
	 * sets the previousTime to -1
	 */
	public void stop() {
		previousTime = -1;
		super.stop();
	}
	/*
	 * checks if the timer is paused
	 */
	public boolean isPaused () {
		return previousTime ==-1;
	}

}
