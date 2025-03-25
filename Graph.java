public class Graph{
    boolean[][] graph;
    double[][] tdiValues;
    public Graph(int size){
        generateGraph(size);
        restartTdiValues();
    }
    private void generateGraph(int size){
        this.graph=new boolean[size][size];
        this.tdiValues=new double[size][size];
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                graph[i][j]= (i!=j) && (Math.random()<0.2);
            }
        }
    }
    void restartTdiValues(){
        for(int i=0;i<this.tdiValues.length;i++){
            for(int j=0;j<tdiValues[0].length;j++){
                this.tdiValues[i][j]=4*distance(i,j,this.tdiValues.length-1,this.tdiValues[0].length-1);
            }
        }
    }
    double distance(int x1,int y1,int x2,int y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
}