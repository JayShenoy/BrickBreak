package brickbreak;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BrickBreak extends Application {
    private Stage gameStage;
    private Timeline gameLoop;
    private Group root;
    private Scene gameScene;
    private Rectangle paddle;
    private Circle ball;
    private int ballSpeedX;
    private int ballSpeedY;
    private Group bricks;
    
    @Override
    public void start(Stage primaryStage) {
        gameStage = primaryStage;
        gameStage.setTitle("BrickBreak");
        
        initGame();
        
        gameStage.setScene(gameScene);
        gameStage.show();
        gameLoop.play();
    }
    
    public void initGame() {
        root = new Group();
        gameScene = new Scene(root, 520, 400, Color.BLACK);
        paddle = new Rectangle(gameScene.getWidth() / 2 - 40, gameScene.getHeight() - 50, 80, 20);
        paddle.setFill(Color.WHITE);
        ball = new Circle(gameScene.getWidth() / 2, 130, 8, Color.WHITE);
        ballSpeedX = 0;
        ballSpeedY = 3;
        bricks = new Group();
        root.getChildren().addAll(paddle, ball, bricks);

        Color[] brickColor = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE};

        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 8; j++) {
                Rectangle thisBrick = new Rectangle(j * 65, i * 15 + 40, 65, 15);
                thisBrick.setFill(brickColor[i]);
                bricks.getChildren().add(thisBrick);
            }
        }
        
        gameScene.setOnMouseMoved(e -> {
            paddle.setX(e.getX() - paddle.getWidth() / 2);
        });
        
        gameLoop = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(10), ae -> updateGame());
        gameLoop.getKeyFrames().add(kf);
        gameLoop.setCycleCount(Timeline.INDEFINITE);
    }
    
    public void updateGame() {
        detectCollision();
        ball.setCenterX(ball.getCenterX() + ballSpeedX);
        ball.setCenterY(ball.getCenterY() + ballSpeedY);
    }
    
    public void detectCollision() {
        if(ball.getBoundsInParent().intersects(paddle.getBoundsInParent()) && ball.getCenterY() <= paddle.getY()) {
            ballSpeedX = randomNumber();
            ballSpeedY = ballSpeedY * -1;
        }
        
        if(ball.getCenterX() <= 0 || ball.getCenterX() >= gameScene.getWidth()) {
            ballSpeedX = ballSpeedX * -1;
        }
        
        if(ball.getCenterY() <= 0) {
            ballSpeedY *= -1;
        }
        
        if(ball.getCenterY() >= gameScene.getHeight()) {
            Text youLose = new Text(gameScene.getWidth() / 2 - 120, gameScene.getHeight() / 2, "You Lose!");
            Font font = new Font(50);
            youLose.setFont(font);
            youLose.setFill(Color.WHITE);
            root.getChildren().add(youLose);
        }
        
        for(int i = 0; i < bricks.getChildren().size(); i++) {
            if(ball.getBoundsInParent().intersects(bricks.getChildren().get(i).getBoundsInParent())) {
                bricks.getChildren().remove(i);
                ballSpeedY *= -1;
                increaseBallSpeedY();
                
                if(bricks.getChildren().size() == 0) {
                    Text youWin = new Text(gameScene.getWidth() / 2 - 120, gameScene.getHeight() / 2, "You Win!");
                    Font font = new Font(50);
                    youWin.setFont(font);
                    youWin.setFill(Color.WHITE);
                    root.getChildren().add(youWin);
                }
            }
        }
    }
    
    public int randomNumber() {
        Random generator = new Random();
        int randNumber = generator.nextInt(7) - 3;
        return randNumber;
    }
    
    public void increaseBallSpeedY() {
        if(bricks.getChildren().size() % 10 == 0) {
            if(ballSpeedY > 0) ballSpeedY++;
            else if(ballSpeedY < 0) ballSpeedY--;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}