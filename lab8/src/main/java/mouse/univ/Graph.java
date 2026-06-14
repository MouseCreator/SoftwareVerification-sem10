package mouse.univ;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Graph {
    private final List<Edge> edgeList;
}
