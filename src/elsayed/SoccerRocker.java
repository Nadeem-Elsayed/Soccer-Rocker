package elsayed;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.AudioClip;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/*
 * P.S my high score is 115, try and beat that
 */
public class SoccerRocker extends Application {
	//Scene and dimensions
	Scene scene;
	static final double GAME_WIDTH = 480;
	static final double GAME_HEIGHT = 640;

	//Groups
	Group titleScreen;
	Group gameScreen;
	Group gameOverScreen;

	//Sprites
	int ballCount = 1;
	ArrayList<Ball> balls = new ArrayList<Ball>();
	private Ball ball = new Ball (new Rectangle (75, 75), (GAME_WIDTH-75)/2, 100, 75, 75, "SoccerBall.png");
	public static Shoe shoe = new Shoe (new Rectangle (0, 10, 100, 40), (GAME_WIDTH-100)/2, 430, 100, 50, "SoccerShoe.png");
	Shape pauseButton;

	//Animation/GameTimer
	GameTimer timer = new GameTimer(elapsedTime ->update(elapsedTime));

	//Keyboard Input
	private final HashSet <KeyCode> keyboard = new HashSet<KeyCode> ();
	static boolean leftPressed = false;
	static boolean rightPressed = false;

	//---------------------Sounds/Music
	//audio clip is a quick throw away use of a sound
	AudioClip whistle = new AudioClip ("file:ResourcesElsayed/whistle.mp3");
	//song is Glorious Morning 2 by WaterFlame by the way
	//mediaplayer is better suited for long music
	MediaPlayer music;

	//-----------score and soccer balls
	//centeredScore used for game over screen
	StackPane centeredScore;
	static Label scoreLabel; //score counter at top changes while game is going
	static Label finalScore; //displayed on game end
	static int score = 0;//for keeping track

	//grass
	Line[] movingGrass;

	//Fonts (only font i am using)
	final static public Font TITLE_FONT = Font.loadFont("file:ResourcesElsayed/Kanit.ttf", 30);

	/*
	 * Update method in which the game updates the ball and shoe, keeps updating over and over
	 * i.e keeps repeating this code over and over until loss
	 */
	public void update (double elapsedTime) {
		
		//if a ball detects itself to be outside of the game screen, the game ends
		if (Ball.gameOver==true) {
			gameOver();
		}
		//for each ball
		for (int i = 0; i<balls.size(); i++) {
			//updates balls
			balls.get(i).update(elapsedTime);
			//checks for game over
			deathCheck(balls.get(i));
		}
		//updates the shoe
		shoe.update(elapsedTime);

		//update grass so it waves a little
		for (int i = 0; i<movingGrass.length; i++) {
			if (Math.random()*3 >=2) {
			movingGrass[i].setStartX(movingGrass[i].getEndX()-1+Math.random()*2);
			}
		}
		//checks if the user is trying to move the shoe
		shoe.setXVelocity(0);
		if (keyboard.contains(KeyCode.LEFT)) {
			shoe.setXVelocity(-700);
		}
		if (keyboard.contains(KeyCode.RIGHT)) {
			shoe.setXVelocity(700);
		}
		
		if (score/ballCount >=10) {//one extra ball for every 15 score
			Ball temp = new Ball(new Rectangle (75, 75), (GAME_WIDTH-75)/2, 100, 75, 75, "SoccerBall.png");
			ballCount++;
			balls.add(temp);
			gameScreen.getChildren().add(temp);

		}

	}
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Soccer Rocker!");
		stage.setResizable(false);

		//set up different states/screens
		setTitleScreen();
		setGameScreen();
		setGameOverScreen();

		//start at title screen
		scene = new Scene(titleScreen, GAME_WIDTH, GAME_HEIGHT);

		//need the whole file path for media, not the same as AudioClip, weird stuff
		String path = ("file:/" + System.getProperty("user.dir") + "/ResourcesElsayed/Music.mp3").replace("\\", "/");
		Media media = new Media (path);

		music = new MediaPlayer (media);
		music.play();
		music.setCycleCount(MediaPlayer.INDEFINITE);//music loops

		stage.setScene(scene);
		stage.show();

		scene.setOnKeyPressed(key -> keyPressed(key));
		scene.setOnKeyReleased(key -> keyReleased(key));
	}

	public static void main(String[] args) {
		launch();
	}
	/*
	 * checks for when the balls fall under
	 */
	public void deathCheck(Sprite ball) {
		if (ball.getY()>SoccerRocker.GAME_HEIGHT) {
			gameOver();
		}
	}
	/*
	 * When ball falls down
	 */
	public void gameOver() {
		//reset balls and ball count for the array and count
		balls.clear();
		balls.add(ball);
		ballCount = 1;
		//get rid of balls from on screen add back everything else
		gameScreen.getChildren().clear();
		gameScreen.getChildren().addAll(getBackground());
		gameScreen.getChildren().addAll(movingGrass);
		gameScreen.getChildren().addAll(centeredScore, ball, shoe);
		//game stops, game over screen
		timer.stop();
		scene.setRoot(gameOverScreen);
		//final score message
		finalScore.setText("Final Score is: " + Integer.toString(score));
		score = 0;
	}
	// the game over screen
	public void setGameOverScreen() {
		//-------------------All labels for the game end
		Label gameOver = new Label ("GAME OVER!");
		gameOver.setFont(TITLE_FONT);
		gameOver.setTextFill(Color.WHITE);

		finalScore = new Label ("Final Score is: " + Integer.toString(score));
		finalScore.setFont(TITLE_FONT);
		finalScore.setTextFill(Color.WHITE);
		finalScore.relocate(0, 40);

		Group allText = new Group(gameOver, finalScore);

		//---------------------Buttons
		Button again = new Button ("TRY AGAIN");
		again.setFont(TITLE_FONT);
		again.setPrefSize(200, 100);

		Button toMenu = new Button ("MAIN MENU");
		toMenu.setFont(TITLE_FONT);
		toMenu.setPrefSize(200, 100);
		toMenu.relocate(220, 0);

		Group buttons = new Group (again, toMenu);

		//-----------------Stack pane for centering everything
		StackPane centeredLabel = new StackPane();
		StackPane.setAlignment(allText, Pos.TOP_CENTER);
		StackPane.setAlignment(buttons, Pos.CENTER);
		
		//dimensions of stack pane to make everything perfectly centered
		centeredLabel.setPrefWidth(GAME_WIDTH);
		centeredLabel.setPrefHeight(GAME_HEIGHT);
		centeredLabel.getChildren().addAll(allText, buttons);

		gameOverScreen = new Group (getBackground(), centeredLabel);
		gameOverScreen.getChildren().addAll(makeGrass());
		
		//-------------button actions
		again.setOnAction(tryy -> {
			
			//reset ball position, set velocity to 0
			ball.relocate((GAME_WIDTH-ball.getGraphicWidth())/2, 200);
			ball.setXVelocity(0);
			ball.setYVelocity(0);
			
			//reset shoe position to center
			shoe.relocate((GAME_WIDTH-shoe.getGraphicWidth())/2, 430);
			
			//reset game screen and score
			scene.setRoot(gameScreen);
			score = 0;
			scoreLabel.setText(Integer.toString(score));
			
			//start
			whistle.play();
			timer.start();

		});;

		toMenu.setOnAction(leave -> scene.setRoot(titleScreen));
	}
	/*
	 * what happens after the program detects a key being pressed
	 */
	public void keyPressed (KeyEvent key) {

		//------------------for pause button
		//actual pause icon
		Rectangle leftPause = new Rectangle(180, 40, 20, 300);
		Rectangle rightPause = new Rectangle(300, 40, 20, 300);
		
		pauseButton = Shape.union(leftPause, rightPause);
		pauseButton.setFill(Color.WHITE);
		
		//-------------------pause
		
		/*
		 *check if P is pressed
		 *check if keyboard already contains P, ignore input if so (no repeat/stuttery movements)
		 *check if on game screen
		*/
		if (key.getCode() == KeyCode.P && !keyboard.contains(KeyCode.P) && scene.getRoot() ==gameScreen) {
			if (timer.isPaused()) {
				timer.start();
				
				//this is me trying to get rid of the pauseButton from the group
				//should have used remove method
				gameScreen.getChildren().clear();
				gameScreen.getChildren().add(getBackground());
				gameScreen.getChildren().addAll(movingGrass);
				gameScreen.getChildren().addAll(balls);
				gameScreen.getChildren().addAll(centeredScore, shoe);
			} else {//if timer is not paused
				timer.stop();
				gameScreen.getChildren().add(pauseButton);
			}
		}
		if (key.getCode() == KeyCode.ESCAPE) {
			Platform.exit();
		}
		//add to keyboard
		keyboard.add(key.getCode());
	}
	public void keyReleased (KeyEvent key) {
		keyboard.remove(key.getCode());
	}
	/*
	 * returns the sky and grass background
	 * static because I am going to have an array for the grass strands
	 */
	public Group getBackground () {

		//-------------grass
		//land
		Rectangle grass = new Rectangle (0, 480, 640, 160);
		grass.setFill(Color.GREEN);
		
		//---------------color of sky
		Stop darkBlue = new Stop (0, Color.MIDNIGHTBLUE);
		Stop lightBlue = new Stop (0.5, Color.DEEPSKYBLUE);
		Stop orange = new Stop (1, Color.ORANGE);
		LinearGradient sunset = new LinearGradient (0, 0, 0, 380, false, CycleMethod.NO_CYCLE, darkBlue, lightBlue, orange);

		//sky
		Rectangle sky = new Rectangle (0, 0, 640, 480);
		sky.setFill(sunset);

		//add to group
		Group background = new Group();
		background.getChildren().addAll(sky, grass);
		return background;
	}
	/*
	 * makes the grass array, used for all screens
	 */
	public Line[] makeGrass () {
		Line []backGrass= new Line[2000];

		//for every single grasslet
		for (int i = 0; i<backGrass.length; i++) {
			//randomish values
			double tempx = Math.random()*640;
			double tempy = Math.random()*10+460;
			backGrass[i] = new Line(tempx, tempy, tempx-2.5+Math.random()*5, tempy+20);
			backGrass[i].setStroke(Color.GREEN);
		}
		return backGrass;
	}
	/*
	 * makes the title screen
	 */
	public void setTitleScreen () {
		//Label
		Label startLabel = new Label ("SOCCER ROCKER!");
		startLabel.setFont(TITLE_FONT);
		startLabel.setTextFill(Color.WHITE);

		//Button
		Button startButton = new Button("START");
		startButton.setPrefSize(200, 150);
		startButton.relocate(220, 150);
		startButton.setFont(TITLE_FONT);

		//center stuff
		StackPane centeredStuff = new StackPane();
		StackPane.setAlignment(startLabel, Pos.TOP_CENTER);
		StackPane.setAlignment(startButton, Pos.CENTER);
		
		//dimensions to get the centering right
		centeredStuff.setPrefWidth(GAME_WIDTH);
		centeredStuff.setPrefHeight(GAME_HEIGHT);
		
		//add to group
		centeredStuff.getChildren().addAll(startLabel, startButton);

		//------------------button actions
		startButton.setOnAction(start -> 
		{
			//start the timer and set the screen/state
			timer.start();
			scene.setRoot(gameScreen);
			
			//whistle
			whistle.play();
			
			//move the ball and shoe to location, reset ball speed
			ball.relocate((GAME_WIDTH-ball.getGraphicWidth())/2, 200);
			shoe.relocate((GAME_WIDTH-shoe.getGraphicWidth())/2, 430);
			ball.setXVelocity(0);
			ball.setYVelocity(0);
			
			//set score to 0
			score = 0;
			scoreLabel.setText(Integer.toString(score));
		});
		titleScreen = new Group(getBackground(), centeredStuff);
		titleScreen.getChildren().addAll(makeGrass());
	}
	/*
	 *  the game screen where all the magic happens
	 */
	public void setGameScreen() {
		
		//displays score
		scoreLabel = new Label (Integer.toString(score));
		scoreLabel.setFont(TITLE_FONT);
		scoreLabel.setTextFill(Color.WHITE);
		scoreLabel.setAlignment(Pos.TOP_CENTER);
		
		//StackPane so that the score is centered
		centeredScore = new StackPane();
		centeredScore.setPrefWidth(GAME_WIDTH);
		centeredScore.getChildren().add(scoreLabel);

		//balls array gets the ball to begin with
		balls.add(ball);
		movingGrass = makeGrass();// will make this grass move

		gameScreen = new Group(getBackground());
		gameScreen.getChildren().addAll(movingGrass);
		gameScreen.getChildren().addAll(centeredScore, ball, shoe);

	}
}
