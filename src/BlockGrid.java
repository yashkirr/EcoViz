public class BlockGrid extends Grid{
    private static Block[][] grid;

    BlockGrid(Block[][] grid){
        this.grid = grid;
    }

    public static Block[][] getGrid(){ return grid;}

    public static void setBlockDIm(int x, int y) {
        grid = new Block[x][y];
    }
    
    public static Block getBlock(int x, int y){ return grid[x][y];}
}
