import java.util.Random;

/**
 * This class handles the member variables and methods for the maze in
 * the maze game. Mazes are randomly generated using recursive backtracking,
 * which is explained here http://www.astrolog.org/labyrnth/algrithm.htm under
 * recursive backtracking.
 * @author Kenneth Ashley
 * @email kashley@ucsd.edu 
 * @pid A12763521
 */
public class Maze {
    public static final int SIZE = 8;
    private static Random rand = new Random();
    private boolean[][] horizontal; // Wall above cell
    private boolean[][] vertical; // Wall to the left of cell
    private boolean[][] visited; // Used to generate maze
    
    
    /**
     * Creates a new Maze object with a randomly generated maze
     */
    public Maze() {
        horizontal = new boolean[SIZE][SIZE];
        vertical = new boolean[SIZE][SIZE];
        visited = new boolean[SIZE][SIZE];
        
        reset();
    }
    
    /**
     * Resets and generates a new maze
     */
    public void reset() {
        // Initialize all cells as unvisited and walls as present
        for(int r = 0; r < SIZE; r++) {
            for(int c = 0; c < SIZE; c++) {
                visited[r][c] = false;
                horizontal[r][c] = true;
                vertical[r][c]  = true;
            }
        }
        
        // Generate new maze, starting at top left corner
        generate(0, 0);
    }
    
    /**
     * Generates a maze using recursive backtracking
     * @param r row of current cell
     * @param c column of current cell
     */
    private void generate(int r, int c) {
        visited[r][c] = true;

        // While there is an unvisited adjacent cell for the given cell
        while(hasEmptyAdjacent(r, c)) {
            // Randomly select an unvisited adjacent cell
            // Then carve in its direction
            while(true) {
                int randInt = rand.nextInt(4);
                // Up
                if(randInt == 0 && r > 0 && !visited[r-1][c]) {
                    horizontal[r][c] = false;
                    generate(r-1, c);
                    break;
                }
                // Right
                else if(randInt == 1 && c < SIZE-1 && !visited[r][c+1]) {
                    vertical[r][c+1] = false;
                    generate(r, c+1);
                    break;
                }
                // Down
                else if(randInt == 2 && r < SIZE-1 && !visited[r+1][c]) {
                    horizontal[r+1][c] = false;
                    generate(r+1, c);
                    break;
                }
                // Left
                else if(randInt == 3 && c > 0 && !visited[r][c-1]) {
                    vertical[r][c] = false;
                    generate(r, c-1);
                    break;
                }
            }
        }
    }
    
    /**
     * @param r row of cell
     * @param c column of cell
     * @return True if there is an unvisited adjacent cell
     */
    private boolean hasEmptyAdjacent(int r, int c) {
        if(r > 0 && !visited[r-1][c])
            return true;
        if(c < SIZE-1 && !visited[r][c+1])
            return true;
        if(r < SIZE-1 && !visited[r+1][c])
            return true;
        if(c > 0 && !visited[r][c-1])
            return true;
        return false;
    }
    
    /**
     * @param r row of cell
     * @param c column of cell
     * @return True if wall is present above cell
     */
    public boolean getHorizontal(int r, int c) {
        if(r >= SIZE) // To find bottom wall
            return true;
        return horizontal[r][c];
    }
    
    /**
     * @param r row of cell
     * @param c column of cell
     * @return True if wall is present left of cell
     */
    public boolean getVertical(int r, int c) {
        if(c >= SIZE) // To find right wall
            return true;
        return vertical[r][c];
    }

    /* Pre-made maze for testing
    private boolean[][] horizontal = {
            { false, true, true, true, true, true, true, true },
            { true, false, true, false, false, false, true, false },
            { false, true, true, true, true, true, false, false },
            { true, false, false, false, false, false, false, true },
            { false, true, true, true, true, true, true, false },
            { false, true, false, true, true, true, true, true },
            { true, false, true, true, false, false, false, false },
            { false, true, true, false, true, true, false, false } };
    private boolean[][] vertical = {
            { true, false, false, true, false, true, false, false, },
            { true, false, false, false, true, false, false, true },
            { true, false, true, true, false, true, false, true, },
            { true, false, true, false, true, false, true, false },
            { true, false, false, true, false, false, false, false, },
            { true, false, true, false, false, true, false, false },
            { true, true, false, false, true, false, true, true },
            { true, false, false, false, true, false, false, true } };*/
}