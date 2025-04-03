import java.util.*;
class Graph{
    boolean[][] graph;
    double[][] tdiValues;
    Position[][] index_data;
    int size;
    public Graph(int size){
        generateGraph(size);
        restartTdiValues();
        this.size=size;
    }
    private void generateGraph(int size){
        this.graph=new boolean[size][size];
        this.tdiValues=new double[size][size];
        this.index_data=new Position[size][size];
        this.size=size;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                this.graph[i][j]= (i!=j) && (Math.random()<0.2);
            }
        }
    }
    void restartTdiValues(){
        double initialTdiValue=10*Graph.distance(new Position(0,0),new Position(this.tdiValues.length-1,this.tdiValues[0].length-1));
        Position initialPosition=new Position(0, 0);
        for(int i=0;i<this.tdiValues.length;i++){
            for(int j=0;j<tdiValues[0].length;j++){
                this.tdiValues[i][j]=initialTdiValue;
                this.index_data[i][j]=initialPosition;
            }
        }
    }
    static double distance(Position p1,Position p2){
        return Math.sqrt((p1.row-p2.row)*(p1.row-p2.row)+(p1.col-p2.col)*(p1.col-p2.col));
    }
    void updateTdiValues(ArrayList<Position> path){
        //This function is working
        double accummulator=0;
        for(int i=path.size()-2;i>-1;i--){
            accummulator+=distance(path.get(i),path.get(i+1));
            if(accummulator<tdiValues[path.get(i).row][path.get(i).col]){
                this.tdiValues[path.get(i).row][path.get(i).col]=accummulator;
                this.index_data[path.get(i).row][path.get(i).col]=path.get(i+1);
            }else{
                accummulator=tdiValues[path.get(i).row][path.get(i).col];
            }
        }
        return;
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
    @Override
    public String toString(){
        return "("+this.row+","+this.col+")";
    }
}
class Ant {
    ArrayList<Position> path;
    double pathCost;
    Graph grid;
    int stepSize;
    int alpha,beta;
    public Ant(Graph grid,int stepSize,int alpha,int beta){
        this.grid = grid;
        this.path=new ArrayList<Position>();
        this.pathCost=0;
        this.stepSize=stepSize;
        this.alpha=alpha;
        this.beta=beta;
    }
    boolean isAllowed(Position p1, Position p2) {
        double i1 = p1.row+0.5, j1 = p1.col+0.5, i2 = p2.row+0.5, j2 = p2.col+0.5;
    
        // Ensure valid bounds
        if (i1 < 0 || i2 < 0 || j1 < 0 || j2 < 0 || 
            i1 >= grid.graph.length || i2 >= grid.graph.length ||
            j1 >= grid.graph[0].length || j2 >= grid.graph[0].length) {
            return false;
        }
    
        // If the starting or ending point is inside an obstacle, return false
        if (grid.graph[(int) Math.floor(i1)][(int) Math.floor(j1)] || 
            grid.graph[(int) Math.floor(i2)][(int) Math.floor(j2)]) {
            return false;
        }
    
        double dx = i2 - i1;
        double dy = j2 - j1;
        int steps = (int) Math.max(Math.abs(dx), Math.abs(dy)) * 1000;
        double xinc = dx / steps;
        double yinc = dy / steps;
        // System.out.println(xinc+" "+yinc);
        for (int k = 0; k < steps; k++) {
            i1 += xinc;
            j1 += yinc;
            // System.out.println("("+i1+","+j1+")");
            int i,j;
            if(Math.abs(Math.ceil(i1)-i1)<1e-9){
                i = (int) Math.ceil(i1);
            }else{
                i = (int) Math.floor(i1);
            }
            if(Math.abs(Math.ceil(j1)-j1)<1e-9){
                j = (int) Math.ceil(j1);
            }else{
                j = (int) Math.floor(j1);
            }
            // Ignore checking boundaries (i, j) exactly
            if ( Math.abs(i1 - i)< 1e-9 || Math.abs(j1 - j)< 1e-9) {
                continue;
            }
    
            // If the point is **inside** an obstacle, return false
            if (grid.graph[i][j]) {
                return false;
            }
        }
    
        return true;
    }
    
    private Position nextPosition(Position currPosition){
        //Ali needs to implement this
        int row=currPosition.row;
        int col=currPosition.col;
        Position target=new Position(this.grid.size-1, this.grid.size-1);
        List<Position> allowed=new ArrayList<>();
        for(int i=row-this.stepSize;i<=row+this.stepSize;i++) {
            for(int j=col-this.stepSize;j<=col+this.stepSize;j++) {
                if(i==row && j==col){
                    continue;
                }
                Position p=new Position(i, j);
                if(isAllowed(currPosition, p)) {
                    allowed.add(p);
                }
            }
        }
        double[] probability=new double[allowed.size()];
        int indx=-1;
        double sum=0;
        for(Position p:allowed) {
            int i=p.row;
            int j=p.col;
            probability[++indx]=(Math.pow((1.0/this.grid.distance(p, target)), this.alpha)+Math.pow((1.0/this.grid.tdiValues[i][j]), this.beta));
            sum+=probability[indx];
        }
        for(int i=0;i<probability.length;i++) {
            probability[i]=probability[i]/sum;
        }

        for(int i=1;i<probability.length;i++) {
            probability[i]=probability[i-1]+probability[i];
        }

        double rand=Math.random();
        if(rand<probability[0]) {
            return allowed.get(0);
        }

        for(int i=0;i<probability.length-1;i++) {
            if(rand>=probability[i] && rand<probability[i+1]) {
                return allowed.get(i+1);
            }
        }
        return allowed.getLast();
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
        //initialisation
        int noOfAnts=1,noOfIterations=1000,stepSize=2;
        int alpha=1,beta=7;
        Graph grid=new Graph(10);
        Ant[] ants=new Ant[noOfAnts];
        ArrayList<Position> solution=new ArrayList<>();
        for(int i=0;i<noOfAnts;i++){
            ants[i]=new Ant(grid,stepSize,alpha,beta);
        }
        //main logic
        for(int i=0;i<noOfIterations;i++){
            for(int j=0;j<noOfAnts;j++){
                ants[j].findSolution();
                System.out.print(ants[j].path.size()+" ");
            }
            for(int j=0;j<noOfAnts;j++){
                grid.updateTdiValues(ants[j].path);
            }
            for(int j=0;j<noOfAnts;j++){
                ants[j].restartPath();
            }
        }
        Position goalPosition=new Position(grid.size-1,grid.size-1);
        solution.addLast(new Position(0, 0));
        while(!solution.getLast().equals(goalPosition)) {
            solution.addLast(grid.index_data[solution.getLast().row][solution.getLast().col]);
        }
        System.out.println("solution :"+solution.size());
        System.out.println(solution);
    }
    // public static boolean[][] test(){
    //     Ant testAnt=new Ant(new Graph(20));
    //     testAnt.isAllowed(new Position(0, 0),new Position(1,3));
    //     testAnt.isAllowed(new Position(1,0),new Position(2,1));
    //     testAnt.isAllowed(new Position(3,2),new Position(2,1));
    //     testAnt.isAllowed(new Position(2,1),new Position(4,0));
    //     testAnt.isAllowed(new Position(1,2),new Position(1,1));
    //     testAnt.isAllowed(new Position(19,19),new Position(0,0));
    //     testAnt.isAllowed(new Position(0,0),new Position(3,7));
    //     return testAnt.grid.graph;
    //     Graph grid=new Graph(5);
    //     ArrayList<Position> path = new ArrayList<>();
    //     for(int i=0;i<5;i++){
    //         path.addLast(new Position(i, i));
    //     }
    //     System.out.println("Before :");
    //     for(int i=0;i<5;i++){
    //         for(int j=0;j<5;j++){
    //             System.out.print(grid.tdiValues[i][j]+" ");
    //         }
    //         System.out.println();
    //     }
    //     for(int i=0;i<5;i++){
    //         for(int j=0;j<5;j++){
    //             System.out.print(grid.index_data[i][j]+" ");
    //         }
    //         System.out.println();
    //     }
    //     grid.updateTdiValues(path);
    //     System.out.println("After :");
    //     for(int i=0;i<5;i++){
    //         for(int j=0;j<5;j++){
    //             System.out.print(grid.tdiValues[i][j]+" ");
    //         }
    //         System.out.println();
    //     }
    //     for(int i=0;i<5;i++){
    //         for(int j=0;j<5;j++){
    //             System.out.print(grid.index_data[i][j]+" ");
    //         }
    //         System.out.println();
    //     }
    // }
}
