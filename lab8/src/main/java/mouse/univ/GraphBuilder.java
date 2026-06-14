package mouse.univ;

import java.util.ArrayList;
import java.util.List;

public class GraphBuilder {

    private final List<Edge> edgeList;

    public GraphBuilder() {
        this.edgeList = new ArrayList<>();
    }

    public static GraphBuilder init() {
        return new GraphBuilder();
    }

    public Graph build() {
        return new Graph(edgeList);
    }

    public GraphBuilder edge(String src, String dest,  int weight) {
        edgeList.add(new Edge(src, weight, dest));
        return this;
    }

    public static Graph loop(int length, int edgeWeight) {
        String prefix = "V";
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            String from = prefix + i;
            int dest = i == length - 1 ? 0 : i + 1;
            String to = prefix + dest;
            edges.add(new Edge(from, edgeWeight, to));
        }

        return new Graph(edges);
    }
}
