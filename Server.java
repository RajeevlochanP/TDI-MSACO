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
            Graph graph=new Graph(20);
            boolean[][] data =graph.graph;
            graph.isValid(0,0,2,1);
            graph.isValid(1,0,2,1);
            graph.isValid(3,2,2,1);
            graph.isValid(2,1,4,0);
            graph.isValid(1,2,1,1);
            graph.isValid(19,19,0,0);
            graph.isValid(0,0,3,7);
            // graph.isValid(19, 0, 0, 19);
            System.out.println();
            StringBuilder jsonResponse = new StringBuilder("[");
            for (int i = 0; i < data.length; i++) {
                jsonResponse.append(Arrays.toString(data[i]));
                if (i < data.length - 1) {
                    jsonResponse.append(", ");
                }
            }
            jsonResponse.append("]");

            byte[] responseBytes = jsonResponse.toString().getBytes();
            t.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            t.sendResponseHeaders(200, responseBytes.length);

            OutputStream os = t.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }
}