package mouse.univ;

import mouse.univ.exception.InfinitelyLowWeightException;
import mouse.univ.exception.InvalidGraphException;
import mouse.univ.exception.InvalidVertexException;
import mouse.univ.exception.VerticesNotConnectedException;

import java.util.*;

public class ShortestPathFinderImpl implements ShortestPathFinder {

    private static final int MAX_VERTICES = 100;

    private static final int MIN_EDGE_WEIGHT = -500;
    private static final int MAX_EDGE_WEIGHT = 500;

    private static final int INF = Integer.MAX_VALUE / 4;

    @Override
    public int findShortestPath(Graph graph, String from, String to) {
        validateGraphNotNull(graph);

        List<Edge> edges = graph.getEdgeList();
        Set<String> vertices = collectAndValidateVertices(edges);

        validateVertexCount(vertices);
        validateRequestedVertices(vertices, from, to);

        Map<String, Integer> distances = initDistances(from, vertices);

        calculateDistancesFromSource(vertices, edges, distances);

        validateNoNegativeLoops(edges, distances);

        return validateOutputAndReturn(to, distances);
    }

    private static int validateOutputAndReturn(String to, Map<String, Integer> distances) {
        int result = distances.get(to);
        if (result == INF) {
            throw new VerticesNotConnectedException();
        }
        return result;
    }

    private static void validateNoNegativeLoops(List<Edge> edges, Map<String, Integer> distances) {
        for (Edge edge : edges) {
            String source = edge.getSource();
            String target = edge.getDestination();
            int weight = edge.getWeight();

            int sourceDistance = distances.get(source);

            if (sourceDistance != INF && sourceDistance + weight < distances.get(target)) {
                throw new InfinitelyLowWeightException();
            }
        }
    }

    private static void calculateDistancesFromSource(Set<String> vertices, List<Edge> edges, Map<String, Integer> distances) {
        int vertexCount = vertices.size();

        for (int i = 0; i < vertexCount - 1; i++) {
            boolean changed = false;

            for (Edge edge : edges) {
                String source = edge.getSource();
                String target = edge.getDestination();
                int weight = edge.getWeight();

                int sourceDistance = distances.get(source);

                if (sourceDistance != INF && sourceDistance + weight < distances.get(target)) {
                    distances.put(target, sourceDistance + weight);
                    changed = true;
                }
            }

            if (!changed) {
                break;
            }
        }
    }

    private static Map<String, Integer> initDistances(String from, Set<String> vertices) {
        Map<String, Integer> distances = new HashMap<>();

        for (String vertex : vertices) {
            distances.put(vertex, INF);
        }

        distances.put(from, 0);
        return distances;
    }

    private void validateGraphNotNull(Graph graph) {
        if (graph == null || graph.getEdgeList() == null) {
            throw new InvalidGraphException();
        }
    }

    private Set<String> collectAndValidateVertices(List<Edge> edges) {
        Set<String> vertices = new HashSet<>();

        for (Edge edge : edges) {
            if (edge == null) {
                throw new InvalidGraphException();
            }

            String from = edge.getSource();
            String to = edge.getDestination();
            int weight = edge.getWeight();

            if (from == null || to == null) {
                throw new InvalidGraphException();
            }

            if (weight < MIN_EDGE_WEIGHT || weight > MAX_EDGE_WEIGHT) {
                throw new InvalidGraphException();
            }

            vertices.add(from);
            vertices.add(to);
        }

        return vertices;
    }

    private void validateVertexCount(Set<String> vertices) {
        if (vertices.isEmpty() || vertices.size() > MAX_VERTICES) {
            throw new InvalidGraphException();
        }
    }

    private void validateRequestedVertices(Set<String> vertices, String from, String to) {
        if (!vertices.contains(from) || !vertices.contains(to)) {
            throw new InvalidVertexException();
        }
    }
}
