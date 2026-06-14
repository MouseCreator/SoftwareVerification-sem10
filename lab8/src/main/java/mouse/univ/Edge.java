package mouse.univ;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge {
    private String source;
    private int weight;
    private String destination;
}
