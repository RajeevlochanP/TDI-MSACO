import java.util.*;
class Graph{
    boolean[][] graph;
    double[][] tdiValues;
    int size;
    public Graph(int size){
        generateGraph(size);
        restartTdiValues();
        this.size=size;
    }
    private void generateGraph(int size){
        this.graph=new boolean[size][size];
        this.tdiValues=new double[size][size];
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                this.graph[i][j]= (i!=j) && (Math.random()<0.2);
            }
        }
    }
    void restartTdiValues(){
        for(int i=0;i<this.tdiValues.length;i++){
            for(int j=0;j<tdiValues[0].length;j++){
                this.tdiValues[i][j]=4*Graph.distance(new Position(i,j),new Position(this.tdiValues.length-1,this.tdiValues[0].length-1));
            }
        }
    }
    static double distance(Position p1,Position p2){
        return Math.sqrt((p1.row-p2.row)*(p1.row-p2.row)+(p1.col-p2.col)*(p1.col-p2.col));
    }
    void updateTdiValues(ArrayList<Position> path){
        //rajeev need to implement this
    }
}
class Position{
    int row,col;
    Position(int row,int col){
        this.row=row;
        this.col=col;
    }
    @Override
    public boolean equals(Object obj){
        return (obj.getClass()==this.getClass()) && (((Position)obj).row == this.row) && (((Position)obj).col == this.col);
    }
}
class Ant {
    ArrayList<Position> path;
    double pathCost;
    Graph grid;
    public Ant(Graph grid){
        this.grid = grid;
        this.path=new ArrayList<Position>();
        pathCost=0;
    }
    boolean isAllowed(Position p1, Position p2) {
        double i1 = p1.row, j1 = p1.col, i2 = p2.row, j2 = p2.col;
        if (i1 < 0 || i2 < 0 || j1 < 0 || j2 < 0) {
            return false;
        }
        // System.out.println("just test" + grid.graph[(int)i1][(int)j1] );
        if (grid.graph[(int) i1][(int) j1] || grid.graph[(int) i2][(int) j2]) {
            System.out.print("false ");
            return false;
        }
        double dx = i2 - i1;
        double dy = j2 - j1;
        int steps = (int) Math.max(Math.abs(dx), Math.abs(dy));
        double xinc = (double) dx / steps;
        double yinc = (double) dy / steps;
        for (int k = 0; k < steps; k++) {
            i1 += xinc;
            j1 += yinc;
            int i = (int) i1;
            int j = (int) j1;
            System.out.println(i1+" "+j1);
            // System.out.print("(" + i + "," + j + ") ");
            if (grid.graph[i][j]) {
                System.out.print("false ");
                return false;
            }
            if ((i + 1 < grid.graph.length && Math.ceil(i1) != i1)) {
                // System.out.print("(" + (i + 1) + "," + j + ") ");
            }
            if ((j + 1 < grid.graph[0].length && Math.ceil(j1) != j1)) {
                // System.out.print("(" + i + "," + (j + 1) + ") ");
            }
            if (i + 1 < grid.graph.length && Math.ceil(i1) != i1 && grid.graph[i + 1][j]) {
    
                System.out.print("false ");
                return false;
            }
            if (j + 1 < grid.graph[0].length && Math.ceil(j1) != j1 && grid.graph[i][j + 1]) {
                System.out.print("false ");
                return false;
            }
        }
        System.out.print("true ");
        return true;
    }
    private Position nextPosition(Position currPosition){
        //Ali need to implement this
        return null;
    }
    void findSolution(){
        Position goalPosition=new Position(grid.size-1,grid.size-1);
        this.path.add(new Position(0,0));
        for(int i=0;!path.get(i).equals(goalPosition);i++){
            this.path.add(nextPosition(path.get(i)));
            this.pathCost+=Graph.distance(this.path.get(i), this.path.get(i+1));
        }
    }
    void restartPath(){
        this.path.clear();
        this.pathCost=0;
        return;
    }
}
public class Main {
    public static void main(String[] args) {
        // //initialisation
        // int noOfAnts=50,noOfIterations=100,stepSize=2;
        // float alpha=1,beta=7;
        // Graph grid=new Graph(20); 
        // Ant[] ants=new Ant[noOfAnts];
        // ArrayList<Position> bestPath;
        // double bestPathCost=Double.POSITIVE_INFINITY;
        // for(int i=0;i<noOfAnts;i++){
        //     ants[i]=new Ant(grid);
        // }
        // //main logic
        // for(int i=0;i<noOfIterations;i++){
        //     for(int j=0;j<noOfAnts;j++){
        //         ants[j].findSolution();
        //     }
        //     for(int j=0;j<noOfAnts;j++){
        //         grid.updateTdiValues(ants[j].path);
        //     }
        //     for(int j=0;j<noOfAnts;j++){
        //         if(ants[j].pathCost<bestPathCost){
        //             bestPath=ants[j].path.clone();
        //             bestPathCost=ants[j].pathCost;
        //         }
        //     }
        //     for(int j=0;j<noOfAnts;j++){
        //         ants[j].restartPath();
        //     }
        // }
    }
    public static boolean[][] test(){
        Ant testAnt=new Ant(new Graph(20));
        testAnt.isAllowed(new Position(0, 0),new Position(1,3));
        testAnt.isAllowed(new Position(1,0),new Position(2,1));
        testAnt.isAllowed(new Position(3,2),new Position(2,1));
        testAnt.isAllowed(new Position(2,1),new Position(4,0));
        testAnt.isAllowed(new Position(1,2),new Position(1,1));
        testAnt.isAllowed(new Position(19,19),new Position(0,0));
        testAnt.isAllowed(new Position(0,0),new Position(3,7));
        return testAnt.grid.graph;
    }
}
