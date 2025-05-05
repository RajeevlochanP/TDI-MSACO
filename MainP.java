import java.util.*;

class GraphP{
    boolean[][] graph;
    double[][] phermoneValues;
    Position[][] index_data;
    int size;
    int Q=100;
    double evopRate=0.9;
    public GraphP(int size){
        generateGraphP(size);
        restartPhermoneValues();
        this.size=size;
    }
    private void generateGraphP(int size){
        this.graph=new boolean[size][size];
        this.phermoneValues=new double[size*size][size*size];
        this.index_data=new Position[size][size];
        this.size=size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.graph[i][j] = false;
            }
        }
        int[][] trueIndices = {
            {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}, {0, 9}, {0, 10}, {0, 12}, {0, 13}, {0, 14}, {0, 15}, {0, 16}, {0, 17},
            {2, 1}, {2, 4}, {2, 7}, {2, 8}, {2, 11}, {2, 14}, {2, 17}, {2, 18},
            {3, 1}, {3, 4}, {3, 7}, {3, 8}, {3, 11}, {3, 14}, {3, 17}, {3, 18},
            {4, 1}, {4, 4}, {4, 7}, {4, 8}, {4, 11}, {4, 14}, {4, 17}, {4, 18},
            {6, 1}, {6, 6}, {6, 11}, {6, 16},
            {7, 1}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {7, 8}, {7, 11}, {7, 14}, {7, 15}, {7, 16}, {7, 17}, {7, 18},
            {8, 1}, {8, 6}, {8, 11}, {8,12}, {8, 16},
            {10, 2}, {10, 3}, {10, 4}, {10, 5}, {10, 6}, {10, 7}, {10, 9}, {10, 10}, {10, 12}, {10, 13}, {10, 14}, {10, 15}, {10, 16}, {10, 17},
            {12, 1}, {12, 4}, {12, 7}, {12, 8}, {12, 11}, {12, 14}, {12, 17}, {12, 18},
            {13, 1}, {13, 4}, {13, 7}, {13, 8}, {13, 11}, {13, 14}, {13, 17}, {13, 18},
            {14, 1}, {14, 4}, {14, 7}, {14, 8}, {14, 11}, {14, 14}, {14, 17}, {14, 18},
            {16, 1}, {16, 6}, {16, 11}, {16, 16},
            {17, 1}, {17, 4}, {17, 5}, {17, 6}, {17, 7}, {17, 8}, {17, 11}, {17, 14}, {17, 15}, {17, 16}, {17, 17}, {17, 18},
            {18, 1}, {18, 6}, {18, 11}, {18,12}, {18, 16}
        };
        for (int[] index : trueIndices) {
            this.graph[index[0]][index[1]] = true;
        }
        // for(int i=0;i<size;i++){
        //     for(int j=0;j<size;j++){
        //         this.graph[i][j]= (i!=j) && (Math.random()<0.3);
        //     }
        // }
    }
    void restartPhermoneValues(){
        double initialPhermoneValue=1; //need to see what he used in research paper and update
        for(int i=0;i<this.phermoneValues.length;i++){
            for(int j=0;j<phermoneValues[0].length;j++){
                this.phermoneValues[i][j]=initialPhermoneValue;
            }
        }
        this.phermoneValues[size-1][size-1]=0;
    }
    static double distance(Position p1,Position p2){
        return Math.sqrt((p1.row-p2.row)*(p1.row-p2.row)+(p1.col-p2.col)*(p1.col-p2.col));
    }
    synchronized void updatePhermoneValues(ArrayList<Position> path) {
        //Ali needs to implement this
        // pheromone at each = Q/Path length &&  cycles already removed...
        double pathLength=0;
        for(int i=0;i<path.size()-1;i++) {
            pathLength+=distance(path.get(i), path.get(i+1));
        }
        for(int i=0;i<path.size()-1;i++) {
            Position st=path.get(i);
            Position nxt=path.get(i+1);
            int row=(st.row)*this.size+st.col;
            int col=(nxt.row)*this.size+nxt.col;
            // Evaporate this value before calling update pheromone
            this.phermoneValues[row][col] +=((this.Q)*(GraphP.distance(st, nxt)))/(pathLength*pathLength);
        }

    }
}
// class Position{
//     int row,col;
//     Position(int row,int col){
//         this.row=row;
//         this.col=col;
//     }
//     @Override
//     public boolean equals(Object obj){
//         return (obj.getClass()==this.getClass()) && (((Position)obj).row == this.row) && (((Position)obj).col == this.col);
//     }
//     @Override
//     public String toString(){
//         return "("+this.row+","+this.col+")";
//     }
// }
class AntP implements Runnable{
    ArrayList<Position> path;
    double pathCost;
    GraphP grid;
    int stepSize;
    int alpha,beta;
    Thread t;
    public AntP(GraphP grid,int stepSize,int alpha,int beta){
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
        //I will Implement this
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
        if(allowed.size()==0){
            System.err.println("No solution for this graph");
            return null;
        }
        double[] probability=new double[allowed.size()];
        int indx=-1;
        double sum=0;
        for(Position p:allowed) {
            int i=p.row;
            int j=p.col;
            probability[++indx]=(Math.pow((1.0/GraphP.distance(p, target)), this.alpha)*Math.pow(this.grid.phermoneValues[row*this.grid.size+col][i*grid.size+j],this.beta));
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
    @Override
    public void run(){
        Position goalPosition=new Position(grid.size-1,grid.size-1);
        this.path.add(new Position(0,0));
        for(int i=0;!path.get(i).equals(goalPosition);i++){
            this.path.add(nextPosition(path.get(i)));
            this.pathCost+=GraphP.distance(this.path.get(i), this.path.get(i+1));
        }
        //here need to remove cycles after finding path
        LinkedList<Position> p=new LinkedList<>(this.path);
        for(int i=0;i<p.size();i++){
            Position temp=p.get(i);
            int lastIndex=p.lastIndexOf(temp);
            int length=lastIndex-i;
            for(int j=0;j<length;j++){
                p.remove(i+1);
            }
        }
        this.path.clear();
        this.path.addAll(p);
        this.pathCost=0;
        for(int i=1;i<this.path.size();i++){
            this.pathCost+=GraphP.distance(this.path.get(i-1), this.path.get(i));
        }
        // grid.updatePhermoneValues(this.path); this is wrong
    }
    void restartPath(){
        this.path.clear();
        this.pathCost=0;
        return;
    }
}
public class MainP {
    GraphP grid;
    ArrayList<Position> solution;
    double solutionCost=0;
    ArrayList<ArrayList<Position>> solutions;
    double[] solutionsCost;
    public MainP(boolean[][] graph){
        int noOfAnts=1,noOfIterations=15,stepSize=1;
        int alpha=1,beta=5;
        this.solutions=new ArrayList<>();
        this.solutionsCost=new double[noOfIterations];
        this.grid=new GraphP(20);
        this.grid.graph=graph;
        AntP[] ants=new AntP[noOfAnts];
        this.solution=new ArrayList<>();
        for(int i=0;i<noOfAnts;i++){
            ants[i]=new AntP(grid,stepSize,alpha,beta);
        }
        //main logic
        for(int i=0;i<noOfIterations;i++) {
            for(int j=0;j<noOfAnts;j++){
                ants[j].t=new Thread(ants[j]);
                ants[j].t.start();
                // System.out.print(ants[j].path.size()+" ");
            }
            for(int j=0;j<noOfAnts;j++){
                try{
                ants[j].t.join();
                }catch(InterruptedException e){
                    System.err.println(e.getMessage());
                }
            }
            for(int row=0;row<grid.phermoneValues.length;row++) {
                for(int col=0;col<grid.phermoneValues.length;col++) {
                    grid.phermoneValues[row][col]=grid.evopRate*(grid.phermoneValues[row][col]);
                }
            }
            for(int j=0;j<noOfAnts;j++){
                grid.updatePhermoneValues(ants[j].path);
            }
            System.out.print(i+", ");
            // //trying to get solution from every iteration
            double minPathCost=ants[0].pathCost;
            int antNumber=0;
            for(int j=1;j<noOfAnts;j++){
                if(minPathCost>ants[j].pathCost){
                    minPathCost=ants[j].pathCost;
                    antNumber=j;
                }
            }
            this.solutions.addLast((ArrayList<Position>)ants[antNumber].path.clone());
            this.solutionsCost[i]=minPathCost;
            if(i==0){
                this.solution.clear();
                this.solution.addAll(this.solutions.getLast());
                this.solutionCost=this.solutionsCost[i];
            }else{
                if(this.solutionCost>this.solutionsCost[i]){
                    this.solutionCost=this.solutionsCost[i];
                    this.solution.clear();
                    this.solution.addAll(this.solutions.getLast());
                }else{
                    this.solutionsCost[i]=this.solutionCost;
                    this.solutions.add(i,this.solution);
                }
            }
            for(int j=0;j<noOfAnts;j++){
                ants[j].restartPath();
            }
        }
        this.solution.clear();
        this.solution.addAll(this.solutions.getLast());
        this.solutionCost=this.solutionsCost[noOfIterations-1];
    }
    /*public static void main(String[] args) {
        //initialisation
        int noOfAnts=1,noOfIterations=3,stepSize=1;
        int alpha=1,beta=7;
        GraphP grid=new GraphP(20);
        Ant[] ants=new Ant[noOfAnts];
        ArrayList<Position> solution=new ArrayList<>();
        for(int i=0;i<noOfAnts;i++){
            ants[i]=new Ant(grid,stepSize,alpha,beta);
        }
        //main logic
        for(int i=0;i<noOfIterations;i++){
            for(int j=0;j<noOfAnts;j++){
                ants[j].findSolution();
                // System.out.print(ants[j].path.size()+" ");
            }
            for(int j=0;j<noOfAnts;j++){
                grid.updatePhermoneValues(ants[j].path);
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
    }*/
    // public static void main(String[] args) {
    //     GraphP grid=new GraphP(5);
    //     ArrayList<Position> path=new ArrayList<>(Arrays.asList(new Position(0,0),new Position(1,0),new Position(1,1),new Position(2,1),new Position(1,0),new Position(2,0),new Position(3,1),new Position(3,2),new Position(4, 4)));
    //     grid.updatePhermoneValues(path);
    // }
}