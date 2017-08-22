import javafx.scene.image.Image;

/**
 * This class handles the member variables and methods for the sprite in
 * the maze game. A sprite object has a unique image for each direction,
 * a location (row/column), and direction that it is facing.
 * @author Kenneth Ashley
 * @email kashley@ucsd.edu 
 * @pid A12763521
 */
public class Sprite {
    private Image upSprite;
    private Image rightSprite;
    private Image downSprite;
    private Image leftSprite;
    private Direction dir;
    private int row;
    private int col;
    
    /**
     * Constructor to create a Sprite object with predetermined images,
     * at location [0][0] and direction UP
     * @param size Requested size for images
     */
    public Sprite(double size) {
        // Images are constructed with parameters to maintain aspect ratio
        // and use a faster filtering algorithm rather than a smoother one
        upSprite = new Image("up.png", size, size, true, false);
        rightSprite = new Image("right.png", size, size, true, false);
        downSprite = new Image("down.png", size, size, true, false);
        leftSprite = new Image("left.png", size, size, true, false);
        reset();
    }
    
    /**
     * Sets the location and direction of the sprite to the default
     * location [0][0] and direction UP
     */
    public void reset() {
        row = 0;
        col = 0;
        dir = Direction.UP;
    }
    
    /**
     * @return Image for the direction the sprite is facing
     */
    public Image getImage() {
        switch(dir) {
        case UP:
            return upSprite;
        case RIGHT:
            return rightSprite;
        case DOWN:
            return downSprite;
        case LEFT:
            return leftSprite;
        default:
            return null;
        }
    }
    
    /**
     * @param dir Direction to set sprite to
     */
    public void setDirection(Direction dir) {
        this.dir = dir;
    }
    
    /**
     * @return Direction sprite is facing
     */
    public Direction getDirection() {
        return dir;
    }
    
    /**
     * Updates the row and column of the sprite's location in
     * the direction that the sprite is facing.
     */
    public void move() {
        switch(dir) {
        case UP:
            row -= 1;
            break;
        case RIGHT:
            col += 1;
            break;
        case DOWN:
            row += 1;
            break;
        case LEFT:
            col -= 1;
        }
    }
    
    /**
     * @return Row the sprite is located in
     */
    public int getRow() {
        return row;
    }
    
    /**
     * @return Column the sprite is located in
     */
    public int getCol() {
        return col;
    }
}
