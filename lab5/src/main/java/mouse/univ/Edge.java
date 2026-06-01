package mouse.univ;

import lombok.Data;

@Data
public class Edge {
    private int from;
    private int to;
    private int weight;

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
