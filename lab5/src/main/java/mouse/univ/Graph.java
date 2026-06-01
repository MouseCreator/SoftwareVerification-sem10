package mouse.univ;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Graph {

    private List<Edge> edgeList;

    public static Graph fromEdgeList(List<Edge> edges) {
        return new Graph(edges);
    }
}
