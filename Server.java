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

    private static void append2DDoubleArray(StringBuilder json, double[][] array) {
        json.append("[");
        for (int i = 0; i < array.length; i++) {
            json.append(Arrays.toString(array[i]));
            if (i < array.length - 1) {
                json.append(", ");
            }
        }
        json.append("]");
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
            /*
             * Main results = new Main();
             * MainT results1 = new MainT(results.grid.graph);
             * MainP results2 = new MainP(results.grid.graph);
             * // Process Main
             * int[][] solution = new int[results.solution.size()][2];
             * for (int i = 0; i < results.solution.size(); i++) {
             * solution[i][0] = results.solution.get(i).row;
             * solution[i][1] = results.solution.get(i).col;
             * }
             * boolean[][] data = results.grid.graph;
             * 
             * // Process Main1
             * int[][] solution1 = new int[results1.solution.size()][2];
             * for (int i = 0; i < results1.solution.size(); i++) {
             * solution1[i][0] = results1.solution.get(i).row;
             * solution1[i][1] = results1.solution.get(i).col;
             * }
             * boolean[][] data1 = results1.grid.graph;
             * 
             * // Build JSON string
             * StringBuilder jsonBuilder = new StringBuilder();
             * jsonBuilder.append("{");
             * 
             * // Append grid data
             * jsonBuilder.append("\"grid\": [");
             * for (int i = 0; i < data.length; i++) {
             * jsonBuilder.append(Arrays.toString(data[i]));
             * if (i < data.length - 1)
             * jsonBuilder.append(", ");
             * }
             * jsonBuilder.append("], ");
             * 
             * // Append solution data
             * jsonBuilder.append("\"solution\": [");
             * for (int i = 0; i < solution.length; i++) {
             * jsonBuilder.append(Arrays.toString(solution[i]));
             * if (i < solution.length - 1)
             * jsonBuilder.append(", ");
             * }
             * jsonBuilder.append("], ");
             * jsonBuilder.append("\"solutionCost\":
             * ").append(results.solutionCost).append(", ");
             * 
             * // Append solutionsCost array
             * jsonBuilder.append("\"solutionsCost\": [");
             * for (int i = 0; i < results.solutionsCost.length; i++) {
             * jsonBuilder.append(results.solutionsCost[i]);
             * if (i < results.solutionsCost.length - 1) {
             * jsonBuilder.append(", ");
             * }
             * }
             * jsonBuilder.append("], ");
             * 
             * // Append grid1 data
             * jsonBuilder.append("\"grid1\": [");
             * for (int i = 0; i < data1.length; i++) {
             * jsonBuilder.append(Arrays.toString(data1[i]));
             * if (i < data1.length - 1) {
             * jsonBuilder.append(", ");
             * }
             * }
             * jsonBuilder.append("], ");
             * 
             * // Append solution1 data
             * jsonBuilder.append("\"solution1\": [");
             * for (int i = 0; i < solution1.length; i++) {
             * jsonBuilder.append(Arrays.toString(solution1[i]));
             * if (i < solution1.length - 1)
             * jsonBuilder.append(", ");
             * }
             * jsonBuilder.append("], ");
             * jsonBuilder.append("\"solutionCost1\":
             * ").append(results1.solutionCost).append(", ");
             * 
             * // Append solutionsCost1 array
             * jsonBuilder.append("\"solutionsCost1\": [");
             * for (int i = 0; i < results1.solutionsCost.length; i++) {
             * jsonBuilder.append(results1.solutionsCost[i]);
             * if (i < results1.solutionsCost.length - 1)
             * jsonBuilder.append(", ");
             * }
             * jsonBuilder.append("]");
             * 
             * jsonBuilder.append("}");
             */
            Main results = new Main();
            MainT results1 = new MainT(results.grid.graph);
            MainP results2 = new MainP(results.grid.graph);

            // Process Main
            int[][] solution = new int[results.solution.size()][2];
            for (int i = 0; i < results.solution.size(); i++) {
                solution[i][0] = results.solution.get(i).row;
                solution[i][1] = results.solution.get(i).col;
            }
            boolean[][] data = results.grid.graph;

            // Process Main1
            int[][] solution1 = new int[results1.solution.size()][2];
            for (int i = 0; i < results1.solution.size(); i++) {
                solution1[i][0] = results1.solution.get(i).row;
                solution1[i][1] = results1.solution.get(i).col;
            }
            boolean[][] data1 = results1.grid.graph;

            // Process MainP (results2)
            int[][] solution2 = new int[results2.solution.size()][2];
            for (int i = 0; i < results2.solution.size(); i++) {
                solution2[i][0] = results2.solution.get(i).row;
                solution2[i][1] = results2.solution.get(i).col;
            }
            boolean[][] data2 = results2.grid.graph;

            // Build JSON string
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");

            // Append grid data
            jsonBuilder.append("\"grid\": [");
            for (int i = 0; i < data.length; i++) {
                jsonBuilder.append(Arrays.toString(data[i]));
                if (i < data.length - 1)
                    jsonBuilder.append(", ");
            }
            jsonBuilder.append("], ");

            // Append solution data
            jsonBuilder.append("\"solution\": [");
            for (int i = 0; i < solution.length; i++) {
                jsonBuilder.append(Arrays.toString(solution[i]));
                if (i < solution.length - 1)
                    jsonBuilder.append(", ");
            }
            jsonBuilder.append("], ");
            jsonBuilder.append("\"solutionCost\": ").append(results.solutionCost).append(", ");

            // Append solutionsCost array
            jsonBuilder.append("\"solutionsCost\": [");
            for (int i = 0; i < results.solutionsCost.length; i++) {
                jsonBuilder.append(results.solutionsCost[i]);
                if (i < results.solutionsCost.length - 1) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append("], ");

            // Append grid1 data
            jsonBuilder.append("\"grid1\": [");
            for (int i = 0; i < data1.length; i++) {
                jsonBuilder.append(Arrays.toString(data1[i]));
                if (i < data1.length - 1) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append("], ");

            // Append solution1 data
            jsonBuilder.append("\"solution1\": [");
            for (int i = 0; i < solution1.length; i++) {
                jsonBuilder.append(Arrays.toString(solution1[i]));
                if (i < solution1.length - 1)
                    jsonBuilder.append(", ");
            }
            jsonBuilder.append("], ");
            jsonBuilder.append("\"solutionCost1\": ").append(results1.solutionCost).append(", ");

            // Append solutionsCost1 array
            jsonBuilder.append("\"solutionsCost1\": [");
            for (int i = 0; i < results1.solutionsCost.length; i++) {
                jsonBuilder.append(results1.solutionsCost[i]);
                if (i < results1.solutionsCost.length - 1)
                    jsonBuilder.append(", ");
            }
            jsonBuilder.append("], "); // Add comma here for results2 entries

            // Append grid2 data
            jsonBuilder.append("\"grid2\": [");
            for (int i = 0; i < data2.length; i++) {
                jsonBuilder.append(Arrays.toString(data2[i]));
                if (i < data2.length - 1) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append("], ");

            // Append solution2 data
            jsonBuilder.append("\"solution2\": [");
            for (int i = 0; i < solution2.length; i++) {
                jsonBuilder.append(Arrays.toString(solution2[i]));
                if (i < solution2.length - 1) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append("], ");
            jsonBuilder.append("\"solutionCost2\": ").append(results2.solutionCost).append(", ");

            jsonBuilder.append("\"averagePathLength2\": ");
            append2DDoubleArray(jsonBuilder, results2.averagePathLength);
            jsonBuilder.append(", ");

            jsonBuilder.append("\"shortestPathLength2\": ");
            append2DDoubleArray(jsonBuilder, results2.shortestPathLength);
            jsonBuilder.append(", ");

            // Append solutionsCost2 array
            jsonBuilder.append("\"solutionsCost2\": [");
            for (int i = 0; i < results2.solutionsCost.length; i++) {
                jsonBuilder.append(results2.solutionsCost[i]);
                if (i < results2.solutionsCost.length - 1) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append("]"); // No trailing comma for the last entry

            jsonBuilder.append("}");
            // System.out.println(jsonBuilder);
            byte[] responseBytes = jsonBuilder.toString().getBytes();
            t.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            t.sendResponseHeaders(200, responseBytes.length);

            OutputStream os = t.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }
}