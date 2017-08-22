import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * This class handles the logic of the maze game, as well as GUI
 * elements inherited from the javafx Application class.
 * @author Kenneth Ashley
 * @email kashley@ucsd.edu 
 * @pid A12763521
 */
public class Start extends Application {
    private static final String PYTHON_SCRIPT = "Controller.py";
    private static final double CANVAS_SIZE = 502.5;
    private static final double SPRITE_SIZE = 40;
    private static final double CELL_SIZE = 60;
    private static final double WALL_THICKNESS = 2.5;
    private static final double WALL_LENGTH = CELL_SIZE+WALL_THICKNESS*2;
    private static final double SPRITE_INSET = (WALL_LENGTH-SPRITE_SIZE)/2;
    private static final double FONT_SIZE = CANVAS_SIZE/5.25;
    private Maze maze;
    private Sprite sprite;
    private boolean won;
    private long startTime;
    
    
    /**
     * Launches the python script for computer vision input, then calls the 
     * javafx launch method to start the program.
     */
    public static void main(String[] args) {
        try { 
            Runtime.getRuntime()
                    .exec("python " + PYTHON_SCRIPT);
            // Python script will only work with a specific set of libraries
            // and a properly configured class path. In other words, it can
            // only be run on our computers.
        } catch(Exception e) {
            System.out.println("Error launching Python script");
        }
        launch(args);
    }
    
    /**
     * Configures and initializes GUI elements and game objects. 
     * Initializes key event handler and animation timer. 
     * Launches the game window.
     */
    @Override
    public void start(Stage stage) {
        // Initialize javafx variables for GUI
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Initializing member variables for use in game graphics/logic
        maze = new Maze();
        sprite = new Sprite(SPRITE_SIZE);
        won = false;
        drawMaze(gc);
        updateSprite(gc, false);
        
        // Event handler for key presses (updates direction of sprite)
        scene.setOnKeyPressed(
            new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    Direction originalDir = sprite.getDirection();
                    String keyCode = e.getCode().toString();
                    
                    // Update sprite direction
                    switch(keyCode) {
                    case "UP":
                        sprite.setDirection(Direction.UP);
                        break;
                    case "RIGHT":
                        sprite.setDirection(Direction.RIGHT);
                        break;
                    case "DOWN":
                        sprite.setDirection(Direction.DOWN);
                        break;
                    case "LEFT":
                        sprite.setDirection(Direction.LEFT);
                    }
                    
                    // Only update sprite if direction changed
                    if(!won && sprite.getDirection() != originalDir)
                        updateSprite(gc, false);
                }
            });
        
        // Thread which handles animation of scene
        // Loops at a rate of 60 frames per second
        new AnimationTimer() {
            public void handle(long currentTime) {
                // Calculate elapsed time in seconds (from nano seconds)
                double timeElapse = (currentTime-startTime)/1000000000.0;
                
                // Update the sprite every second
                if(!won && timeElapse >= 1)
                    updateSprite(gc, true);
                
                // After 5 seconds have passed after winning
                if(won && timeElapse >= 5)
                    resetGame(gc);
            }
        }.start();
        
        // Initialize and show stage
        stage.setTitle("Maze!");
        stage.getIcons().add(new Image("icon.png"));
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Draws the maze using the given GraphicsContext object
     * @param gc The GraphicsContext object to draw with
     */
    private void drawMaze(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        
        // Draw bottom & right borders (with gap for exit in bottom)
        gc.fillRect(0, CANVAS_SIZE-WALL_THICKNESS, 
                CANVAS_SIZE-WALL_LENGTH, WALL_THICKNESS);
        gc.fillRect(CANVAS_SIZE-WALL_THICKNESS, 0, 
                WALL_THICKNESS, CANVAS_SIZE);
        
        // Loop through each cell of the maze
        for(int r = 0; r < Maze.SIZE; r++) {
            for(int c = 0; c < Maze.SIZE; c++) {
                if(maze.getHorizontal(r, c))
                    gc.fillRect(c*(WALL_LENGTH-WALL_THICKNESS), 
                            r*(WALL_LENGTH-WALL_THICKNESS), 
                            WALL_LENGTH, WALL_THICKNESS);
                if(maze.getVertical(r, c))
                    gc.fillRect(c*(WALL_LENGTH-WALL_THICKNESS), 
                            r*(WALL_LENGTH-WALL_THICKNESS), 
                            WALL_THICKNESS, WALL_LENGTH);
            }
        }
    }
    
    /**
     * Updates the direction/location of sprite graphically
     * @param gc The GraphicsContext object to draw with
     * @param move True if sprite should try to move
     */
    private void updateSprite(GraphicsContext gc, boolean move) {
        // Remove old sprite
        gc.setFill(Color.WHITE);
        gc.fillRect(
                sprite.getCol()*(WALL_LENGTH-WALL_THICKNESS)+SPRITE_INSET,
                sprite.getRow()*(WALL_LENGTH-WALL_THICKNESS)+SPRITE_INSET,
                SPRITE_SIZE, SPRITE_SIZE);
        
        if(move) {
            // Move sprite if possible
            int row = sprite.getRow(), col = sprite.getCol();
            switch(sprite.getDirection()) {
            case UP:
                if(!maze.getHorizontal(row, col))
                    sprite.move();
                break;
            case RIGHT:
                if(!maze.getVertical(row, col+1))
                    sprite.move();
                break;
            case DOWN: 
                if(!maze.getHorizontal(row+1, col))
                    sprite.move();
                // Check for win
                else if(row == maze.SIZE-1 && col == maze.SIZE-1) {
                    won = true;
                    // Print "You Win" text
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(2);
                    gc.setFont(Font.font("Times New Roman", FontWeight.BOLD, FONT_SIZE));
                    gc.fillText("You Win!", CANVAS_SIZE/6, CANVAS_SIZE/3);
                    gc.strokeText("You Win!", CANVAS_SIZE/6, CANVAS_SIZE/3);
                }
                break;
            case LEFT:
                if(!maze.getVertical(row, col))
                    sprite.move();
            }
        }

        // Draw new sprite
        gc.drawImage(sprite.getImage(),
                sprite.getCol()*(WALL_LENGTH-WALL_THICKNESS)+SPRITE_INSET,
                sprite.getRow()*(WALL_LENGTH-WALL_THICKNESS)+SPRITE_INSET);
        startTime = System.nanoTime(); // Reset start time for next move
    }
    
    /**
     * Clears the canvas and draws a new game with a newly
     * generated maze and reset sprite
     * @param gc The graphics context object to draw with
     */
    public void resetGame(GraphicsContext gc) {
        // Wipe screen
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
        
        // Reset and re-draw maze and sprite
        maze.reset();
        sprite.reset();
        drawMaze(gc);
        updateSprite(gc, false);
        
        won = false;
    }
}
