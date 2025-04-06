import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // Creates a default executor
        server.start();
        System.out.println("Server started on port 8000...");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            if ("OPTIONS".equalsIgnoreCase(t.getRequestMethod())) {
                t.sendResponseHeaders(204, -1); // No content for preflight responses
                return;
            }

            // Generate JSON response
            Main results=new Main();
            int[][] solution = new int[results.solution.size()][2];
            for (int i = 0; i < results.solution.size(); i++) {
                solution[i][0] = results.solution.get(i).row;
                solution[i][1] = results.solution.get(i).col;
            }
            boolean[][] data =results.grid.graph;
            // System.out.println();
            // Manually build JSON string
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
            
        // Append grid data
        jsonBuilder.append("\"grid\": [");
        for (int i = 0; i < data.length; i++) {
            jsonBuilder.append(Arrays.toString(data[i]));
            if (i < data.length - 1) jsonBuilder.append(", ");
        }
        jsonBuilder.append("], ");
    
        // Append solution data
        jsonBuilder.append("\"solution\": [");
        for (int i = 0; i < solution.length; i++) {
            jsonBuilder.append(Arrays.toString(solution[i]));
            if (i < solution.length - 1) jsonBuilder.append(", ");
        }
        jsonBuilder.append("], ");
        jsonBuilder.append("\"solutionCost\": "+results.solutionCost);
        jsonBuilder.append("}");

            byte[] responseBytes = jsonBuilder.toString().getBytes();
            t.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            t.sendResponseHeaders(200, responseBytes.length);

            OutputStream os = t.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }
}