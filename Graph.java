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
                // if(i==19 && j==19 ){
                //     graph[i][j] = true;
                // }
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
                    //   0        0       1        2
    boolean isValid(float i1,float j1,float i2,float j2){


        if(i1<0 || i2<0 || j1<0 || j2<0){
            return false;
        }

       // System.out.println("just test" + graph[(int)i1][(int)j1] );
        if(graph[(int)i1][(int)j1] || graph[(int)i2][(int)j2]){
            System.out.print("false ");
            return false;
        }


        
        float dx = i2-i1; 
        float dy = j2-j1; 
        int steps = (int)Math.max(Math.abs(dx),Math.abs(dy));
    
        float xinc = (float)dx/steps; 
        float yinc = (float)dy/steps; 

       for(int k=0;k<steps;k++){
            
            i1+=xinc;
            j1+=yinc; 
    
            
    
            int i= (int)i1; 
            int j= (int)j1; 

            System.out.print("(" + i + "," + j + ") ");
    
            if(graph[i][j]){
                
                System.out.print("false ");
                return false;
            }
            
            if((i + 1 < graph.length && Math.ceil(i1) != i1) ){
                System.out.print("(" + i+1 + "," + j + ") ");
            }
            
            if( (j + 1 < graph[0].length && Math.ceil(j1) != j1)){
                System.out.print("(" + i + "," + (j+1) + ") ");
            }

            if (i + 1 < graph.length && Math.ceil(i1) != i1 && graph[i + 1][j]) {
                
                System.out.print("false ");
                return false;
            }
    
            if (j + 1 < graph[0].length && Math.ceil(j1) != j1 && graph[i][j + 1]) {
                System.out.print("false ");
                return false;
            }
        }
        System.out.print("true ");
    return true;
    }
    // boolean isValid(int x1, int y1, int x2, int y2) {
    //     // Check if the start or end points are in an obstacle
    //     if (graph[x1][y1] || graph[x2][y2]) {
    //         System.out.print("false ");
    //         return false;
    //     }
    
    //     int dx = Math.abs(x2 - x1);
    //     int dy = Math.abs(y2 - y1);
    //     int sx = (x1 < x2) ? 1 : -1;
    //     int sy = (y1 < y2) ? 1 : -1;
    //     int err = dx - dy;
    
    //     while (true) {
    //         System.out.print("("+x1+","+y1+")");
    //         if (graph[x1][y1]) {  // Check if the current cell is an obstacle
    //             System.out.print("false ");
    //             return false;
    //         }
    //         if (x1 == x2 && y1 == y2) {
    //             break;  // Reached the destination
    //         }
    
    //         int e2 = 2 * err;
    //         if (e2 > -dy) {
    //             err -= dy;
    //             x1 += sx;
    //         }
    //         if (e2 < dx) {
    //             err += dx;
    //             y1 += sy;
    //         }
    //     }
    
    //     System.out.print("true ");
    //     return true;
    // }    
}