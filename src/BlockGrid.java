public class BlockGrid extends Grid{
    private static int dimx;
    private static int dimy;

    private static Block[][] grid;

    BlockGrid(Block[][] grid){
        this.grid = grid;
    }

    public static Block[][] getGrid(){ return grid;}

    public static void setBlockDIm(int x, int y) {
        dimx = x;
        dimy = y;
        grid = new Block[x][y];
    }
    public static int getDimX(){ return dimx;}
    public static int getDimY(){ return dimy;}
    
    public static Block getBlock(int x, int y){ return grid[x][y];}

    public static void setPlant(){
        for(Block[] B: grid){
            for(Block b: B){
                for(Plant plant: b.canopy){
                    if(!plant.hide){
                        b.foliage += 3;
                    }
                }
                for(Plant plant: b.undergrowth){
                    if(!plant.hide){ b.foliage += 1;}
                }
                b.fuel = b.canopy.size()*40 + (int)Math.ceil(b.undergrowth.size()*5);
            }
        }
    }

    public static void windSlope(int windX, int windY){
        int a;
        if(windX==0) a = 1;
        else a = Math.abs(windX)/windX;      //wind unit vectors
        int b;
        if(windY==0) b = 1;
        else b = Math.abs(windY)/windY;
        float n = (float)Math.sqrt(2*FileLoader.getSpacing());
        float speed = (float)Math.sqrt(windX*windX+windY*windY);
        float steep;
        for(int x = 1; x<dimx-1; x++){
            for(int y = 1; y<dimy-1; y++){
                grid[x][y].slope = (grid[x][y+b].elv + grid[x+a][y].elv - 2*grid[x][y].elv)/n;
                if(grid[x][y].slope<-1.5){ grid[x][y].windSlope = 0;}
                else if(grid[x][y].slope<-0.2){ grid[x][y].windSlope = -speed/(10*grid[x][y].slope);}
                else if(grid[x][y].slope<0.2){ grid[x][y].windSlope = speed*(float)0.9;}
                else if(grid[x][y].slope<0.7) { grid[x][y].windSlope = speed*(float)1.1;}
                else { grid[x][y].windSlope = speed/(15*grid[x][y].slope);}
            }
        }
    }
}
